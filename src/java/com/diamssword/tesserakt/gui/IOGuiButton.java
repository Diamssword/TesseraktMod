package com.diamssword.tesserakt.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class IOGuiButton extends GuiButton{
private int textX;
private int textY;
private int mode;
private ResourceLocation img;
private String[] modes = new String[] {"Disabled","Input","Output","In/Out"};
	public IOGuiButton(int id, int xPos, int yPos, int width, int height,int textX,int textY,ResourceLocation texture,int mode,String text) {
		super(id, xPos, yPos, width, height, "");
		this.textX=textX;
		this.textY=textY;
		img = texture;
		this.mode=mode;
		this.displayString = text;
	}
	public void setModesStrings(String... modes)
	{
		this.modes = modes;
	}
	@Override
	 protected int getHoverState(boolean mouseOver)
    {
        int i = 0;

        if (!this.enabled)
        {
            i = 2;
        }
        else if (mouseOver)
        {
            i = 1;
        }

        return i;
    }
	public void setMode(int mode)
	{
		this.mode=mode;
	}
	  @Override
	    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial)
	    {
	        if (this.visible)
	        {
	            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	            int k = this.getHoverState(this.hovered);  
	            mc.getTextureManager().bindTexture(img);
	            GlStateManager.enableBlend();
	            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	            this.drawTexturedModalRect(this.x, this.y, this.textX, this.textY +  (k * this.height), this.width , this.height);
	            this.drawTexturedModalRect(this.x, this.y, this.textX+this.width, this.textY +  (mode * this.height), this.width , this.height);
	      //      this.drawTexturedModalRect(img, this.x, this.y, this.textX, this.textY + (k * this.height), this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
	            this.mouseDragged(mc, mouseX, mouseY);
	        	if(this.hovered)
	        	{
					this.drawHoveringText(this.displayString+":"+modes[this.mode], mouseX, mouseY);
	        	}
	        }
	    }
	  protected void drawHoveringText(String textLine, int x, int y)
		{
			List<String> ls= new ArrayList<String>();
			ls.add(textLine);
			net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(ls, x, y, Minecraft.getMinecraft().currentScreen.width, Minecraft.getMinecraft().currentScreen.height, -1, Minecraft.getMinecraft().fontRenderer);
		}
}
