package net.sigmarizzler.steamy.block.entity.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sigmarizzler.steamy.block.entity.ImplementedInventory;
import net.sigmarizzler.steamy.block.entity.ModBlockEntities;
import net.sigmarizzler.steamy.item.ModItems;
import net.sigmarizzler.steamy.screen.custom.SteamStationScreenHandler;
import org.jetbrains.annotations.Nullable;

public class SteamStationBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {

    private int fuelTime = 0;       // Current time fuel can still be used
    private int maxFuelTime = 0;    // Max fuel duration
    private int waterTime = 0;      // Current "hydration" time left
    private int maxWaterTime = 0;   // Max "hydration" from one water item
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    private static final int GAUNTLET_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    private static final int WATER_SLOT = 2;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

    public SteamStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STEAM_STATION_BE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> SteamStationBlockEntity.this.progress;
                    case 1 -> SteamStationBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: SteamStationBlockEntity.this.progress = value;
                    case 1: SteamStationBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("steam_station.progress", progress);
        nbt.putInt("steam_station.maxProgress", maxProgress);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("steam_station.progress");
        maxProgress = nbt.getInt("steam_station.maxProgress");
        super.readNbt(nbt, registryLookup);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.steamy.steam_station");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SteamStationScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }



    public void tick(World world, BlockPos pos, BlockState state) {
        boolean dirty = false;

        // Consume fuel if needed
        if (fuelTime <= 0 && isFuel(this.getStack(FUEL_SLOT))) {
            ItemStack fuelStack = this.getStack(FUEL_SLOT);
            this.maxFuelTime = this.fuelTime = getFuelTime(fuelStack);
            fuelStack.decrement(1);
        }

        // Consume water if needed
        if (waterTime <= 0 && isWater(this.getStack(WATER_SLOT))) {
            ItemStack waterStack = this.getStack(WATER_SLOT);
            this.maxWaterTime = this.waterTime = 200; // ~10 charges per bucket
            if (waterStack.isOf(Items.WATER_BUCKET)) {
                this.setStack(WATER_SLOT, new ItemStack(Items.BUCKET));
            } else {
                waterStack.decrement(1);
            }
            dirty = true;
        }

        // Charge the gauntlet if all inputs are available
        if (hasValidGauntlet() && fuelTime > 0 && waterTime > 0) {
            progress++;
            fuelTime--;
            waterTime--;

            if (progress >= maxProgress) {
                increaseDurability();
                progress = 0;
            }

            dirty = true;
        } else {
            progress = 0;
        }

        if (dirty) {
            markDirty(world, pos, state);
        }
    }



    private void increaseDurability() {
        ItemStack gauntlet = this.getStack(GAUNTLET_SLOT);
        if (gauntlet.isDamaged()) {
            int healAmount = 1; // tweak based on how much you want to recharge per cycle
            gauntlet.setDamage(Math.max(gauntlet.getDamage() - healAmount, 0));
        }
    }


    private boolean isFuel(ItemStack stack) {
        return stack.isIn(ItemTags.COALS); // Supports coal, charcoal
    }

    private int getFuelTime(ItemStack stack) {
        return 20; // Default burn time; adjust as needed
    }

    private boolean isWater(ItemStack stack) {
        return stack.isOf(Items.WATER_BUCKET) || stack.isOf(Items.POTION);
    }

    private boolean hasValidGauntlet() {
        ItemStack stack = this.getStack(GAUNTLET_SLOT);
        return stack.isOf(ModItems.STEAM_GAUNTLET) && stack.isDamaged();
    }

}
