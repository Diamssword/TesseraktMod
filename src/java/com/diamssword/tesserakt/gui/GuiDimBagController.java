package com.diamssword.tesserakt.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.dimensional_bag.DimBagControllerTile;
import com.diamssword.tesserakt.packets.PacketSendEnable;
import com.diamssword.tesserakt.storage.TesseraktData;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiDimBagController extends GuiScreen {
	Container cont;
	int midX;
	int midY;
	private static final ResourceLocation BG_TEXTURE =new ResourceLocation(Main.MODID+":textures/gui/tesserakt.png"); 
	private int xSize;
	private int ySize;
	private int offset= 0;
	GuiDropDown dropdown;
	GuiSlider red;
	GuiSlider green;
	GuiSlider blue;
	private DimBagControllerTile tile;
	private int color = 0;

	public GuiDimBagController(DimBagControllerTile tile) {
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
		List<String> strs = new ArrayList<String>();
		strs.add("hey");
		strs.add("you");
		strs.add("are");
		strs.add("beautiful");
		strs.add("really");
		this.buttonList.add(dropdown=new GuiDropDown(1, midX+5, midY+20, 100, strs, null));
		this.buttonList.add(red=new GuiSlider(2, midX+5, midY+100, 80,12, "R: ", "",0d,255d,125d,false, true));
		this.buttonList.add(green=new GuiSlider(3, midX+5, midY+114, 80,12, "G: ", "",0d,255d,125d,false, true));
		this.buttonList.add(blue=new GuiSlider(4, midX+5, midY+128, 80,12, "B: ", "",0d,255d,125d,false, true));
		/*this.buttonList.add(new CGuiButton(1, midX+131, midY+18, 22, 20,208,192, BG_TEXTURE));
		this.buttonList.add(new CGuiButton(2, midX+151, midY+18, 22, 20,228,192, BG_TEXTURE));
		this.buttonList.add(new CGuiButton(3, midX+139, midY+40, 16, 16,208,128, BG_TEXTURE));
		this.buttonList.add(new CGuiButton(4, midX+139+16, midY+40, 16, 16,224,128, BG_TEXTURE));
		this.buttonList.add(new CGuiButton(5, midX+138, midY+57, 16, 16,208,64, BG_TEXTURE));
		this.buttonList.add(new CGuiButton(6, midX+138, midY+160-16, 16, 16,224,64, BG_TEXTURE));
		this.buttonList.add(ioEn=new IOGuiButton(7, midX+172-16, midY+57, 16, 16,208,0, BG_TEXTURE, tile.getIoEnergy(), "Energy"));
		this.buttonList.add(ioIt=new IOGuiButton(8, midX+172-16, midY+57+17, 16, 16,208,0, BG_TEXTURE, tile.getIoItem(), "Items"));
		this.buttonList.add(ioFl=new IOGuiButton(9, midX+172-16, midY+57+17+17, 16, 16,208,0, BG_TEXTURE, tile.getIoFluid(), "Fluids"));
		this.buttonList.add(ioRs=new IOGuiButton(10, midX+172-16, midY+57+17+17+17, 16, 16,174,192, BG_TEXTURE, tile.getIoRedstone(), "Redstone"));*/
		//ioRs.setModesStrings("Disabled","Input","Output","Control");
	}
	public void drawDefaultBackground()
	{
		super.drawDefaultBackground();
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		midX = (this.width - this.xSize) / 2;
		midY = ((this.height - this.ySize) / 2)+10;
		drawTexturedModalRect(midX, midY+10, 0, 0, xSize, ySize);
		drawTexturedModalRect(midX+176, midY+66, 176, 56, 20, 80);
		this.itemRender.renderItemIntoGUI(new ItemStack(Blocks.COBBLESTONE), midX+173, midY+57+28);
		this.itemRender.renderItemIntoGUI(new ItemStack(Items.WATER_BUCKET), midX+173, midY+57+44);
		this.itemRender.renderItemIntoGUI(new ItemStack(Items.REDSTONE), midX+173, midY+57+44+14);

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
		if(TesseraktData.names != null)
		{
			Integer[] keys =TesseraktData.names.keySet().toArray(new Integer[0]);
			for(int i = offset;i<offset+10;i++)
			{
				if(i>=keys.length) break;
			//	if(keys[i].equals(tile.getChannel()))
				{

					mc.getTextureManager().bindTexture(BG_TEXTURE);
					drawTexturedModalRect(midX+8, midY+70+(10*(i-offset))-1, 0, 166, 128, 10);
				}
				this.drawString(this.fontRenderer, TesseraktData.names.get(keys[i]), midX+9, midY+70+(10*(i-offset)), Color.white.getRGB());
			}
		}
		if(color >509)
			color=0;
		else
			color++;

		int col=color;
		if(color > 255)
			col = 255-(color-255);
		String name = I18n.format("tile.tesserakt.dim_bag_controller.name");
		this.drawString(this.fontRenderer, name, midX+5, midY+20,new Color(3,col,252).getRGB());
		
		GuiDimBagController.drawRect(midX+90, midY+112, midX+120, midY+142, new Color(red.getValueInt(),green.getValueInt(),blue.getValueInt()).getRGB());
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		
		if(button.id == 2) //disable
		{
			Main.network.sendToServer(new PacketSendEnable(0,false,this.tile.getPos()));
			//tile.desactivate();
		}
		if(button.id == 5 ) //remove name
		{
			if(this.offset >0)
				this.offset--;
		}
		if(button.id == 6 ) //remove name
		{
			this.offset++;
		}
	
	}
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		dropdown.handleClick(mc, mouseX, mouseY);
	}
	public void updateScreen()
	{
		super.updateScreen();
	}

}