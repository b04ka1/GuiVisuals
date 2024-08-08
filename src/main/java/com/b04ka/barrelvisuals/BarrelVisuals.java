package com.b04ka.barrelvisuals;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("barrelvisuals")
public class BarrelVisuals {
   public static final String MODID = "barrelvisuals";
   private final Minecraft mc;
   public static float deltaTime = 0.0F;
   public static final Logger LOGGER = LoggerFactory.getLogger("Barrel Visuals");

   public BarrelVisuals() {
      IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
      modEventBus.addListener(this::commonSetup);
      MinecraftForge.EVENT_BUS.register(this);
      this.mc = Minecraft.getInstance();
//      ModLoadingContext.get().registerConfig(Type.CLIENT, Config.SPEC);
   }

   private void commonSetup(FMLCommonSetupEvent event) {
   }


   @EventBusSubscriber(
      modid = "barrelvisuals",
      bus = Bus.MOD,
      value = {Dist.CLIENT}
   )
   public static class ClientModEvents {
      @SubscribeEvent
      public static void onClientSetup(FMLClientSetupEvent event) {

      }
   }
}
