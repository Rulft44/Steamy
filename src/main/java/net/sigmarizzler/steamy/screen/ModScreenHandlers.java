package net.sigmarizzler.steamy.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.sigmarizzler.steamy.Steamy;
import net.sigmarizzler.steamy.screen.custom.SteamStationScreenHandler;

public class ModScreenHandlers {
    public static final ScreenHandlerType<SteamStationScreenHandler> STEAM_STATION_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Steamy.MOD_ID, "steam_station_screen_handler"),
                    new ExtendedScreenHandlerType<>(SteamStationScreenHandler::new, BlockPos.PACKET_CODEC));


    public static void initialize() {
        Steamy.LOGGER.info("Registering Screen Handlers for " + Steamy.MOD_ID);
    }
}