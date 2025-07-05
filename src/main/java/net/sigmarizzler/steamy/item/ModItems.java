package net.sigmarizzler.steamy.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.sigmarizzler.steamy.Steamy;


public class ModItems {
    public static Item register(Item item, String id) {
        // Create the identifier for the item.
        Identifier itemID = Identifier.of(Steamy.MOD_ID, id);

        // Register the item.
        Item registeredItem = Registry.register(Registries.ITEM, itemID, item);

        // Return the registered item!
        return registeredItem;
    }

    public static final Item STEAM_GAUNTLET = register(new SteamGauntlet(GauntletMaterial.INSTANCE, (new Item.Settings().attributeModifiers(SteamGauntlet
            .createAttributeModifiers(GauntletMaterial.INSTANCE,3, -2.4F))),5),"steam_gauntlet");

    public static final Item BRASS_INGOT = register(
            new Item(new Item.Settings()),
            "brass_ingot"
    );

    public static final Item COPPER_GEAR = register(
            new Item(new Item.Settings()),
            "copper_gear"
    );

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> itemGroup.add(ModItems.STEAM_GAUNTLET));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> itemGroup.add(ModItems.BRASS_INGOT));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> itemGroup.add(ModItems.COPPER_GEAR));
    }
}
