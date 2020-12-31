package com.diamssword.tesserakt.render.tileentity;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.blocks.TesseraktBlock;
import com.diamssword.tesserakt.tileentity.TesseraktTile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class TESRTesserakt extends TileEntitySpecialRenderer<TesseraktTile> {

	private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation(Main.MODID,"textures/blocks/sky.png");
	private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation(Main.MODID,"textures/blocks/field.png");
	private static final ResourceLocation END_PORTAL_TEXTURE_1 = new ResourceLocation(Main.MODID,"textures/blocks/field_1.png");
	private static Map<String,Float> pulseList= new HashMap<String,Float>();
	Random rand = new Random();
	@Override
	public void render(TesseraktTile te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.disableFog();
		boolean off = false;
		boolean priv = false;
		IBlockState st=te.getWorld().getBlockState(te.getPos());
		if(st.getBlock() instanceof TesseraktBlock)
		{
			off = !st.getValue(TesseraktBlock.CONNECTED);
			priv = st.getValue(TesseraktBlock.PRIVATE);
		}
		enderField(x,y-0.001,z,off,te.getPos().getX()+""+te.getPos().getY()+""+te.getPos().getZ(),priv);
	}


	private static final Random RANDOM = new Random(31100L);

	private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
	private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
	public void enderField(double x,double y,double z,boolean off,String posCode,boolean privat)
	{
		//	GlStateManager.disableLighting();
		RANDOM.setSeed(31100L);
		GlStateManager.getFloat(2982, MODELVIEW);
		GlStateManager.getFloat(2983, PROJECTION);
		double d0 = x * x + y * y + z * z;
		int i = this.getPasses(d0);
		float f=0.04f;
		if(!off)
			f = this.getOffset(posCode);

		for (int j = 0; j < i; ++j)
		{
			GlStateManager.pushMatrix();

			if (j == 0)
			{
				this.bindTexture(END_SKY_TEXTURE);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			}

			if (j >= 1)
			{
				if(privat)
					this.bindTexture(END_PORTAL_TEXTURE_1);
				else
				this.bindTexture(END_PORTAL_TEXTURE);
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
			GlStateManager.scale(0.5F, 0.5F, 1.0F);
			float f2 = (float)(j + 1);
			GlStateManager.translate(17.0F / f2, (2.0F + f2 / 1.5F) * ((float)Minecraft.getSystemTime() % 800000.0F / 800000.0F), 0.0F);
			GlStateManager.rotate((f2 * f2 * 4321.0F + f2 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.scale(4.5F - f2 / 4.0F, 4.5F - f2 / 4.0F, 1.0F);
			GlStateManager.multMatrix(PROJECTION);
			GlStateManager.multMatrix(MODELVIEW);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

			float color = 0.2f;
			if(off)
				color=0.03f;
			float f3 = color;
			float f4 = color;
			float f5 = color;

			bufferbuilder.pos(x+0.30f- (double)f, y+0.30f- (double)f, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.70f+ (double)f, y+0.30f- (double)f, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.70f+ (double)f, y+0.70f+ (double)f, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.30f- (double)f, y+0.70f+ (double)f, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();

			bufferbuilder.pos(x+0.30f- (double)f, y+0.70f+ (double)f, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.70f+ (double)f, y+0.70f+ (double)f, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.70f+ (double)f, y+0.30f- (double)f, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.30f- (double)f, y+0.30f- (double)f, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();

			bufferbuilder.pos(x+0.70f+ (double)f, y+0.70f+ (double)f, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.70f+ (double)f, y+0.70f+ (double)f, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.70f+ (double)f, y+0.30f- (double)f, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.70f+ (double)f, y+0.30f- (double)f, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();

			bufferbuilder.pos(x+0.30f- (double)f, y+0.30f- (double)f, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.30f- (double)f, y+0.30f- (double)f, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.30f- (double)f, y+0.70f+ (double)f, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.30f- (double)f, y+0.70f+ (double)f, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();

			bufferbuilder.pos(x+0.30f- (double)f, y+0.30f- (double)f, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.70f+ (double)f, y+0.30f- (double)f, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.70f+ (double)f, y+0.30f- (double)f, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.30f- (double)f, y+0.30f- (double)f, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();

			bufferbuilder.pos(x+0.30f- (double)f, y+0.70f + (double)f, z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x +0.70f+ (double)f, y+0.70f + (double)f,z+0.70f+ (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.70f+ (double)f, y+0.70f + (double)f, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();
			bufferbuilder.pos(x+0.30f- (double)f, y+0.70f + (double)f, z+0.30f- (double)f).color(f3, f4, f5, 1.0F).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
			this.bindTexture(END_SKY_TEXTURE);
		}

		GlStateManager.disableBlend();
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
		GlStateManager.enableLighting();
	}
	protected int getPasses(double p_191286_1_)
	{
		int i;

		if (p_191286_1_ > 36864.0D)
		{
			i = 1;
		}
		else if (p_191286_1_ > 25600.0D)
		{
			i = 3;
		}
		else if (p_191286_1_ > 16384.0D)
		{
			i = 5;
		}
		else if (p_191286_1_ > 9216.0D)
		{
			i = 7;
		}
		else if (p_191286_1_ > 4096.0D)
		{
			i = 9;
		}
		else if (p_191286_1_ > 1024.0D)
		{
			i = 11;
		}
		else if (p_191286_1_ > 576.0D)
		{
			i = 13;
		}
		else if (p_191286_1_ > 256.0D)
		{
			i = 14;
		}
		else
		{
			i = 15;
		}

		return i;
	}

	protected float getOffset(String code)
	{
		Float pulse = pulseList.get(code);
		if(pulse == null)
			pulse=0f;
		pulse= pulse+0.002f;
		if(pulse>0.5f)
			pulse=0f;
		pulseList.put(code, pulse);
		if(pulse <0.26f)
			return pulse;
		else
			return 0.25f-(pulse -0.25f);
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