package net.sigmarizzler.steamy.item;

import net.minecraft.block.BlockState;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import java.util.List;

public class SteamGauntlet extends ToolItem {
    private float miningSpeed;

    public SteamGauntlet(ToolMaterial gauntletMaterial, Settings settings, float miningSpeed) {
        super(gauntletMaterial, settings.maxCount(1).rarity(Rarity.UNCOMMON).maxDamage(1001));
        this.miningSpeed = miningSpeed;
    }

    private static final int DURABILITY_COST_INTERVAL = 100; // ticks (5 seconds)

    public static AttributeModifiersComponent createAttributeModifiers(GauntletMaterial material, int baseAttackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, (double)((float)baseAttackDamage + material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, (double)attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build();
    }

    @Override
    public boolean isCorrectForDrops(ItemStack stack, BlockState state) {
        if (stack.getDamage() >= stack.getMaxDamage() - 1) {
            return false;
        }

        return state.isIn(BlockTags.PICKAXE_MINEABLE) ||
                state.isIn(BlockTags.AXE_MINEABLE) ||
                state.isIn(BlockTags.SHOVEL_MINEABLE);
    }

    @Override
    public float getMiningSpeed(ItemStack stack, BlockState state) {
        if (stack.getDamage() >= stack.getMaxDamage() - 1) {
            return 0.0F;
        }

        if (isCorrectForDrops(stack,state)) {
            return this.miningSpeed;
        }
        return 1.0F;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof PlayerEntity player)) return;

        boolean isInMainHand = player.getMainHandStack() == stack;

        int durabilityLeft = stack.getMaxDamage() - stack.getDamage();

        if (isInMainHand && durabilityLeft >= 2){
            if (world.getTime() % DURABILITY_COST_INTERVAL == 0) {
                stack.damage(1, player, EquipmentSlot.MAINHAND);
            }
        }
    }


    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0xa1e4ff;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        int durabilityLeft = stack.getMaxDamage() - stack.getDamage();
        tooltip.add(Text.literal("Steam Pressure: " + (durabilityLeft-1) + "/" + stack.getMaxDamage()).formatted(Formatting.DARK_GRAY));
    }
}
