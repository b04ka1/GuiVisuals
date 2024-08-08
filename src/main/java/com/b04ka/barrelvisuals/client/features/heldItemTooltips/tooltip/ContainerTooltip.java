package com.b04ka.barrelvisuals.client.features.heldItemTooltips.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class ContainerTooltip extends Tooltip {
   public ContainerTooltip(ItemStack item) {
      this.translationKey = item.getDescriptionId();
      this.primaryValue = item.getCount();
   }

   @Override
   public MutableComponent getTooltipText() {
      MutableComponent tooltip = Component.translatable(this.translationKey);
      tooltip.append(" x").append(String.valueOf(this.primaryValue));
      return tooltip;
   }
}
