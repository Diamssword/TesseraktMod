package com.diamssword.tesserakt.items;

import java.util.List;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.Registers;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BatteryPart extends Item{

	public BatteryPart() {
		this.setUnlocalizedName(Main.MODID+".battery_part");
		this.setRegistryName("battery_part");
		this.setMaxStackSize(1);
		this.setCreativeTab(Registers.tab);
	}
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(stack.getItem() == this)
		{
			tooltip.add("Don't drop it! You might shred reality...");
		}
	}

}
