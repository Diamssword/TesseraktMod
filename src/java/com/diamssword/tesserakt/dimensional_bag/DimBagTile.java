package com.diamssword.tesserakt.dimensional_bag;

import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DimBagTile extends TileEntity {
	private UUID owner;
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if(compound.hasKey("owner"))
			owner = UUID.fromString(compound.getString("owner"));
		
	}
	 public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	    {
	        return (oldState.getBlock() != newSate.getBlock());
	    }
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		if(owner != null)
			compound.setString("owner", owner.toString());
		return super.writeToNBT(compound);
	}
	public UUID getOwner() {
		return owner;
	}
	public void setOwner(UUID owner) {
		this.owner = owner;
		this.markDirty();
	}
}
