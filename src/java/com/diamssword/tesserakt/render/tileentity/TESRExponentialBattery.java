package com.diamssword.tesserakt.render.tileentity;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.tileentity.ExponentialBatteryTile;
import com.diamssword.tesserakt.tileentity.TesseraktTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class TESRExponentialBattery extends TileEntitySpecialRenderer<ExponentialBatteryTile> {

	private static int id=0;
	{
		Sphere sphere = new Sphere();
		//GLU_POINT will render it as dots.
		//GLU_LINE will render as wireframe
		//GLU_SILHOUETTE will render as ?shadowed? wireframe
		//GLU_FILL as a solid.
		sphere.setDrawStyle(GLU.GLU_FILL);
		//GLU_SMOOTH will try to smoothly apply lighting
		//GLU_FLAT will have a solid brightness per face, and will not shade.
		//GLU_NONE will be completely solid, and probably will have no depth to it's appearance.        
		sphere.setNormals(GLU.GLU_SMOOTH);
		//GLU_INSIDE will render as if you are inside the sphere, making it appear inside out.(Similar to how ender portals are rendered)
		sphere.setOrientation(GLU.GLU_OUTSIDE);
		sphere.setTextureFlag(true);
		//Simple 1x1 red texture to serve as the spheres skin, the only pixel in this image is red.

		//Bind our texture to a string.
		//   ForgeHooksClient.textures.put("1x1RED", Minecraft.getMinecraft().renderEngine.allocateAndSetupTexture(bi));
		//sphereID is returned from our sphereID() method
		id = GL11.glGenLists(1);
		//Create a new list to hold our sphere data.
		GL11.glNewList(id, GL11.GL_COMPILE);
		//Offset the sphere by it's radius so it will be centered
		GL11.glTranslatef((float) 0.50F, (float) 0.50F, (float) 0.50F);
		//Call our string that we mapped to our texture
		// ForgeHooksClient.bindTexture("1x1RED", 0);
		//The drawing the sphere is automattically doing is getting added to our list. Careful, the last 2 variables 
		//control the detail, but have a massive impact on performance. 32x32 is a good balance on my machine.
		sphere.draw(0.35F, 16, 16);
		//Drawing done, unbind our texture
		//ForgeHooksClient.unbindTexture();
		//Tell LWJGL that we are done creating our list.
		GL11.glEndList();
	}


	Random rand = new Random();
	public float rot=0;
	@Override
	public void render(ExponentialBatteryTile te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		//Default parameters in TileEntitySpecialRenderer's renderTileEntityAt
			GL11.glTranslated(x,y,z);
		GL11.glRotatef(rot, 1, 1, 1);
		rot= rot+0.1f;
		this.bindTexture(new ResourceLocation(Main.MODID,"textures/blocks/battery/battery_sphere.png"));
		int[] cl=colorForLevel(te.getLevel());
		GL11.glColor3f(cl[0]/255f, cl[1]/255f, cl[2]/255f);
		GL11.glCallList(id);
		GlStateManager.enableLighting();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
		if(Minecraft.getMinecraft().player.isSneaking())
		{
		this.drawNameplate(te, "North", x, y+0.5, z-0.501,0,0);
		this.drawNameplate(te, "South", x, y+0.5, z+0.501,180,0);
		this.drawNameplate(te, "West", x-0.501, y+0.5, z,-90,0);
		this.drawNameplate(te, "East", x+0.501, y+0.5, z,90,0);
		}

	
	}
    protected void drawNameplate(ExponentialBatteryTile te, String str, double x, double y, double z,float roty,float rotx)
    {
        Entity entity = this.rendererDispatcher.entity;
        double d0 = te.getDistanceSq(entity.posX, entity.posY, entity.posZ);

        if (d0 <= (double)(5 * 5))
        {
            EntityRenderer.drawNameplate(this.getFontRenderer(), str, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, 0, roty, rotx, false, false);
        }
    }
	public static int[] colorForLevel(int i)
	{
		switch(i)
		{
		default:
			return new int[]{0,0,0};
		case 1:
			return new int[]{82,0,0};
		case 2:
			return new int[]{152,0,0};
		case 3:
			return new int[]{178,78,0};
		case 4:
			return new int[]{242,242,0};
		case 5:
			return new int[]{0,102,2};
		case 6:
			return new int[]{0,255,5};
		case 7:
			return new int[]{0,188,255};
		}
	}
	public boolean isGlobalRenderer(TesseraktTile te)
	{
		return true;
	}
}