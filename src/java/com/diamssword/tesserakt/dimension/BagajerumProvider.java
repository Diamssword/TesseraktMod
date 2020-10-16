package com.diamssword.tesserakt.dimension;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Configs;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BagajerumProvider extends WorldProvider
{
	private IRenderHandler empty = new NoRenderHandler();
	private IRenderHandler EndSky= new EndSkyRenderHandler();
	private ChunkGeneratorFlat chunkgen;

	public BagajerumProvider()
	{
		this.biomeProvider = new BiomeProviderSingle(Biomes.VOID);
	}
	@SideOnly(Side.CLIENT)
	public net.minecraftforge.client.IRenderHandler getSkyRenderer()
	{
		return EndSky;
	}
	@SideOnly(Side.CLIENT)
	public net.minecraftforge.client.IRenderHandler getWeatherRenderer()
	{
		return empty;
	}
	@Override
	public void setWorldTime(long time)
	{
	}
	public void calculateInitialWeather()
	{
		this.setWorldTime(0);
		super.calculateInitialWeather();
		//this.world.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
	}
	public int getAverageGroundLevel()
	{
		return 127;
	}
	@Override
	public void updateWeather()
	{
		this.world.getWorldInfo().setRaining(false);
	}
	@Override
	public DimensionType getDimensionType()
	{
		return BagajerumDimension.BagajerumDim;
	}

	@Override
	public String getSaveFolder()
	{
		return "DIM"+Configs.DimTPDimensionID+"_Bagajerum";
	}

	@Override
	public IChunkGenerator createChunkGenerator()
	{
		if(chunkgen == null)
		{
			chunkgen =  new ChunkGeneratorFlat(world, world.getSeed(), false, "3;255*bedrock;127");
			this.biomeProvider = new BiomeProviderSingle(Biomes.VOID);
		}
		return chunkgen;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight()
	{
		return 255.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getCloudRenderer()
	{
		return empty;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
	{
		return new Vec3d(0, 0, 0);
	}

	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		return 0.0F;
	}

	/**
	 * Returns array with sunrise/sunset colors
	 */
	@Nullable
	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
	{
		return null;
	}
	protected void generateLightBrightnessTable()
	{
		float f = 0.3F;
		for(int i = 0; i <= 15; ++i) {
			float f1 = 1.0F - (float) i / 15.0F;
			this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
		}
	}

}
