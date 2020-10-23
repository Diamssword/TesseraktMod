package com.diamssword.tesserakt.addons;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.Registers;
import com.diamssword.tesserakt.dimensional_bag.DimBagRefs;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;

@mezz.jei.api.JEIPlugin 
public class ModJEIPlugin implements IModPlugin  {

	public void register(IModRegistry registry) {
		registry.addIngredientInfo(new ItemStack(Registers.exponentialBattery), VanillaTypes.ITEM, Main.MODID+".battery.info");
		registry.addIngredientInfo(new ItemStack(Registers.blockTesserakt), VanillaTypes.ITEM, Main.MODID+".tesserakt.info");
		registry.addIngredientInfo(new ItemStack(DimBagRefs.BagjerumItem), VanillaTypes.ITEM, Main.MODID+".dim_bag.info");
		registry.addIngredientInfo(new ItemStack(Registers.BatteryPart), VanillaTypes.ITEM, Main.MODID+".battery_part.info");
	}
}
