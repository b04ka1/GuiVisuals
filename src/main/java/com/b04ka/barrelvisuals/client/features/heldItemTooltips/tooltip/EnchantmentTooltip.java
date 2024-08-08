package com.b04ka.barrelvisuals.client.features.heldItemTooltips.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentTooltip extends Tooltip {
   boolean showLevels = true;

   public EnchantmentTooltip(Enchantment enchantment, int level) {
      this.translationKey = enchantment.getDescriptionId();
      this.primaryValue = level;
      if (enchantment.getMaxLevel() == 1) {
         this.showLevels = false;
      }
   }

   @Override
   public MutableComponent getTooltipText() {
      MutableComponent tooltip = Component.translatable(this.translationKey);
      if (this.showLevels) {
         tooltip.append(" ").append(Component.translatable("enchantment.level." + this.primaryValue));
      }

      return tooltip;
   }
}
