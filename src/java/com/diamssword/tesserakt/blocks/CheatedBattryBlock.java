package com.diamssword.tesserakt.blocks;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.Registers;
import com.diamssword.tesserakt.tileentity.CheatedBatteryTile;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class CheatedBattryBlock  extends Block{
	public CheatedBattryBlock() {
		super(Material.IRON);
		this.setRegistryName("wtf_man");
	
		this.setUnlocalizedName(Main.MODID+".wtf_man");
		this.setCreativeTab(Registers.tab);

	}
	public CheatedBatteryTile getTileEntity(IBlockAccess world, BlockPos pos) {
		return (CheatedBatteryTile)world.getTileEntity(pos);
	}
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public CheatedBatteryTile createTileEntity(World world, IBlockState state)
	{
		return new CheatedBatteryTile();

	}
}
