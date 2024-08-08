package com.b04ka.barrelvisuals.mixin.client.features.heldItemTooltips;

import com.b04ka.barrelvisuals.client.features.HeldItemTooltips;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({Gui.class})
public class ItemTooltipsMixin {
   @Shadow protected ItemStack lastToolHighlight;

   @Shadow protected int toolHighlightTimer;

   @Shadow @Final protected Minecraft minecraft;

   @Redirect(
           method = {"renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V"},
           at = @At(
                   value = "INVOKE",
                   target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"
           )
   )
   private int drawCustomTooltips(GuiGraphics instance, Font textRenderer, Component text, int x, int y, int color) {
      return HeldItemTooltips.drawItemWithCustomTooltips(
              instance, textRenderer, text, (float)x, (float)(Minecraft.getInstance().getWindow().getGuiScaledHeight() - 38), color, this.lastToolHighlight
      );
   }

   @Redirect(
           method = {"renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V"},
           at = @At(
                   value = "INVOKE",
                   target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"
           )
   )
   private void drawCustomTooltips(GuiGraphics instance, int x1, int y1, int x2, int y2, int color) {
   }

   @Redirect(
           method = {"tick()V"},
           at = @At(
                   value = "INVOKE",
                   target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",
                   ordinal = 1
           )
   )
   private boolean interceptItemStack(ItemStack itemStack) {
      ItemStack nextItem = this.minecraft.player.getInventory().player.getMainHandItem();
      HeldItemTooltips heldItemTooltips = new HeldItemTooltips();
      if (itemStack.getItem() == this.lastToolHighlight.getItem() && !heldItemTooltips.equals(this.lastToolHighlight, nextItem)) {
         this.toolHighlightTimer = 41;
         return true;
      } else {
         return this.lastToolHighlight.isEmpty();
      }
   }
}
