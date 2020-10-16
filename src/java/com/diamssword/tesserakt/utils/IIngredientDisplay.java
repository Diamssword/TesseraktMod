package com.diamssword.tesserakt.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface IIngredientDisplay {

	public ItemStack[][] toDisplay(IBlockState state);
}
