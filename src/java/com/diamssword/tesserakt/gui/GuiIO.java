package com.diamssword.tesserakt.gui;

import java.io.IOException;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.packets.PacketSendIOEnergy;
import com.diamssword.tesserakt.utils.IOTileInterface;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GuiIO extends GuiScreen {
	Container cont;
	int midX;
	int midY;
	private static final ResourceLocation BG_TEXTURE =new ResourceLocation(Main.MODID+":textures/gui/io.png"); 
	private int xSize;
	private int ySize;
	private IOGuiButton ioN;
	private IOGuiButton ioS;
	private IOGuiButton ioE;
	private IOGuiButton ioW;
	private IOGuiButton ioU;
	private CGuiButton caller;
	private CGuiButton close;
	private GuiScreen backscreen;
	private BlockPos pos;
	private IOTileInterface inter;
	public GuiIO(IOTileInterface tile,GuiScreen screen,BlockPos pos) {
		super();
		this.inter=tile;
		this.pos=pos;
		this.xSize = 100;
		this.ySize = 100;
		midX = (this.width - this.xSize) / 2;
		midY = (this.height - this.ySize) / 2;
		backscreen=screen;
	}
	public void initGui()
	{
		super.initGui();
		midX = (this.width - this.xSize) / 2;
		midY = ((this.height - this.ySize) / 2)+20;
		this.buttonList.add(close=new CGuiButton(99, midX+80, midY+4, 16, 16,148,0, BG_TEXTURE));
		this.buttonList.add(ioN=new IOGuiButton(1, midX+40, midY+40, 16, 16,100,0, BG_TEXTURE,inter.getMode(EnumFacing.NORTH), "North"));
		this.buttonList.add(ioS=new IOGuiButton(2, midX+40+20, midY+40+20, 16, 16,100,0, BG_TEXTURE,inter.getMode(EnumFacing.SOUTH), "South"));
		this.buttonList.add(ioE=new IOGuiButton(3, midX+40-20, midY+40, 16, 16,100,0, BG_TEXTURE,inter.getMode(EnumFacing.EAST), "East"));
		this.buttonList.add(ioW=new IOGuiButton(4, midX+40+20, midY+40, 16, 16,100,0, BG_TEXTURE,inter.getMode(EnumFacing.WEST), "West"));
		this.buttonList.add(ioU=new IOGuiButton(5, midX+40, midY+40-20, 16, 16,100,0, BG_TEXTURE,inter.getMode(EnumFacing.UP), "Up"));
		this.buttonList.add(new IOGuiButton(6, midX+40, midY+40+20, 16, 16,100,0, BG_TEXTURE,inter.getMode(EnumFacing.DOWN), "Down"));
	}
	public CGuiButton getExternalButton(int posX,int posY)
	{
		if(caller == null)
			caller=new CGuiButton(1298, posX, posY, 16, 16,132,0, BG_TEXTURE);
		return caller;
	}
	public void checkAction(GuiButton bt)
	{
		if(bt == caller)
		{
			Minecraft.getMinecraft().displayGuiScreen(this);
		}
	}
	public void drawDefaultBackground()
	{
		super.drawDefaultBackground();
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		midX = (this.width - this.xSize) / 2;
		midY = ((this.height - this.ySize) / 2)+10;
		drawTexturedModalRect(midX, midY+10, 0, 0, xSize, ySize);
		this.itemRender.renderItemAndEffectIntoGUI(inter.getItemDisplay(), midX+5, midY+15);

	}

	public boolean doesGuiPauseGame()
	{
		return false;
	}
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		drawTexturedModalRect( midX+40, midY+40+10,(16*3), 100, 16, 16);
		drawTexturedModalRect( midX+60, midY+60+10,(16*4), 100, 16, 16);
		drawTexturedModalRect( midX+20, midY+40+10,(16*2), 100, 16, 16);
		drawTexturedModalRect( midX+60, midY+40+10,(16*5), 100, 16, 16);
		drawTexturedModalRect( midX+40, midY+20+10,0, 100, 16, 16);
		drawTexturedModalRect( midX+40, midY+60+10,(16*1), 100, 16, 16);


	}
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button == close)
		{
			Minecraft.getMinecraft().displayGuiScreen(this.backscreen);
		}
		if(button instanceof IOGuiButton)
		{
			EnumFacing f=getEnumFor((IOGuiButton) button);
			int i = inter.getMode(f)+1;
			if(i >2)
				i=0;
			inter.setMode(f, i);
			((IOGuiButton) button).setMode(i);
			Main.network.sendToServer(new PacketSendIOEnergy(this.pos,i, f));
		}
	}
	public EnumFacing getEnumFor(IOGuiButton btn)
	{
		if(btn ==ioN)
			return EnumFacing.NORTH;
		if(btn ==ioS)
			return EnumFacing.SOUTH;
		if(btn ==ioE)
			return EnumFacing.EAST;
		if(btn ==ioW)
			return EnumFacing.WEST;
		if(btn ==ioU)
			return EnumFacing.UP;
		if(btn ==ioU)
			return EnumFacing.DOWN;
		return EnumFacing.DOWN;
	}
}