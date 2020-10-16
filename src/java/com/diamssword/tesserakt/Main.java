package com.diamssword.tesserakt;

import com.diamssword.tesserakt.dimensional_bag.DimBagRefs;
import com.diamssword.tesserakt.gui.ModGuiHandler;
import com.diamssword.tesserakt.packets.PacketRequestTile;
import com.diamssword.tesserakt.packets.PacketRequestTile.HandlerRequestTile;
import com.diamssword.tesserakt.packets.PacketSendEnable;
import com.diamssword.tesserakt.packets.PacketSendEnable.HandlerSendEnable;
import com.diamssword.tesserakt.packets.PacketSendEnergy;
import com.diamssword.tesserakt.packets.PacketSendEnergy.HandlerSendEnergy;
import com.diamssword.tesserakt.packets.PacketSendIO;
import com.diamssword.tesserakt.packets.PacketSendIO.HandlerSendIO;
import com.diamssword.tesserakt.packets.PacketSendNames;
import com.diamssword.tesserakt.packets.PacketSendNames.HandlerSendNames;
import com.diamssword.tesserakt.packets.PacketSendNamesClient;
import com.diamssword.tesserakt.packets.PacketSendNamesClient.HandlerSendNamesClient;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION)
public class Main {
	static
	{
		FluidRegistry.enableUniversalBucket();
	}
	public static final String MODID = "tesserakt";
	public static final String MODNAME = "Tesserakt";
	public static final String VERSION = "1.4";
	public static SimpleNetworkWrapper network;
	public static Configuration config;

	@SidedProxy(clientSide="com.diamssword.tesserakt.ClientProxy", serverSide="com.diamssword.tesserakt.ServerProxy")	    
	public static CommonProxy proxy;	        

	@Instance
	public static Main instance = new Main();

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);  
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
		Main.config = new Configuration(e.getSuggestedConfigurationFile());
		Configs.syncConfig(Main.config);
		DimBagRefs.init();
	}
	@EventHandler
	public void init(FMLInitializationEvent e) {
		
		proxy.init(e);
		proxy.registerRenderers();
		Main.network = NetworkRegistry.INSTANCE.newSimpleChannel(Main.MODID);
		Main.network.registerMessage(HandlerSendNames.class, PacketSendNames.class, 0, Side.CLIENT);
		Main.network.registerMessage(HandlerSendNamesClient.class, PacketSendNamesClient.class, 1, Side.SERVER);
		Main.network.registerMessage(HandlerSendEnable.class, PacketSendEnable.class, 2, Side.SERVER);
		Main.network.registerMessage(HandlerSendIO.class, PacketSendIO.class, 3, Side.SERVER);
		Main.network.registerMessage(HandlerRequestTile.class, PacketRequestTile.class, 4, Side.SERVER);
		Main.network.registerMessage(HandlerSendEnergy.class, PacketSendEnergy.class, 5, Side.CLIENT);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);   
		WrenchsCompat.Init();

	}
}
