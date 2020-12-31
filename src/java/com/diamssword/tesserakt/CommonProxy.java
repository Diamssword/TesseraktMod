package com.diamssword.tesserakt;

import com.diamssword.tesserakt.Configs.LockItem;
import com.diamssword.tesserakt.dimension.BagajerumDimension;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
		for(String i : Configs.locksItemsStr)
		{
			String id = i;
			int meta = -1;
			if(i.contains("@"))
			{
				int ind=i.lastIndexOf("@");
				String nbS = i.substring(ind+1);
				try {
					meta= Integer.parseInt(nbS);
					id= id.substring(0,ind);
				}catch(NumberFormatException e1)
				{
					e1.printStackTrace();
				}
			}
			System.out.println(id);
			Item item=Item.REGISTRY.getObject(new ResourceLocation(id));
			if(item != null)
			{
				Configs.locksItems.add(new LockItem(item,meta));
			}
		}
		if(Configs.locksItems.isEmpty())
			Configs.locksItems.add(new LockItem(Items.DIAMOND,0));
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
