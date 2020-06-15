package com.diamssword.tesserakt.dimensional_bag;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.utils.TEBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class DimBagStyleBlock extends Block implements TEBlock<DimBagStyleTile>{

	public DimBagStyleBlock() {
		super(Material.CLOTH);

		this.setUnlocalizedName(Main.MODID+".dim_bag_style");
		this.setRegistryName("dim_bag_style");
		this.setBlockUnbreakable();
		this.setResistance(999999999);
	}
	
	@Override
	public DimBagStyleTile createTileEntity(World world, IBlockState state) {
		return new DimBagStyleTile();
	}

	@Override
	public Class<DimBagStyleTile> getTileEntityClass() {
		return DimBagStyleTile.class;
	}
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
}
