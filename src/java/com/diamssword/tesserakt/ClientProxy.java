package com.diamssword.tesserakt;


import com.diamssword.tesserakt.render.tileentity.TESRTesserakt;
import com.diamssword.tesserakt.tileentity.TesseraktTile;

import net.minecraft.client.resources.I18n;
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
	}
	
}
