package com.diamssword.tesserakt.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CGuiButton extends GuiButton{
private int textX;
private int textY;
private ResourceLocation img;
	public CGuiButton(int id, int xPos, int yPos, int width, int height,int textX,int textY,ResourceLocation texture) {
		super(id, xPos, yPos, width, height, "");
		this.textX=textX;
		this.textY=textY;
		img = texture;
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
	      //      this.drawTexturedModalRect(img, this.x, this.y, this.textX, this.textY + (k * this.height), this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
	            this.mouseDragged(mc, mouseX, mouseY);
	        }
	    }
}
