package com.donutpriceoverlay;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoreParser {

    private static final Pattern STRIP = Pattern.compile("§[0-9a-fk-orA-FK-OR]");

    private static final Pattern[] PATTERNS = {
        Pattern.compile("(?i)price[^:]*:\\s*\\$?([\\d,]+)"),
        Pattern.compile("(?i)buying[^:]*:\\s*\\$?([\\d,]+)"),
        Pattern.compile("(?i)order[^:]*:\\s*\\$?([\\d,]+)"),
        Pattern.compile("\\$([\\d,]+)"),
        Pattern.compile("(?i)([\\d,]+)\\s*coins?"),
    };

    public static long extractPrice(ItemStack stack) {
        if (stack.isEmpty()) return -1;
        ItemLore lore = stack.get(DataComponents.LORE);
        if (lore == null || lore.lines().isEmpty()) return -1;
        for (Component line : lore.lines()) {
            String clean = STRIP.matcher(line.getString()).replaceAll("").trim();
            if (clean.isEmpty()) continue;
            for (Pattern p : PATTERNS) {
                Matcher m = p.matcher(clean);
                if (m.find()) {
                    try {
                        long v = Long.parseLong(m.group(1).replace(",", ""));
                        if (v > 0) return v;
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        return -1;
    }

    public static String formatPrice(long p) {
        if (p <= 0) return null;
        if (p >= 1_000_000) return String.format("$%.1fM", p / 1_000_000.0);
        if (p >= 1_000)     return String.format("$%.0fK", p / 1_000.0);
        return "$" + p;
    }

    public static int labelColor(long p) {
        if (p >= 1_000_000) return 0xFFFFD700;
        if (p >= 100_000)   return 0xFFFF8C00;
        if (p >= 10_000)    return 0xFF55FF55;
        return 0xFFFFFFFF;
    }
}
