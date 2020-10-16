package com.diamssword.tesserakt.blocks;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.Registers;
import com.diamssword.tesserakt.tileentity.ExponentialBatteryTile;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ExponentialBatteryBlock  extends Block{
	public ExponentialBatteryBlock() {
		super(Material.IRON);
		this.setRegistryName("exponential_battery");

		this.setUnlocalizedName(Main.MODID+".exponential_battery");
		this.setCreativeTab(Registers.tab);

	}
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			worldIn.notifyBlockUpdate(pos,state, state, 3);

		}
		else
		{
			playerIn.openGui(Main.instance, 3, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}
	public ExponentialBatteryTile getTileEntity(IBlockAccess world, BlockPos pos) {
		return (ExponentialBatteryTile)world.getTileEntity(pos);
	}
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public ExponentialBatteryTile createTileEntity(World world, IBlockState state)
	{
		return new ExponentialBatteryTile();

	}
}
