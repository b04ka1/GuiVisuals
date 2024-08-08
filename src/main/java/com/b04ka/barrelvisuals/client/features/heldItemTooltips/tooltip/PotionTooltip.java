package com.b04ka.barrelvisuals.client.features.heldItemTooltips.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class PotionTooltip extends Tooltip {
   Component tooltip;

   public PotionTooltip(Component tooltip) {
      this.tooltip = tooltip;
   }

   @Override
   public MutableComponent getTooltipText() {
      return (MutableComponent)this.tooltip;
   }
}
