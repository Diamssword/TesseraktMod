package com.diamssword.tesserakt.dimensional_bag;

import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.vecmath.Vector3d;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.events.ServerEvents;
import com.diamssword.tesserakt.events.ServerEvents.EntityPacket;
import com.diamssword.tesserakt.utils.TEBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DimBagOutBlock extends Block implements TEBlock<DimBagOutTile>{

	public DimBagOutBlock() {
		super(Material.CLOTH);
		this.setUnlocalizedName(Main.MODID+".dim_bag_teleporter");
		this.setRegistryName("dim_bag_teleporter");
		this.setBlockUnbreakable();
		this.setResistance(999999999);
		this.setLightLevel(0.8f);
	}

	@Override
	public DimBagOutTile createTileEntity(World world, IBlockState state) {
		return new DimBagOutTile();
	}

	@Override
	public Class<DimBagOutTile> getTileEntityClass() {
		return DimBagOutTile.class;
	}
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity ent)
	{
		if(!worldIn.isRemote)
		{
			if (!ent.isRiding() && !ent.isBeingRidden())
			{
				TileEntity te = worldIn.getTileEntity(pos);
				if(te != null && te instanceof DimBagOutTile)
				{
					UUID owner = ((DimBagOutTile) te).getOwner();
					DimBagNBT bag=DimBagNBT.get(worldIn, owner);
					if(bag != null)
					{
						ServerEvents.tooTP.add(new EntityPacket(ent, new Vector3d(bag.bagPos.getX()+0.5, bag.bagPos.getY()+0.5, bag.bagPos.getZ()+0.5), bag.bagDim));
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		worldIn.spawnParticle(EnumParticleTypes.REDSTONE, true, pos.getX()+1+rand.nextDouble(), pos.getY()+rand.nextDouble(), pos.getZ()+1+rand.nextDouble(), 204/255, rand.nextFloat()*0.7, 153/255);
	}

}
