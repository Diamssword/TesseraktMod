package com.diamssword.tesserakt;


import com.diamssword.tesserakt.dimensional_bag.DimBagOutTile;
import com.diamssword.tesserakt.events.ClientEvents;
import com.diamssword.tesserakt.render.tileentity.StarSkyRender;
import com.diamssword.tesserakt.render.tileentity.TESRDimBagOut;
import com.diamssword.tesserakt.render.tileentity.TESRDimensionalBattery;
import com.diamssword.tesserakt.render.tileentity.TESRTesserakt;
import com.diamssword.tesserakt.tileentity.DimensionalBatteryTile;
import com.diamssword.tesserakt.tileentity.TesseraktTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy{
	public static int tesrRenderId;
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		OBJLoader.INSTANCE.addDomain(Main.MODID);
		ModelLoaderRegistry.registerLoader(OBJLoader.INSTANCE);
		
	}

	public void init(FMLInitializationEvent e) {
		super.init(e);
	} 

	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		
	}
	@Override
	public String localize(String unlocalized, Object... args) {
		return I18n.format(unlocalized, args);
	}
	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TesseraktTile.class, new TESRTesserakt());
		ClientRegistry.bindTileEntitySpecialRenderer(DimensionalBatteryTile.class, new TESRDimensionalBattery());
		ClientRegistry.bindTileEntitySpecialRenderer(DimBagOutTile.class, new TESRDimBagOut());
	}
	public void updateClientSky(BlockPos pos,int size)
	{
		if(ClientEvents.sky == null)
		{
			ClientEvents.sky = new StarSkyRender();
		}
		ClientEvents.sky.setProperties(pos, size);
	}
	public World getClientWorld()
	{
		return Minecraft.getMinecraft().world;
		
	}
}
