package net.tobsend.siegestopper;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.village.VillageSiegeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SiegeStopper.MODID)
public class SiegeStopper {

  // Define mod id in a common place for everything to reference
  public static final String MODID = "siegestopper";
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();

  public SiegeStopper() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    // Register the commonSetup method for modloading
    modEventBus.addListener(this::commonSetup);

    // Register ourselves for server and other game events we are interested in
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(SiegeEventHandler.class);
  }

  private void commonSetup(final FMLCommonSetupEvent event) {}

  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
    LOGGER.info("Siege stopper enabled.");
  }

  @Mod.EventBusSubscriber(
    modid = MODID,
    value = Dist.DEDICATED_SERVER
  )
  public class SiegeEventHandler {

    @SubscribeEvent
    public static void onSiegeEvent(VillageSiegeEvent event) {
      LOGGER.info("Siege was detected.");
      event.getSiege().siegeState = VillageSiege.State.SIEGE_DONE;
      event.setCanceled(true);
      LOGGER.info("Siege stopped.");
    }
  }
}
