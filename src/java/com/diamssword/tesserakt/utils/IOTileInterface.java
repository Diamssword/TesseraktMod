package com.diamssword.tesserakt.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public interface IOTileInterface {

	public int getMode(EnumFacing face);
	public void setMode(EnumFacing face,int mode);
	public ItemStack getItemDisplay();
}
