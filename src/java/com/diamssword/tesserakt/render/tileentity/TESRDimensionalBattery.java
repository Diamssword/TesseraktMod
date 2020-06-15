package com.diamssword.tesserakt.render.tileentity;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.diamssword.tesserakt.tileentity.DimensionalBatteryTile;
import com.diamssword.tesserakt.tileentity.TesseraktTile;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TESRDimensionalBattery extends TileEntitySpecialRenderer<DimensionalBatteryTile> {

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
	        sphere.draw(0.5F, 32, 32);
	        //Drawing done, unbind our texture
	        //ForgeHooksClient.unbindTexture();
	        //Tell LWJGL that we are done creating our list.
	        GL11.glEndList();
	}

	private static int id1=0;
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
	        sphere.setOrientation(GLU.GLU_INSIDE);
	        sphere.setTextureFlag(true);
	        //Simple 1x1 red texture to serve as the spheres skin, the only pixel in this image is red.
	     
	        //Bind our texture to a string.
	     //   ForgeHooksClient.textures.put("1x1RED", Minecraft.getMinecraft().renderEngine.allocateAndSetupTexture(bi));
	        //sphereID is returned from our sphereID() method
	     id1 = GL11.glGenLists(1);
	        //Create a new list to hold our sphere data.
	        GL11.glNewList(id1, GL11.GL_COMPILE);
	        //Offset the sphere by it's radius so it will be centered
	        GL11.glTranslatef((float) 0.50F, (float) 0.50F, (float) 0.50F);
	        //Call our string that we mapped to our texture
	       // ForgeHooksClient.bindTexture("1x1RED", 0);
	        //The drawing the sphere is automattically doing is getting added to our list. Careful, the last 2 variables 
	       //control the detail, but have a massive impact on performance. 32x32 is a good balance on my machine.
	        sphere.draw(0.5F, 32, 32);
	        //Drawing done, unbind our texture
	        //ForgeHooksClient.unbindTexture();
	        //Tell LWJGL that we are done creating our list.
	        GL11.glEndList();
	}
	Random rand = new Random();
	public float rot=0;
	@Override
	public void render(DimensionalBatteryTile te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		//Default parameters in TileEntitySpecialRenderer's renderTileEntityAt
		GL11.glTranslated(x+0.5-(te.size/2d),y+0.5-(te.size/2d),z+0.5-(te.size/2d));
		GL11.glRotatef(rot, 1, 1, 1);
		rot= rot+0.1f;
		this.bindTexture(new ResourceLocation("textures/blocks/melon_top.png"));
		GL11.glScaled(te.size, te.size,te.size);
		GL11.glCallList(id);
		GlStateManager.enableLighting();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
		   GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
	//	OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		//Default parameters in TileEntitySpecialRenderer's renderTileEntityAt
		GL11.glTranslated(x+0.5-(te.size/2d),y+0.5-(te.size/2d),z+0.5-(te.size/2d));
		GL11.glRotatef(rot, 1, 1, 1);
		rot= rot+0.1f;
		this.bindTexture(new ResourceLocation("textures/blocks/glass_yellow.png"));
		GL11.glScaled(te.size, te.size,te.size);
		GL11.glCallList(id1);
		GlStateManager.enableLighting();
		GlStateManager.disableAlpha();
		GL11.glEnable(GL11.GL_LIGHTING);
		   GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public boolean isGlobalRenderer(TesseraktTile te)
	{
		return true;
	}
}