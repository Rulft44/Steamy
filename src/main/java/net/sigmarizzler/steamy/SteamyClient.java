package net.sigmarizzler.steamy;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.sigmarizzler.steamy.screen.ModScreenHandlers;
import net.sigmarizzler.steamy.screen.custom.SteamStationScreen;

public class SteamyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.STEAM_STATION_SCREEN_HANDLER, SteamStationScreen::new);
    }
}
