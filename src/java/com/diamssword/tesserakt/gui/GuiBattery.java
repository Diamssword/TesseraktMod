package com.diamssword.tesserakt.gui;

import java.awt.Color;
import java.io.IOException;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.render.tileentity.TESRExponentialBattery;
import com.diamssword.tesserakt.tileentity.ExponentialBatteryTile;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiBattery extends GuiScreen {
	Container cont;
	int midX;
	int midY;
	private static final ResourceLocation BG_TEXTURE =new ResourceLocation(Main.MODID+":textures/gui/battery.png"); 
	private int xSize;
	private int ySize;
	private ExponentialBatteryTile tile;
	private GuiIO guiio;
	private int color;
	public GuiBattery(ExponentialBatteryTile tile) {
		guiio = new GuiIO(tile,this,tile.getPos());
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
		this.buttonList.add(guiio.getExternalButton(midX+155, midY+4));
	}
	public void drawDefaultBackground()
	{
		super.drawDefaultBackground();
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		midX = (this.width - this.xSize) / 2;
		midY = ((this.height - this.ySize) / 2)+10;
		drawTexturedModalRect(midX, midY+10, 0, 0, xSize, ySize);

		int col =this.tile.getLevel();
		if(col >= 7)
			col=6;

		int[] cls =TESRExponentialBattery.colorForLevel(col);
		GlStateManager.color(cls[0]/255f,cls[1]/255f,cls[2]/255f);
		double size=tile.getPercentFilled();
		int i=(int)(100d*size);
		drawTexturedModalRect(midX+13, (int)(midY+10+31+(100d-i)), 176, 0, 10, i);
		if(tile.getMaxEnergy() != Integer.MAX_VALUE)
		{
			size=tile.getAdditionalPercentFilled();
			i=(int)(100d*size);

			cls =TESRExponentialBattery.colorForLevel(7);
			GlStateManager.color(cls[0]/255f,cls[1]/255f,cls[2]/255f);
			drawTexturedModalRect(midX+153, (int)(midY+10+31+(100d-i)), 176, 0, 10,i);
		}
		else
		{
			GlStateManager.color(1, 1, 1);
			drawTexturedModalRect(midX+153, (int)(midY+10+31), 186, 0, 10,100);
		}
		GlStateManager.color(1, 1, 1);
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

		this.fontRenderer.drawString("I/O: "+formatPower(this.tile.getMaxIO()) +"FE/t", midX+38, midY+80,4210752);
		this.fontRenderer.drawString("Stored: "+formatPower(this.tile.getEnergy()) +"FE", midX+38, midY+40,4210752);
		this.fontRenderer.drawString("Max: "+formatPower(this.tile.getMaxEnergy()) +"FE", midX+38, midY+60,4210752);
		this.fontRenderer.drawString("OverCharge: "+formatPower(this.tile.getAdditionalEnergy()) +"FE", midX+38, midY+100,4210752);
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
		guiio.checkAction(button);
	}

}