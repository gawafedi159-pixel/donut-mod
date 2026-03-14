package com.donutpriceoverlay;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DonutPriceOverlay implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("donutpriceoverlay");
    @Override
    public void onInitialize() {
        LOGGER.info("[DonutPriceOverlay] Loaded.");
    }
}
