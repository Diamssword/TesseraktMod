package com.diamssword.tesserakt.dimensional_bag;

import com.diamssword.tesserakt.Main;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class DimBagWallBlock extends Block{

	public DimBagWallBlock() {
		super(Material.CLOTH);
		this.setUnlocalizedName(Main.MODID+".dim_bag_wall_block");
		this.setRegistryName("dim_bag_wall_block");
		this.setBlockUnbreakable();
		this.setResistance(999999999);
		this.setBlockUnbreakable();
		this.setResistance(9999999999999f);
	}

}
