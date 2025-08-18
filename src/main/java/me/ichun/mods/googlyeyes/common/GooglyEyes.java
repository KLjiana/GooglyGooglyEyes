package me.ichun.mods.googlyeyes.common;

import me.ichun.mods.googlyeyes.common.core.EventHandler;
import me.ichun.mods.googlyeyes.common.core.ModConfigClient;
import me.ichun.mods.ichunutil.api.common.head.HeadInfo;
import me.ichun.mods.ichunutil.client.core.EventHandlerClient;
import me.ichun.mods.ichunutil.common.head.HeadHandler;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
@Mod(GooglyEyes.MOD_ID)
public class GooglyEyes {
    public static final String MOD_NAME = "Googly Eyes";
    public static final String MOD_ID = "googlyeyes";

    public static final Logger LOGGER = LogManager.getLogger();

    public static EventHandler eventHandler = null;

    public static EventHandlerClient eventHandlerClient; //TODO if we have a packet channel we should only need it if a mod dep needs it.

    public GooglyEyes() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::processIMC);

        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ModConfigClient.SPEC);

            MinecraftForge.EVENT_BUS.register(eventHandler = new EventHandler());
            ModConfigClient.onConfigLoaded();

//            modLoadingContext.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> me.ichun.mods.ichunutil.client.core.EventHandlerClient::getConfigGui);
            //Set the new acid Eyes Supplier
            BooleanSupplier oldAcidEyesBooleanSupplier = HeadHandler.acidEyesBooleanSupplier;
            HeadHandler.acidEyesBooleanSupplier = () -> (ModConfigClient.acidTripEyes.get() || oldAcidEyesBooleanSupplier.getAsBoolean());
            modEventBus.addListener(this::finishLoading);
            modEventBus.addListener(this::addLayer);
        });
        // iChunutil need
//        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> LOGGER.log(Level.ERROR, "You are loading " + MOD_NAME + " on a server. " + MOD_NAME + " is a client only mod!"));

        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
//        modLoadingContext.registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    @OnlyIn(Dist.CLIENT)
    private void finishLoading(FMLLoadCompleteEvent event) {
        HeadHandler.init(); //initialise our head trackers
    }

    @OnlyIn(Dist.CLIENT)
    private void addLayer(EntityRenderersEvent.AddLayers event) {
        EventHandler.addLayers(event); //Let's add the layers
    }

    private void processIMC(InterModProcessEvent event) {
        event.getIMCStream(m -> m.equalsIgnoreCase("headinfo")).forEach(msg -> {
            Object o = msg.messageSupplier().get();
            if (o instanceof String s) {
                HeadHandler.IMC_HEAD_INFO.add(s);
                GooglyEyes.LOGGER.info("IMC-headinfo: Added HeadInfo json for interpretation later from: {}", msg.getSenderModId());
            } else if (o instanceof HeadInfo.HeadHolder headInfo) {
                if (headInfo.clz == null || headInfo.info == null) //just in case
                {
                    GooglyEyes.LOGGER.warn("IMC-headinfo: Custom HeadInfo has null object from mod: {}", msg.getSenderModId());
                } else {
                    HeadHandler.IMC_HEAD_INFO_OBJ.add(headInfo);
                    GooglyEyes.LOGGER.info("IMC-headinfo: Caching custom HeadInfo {} from: {}", headInfo.info.getClass().getSimpleName(), msg.getSenderModId());
                }
            } else {
                GooglyEyes.LOGGER.warn("IMC-headinfo: {} passed HeadInfo object is not a string: {}", msg.getSenderModId(), o);
            }
        });
    }
}
