package me.ichun.mods.ichunutil.client.core;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandlerClient {



//    @SubscribeEvent
//    public void onRenderTick(TickEvent.RenderTickEvent event)
//    {
//        if(event.phase == TickEvent.Phase.START)
//        {
//            Minecraft mc = Minecraft.getInstance();
//
//            partialTick = event.renderTickTime;
//
//            if(screenWidth != mc.getMainWindow().getFramebufferWidth() || screenHeight != mc.getMainWindow().getFramebufferHeight())
//            {
//                screenWidth = mc.getMainWindow().getFramebufferWidth();
//                screenHeight = mc.getMainWindow().getFramebufferHeight();
//
//                for(Framebuffer buffer : RenderHelper.frameBuffers)
//                {
//                    buffer.resize(screenWidth, screenHeight, Minecraft.IS_RUNNING_ON_MAC);
//                }
//            }
//        }
//    }

//    @SubscribeEvent
//    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event)
//    {
//        if((iChunUtil.configClient.buttonOptionsShiftOpensMods || ObfHelper.isDevEnvironment()) && event.getGui() instanceof IngameMenuScreen)
//        {
//            for(Widget widget : event.getWidgetList())
//            {
//                if(widget instanceof Button && widget.getMessage() instanceof TranslationTextComponent && ((TranslationTextComponent)widget.getMessage()).getKey().equals("menu.options"))
//                {
//                    Button.IPressable oriPress = ((Button)widget).onPress;
//                    ((Button)widget).onPress = button -> {
//                        if(ObfHelper.isDevEnvironment() && !Screen.hasControlDown())
//                        {
//                            if(Screen.hasShiftDown())
//                            {
//                                oriPress.onPress(button);
//                            }
//                            else
//                            {
//                                Minecraft.getInstance().displayGuiScreen(getConfigGui(Minecraft.getInstance(), Minecraft.getInstance().currentScreen));
//                            }
//                        }
//                        else if(iChunUtil.configClient.buttonOptionsShiftOpensMods)
//                        {
//                            if(Screen.hasShiftDown())
//                            {
//                                Minecraft.getInstance().displayGuiScreen(new net.minecraftforge.fml.client.gui.screen.ModListScreen(Minecraft.getInstance().currentScreen));
//                            }
//                            else
//                            {
//                                oriPress.onPress(button);
//                            }
//                        }
//                    };
//                    break;
//                }
//            }
//        }
//    }
}
