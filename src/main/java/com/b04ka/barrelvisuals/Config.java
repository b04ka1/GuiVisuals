//package com.enthusiasm.sourcevisuals;
//
//import net.minecraftforge.common.ForgeConfigSpec;
//import net.minecraftforge.common.ForgeConfigSpec.Builder;
//import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
//import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
//import net.minecraftforge.fml.event.config.ModConfigEvent;
//
//@EventBusSubscriber(
//   modid = "sourcevisuals",
//   bus = Bus.MOD
//)
//public class Config {
//   private static final Builder BUILDER = new Builder();
//   public static final ConfigValue<Boolean> BRANDING = BUILDER.comment("Включить рендер брендинга проекта и сервера:").define("branding", true);
//   public static boolean isBrandingEnabled;
//   public static final ForgeConfigSpec SPEC = BUILDER.build();
//
//   @SubscribeEvent
//   public static void onLoad(ModConfigEvent event) {
//      isBrandingEnabled = (Boolean)BRANDING.get();
//   }
//}
