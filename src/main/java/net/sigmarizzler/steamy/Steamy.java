package net.sigmarizzler.steamy;

import net.fabricmc.api.ModInitializer;

import net.sigmarizzler.steamy.block.ModBlocks;
import net.sigmarizzler.steamy.block.entity.ModBlockEntities;
import net.sigmarizzler.steamy.item.ModItems;
import net.sigmarizzler.steamy.screen.ModScreenHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Steamy implements ModInitializer {
	public static final String MOD_ID = "steamy";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("We rizzing up baby gronk with this one! :fire: trolololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololololol");
		//skibidi

		ModItems.initialize();
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModScreenHandlers.initialize();

	}
}