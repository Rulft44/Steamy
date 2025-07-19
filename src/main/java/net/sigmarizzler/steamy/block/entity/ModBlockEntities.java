package net.sigmarizzler.steamy.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.sigmarizzler.steamy.Steamy;
import net.sigmarizzler.steamy.block.ModBlocks;
import net.sigmarizzler.steamy.block.entity.custom.SteamStationBlockEntity;

public class ModBlockEntities {
    public static final BlockEntityType<SteamStationBlockEntity> STEAM_STATION_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Steamy.MOD_ID, "steam_station_be"),
                    BlockEntityType.Builder.create(SteamStationBlockEntity::new, ModBlocks.STEAM_STATION).build(null));

    public static void initialize(){
        Steamy.LOGGER.info("Registering Block Entities for " + Steamy.MOD_ID);
    }
}
