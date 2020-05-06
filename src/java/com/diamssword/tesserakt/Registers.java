package com.diamssword.tesserakt;

import com.diamssword.tesserakt.blocks.CraftBlock;
import com.diamssword.tesserakt.blocks.EmptyBlock;
import com.diamssword.tesserakt.blocks.TesseraktBlock;
import com.diamssword.tesserakt.tileentity.FrameTile;
import com.diamssword.tesserakt.tileentity.TesseraktTile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
		@SubscribeEvent
		public static void registerBlock(RegistryEvent.Register<Block> e)
		{
			e.getRegistry().register(blockTesserakt);
			e.getRegistry().register(blockFrameFull);
			e.getRegistry().register(blockFrameEmpty);
			GameRegistry.registerTileEntity(TesseraktTile.class, blockTesserakt.getRegistryName());
			if(Configs.FrameFill)
				GameRegistry.registerTileEntity(FrameTile.class, blockFrameEmpty.getRegistryName());
		}
		@SubscribeEvent
		public static void registerItem(RegistryEvent.Register<Item> e)
		{
			e.getRegistry().register(new ItemBlock(blockTesserakt).setUnlocalizedName(blockTesserakt.getUnlocalizedName()).setRegistryName(blockTesserakt.getRegistryName()));
			e.getRegistry().register(new ItemBlock(blockFrameEmpty).setUnlocalizedName(blockFrameEmpty.getUnlocalizedName()).setRegistryName(blockFrameEmpty.getRegistryName()));
			e.getRegistry().register(new ItemBlock(blockFrameFull).setUnlocalizedName(blockFrameFull.getUnlocalizedName()).setRegistryName(blockFrameFull.getRegistryName()));
		}

		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent e) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockTesserakt), 0, new ModelResourceLocation(blockTesserakt.getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockFrameFull), 0, new ModelResourceLocation(blockFrameFull.getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockFrameEmpty), 0, new ModelResourceLocation(blockFrameEmpty.getRegistryName(), "inventory"));
		}
}
