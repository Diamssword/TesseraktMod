package com.diamssword.tesserakt.utils;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface TEBlock<TE extends TileEntity> {
public abstract Class<TE> getTileEntityClass();
	
	@SuppressWarnings("unchecked")
	default TE getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TE)world.getTileEntity(pos);
	}
	
	default boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Nullable
	public TE createTileEntity(World world, IBlockState state);
	
}
