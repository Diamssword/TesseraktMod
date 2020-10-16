package com.diamssword.tesserakt.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.Registers;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnderWoolBlock extends Block{
	public EnderWoolBlock(String name) {
		super(Material.CLOTH);
		this.setRegistryName(name);
		this.setHardness(0.8f);
		this.setResistance(1000f);
		this.setHarvestLevel("shear", 1);
		this.setSoundType(SoundType.CLOTH);
		this.setUnlocalizedName(Main.MODID+"."+name);
		this.setCreativeTab(Registers.tab);

	}
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(stack.getItem() == Item.getItemFromBlock(this))
		{
			tooltip.add("Used for crafts");
			tooltip.add("Teleport jumping entity on top of it to the closest ender wool in the axis it is facing");
		}
	}
}
