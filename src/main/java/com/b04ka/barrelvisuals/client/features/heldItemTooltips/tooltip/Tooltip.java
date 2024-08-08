package com.b04ka.barrelvisuals.client.features.heldItemTooltips.tooltip;

import net.minecraft.network.chat.MutableComponent;

public abstract class Tooltip {
   public String translationKey;
   public int primaryValue;

   public abstract MutableComponent getTooltipText();

   @Override
   public boolean equals(Object that) {
      return that instanceof Tooltip tooltip ? tooltip.getTooltipText().equals(this.getTooltipText()) : false;
   }
}
