package com.diamssword.tesserakt.blocks;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.Registers;
import com.diamssword.tesserakt.tileentity.DimensionalBatteryTile;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DimensionalBatteryBlock  extends Block{
	protected static final AxisAlignedBB BASE_HITBOX = new AxisAlignedBB(0.15D, 0.0D, 0.15D, 0.85D, 0.95D, 0.85D);
	public DimensionalBatteryBlock() {
		super(Material.IRON);
		this.setRegistryName("dimensional_battery");
	
		this.setUnlocalizedName(Main.MODID+".dimensional_battery");
		this.setCreativeTab(Registers.tab);

	}
	  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	    {
		  DimensionalBatteryTile te = this.getTileEntity(worldIn, pos);
		  if(hitY>0.5f)
			  te.setSize(te.size+1);
		  else
			  te.setSize(te.size-1);
	        return true;
	    }
	public DimensionalBatteryTile getTileEntity(IBlockAccess world, BlockPos pos) {
		return (DimensionalBatteryTile)world.getTileEntity(pos);
	}
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public DimensionalBatteryTile createTileEntity(World world, IBlockState state)
	{
		return new DimensionalBatteryTile();

	}
/*	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		DimensionalBatteryTile tile =this.getTileEntity(source, pos);
		if(tile != null)
		return tile.getHitBox();
		else
			return super.getBoundingBox(state, source, pos);
		
	}
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
	{
		DimensionalBatteryTile tile =this.getTileEntity(worldIn, pos);
		if(tile != null)
		addCollisionBoxToList(pos, entityBox, collidingBoxes, tile.getHitBox());
	}*/
}
