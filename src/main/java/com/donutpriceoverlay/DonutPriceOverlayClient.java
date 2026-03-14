package com.donutpriceoverlay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class DonutPriceOverlayClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DonutPriceOverlay.LOGGER.info("[DonutPriceOverlay] Client ready.");
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            String address = "";
            if (handler.getConnection() != null && handler.getConnection().getRemoteAddress() != null)
                address = handler.getConnection().getRemoteAddress().toString().toLowerCase();
            if (address.contains("donut")) {
                Minecraft mc = Minecraft.getInstance();
                mc.execute(() -> {
                    if (mc.player != null)
                        mc.player.sendSystemMessage(Component.literal(
                            "§8[§a§lDonut Price Overlay§8] §fActive! Prices shown on every order slot."));
                });
            }
        });
    }
}
