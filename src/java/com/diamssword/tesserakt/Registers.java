package com.diamssword.tesserakt;

import java.util.ArrayList;
import java.util.List;

import com.diamssword.tesserakt.blocks.CraftBlock;
import com.diamssword.tesserakt.blocks.EmptyBlock;
import com.diamssword.tesserakt.blocks.EnderWoolBlock;
import com.diamssword.tesserakt.blocks.ExponentialBatteryBlock;
import com.diamssword.tesserakt.blocks.TesseraktBlock;
import com.diamssword.tesserakt.items.BatteryPart;
import com.diamssword.tesserakt.items.EnderStar;
import com.diamssword.tesserakt.tileentity.ExponentialBatteryTile;
import com.diamssword.tesserakt.tileentity.FrameTile;
import com.diamssword.tesserakt.tileentity.TesseraktTile;
import com.diamssword.tesserakt.utils.TEBlock;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class Registers {
	public static CreativeTabs tab = new CreativeTabs(Main.MODID) {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(blockTesserakt,1);
		}};



		public static TesseraktBlock blockTesserakt = new TesseraktBlock();
		public static EmptyBlock blockFrameEmpty = new EmptyBlock("frame_empty");
		public static CraftBlock blockFrameFull= new CraftBlock("frame_full");
		public static ExponentialBatteryBlock exponentialBattery= new ExponentialBatteryBlock();
		public static EnderWoolBlock blockEnderWool= new EnderWoolBlock("ender_wool");
		public static EnderStar ItemEnderStar= new EnderStar();
		public static BatteryPart BatteryPart = new BatteryPart();
		public static List<Block> blocks= new ArrayList<Block>();
		public static List<Item> items= new ArrayList<Item>();
		static {
			items.add(ItemEnderStar);
			items.add(BatteryPart);
		}
		@SubscribeEvent
		public static void registerBlock(RegistryEvent.Register<Block> e)
		{
			e.getRegistry().register(blockTesserakt);
			e.getRegistry().register(blockFrameFull);
			e.getRegistry().register(blockFrameEmpty);
			e.getRegistry().register(blockEnderWool);
			e.getRegistry().register(exponentialBattery);
	//		e.getRegistry().register(blockDimensionalBattery);
			GameRegistry.registerTileEntity(TesseraktTile.class, blockTesserakt.getRegistryName());
			GameRegistry.registerTileEntity(ExponentialBatteryTile.class, exponentialBattery.getRegistryName());
	//		GameRegistry.registerTileEntity(DimensionalBatteryTile.class, blockDimensionalBattery.getRegistryName());
			if(Configs.FrameFill)
				GameRegistry.registerTileEntity(FrameTile.class, blockFrameEmpty.getRegistryName());
			

			//e.getRegistry().register(new CheatedBattryBlock());
			//GameRegistry.registerTileEntity(CheatedBatteryTile.class, new CheatedBattryBlock().getRegistryName());
			
			for(Block b: blocks)
			{
				e.getRegistry().register(b);
				if(b instanceof TEBlock)
				{
					GameRegistry.registerTileEntity(((TEBlock<?>)b).getTileEntityClass(), new ResourceLocation(b.getRegistryName().getResourceDomain(),"TE."+b.getRegistryName().getResourcePath()));
				}
					
			}
			
		}
		@SubscribeEvent
		public static void registerItem(RegistryEvent.Register<Item> e)
		{
			e.getRegistry().register(new ItemBlock(blockTesserakt).setUnlocalizedName(blockTesserakt.getUnlocalizedName()).setRegistryName(blockTesserakt.getRegistryName()));
			e.getRegistry().register(new ItemBlock(blockFrameEmpty).setUnlocalizedName(blockFrameEmpty.getUnlocalizedName()).setRegistryName(blockFrameEmpty.getRegistryName()));
			e.getRegistry().register(new ItemBlock(blockFrameFull).setUnlocalizedName(blockFrameFull.getUnlocalizedName()).setRegistryName(blockFrameFull.getRegistryName()));
			e.getRegistry().register(new ItemBlock(blockEnderWool).setUnlocalizedName(blockEnderWool.getUnlocalizedName()).setRegistryName(blockEnderWool.getRegistryName()));
			e.getRegistry().register(new ItemBlock(exponentialBattery).setUnlocalizedName(exponentialBattery.getUnlocalizedName()).setRegistryName(exponentialBattery.getRegistryName()));
		//	e.getRegistry().register(new ItemBlock(blockDimensionalBattery).setUnlocalizedName(blockDimensionalBattery.getUnlocalizedName()).setRegistryName(blockDimensionalBattery.getRegistryName()));
			
			
			
			//e.getRegistry().register(new ItemBlock(new CheatedBattryBlock()).setUnlocalizedName(new CheatedBattryBlock().getUnlocalizedName()).setRegistryName(new CheatedBattryBlock().getRegistryName()));
			for(Item b: items)
			{
				e.getRegistry().register(b);
			}
		}

		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent e) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockTesserakt), 0, new ModelResourceLocation(blockTesserakt.getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockFrameFull), 0, new ModelResourceLocation(blockFrameFull.getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockFrameEmpty), 0, new ModelResourceLocation(blockFrameEmpty.getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockEnderWool), 0, new ModelResourceLocation(blockEnderWool.getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(exponentialBattery), 0, new ModelResourceLocation(exponentialBattery.getRegistryName(), "inventory"));
			for(Item b: items)
			{
				ModelLoader.setCustomModelResourceLocation(b, 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
			}
		}
}
