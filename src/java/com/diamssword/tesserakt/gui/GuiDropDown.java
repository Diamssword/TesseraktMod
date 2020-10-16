package com.diamssword.tesserakt.gui;

import java.awt.Color;
import java.util.List;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiDropDown extends GuiButton{
	private String selected=null;
	private List<String> choices;
	private boolean dropped=false;
	private int hoveredIndex=-1;
	private static final ResourceLocation img = new ResourceLocation(Main.MODID,"textures/gui/dropdown.png");
	public GuiDropDown(int id, int xPos, int yPos, int width,List<String> choices,@Nullable String selected) {
		super(id, xPos, yPos, width, 14, "");
		this.choices = choices;
		if(!this.choices.isEmpty() && selected == null)
			this.selected = this.choices.get(0);
		else if(selected != null)
			this.selected=selected;

	}
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial)
	{
		if (this.visible)
		{
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

			mc.getTextureManager().bindTexture(img);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			int barSize = this.width-14;

			this.drawTexturedModalRect(this.x, this.y, 0,0, barSize/2 , this.height/2);
			this.drawTexturedModalRect(this.x, this.y+(this.height/2), 0,7, barSize/2 , this.height/2);
			this.drawTexturedModalRect(this.x+(barSize/2), this.y, 79-(barSize/2),0, barSize/2 , this.height/2);
			this.drawTexturedModalRect(this.x+(barSize/2), this.y+(this.height/2), 79-(barSize/2),14-(this.height/2), barSize/2 , this.height/2);

			this.drawTexturedModalRect(this.x+barSize, this.y, 79+(this.dropped?14:0),this.hovered?14:0, 14 , 14);

			if(this.selected != null)
			{
				String buttonText = this.selected;
				int strWidth = mc.fontRenderer.getStringWidth(buttonText);
				int ellipsisWidth = mc.fontRenderer.getStringWidth("...");

				if (strWidth > width - 16 && strWidth > ellipsisWidth)
					buttonText = mc.fontRenderer.trimStringToWidth(buttonText, width - 16 - ellipsisWidth).trim() + "...";   
				this.drawString(mc.fontRenderer,buttonText, this.x+2, this.y+3, Color.white.getRGB());
			}
			if(this.dropped)
				this.drawList(mc, mouseX, mouseY, partial);
			this.mouseDragged(mc, mouseX, mouseY);
		}
	}
	public void handleClick(Minecraft mc, int mouseX, int mouseY)
	{
		if(this.mousePressed(mc, mouseX, mouseY))
		{
			this.dropped = !this.dropped;
		}
		else if(this.dropped)
		{
			if(this.hoveredIndex>-1 && this.hoveredIndex < this.choices.size())
			{
				this.selected = this.choices.get(this.hoveredIndex);
				this.dropped = false;
			}
		}
	}
	private void drawList(Minecraft mc, int mouseX, int mouseY, float partial)
	{
		int barSize = this.width-14;
		int height = (this.choices.size()*10)+1;
		mc.getTextureManager().bindTexture(img);
		this.drawTexturedModalRect(this.x, this.y+this.height, 0,14, barSize/2 , height/2);
		this.drawTexturedModalRect(this.x, this.y+this.height+(height/2), 0,98-(height/2), barSize/2 , height/2);
		this.drawTexturedModalRect(this.x+(barSize/2), this.y+this.height, 79-(barSize/2),14, barSize/2 , height/2);
		this.drawTexturedModalRect(this.x+(barSize/2), this.y+this.height+(height/2), 79-(barSize/2),98-(height/2), barSize/2 , height/2);
		hoveredIndex=-1;
		for(int i=0;i<this.choices.size();i++)
		{
			this.hovered = mouseX >= this.x+1 && mouseY >= this.y+15+(i*10) && mouseX < this.x + barSize && mouseY < this.y +15+(i*10)+10;
			if(this.hovered)
			{
				hoveredIndex=i;
				mc.getTextureManager().bindTexture(img);
				this.drawTexturedModalRect(this.x+1, this.y+14+(i*10), 0,98, barSize/2,10);
				this.drawTexturedModalRect(this.x+1+(barSize/2), this.y+14+(i*10), 78-(barSize/2)+3,98, barSize/2,10);
			}
			this.drawString(mc.fontRenderer, this.choices.get(i), this.x+2, this.y+15+(i*10), Color.white.getRGB());

		}
	}
}
