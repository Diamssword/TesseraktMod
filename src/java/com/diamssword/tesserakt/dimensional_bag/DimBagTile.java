package com.diamssword.tesserakt.dimensional_bag;

import java.util.List;
import java.util.UUID;

import javax.vecmath.Vector3d;

import com.diamssword.tesserakt.Configs;
import com.diamssword.tesserakt.events.ServerEvents;
import com.diamssword.tesserakt.events.ServerEvents.EntityPacket;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DimBagTile extends TileEntity implements ITickable {
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
	@Override
	public void update() {
		if(this.getClass() == DimBagTile.class && this.world != null && !this.world.isRemote)
		{
			IBlockState st=	this.world.getBlockState(this.pos);
			if(st.getValue(DimBagBlock.TRIGGERED) && this.getOwner() != null)
			{
				AxisAlignedBB bb=new AxisAlignedBB(this.pos.getX()+0.1,this.pos.getY()+0.9,this.pos.getZ()+0.1,this.pos.getX()+0.8,this.pos.getY()+1.5,this.pos.getZ()+0.8);
				List<Entity> es =this.world.getEntitiesWithinAABBExcludingEntity(null, bb);
				if(!es.isEmpty())
				{
					for(Entity e: es)
					{
						DimBagNBT bag=DimBagNBT.get(this.world, this.getOwner());
						if(bag != null)
						{
							BlockPos pos =DimBagLogic.getRoomCoords(this.getOwner(), this.world);
							ServerEvents.tooTP.add(new EntityPacket(e, new Vector3d(pos.getX()+0.5, pos.getY()+0.5,pos.getZ()+0.5), Configs.DimTPDimensionID));
						}
					}
				}
			}
		}

	}
}
