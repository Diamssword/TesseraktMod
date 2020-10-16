package com.diamssword.tesserakt.render.tileentity;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector3d;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.tileentity.TesseraktTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class StarSkyRender extends ISkyRender {

	private static final ResourceLocation SKY_TEXTURE = new ResourceLocation(Main.MODID,"textures/blocks/bag_sky.png");
	private static final ResourceLocation STARS_TEXTURE = new ResourceLocation(Main.MODID,"textures/blocks/stars.png");

	private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
	private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
	private static Map<Integer,Float[]> colors = new HashMap<Integer,Float[]>();
	public void render(EntityPlayerSP pl)
	{
		Vector3d coords=this.getRelativePos(pl);
		double x=coords.x;
		double y=coords.y;
		double z=coords.z;
		GlStateManager.getFloat(2982, MODELVIEW);
		GlStateManager.getFloat(2983, PROJECTION);
		int i =8;
		float f=width;
		for (int j = 0; j < i; ++j)
		{
			GlStateManager.pushMatrix();

			if (j == 0)
			{
				man.bindTexture(SKY_TEXTURE);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			}

			if (j >= 1)
			{
				man.bindTexture(STARS_TEXTURE);
				Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
			}

			if (j == 1)
			{
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			}

			GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
			GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
			GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
			GlStateManager.texGen(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0F, 0.0F, 0.0F, 0.0F));
			GlStateManager.texGen(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0F, 1.0F, 0.0F, 0.0F));
			GlStateManager.texGen(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0F, 0.0F, 1.0F, 0.0F));
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			GlStateManager.translate(0.5F, 0.5F, 0.0F);
			//GlStateManager.scale(0.5F, 0.5F, 1.0F);
			float f2 = (float)(j + 1);
			GlStateManager.translate(17.0F / f2, (2.0F + f2 / 1.5F) * ((float)Minecraft.getSystemTime() % 800000.0F / 800000.0F), 0.0F);
			GlStateManager.rotate((f2 * f2 * 4321.0F + f2 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.scale(4.5F - f2 / 4.0F, 4.5F - f2 / 4.0F, 1.0F);
			GlStateManager.multMatrix(PROJECTION);
			GlStateManager.multMatrix(MODELVIEW);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

			float color = 1f;
			float f3 = color;
			float f4 = color;
			float f5 = color;
			if (j >= 1)
			{
				if(colors.get(j) == null)
				{
					colors.put(j, new Float[] {(float) Math.random(),(float) Math.random(),(float) Math.random()});
				}
				Float[] col=colors.get(j);
				f3 = col[0];
				f4 = col[1];
				f5 = col[2];
			}

			bufferbuilder.pos(x+0.30f- (double)f, y, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.70f+ (double)f, y, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.70f+ (double)f, y, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.30f- (double)f, y, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();

			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
			man.bindTexture(SKY_TEXTURE);
		}

	//	GlStateManager.disableBlend();
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
		GlStateManager.enableLighting();
	}
	private FloatBuffer getBuffer(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
	{
		this.buffer.clear();
		this.buffer.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
		this.buffer.flip();
		return this.buffer;
	}
	private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);
	public boolean isGlobalRenderer(TesseraktTile te)
	{
		return true;
	}
}
