package com.diamssword.tesserakt;

import com.diamssword.tesserakt.dimension.BagajerumDimension;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
@SuppressWarnings("deprecation")
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
	
	}

	public void init(FMLInitializationEvent e) {
		BagajerumDimension.init();
	}

	public void postInit(FMLPostInitializationEvent e) {
	}
	public String localize(String unlocalized, Object... args) {
		return I18n.translateToLocalFormatted(unlocalized, args);
	}
	public void registerRenderers() {

	}
	public void updateClientSky(BlockPos pos,int size)
	{
		
	}
	public World getClientWorld()
	{
		return null;
		
	}
}
