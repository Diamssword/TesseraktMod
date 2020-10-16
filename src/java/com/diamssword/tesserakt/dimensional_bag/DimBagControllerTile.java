package com.diamssword.tesserakt.dimensional_bag;

import java.util.UUID;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.render.tileentity.ISkyRender;
import com.diamssword.tesserakt.render.tileentity.StarSkyRender;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DimBagControllerTile extends TileEntity implements ITickable{
	private UUID owner;
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return (oldState.getBlock() != newSate.getBlock());
	}
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

	@Override
	public void update() {		
		if(world != null && pos != null &&world.isRemote && world.getTotalWorldTime()%20==0 && this.world.getBlockState(pos).getValue(DimBagControllerBlock.UPGRADES)>0)
		{
			BlockPos[] poss=getRoofFromDimCoord(this.pos,this.world);

			Main.proxy.updateClientSky(poss[0], poss[1].getX());
		}

	}
	public static BlockPos[] getRoofFromDimCoord(BlockPos pos,World w)
	{
		pos=pos.south(3).east(2);
		BlockPos pos1= pos.up();
		Block b= null;
		while(b != DimBagRefs.BagWall)
		{
			pos1=pos1.up();
			b=w.getBlockState(pos1).getBlock();
			if(pos1.getY()>200)
				break;
		}
		int size = pos1.getY()-pos.getY();
		return new BlockPos[] {new BlockPos(pos.getX()+size-1,pos1.getY(),pos1.getZ()),new BlockPos(size,0,0)};
	}
}
