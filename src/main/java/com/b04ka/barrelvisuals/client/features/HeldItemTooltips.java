package com.b04ka.barrelvisuals.client.features;

import com.b04ka.barrelvisuals.client.features.heldItemTooltips.tooltip.ContainerTooltip;
import com.b04ka.barrelvisuals.client.features.heldItemTooltips.tooltip.EnchantmentTooltip;
import com.b04ka.barrelvisuals.client.features.heldItemTooltips.tooltip.PotionTooltip;
import com.b04ka.barrelvisuals.client.features.heldItemTooltips.tooltip.Tooltip;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class HeldItemTooltips {
   private static final int tooltipSize = 6;
   private static final int screenBorder = 0;

   public static int drawItemWithCustomTooltips(GuiGraphics drawContext, Font fontRenderer, Component text, float x, float y, int color, ItemStack currentStack) {
      int tooltipOffset = 0;
      Player player = Minecraft.getInstance().player;
      if (null != player && null != Minecraft.getInstance().gameMode) {
         if (Minecraft.getInstance().gameMode.canHurtPlayer()) {
            y -= 16.0F;
            if (player.getArmorValue() > 0) {
               y -= 10.0F;
            }

            if (player.getAbsorptionAmount() > 0.0F) {
               y -= 10.0F;
            }
         } else if (player.getVehicle() != null && player.getVehicle() instanceof LivingEntity) {
            y -= 16.0F;
         }

         List<Component> tooltips = Lists.newArrayList();

         for (Tooltip tooltip : getTooltips(currentStack)) {
            tooltips.add(tooltip.getTooltipText());
         }

         boolean showMoreTooltip = tooltips.size() > 6;
         if (showMoreTooltip) {
            int xMore = tooltips.size() - 5;
            tooltips.subList(5, tooltips.size()).clear();
            tooltips.add(Component.translatable("container.shulkerBox.more", new Object[]{xMore}).withStyle(ChatFormatting.GRAY));
         }

         tooltipOffset = 12 * tooltips.size();
         int maxLength = getMaxTooltipLength(tooltips, fontRenderer, currentStack);
         renderBackground(drawContext, y, 0, tooltipOffset, maxLength, color >> 24 & 0xFF);
         int i = tooltips.size() - 1;

         for (Component elem : tooltips) {
            renderTooltip(drawContext, fontRenderer, y - 0.0F - (float)(12 * i), color, ((MutableComponent)elem).withStyle(ChatFormatting.GRAY));
            i--;
         }

         return drawContext.drawString(fontRenderer, text, (int)x, (int)(y - (float)tooltipOffset - 0.0F), color);
      } else {
         return 0;
      }
   }

   public static List<Tooltip> getTooltips(ItemStack currentStack) {
      Item item = currentStack.getItem();
      List<Tooltip> result = Lists.newArrayList();
      if (item == Items.ENCHANTED_BOOK || currentStack.isEnchanted()) {
         generateTooltipsFromEnchantMap(EnchantmentHelper.getEnchantments(currentStack), result);
      }
      else if (!(item instanceof PotionItem) && !(item instanceof TippedArrowItem)) {
         if (item.toString().contains("shulker_box")) {

            CompoundTag compoundTag = currentStack.getTagElement("BlockEntityTag");
            if (compoundTag != null && compoundTag.contains("Items", 9)) {
               generateTooltipsFromShulkerBox(compoundTag, result);
            }
         }
         else if (item instanceof BundleItem && currentStack.getTooltipImage().isPresent() && currentStack.is(Items.BUNDLE)) {
            generateTooltipsFromContainer(((BundleTooltip)currentStack.getTooltipImage().get()).getItems(), result);
         }
         else if (item.toString().contains("toolbox")){
            CompoundTag compoundTag = currentStack.getTagElement("Inventory");
            if (compoundTag != null && compoundTag.contains("Items", 9)) {
               generateTooltipsFromToolBox(compoundTag, result);
            }
         }
      }
      else {
         List<Component> generated = Lists.newArrayList();
         item.appendHoverText(currentStack, null, generated, TooltipFlag.NORMAL);
         generateTooltipsForPotion(generated, result);
      }

      return result;
   }

   public boolean equals(ItemStack item1, ItemStack item2) {
      List<Tooltip> itemTooltips1 = getTooltips(item1);
      List<Tooltip> itemTooltips2 = getTooltips(item2);
      return Objects.equals(itemTooltips1, itemTooltips2);
   }

   private static void generateTooltipsFromShulkerBox(CompoundTag compoundTag, List<Tooltip> instance) {
      NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
      ContainerHelper.loadAllItems(compoundTag, items);
      generateTooltipsFromContainer(items, instance);
   }

   private static void generateTooltipsFromToolBox(CompoundTag compoundTag, List<Tooltip> instance) {
      NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);
      ContainerHelper.loadAllItems(compoundTag, items);
      generateTooltipsFromContainer(items, instance);
   }

   private static void generateTooltipsFromContainer(List<ItemStack> items, List<Tooltip> instance) {
      for (ItemStack item : items) {
         if (!item.isEmpty()) {
            instance.add(new ContainerTooltip(item));
         }
      }
   }

   private static void generateTooltipsFromEnchantMap(Map<Enchantment, Integer> enchantments, List<Tooltip> instance) {
      enchantments.forEach((enchantment, value) -> instance.add(new EnchantmentTooltip(enchantment, value)));
   }

   private static void generateTooltipsForPotion(List<Component> texts, List<Tooltip> instance) {
      int startIndex = texts.indexOf(CommonComponents.EMPTY);
      if (startIndex > 0) {
         texts.subList(startIndex, texts.size()).clear();
      }

      texts.forEach(current -> instance.add(new PotionTooltip(current)));
   }

   private static void renderBackground(GuiGraphics drawContext, float y, int screenBorder, int tooltipOffset, int maxLength, int alpha) {
      Minecraft client = Minecraft.getInstance();
      int background = Mth.lerpInt((float)alpha / 255.0F, 0, Mth.ceil(127.5)) << 24;
      drawContext.fill(
              Mth.ceil((float)(client.getWindow().getGuiScaledWidth() - maxLength) / 2.0F - 3.0F),
              Mth.ceil(y - (float)tooltipOffset - 5.0F - (float)screenBorder),
              Mth.ceil((float)(client.getWindow().getGuiScaledWidth() + maxLength) / 2.0F + 1.0F),
              Mth.ceil(y - (float)tooltipOffset - 4.0F - (float)screenBorder),
              background
      );
      drawContext.fill(
              Mth.ceil((float)(client.getWindow().getGuiScaledWidth() - maxLength) / 2.0F - 3.0F),
              Mth.ceil(y + 12.0F - (float)screenBorder),
              Mth.ceil((float)(client.getWindow().getGuiScaledWidth() + maxLength) / 2.0F + 1.0F),
              Mth.ceil(y + 13.0F - (float)screenBorder),
              background
      );
      drawContext.fill(
              Mth.ceil((float)(client.getWindow().getGuiScaledWidth() - maxLength) / 2.0F - 4.0F),
              Mth.ceil(y - (float)tooltipOffset - 4.0F - (float)screenBorder),
              Mth.ceil((float)(client.getWindow().getGuiScaledWidth() + maxLength) / 2.0F + 2.0F),
              Mth.ceil(y + 12.0F - (float)screenBorder),
              background
      );
   }

   private static void renderTooltip(GuiGraphics drawContext, Font fontRenderer, float y, int color, Component text) {
      int enchantX = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - fontRenderer.width(text)) / 2;
      drawContext.drawString(fontRenderer, text, enchantX, (int)y, color);
   }

   private static int getMaxTooltipLength(List<Component> tooltips, Font textRenderer, ItemStack itemStack) {
      int maxLength = textRenderer.width(itemStack.getHoverName());

      for (Component elem : tooltips) {
         int tipLength = textRenderer.width(elem);
         if (maxLength < tipLength) {
            maxLength = tipLength;
         }
      }

      return maxLength;
   }
}
