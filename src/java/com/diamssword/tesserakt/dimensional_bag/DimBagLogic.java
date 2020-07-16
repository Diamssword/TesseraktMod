package com.diamssword.tesserakt.dimensional_bag;

import java.util.UUID;

import com.diamssword.tesserakt.Configs;
import com.diamssword.tesserakt.storage.TesseraktData;
import com.diamssword.tesserakt.utils.ModTeleporter;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class DimBagLogic {


	public static BlockPos getPlayersCoords(EntityPlayer p)
	{
		return getRoomCoords(p.getUniqueID(),p.world);
	}
	public static BlockPos getRoomCoords(UUID id,World world)
	{
		DimBagNBT bag=DimBagNBT.get(world, id);
		if(bag.dimPos != null && !bag.dimPos.equals(new BlockPos(0,0,0)))
		{
			return bag.dimPos;
		}
		else
		{
			bag.id = id;
			NBTTagCompound tag=TesseraktData.getDimBag(world);
			int x=	tag.getInteger("bagajerumPos");
			Chunk c=world.getChunkFromBlockCoords(new BlockPos(x,128,0));

			BlockPos pos=new BlockPos(c.getPos().getXStart()-2,128,c.getPos().getZStart()+8);
			tag.setInteger("bagajerumPos", x+Configs.DimBagspacing);
			bag.dimPos = pos;
			DimBagNBT.save(world, bag);
			return pos;
		}
	}

	public static void TeleportPlayerToRoom(EntityPlayer player,UUID roomID)
	{
		DimBagNBT bag=DimBagNBT.get(player.world, roomID);
		boolean created=false;
		if(bag.dimPos != null && !bag.dimPos.equals(new BlockPos(0,0,0)))
			created=true;
		BlockPos pos = getRoomCoords(roomID,player.world);
		if(Configs.DimTPDimensionID != player.world.provider.getDimension())
		{

			player.changeDimension(Configs.DimTPDimensionID, new ModTeleporter((WorldServer) player.world));

		}
		player.setPositionAndUpdate(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5);
		if(!created)
		{

			fill(new BlockPos(pos.getX()-5,127,pos.getZ()-3),new BlockPos(pos.getX()+2,127+5,pos.getZ()+3),player.world,DimBagRefs.BagWall.getDefaultState());

			setPortal(new BlockPos(pos.getX()-4,pos.getY()+3, pos.getZ()-2),new BlockPos(pos.getX()-4,pos.getY(), pos.getZ()+2),player.world, roomID);

			BlockPos pos2=new BlockPos(pos.getX()+1,pos.getY()+1, pos.getZ()-3);
			player.world.setBlockState(pos2,DimBagRefs.BagjerumControllerBlock.getDefaultState());
			TileEntity tile = player.world.getTileEntity(pos2);
			if(tile != null && tile instanceof DimBagControllerTile)
			{
				((DimBagControllerTile) tile).setOwner(roomID);
			}
			buildRoom(player.world.getChunkFromBlockCoords(pos.east(6)),bag.size+3);
			fill(new BlockPos(pos.getX()+2,128,pos.getZ()-2),new BlockPos(pos.getX()+2,131,pos.getZ()+2),player.world,Blocks.AIR.getDefaultState());
		}
	}


	public static void buildRoom(Chunk chunk,int size)
	{
		int x=chunk.getPos().getXStart();
		int z=chunk.getPos().getZStart()+8;
		fill(new BlockPos(x,127,z-(size/2)),new BlockPos(x+size-1,127+(size/2)+2,z+(size/2)),chunk.getWorld(),DimBagRefs.BagWall.getDefaultState());


	}

	public static void fill(BlockPos pos1, BlockPos pos2,World world,IBlockState block)
	{
		BlockPos pos3 = new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
		BlockPos pos4 = new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
		for (int l = pos3.getZ(); l <= pos4.getZ(); ++l)
		{
			for (int i1 = pos3.getY(); i1 <= pos4.getY(); ++i1)
			{
				for (int j1 = pos3.getX(); j1 <= pos4.getX(); ++j1)
				{
					BlockPos blockpos = new BlockPos(j1, i1, l);
					if (j1 != pos3.getX() && j1 != pos4.getX() && i1 != pos3.getY() && i1 != pos4.getY() && l != pos3.getZ() && l != pos4.getZ())
					{
						if(world.getBlockState(blockpos).getBlock() == DimBagRefs.BagWall|| world.getBlockState(blockpos).getBlock() ==Blocks.BEDROCK)						
							world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
					}
					else if(world.isAirBlock(blockpos) || world.getBlockState(blockpos).getBlock() ==Blocks.BEDROCK|| world.getBlockState(blockpos).getBlock() == DimBagRefs.BagWall )
						world.setBlockState(blockpos, block);
				}
			}
		}
	}

	public static void setPortal(BlockPos pos1, BlockPos pos2,World world,UUID owner)
	{
		BlockPos pos3 = new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
		BlockPos pos4 = new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
		for (int l = pos3.getZ(); l <= pos4.getZ(); ++l)
		{
			for (int i1 = pos3.getY(); i1 <= pos4.getY(); ++i1)
			{
				for (int j1 = pos3.getX(); j1 <= pos4.getX(); ++j1)
				{
					BlockPos blockpos = new BlockPos(j1, i1, l);
					world.setBlockState(blockpos, DimBagRefs.BagjerumOutBlock.getDefaultState());
					TileEntity te =  world.getTileEntity(blockpos);
					if(te != null && te instanceof DimBagOutTile)
					{
						DimBagOutTile t =(DimBagOutTile) te;
						t.setOwner(owner);
					}
				}
			}
		}
	}
}
