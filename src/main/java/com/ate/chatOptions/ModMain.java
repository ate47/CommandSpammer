package com.ate.chatOptions;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

@Mod(name=ModMain.NAME,version=ModMain.VERSION,clientSideOnly=true, modid=ModMain.MODID)
public class ModMain {
	public static final String MODID="chatoptions",NAME="ChatOptions",VERSION="1.1";
	public static String spamCommand="";
	public static int ticktowait=100;
	public static int numberOfAction=10;
	public static boolean showButton = true;
	public static boolean renderGameOverlay = true;
	public static boolean stopOnClose = false;
	public static List<TextOption> textOptions = new ArrayList<TextOption>();

	public static int action=0;
	public static int tickw=0;
	public static boolean isEnable=false;
	public static boolean hasDoFakemsg=false;
	
	public static Configuration cfg;
	public static void syncConfig(){
		cfg.setCategoryComment("ChatOptions", "ChatOptions");
		spamCommand=cfg.getString("spamCommand", "ChatOptions", spamCommand, "");
		ticktowait=cfg.getInt("ticktowait", "ChatOptions", ticktowait, 0, Integer.MAX_VALUE, "", spamCommand);
		numberOfAction=cfg.getInt("numberOfAction", "ChatOptions", numberOfAction, 0, Integer.MAX_VALUE, "");
		stopOnClose=cfg.getBoolean("stopOnClose", "ChatOptions", stopOnClose, "stop the iteration when you close the menu");
		showButton=cfg.getBoolean("showButton", "gui", showButton, "Set to false to hide the Spammer button");
		renderGameOverlay=cfg.getBoolean("renderGameOverlay", "gui", renderGameOverlay, "Render iteration count out of the menu");
		cfg.save();
	}
	public static void saveConfig(){
		cfg.get("ChatOptions", "spamCommand", spamCommand).set(spamCommand);
		cfg.get("ChatOptions", "ticktowait", ticktowait).set(ticktowait);
		cfg.get("ChatOptions", "numberOfAction", numberOfAction).set(numberOfAction);
		cfg.get("ChatOptions", "stopOnClose", stopOnClose).set(stopOnClose);
		cfg.get("gui", "showButton", showButton).set(showButton);
		cfg.get("gui", "renderGameOverlay", renderGameOverlay).set(renderGameOverlay);
		cfg.save();
	}
	@Instance
	public static ModMain instance;
	@EventHandler 
	public void preInit(FMLPreInitializationEvent ev){
		textOptions.add(new TextOption() {
			public String getReplacement(Minecraft mc) {return String.valueOf(action);}
			public String getName() {return "i";}
		});
		textOptions.add(new TextOption() {
			public String getReplacement(Minecraft mc) {return mc.thePlayer.getName();}
			public String getName() {return "pname";}
		});
		textOptions.add(new TextOption() {
			public String getReplacement(Minecraft mc) {return String.valueOf((int)mc.thePlayer.posX);}
			public String getName() {return "px";}
		});
		textOptions.add(new TextOption() {
			public String getReplacement(Minecraft mc) {return String.valueOf(mc.thePlayer.posX);}
			public String getName() {return "plx";}
		});
		textOptions.add(new TextOption() {
			public String getReplacement(Minecraft mc) {return String.valueOf((int)mc.thePlayer.posY);}
			public String getName() {return "py";}
		});
		textOptions.add(new TextOption() {
			public String getReplacement(Minecraft mc) {return String.valueOf(mc.thePlayer.posY);}
			public String getName() {return "ply";}
		});
		textOptions.add(new TextOption() {
			public String getReplacement(Minecraft mc) {return String.valueOf((int)mc.thePlayer.posZ);}
			public String getName() {return "pz";}
		});
		textOptions.add(new TextOption() {
			public String getReplacement(Minecraft mc) {return String.valueOf(mc.thePlayer.posZ);}
			public String getName() {return "plz";}
		});
		textOptions.add(new TextOption() {
			public String getReplacement(Minecraft mc) {return String.valueOf(mc.thePlayer.getHealth());}
			public String getName() {return "hp";}
		});
		textOptions.add(new TextOption() {
			public String getReplacement(Minecraft mc) {return String.valueOf(mc.thePlayer.getMaxHealth());}
			public String getName() {return "mhp";}
		});
		textOptions.add(new TextOption() {
			public String getReplacement(Minecraft mc) {if(mc.thePlayer.getTeam()!=null)return String.valueOf(mc.thePlayer.getTeam().getRegisteredName());else return "?";}
			public String getName() {return "tname";}
		});
		textOptions.sort(new Comparator<TextOption>() {
			public int compare(TextOption o1, TextOption o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		cfg=new Configuration(new File(new File(ev.getModConfigurationDirectory(), "ATEMod"),"ChatOptions.cfg"));
		syncConfig();
		MinecraftForge.EVENT_BUS.register(instance);
		FMLCommonHandler.instance().bus().register(instance);
		ClientCommandHandler.instance.registerCommand(new SpammerCommand());
	}
	public static GuiButton spamer;
	@SubscribeEvent
	public void initGui(InitGuiEvent ev){
		if(ev.gui.mc.currentScreen instanceof GuiChat && showButton)
			ev.buttonList.add(spamer=new GuiButton(1901, ev.gui.width-101, ev.gui.height-42,100,20, I18n.format("chatOption.spammer.button")));
	}
	@SubscribeEvent
	public void actionGui(ActionPerformedEvent ev){
		if(ev.gui.mc.currentScreen instanceof GuiChat && ev.button.equals(spamer) && showButton)ev.gui.mc.displayGuiScreen(new GuiSpammer(ev.gui));
	}
	@SubscribeEvent
	public void onDisconnect(ClientDisconnectionFromServerEvent ev) {
		isEnable = false;
	}
	@SubscribeEvent
	public void onConnect(ClientConnectedToServerEvent ev) {
		isEnable = false;
	}
	@SubscribeEvent
	public void onTick(TickEvent ev) {
		if(isEnable){
			Minecraft mc = Minecraft.getMinecraft();
			if(!(mc.currentScreen instanceof GuiSpammer) && stopOnClose) {
				isEnable = false;
			}else if(action<numberOfAction){
				if(tickw==ticktowait)tickw=0;
				if(tickw==0){
					if(action%5==0 && !hasDoFakemsg){
						mc.thePlayer.sendChatMessage("/"+new Random());
						hasDoFakemsg=true;
					}else{
						hasDoFakemsg=false;
						action++;
						sendMessage(mc, "/"+spamCommand);
					}
				}
				tickw++;
			}else{
				isEnable=false;
			}
		}
	}
	public static void sendMessage(Minecraft mc, String message) {
		if(mc.thePlayer==null)return;
		for (TextOption to: textOptions)
			message=message.replaceAll("[&]"+to.getName(),to.getReplacement(mc));
		mc.thePlayer.sendChatMessage(message);
	}
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent ev) {
		if(isEnable && renderGameOverlay) {
			drawRightString(Minecraft.getMinecraft().fontRendererObj, String.valueOf(action) + " / " + String.valueOf(numberOfAction), 
					ev.resolution.getScaledWidth(), 0, Color.CYAN.getRGB());
			GlStateManager.color(1.0F, 1.0F, 1.0F);
		}
	}
	public static void drawRightString(FontRenderer fontRendererObj, String text,int x,int y,int color){
		drawRightString(fontRendererObj,text, x, y, fontRendererObj.FONT_HEIGHT,color);
	}
	public static void drawRightString(FontRenderer fontRendererObj,String text,int x,int y,int height,int color){
		fontRendererObj.drawString(I18n.format(text), x-fontRendererObj.getStringWidth(I18n.format(text)), y+height/2-fontRendererObj.FONT_HEIGHT/2, color);
	}
}
