package com.diamssword.tesserakt;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class WrenchsCompat {

	private static List<Item> wrenchs = new ArrayList<Item>();
	private static String[] items = new String[] {
			"thermalfoundation:wrench",
			"enderio:item_yeta_wrench",
			"buildcraft:wrench",
			"immersiveengineering:tool",
			"mekanism:configurator"
	};
	public static void Init()
	{
		for(String s : items)
		{
			Item item=Item.REGISTRY.getObject(new ResourceLocation(s));
			if(item != null)
				wrenchs.add(item);
		}
	}
	public static boolean isWrench(ItemStack stack)
	{
		return wrenchs.contains(stack.getItem());
	}
}
