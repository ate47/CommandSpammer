package com.ate.chatOptions;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiSpammer extends GuiScreen {
	public boolean doesGuiPauseGame() {
		return false;
	}
	public GuiScreen Last;
	public GuiSpammer(GuiScreen Last){
		this.Last=Last;
	}
	public GuiTextField msg,tick,nb;
	public GuiButton send, stoponclose, renderoverlay, showbutton;
	public void initGui() {
		buttonList.add(new GuiButton(2, width/2+1, height/2-41,100,20, I18n.format("chatOption.spammer.try")));
		buttonList.add(new GuiButton(0, width/2+1, height/2-21,100,20, I18n.format("gui.done")));
		buttonList.add(send=new GuiButton(1, width/2+1, height/2,100,20, I18n.format("chatOption.spammer.send")));
		buttonList.add(stoponclose=new GuiButton(3, width/2-100, height/2+21,I18n.format("chatOption.cmd.set.stoponclose")));
		buttonList.add(renderoverlay=new GuiButton(4, width/2-100, height/2+42,I18n.format("chatOption.cmd.set.renderoverlay")));
		buttonList.add(showbutton=new GuiButton(5, width/2-100, height/2+63,I18n.format("chatOption.cmd.set.showbutton")));
		msg =new GuiTextField(0, fontRendererObj, width/2-99, height/2-41, 98, 18);
		tick=new GuiTextField(0, fontRendererObj, width/2-99, height/2-20, 98, 18);
		nb  =new GuiTextField(0, fontRendererObj, width/2-99, height/2+1, 98, 18);
		msg.setMaxStringLength(99);
		msg.setText(ModMain.spamCommand);
		tick.setText(String.valueOf(ModMain.ticktowait));
		nb.setText(String.valueOf(ModMain.numberOfAction));
		super.initGui();
	}
	private boolean isEnable(){
		boolean a=true;
		a=!msg.getText().isEmpty();
		try {
			Integer.valueOf(tick.getText());
			tick.setTextColor(Color.WHITE.getRGB());
		} catch (Exception e) {
			tick.setTextColor(Color.RED.getRGB());
			a=false;
		}
		try {
			Integer.valueOf(nb.getText());
			nb.setTextColor(Color.WHITE.getRGB());
		} catch (Exception e) {
			nb.setTextColor(Color.RED.getRGB());
			a=false;
		}
		return a;
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		ModMain.drawRightString(fontRendererObj, I18n.format("chatOption.spammer.msg")+" : /",msg.xPosition,msg.yPosition,18,Color.WHITE.getRGB());
		ModMain.drawRightString(fontRendererObj, I18n.format("chatOption.spammer.tick")+" : ",tick.xPosition,tick.yPosition,18,Color.WHITE.getRGB());
		ModMain.drawRightString(fontRendererObj, I18n.format("chatOption.spammer.nb")+" : ",nb.xPosition,nb.yPosition,18,Color.WHITE.getRGB());
		send.enabled=isEnable();
		msg.drawTextBox();
		tick.drawTextBox();
		nb.drawTextBox();
		if(ModMain.isEnable && !ModMain.renderGameOverlay)
			ModMain.drawRightString(Minecraft.getMinecraft().fontRendererObj, String.valueOf(ModMain.action) + " / " + String.valueOf(ModMain.numberOfAction), 
					width, 0, Color.CYAN.getRGB());
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		msg.mouseClicked(mouseX, mouseY, mouseButton);
		tick.mouseClicked(mouseX, mouseY, mouseButton);
		nb.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id==0)mc.displayGuiScreen(Last);
		if(button.id==1){
			if(ModMain.isEnable){
				ModMain.isEnable=false;
			} else {
				ModMain.action=0;
				ModMain.tickw=0;
				ModMain.isEnable=true;
			}
		}
		if(button.id==2)ModMain.sendMessage(mc, "/"+ModMain.spamCommand);
		if(button.equals(stoponclose)) ModMain.stopOnClose=!ModMain.stopOnClose;
		if(button.equals(renderoverlay)) ModMain.renderGameOverlay=!ModMain.renderGameOverlay;
		if(button.equals(showbutton)) ModMain.showButton=!ModMain.showButton;
		
		super.actionPerformed(button);
	}
	public void updateScreen() {
		msg.updateCursorCounter();
		tick.updateCursorCounter();
		nb.updateCursorCounter();
		if(ModMain.isEnable) send.displayString = I18n.format("chatOption.spammer.stop");
		else send.displayString = I18n.format("chatOption.spammer.send");
		ModMain.spamCommand=msg.getText();
		try{ModMain.numberOfAction=Integer.valueOf(nb.getText());}catch (Exception e) {}
		try{ModMain.ticktowait=Integer.valueOf(tick.getText());}catch (Exception e) {}
		stoponclose.packedFGColour = getColor(ModMain.stopOnClose);
		renderoverlay.packedFGColour = getColor(ModMain.renderGameOverlay);
		showbutton.packedFGColour = getColor(ModMain.showButton);
		super.updateScreen();
	}
	public static int getColor(boolean b) {
		if(b)return new Color(85, 255, 85).getRGB();
		else return new Color(255, 85, 85).getRGB();
	}
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		msg.textboxKeyTyped(typedChar, keyCode);
		tick.textboxKeyTyped(typedChar, keyCode);
		nb.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	public void onGuiClosed() {
		ModMain.saveConfig();
		super.onGuiClosed();
	}
}