package com.diamssword.tesserakt.dimension;

import com.diamssword.tesserakt.Configs;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class BagajerumDimension
{
	public static DimensionType BagajerumDim = null;
	
	public static void init()
	{
		if (BagajerumDim == null)
		{
			registerDimensionType();
		}
		if (!DimensionManager.isDimensionRegistered(Configs.DimTPDimensionID))
		{
			registerDimension();
		}
	}
	
	private static void registerDimensionType()
	{
		BagajerumDim = DimensionType.register(Configs.DimTPDimensionID + ":bagajerum", "_bagajerum", Configs.DimTPDimensionID, BagajerumProvider.class, false);
	}
	
	private static void registerDimension()
	{
		DimensionManager.registerDimension(Configs.DimTPDimensionID, BagajerumDim);
	}
}
