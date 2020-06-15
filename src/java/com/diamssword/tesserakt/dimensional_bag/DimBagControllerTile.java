package com.diamssword.tesserakt.dimensional_bag;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class DimBagControllerTile extends TileEntity {
	private UUID owner;
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if(compound.hasKey("owner"))
			owner = UUID.fromString(compound.getString("owner"));
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
