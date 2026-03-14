package com.donutpriceoverlay.mixin;

import com.donutpriceoverlay.LoreParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class HandledScreenMixin {

    @Shadow protected int leftPos;
    @Shadow protected int topPos;

    @Inject(method = "renderSlot", at = @At("TAIL"))
    private void onRenderSlot(GuiGraphics graphics, Slot slot, CallbackInfo ci) {
        ItemStack stack = slot.getItem();
        if (stack.isEmpty()) return;

        long price = LoreParser.extractPrice(stack);
        if (price <= 0) return;

        String label = LoreParser.formatPrice(price);
        if (label == null) return;

        var font = Minecraft.getInstance().font;
        int slotX = leftPos + slot.x;
        int slotY = topPos + slot.y;
        int textWidth = font.width(label);
        float scale = Math.min(1.0f, 15.0f / textWidth);

        graphics.pose().pushPose();
        graphics.pose().translate(slotX + 15 - textWidth * scale, slotY + 1, 300f);
        graphics.pose().scale(scale, scale, 1.0f);
        graphics.drawString(font, label, 0, 0, LoreParser.labelColor(price), true);
        graphics.pose().popPose();
    }
}
