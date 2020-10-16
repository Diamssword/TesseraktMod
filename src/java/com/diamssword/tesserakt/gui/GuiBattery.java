package com.diamssword.tesserakt.gui;

import java.awt.Color;
import java.io.IOException;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.packets.PacketSendEnable;
import com.diamssword.tesserakt.packets.PacketSendIO;
import com.diamssword.tesserakt.packets.PacketSendNamesClient;
import com.diamssword.tesserakt.storage.TesseraktData;
import com.diamssword.tesserakt.tileentity.ExponentialBatteryTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiBattery extends GuiScreen {
	Container cont;
	int midX;
	int midY;
	private static final ResourceLocation BG_TEXTURE =new ResourceLocation(Main.MODID+":textures/gui/battery.png"); 
	private int xSize;
	private int ySize;
	private ExponentialBatteryTile tile;
	private int color;
	public GuiBattery(ExponentialBatteryTile tile) {
		super();
		this.tile=tile;
		this.xSize = 176;
		this.ySize = 166;
		midX = (this.width - this.xSize) / 2;
		midY = (this.height - this.ySize) / 2;

	}
	public void initGui()
	{
		super.initGui();
		midX = (this.width - this.xSize) / 2;
		midY = ((this.height - this.ySize) / 2)+20;
	}
	public void drawDefaultBackground()
	{
		super.drawDefaultBackground();
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		midX = (this.width - this.xSize) / 2;
		midY = ((this.height - this.ySize) / 2)+10;
		drawTexturedModalRect(midX, midY+10, 0, 0, xSize, ySize);
		double size=tile.getPercentFilled();
		int i=(int)(100d*size);
		drawTexturedModalRect(midX+13, (int)(midY+10+31+(100d-i)), 176, 0, 10, i);
		size=tile.getAdditionalPercentFilled();
		i=(int)(100d*size);
		drawTexturedModalRect(midX+153, (int)(midY+10+31+(100d-i)), 176, 0, 10,i);

		//GlStateManager.color(colorRed, colorGreen, colorBlue);
	}

	public boolean doesGuiPauseGame()
	{
		return false;
	}
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.tile = (ExponentialBatteryTile) this.mc.world.getTileEntity(this.tile.getPos());
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		if(color >200)
			color=0;
		else
			color++;

		int col=color;
		if(color > 100)
			col = 100-(color-100);
		if(col <0)
			col=0;
		String name = I18n.format("Exponential Battery");
		int color = new Color(255,col,0).getRGB();
		this.fontRenderer.drawString(name, midX+5, midY+15,color);

		this.fontRenderer.drawString("I/O: "+formatPower(this.tile.getMaxIO()) +"FE", midX+28, midY+80,4210752);
		this.fontRenderer.drawString("Stored: "+formatPower(this.tile.getEnergy()) +"FE", midX+28, midY+40,4210752);
		this.fontRenderer.drawString("Max: "+formatPower(this.tile.getMaxEnergy()) +"FE", midX+28, midY+60,4210752);
	}
	private String formatPower(int pow)
	{

		if(pow >=1000000000)
			return cutNb(pow/1000000000d)+"G";
		if(pow >=1000000)
			return cutNb(pow/1000000d)+"M";
		if(pow >=1000d)
			return cutNb(pow/1000d)+"K";
		return pow+"";
	}
	private String cutNb(double nb)
	{
		String st = nb+"";
		return st.substring(0,Math.min(st.length(),5));
	}
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
	}

}