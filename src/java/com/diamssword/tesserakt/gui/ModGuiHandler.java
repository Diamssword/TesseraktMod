package com.diamssword.tesserakt.gui;

import com.diamssword.tesserakt.dimensional_bag.DimBagControllerTile;
import com.diamssword.tesserakt.tileentity.ExponentialBatteryTile;
import com.diamssword.tesserakt.tileentity.TesseraktTile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {
	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x,y,z));
		if(ID ==1 && tile != null && tile instanceof TesseraktTile)
			return new GuiTesserakt((TesseraktTile) tile);
		if(ID ==2 && tile != null && tile instanceof DimBagControllerTile)
			return new GuiDimBagController((DimBagControllerTile) tile);
		if(ID ==3 && tile != null && tile instanceof ExponentialBatteryTile)
			return new GuiBattery( (ExponentialBatteryTile) tile);
		return null;
	}
}