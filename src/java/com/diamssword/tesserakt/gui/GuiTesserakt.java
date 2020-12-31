package com.diamssword.tesserakt.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.packets.PacketSendEnable;
import com.diamssword.tesserakt.packets.PacketSendIO;
import com.diamssword.tesserakt.packets.PacketSendNamesClient;
import com.diamssword.tesserakt.storage.TesseraktData;
import com.diamssword.tesserakt.tileentity.TesseraktTile;

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

public class GuiTesserakt extends GuiScreen {
	Container cont;
	int midX;
	int midY;
	private static final ResourceLocation BG_TEXTURE =new ResourceLocation(Main.MODID+":textures/gui/tesserakt.png"); 
	private GuiTextField idField;
	private GuiTextField nameField;
	private int xSize;
	private int ySize;
	private int offset= 0;
	private IOGuiButton ioEn;
	private IOGuiButton ioFl;
	private IOGuiButton ioIt;
	private IOGuiButton ioRs;
	private TesseraktTile tile;
	private int color = 0;

	public GuiTesserakt(TesseraktTile tile) {
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
		this.idField = new GuiTextField(10, this.fontRenderer,  midX+ 102,  midY+26, 26, 12);
		this.nameField = new GuiTextField(11, this.fontRenderer,  midX+8,  midY+42, 128, 12);
		this.idField.setMaxStringLength(3);
		this.idField.setFocused(false);
		this.idField.setText(tile.getChannel()+"");
		this.nameField.setMaxStringLength(20);
		if(tile.getOwner() != null)
		{
			if(TesseraktData.privatenames != null)
			{
				Map<Integer,String> map=TesseraktData.privatenames.get(tile.getOwner());
				if(map != null)
				{
					String txt=map.get(tile.getChannel());
					if(txt == null)
						txt="";
					this.nameField.setText(txt);
				}
			}
		}
		else if(TesseraktData.names != null)
		{
			String txt=TesseraktData.names.get(tile.getChannel());
			if(txt == null)
				txt="";
			this.nameField.setText(txt);
		}
		this.buttonList.add(new CGuiButton(1, midX+131, midY+18, 22, 20,208,192, BG_TEXTURE));
		this.buttonList.add(new CGuiButton(2, midX+151, midY+18, 22, 20,228,192, BG_TEXTURE));
		this.buttonList.add(new CGuiButton(3, midX+139, midY+40, 16, 16,208,128, BG_TEXTURE));
		this.buttonList.add(new CGuiButton(4, midX+139+16, midY+40, 16, 16,224,128, BG_TEXTURE));
		this.buttonList.add(new CGuiButton(5, midX+138, midY+57, 16, 16,208,64, BG_TEXTURE));
		this.buttonList.add(new CGuiButton(6, midX+138, midY+160-16, 16, 16,224,64, BG_TEXTURE));
		this.buttonList.add(ioEn=new IOGuiButton(7, midX+172-16, midY+57, 16, 16,208,0, BG_TEXTURE, tile.getIoEnergy(), "Energy"));
		this.buttonList.add(ioIt=new IOGuiButton(8, midX+172-16, midY+57+17, 16, 16,208,0, BG_TEXTURE, tile.getIoItem(), "Items"));
		this.buttonList.add(ioFl=new IOGuiButton(9, midX+172-16, midY+57+17+17, 16, 16,208,0, BG_TEXTURE, tile.getIoFluid(), "Fluids"));
		this.buttonList.add(ioRs=new IOGuiButton(10, midX+172-16, midY+57+17+17+17, 16, 16,174,192, BG_TEXTURE, tile.getIoRedstone(), "Redstone"));
		ioRs.setModesStrings("Disabled","Input","Output","Control");
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
		this.idField.drawTextBox();
		this.nameField.drawTextBox();
		Map<Integer,String> names = new HashMap<Integer,String>();
		if(tile.getOwner() != null)
		{
			if(TesseraktData.privatenames != null)
			{
				Map<Integer,String> map1=TesseraktData.privatenames.get(tile.getOwner());
				if(map1 ==null)
					map1= new HashMap<Integer,String>();
				names=map1;
			}
		}
		else
		{
			names=TesseraktData.names;
		}
		if(names != null)
		{
			Integer[] keys =names.keySet().toArray(new Integer[0]);
			for(int i = offset;i<offset+10;i++)
			{
				if(i>=keys.length) break;
				if(keys[i].equals(tile.getChannel()))
				{

					mc.getTextureManager().bindTexture(BG_TEXTURE);
					drawTexturedModalRect(midX+8, midY+70+(10*(i-offset))-1, 0, 166, 128, 10);
				}
				this.drawString(this.fontRenderer, names.get(keys[i]), midX+9, midY+70+(10*(i-offset)), Color.white.getRGB());
			}
		}
		ioEn.setMode(tile.getIoEnergy());
		ioIt.setMode(tile.getIoItem());
		ioFl.setMode(tile.getIoFluid());
		if(color >509)
			color=0;
		else
			color++;

		int col=color;
		if(color > 255)
			col = 255-(color-255);
		String name = I18n.format("Tesserakt");
		this.drawString(this.fontRenderer, name, midX+5, midY+20,new Color(3,col,252).getRGB());
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button.id == 1 && !this.idField.getText().isEmpty()) //enable
		{
			try {
				int chan = Integer.parseInt(this.idField.getText());
				Main.network.sendToServer(new PacketSendEnable(chan,true,this.tile.getPos()));
				tile.setChannelAndActivate(chan);
			}catch(NumberFormatException e) {}
		}
		if(button.id == 2) //disable
		{
			Main.network.sendToServer(new PacketSendEnable(0,false,this.tile.getPos()));
			tile.desactivate();
		}
		if(button.id == 3 && !this.idField.getText().isEmpty()) //add name
		{
			try {
				int chan = Integer.parseInt(this.idField.getText());
				String name =this.nameField.getText().trim();
				if(!name.isEmpty())
				{
					if(tile.getOwner() != null)
						Main.network.sendToServer(new PacketSendNamesClient(chan,name,tile.getOwner()));
					else
						Main.network.sendToServer(new PacketSendNamesClient(chan,name));
				}
			}catch(NumberFormatException e) {}
		}
		if(button.id == 4 && !this.idField.getText().isEmpty()) //remove name
		{
			try {
				int chan = Integer.parseInt(this.idField.getText());
				if(tile.getOwner() != null)
					Main.network.sendToServer(new PacketSendNamesClient(chan,"",true,tile.getOwner()));
				else
				Main.network.sendToServer(new PacketSendNamesClient(chan,"",true));
			}catch(NumberFormatException e) {}
		}
		if(button.id == 5 )
		{
			if(this.offset >0)
				this.offset--;
		}
		if(button.id == 6 ) 
		{
			this.offset++;
		}
		if(button.id == 7)
		{
			int i = tile.getIoEnergy()+1;
			if(i >3)
				i=0;
			tile.setIO(0, i);
			((IOGuiButton) button).setMode(i);
			Main.network.sendToServer(new PacketSendIO(tile.getPos(), 0, i));
		}
		if(button.id == 8)
		{
			int i = tile.getIoItem()+1;
			if(i >3)
				i=0;
			tile.setIO(1, i);
			((IOGuiButton) button).setMode(i);
			Main.network.sendToServer(new PacketSendIO(tile.getPos(), 1, i));
		}
		if(button.id == 9)
		{
			int i = tile.getIoFluid()+1;
			if(i >3)
				i=0;
			tile.setIO(2, i);
			((IOGuiButton) button).setMode(i);
			Main.network.sendToServer(new PacketSendIO(tile.getPos(), 2, i));
		}
		if(button.id == 10)
		{
			int i = tile.getIoRedstone()+1;
			if(i >3)
				i=0;
			tile.setIO(3, i);
			((IOGuiButton) button).setMode(i);
			Main.network.sendToServer(new PacketSendIO(tile.getPos(), 3, i));
		}
	}
	private void checkListClick(int mouseX, int mouseY)
	{
		int i = mouseX-midX;
		int j = mouseY-midY;
		if(i >=8 && j>=68 && i<=135 && j <=169)
			if(TesseraktData.names != null)
			{
				Integer[] keys =TesseraktData.names.keySet().toArray(new Integer[0]);
				int selected = ((j - 68)/10);
				if(selected+offset < keys.length)
				{
					this.nameField.setText(TesseraktData.names.get(keys[selected+offset]));
					this.idField.setText(keys[selected+offset]+"");
				}
			}
	}
	private void checkListClickPrivate(int mouseX, int mouseY)
	{
		int i = mouseX-midX;
		int j = mouseY-midY;
		if(i >=8 && j>=68 && i<=135 && j <=169)
		{
			if(TesseraktData.privatenames != null)
			{
				Map<Integer,String> map= TesseraktData.privatenames.get(tile.getOwner());
				if(map != null)
				{
					Integer[] keys =map.keySet().toArray(new Integer[0]);
					int selected = ((j - 68)/10);
					if(selected+offset < keys.length)
					{
						this.nameField.setText(map.get(keys[selected+offset]));
						this.idField.setText(keys[selected+offset]+"");
					}
				}
			}
		}
	}
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.idField.mouseClicked(mouseX, mouseY, mouseButton);
		this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
		if(tile.getOwner() != null)
			this.checkListClickPrivate(mouseX, mouseY);
		else
			this.checkListClick(mouseX, mouseY);
	}
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
		if(GuiScreen.isKeyComboCtrlA(keyCode) || GuiScreen.isKeyComboCtrlC(keyCode) || GuiScreen.isKeyComboCtrlV(keyCode) || GuiScreen.isKeyComboCtrlX(keyCode) || (typedChar >='0' && typedChar<='9')|| keyCode==14|| keyCode==203||keyCode==200||keyCode==205||keyCode==208)
		{
			this.idField.textboxKeyTyped(typedChar, keyCode);
		}
		this.nameField.textboxKeyTyped(typedChar, keyCode);
		if(!this.nameField.isFocused())
		{
			if(Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode() == keyCode)
			{
				this.mc.displayGuiScreen((GuiScreen)null);

				if (this.mc.currentScreen == null)
				{
					this.mc.setIngameFocus();
				}
			}
		}
	}
	public void updateScreen()
	{
		this.idField.updateCursorCounter();
		this.nameField.updateCursorCounter();
	}

}