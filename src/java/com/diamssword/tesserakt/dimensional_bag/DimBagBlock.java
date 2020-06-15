package com.diamssword.tesserakt.dimensional_bag;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.utils.TEBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DimBagBlock extends Block implements TEBlock<DimBagTile>{

	protected static final AxisAlignedBB BASE_HITBOX = new AxisAlignedBB(0.15D, 0.0D, 0.15D, 0.85D, 0.5D, 0.85D);
	public static PropertyBool TRIGGERED = PropertyBool.create("triggered");
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public DimBagBlock() {
		super(Material.CLOTH);
		this.setUnlocalizedName(Main.MODID+".dimensional_bag");
		this.setRegistryName("dimensional_bag");
		this.setBlockUnbreakable();
		this.setResistance(999999999);
		this.setDefaultState(this.getDefaultState().withProperty(TRIGGERED, false));
	}
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			TileEntity tile = worldIn.getTileEntity(pos);
			if(tile != null && tile instanceof DimBagTile)
			{
				UUID id =((DimBagTile) tile).getOwner();
				if(id!= null)
				{
					DimBagLogic.TeleportPlayerToRoom(playerIn, id);

				}
			}
		}
		return true;
	}
	@Override
	public DimBagTile createTileEntity(World world, IBlockState state) {
		return new DimBagTile();
	}
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	public boolean isTopSolid(IBlockState state)
	{
		return true;
	}
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BASE_HITBOX;

	}
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
	{
		addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_HITBOX);
	}
	@Override
	public Class<DimBagTile> getTileEntityClass() {
		return DimBagTile.class;
	}
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, TRIGGERED,FACING);
	}
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
		boolean flag1 = ((Boolean)state.getValue(TRIGGERED)).booleanValue();

		if (flag && !flag1)
		{
			worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
			worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 3);
		}
		else if (!flag && flag1)
		{
			worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 3);
		}
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(TRIGGERED, Boolean.valueOf((meta & 8) > 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i = i | ((EnumFacing)state.getValue(FACING)).getIndex();

		if (((Boolean)state.getValue(TRIGGERED)).booleanValue())
		{
			i |= 8;
		}

		return i;
	}

	public static EnumFacing getFacing(int meta)
	{
		if(meta <  EnumFacing.values().length-2)
			return EnumFacing.getFront(meta+2);
		else
			return EnumFacing.NORTH;
	}
}
