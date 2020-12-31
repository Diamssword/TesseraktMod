package com.diamssword.tesserakt.items;

import com.diamssword.tesserakt.Registers;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemSubBlock extends ItemBlock
{
	private String[] names;
	public ItemSubBlock(Block block,  String[] names)
	{
		super(block);
		this.hasSubtypes = true;
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setCreativeTab(Registers.tab);
		this.setMaxStackSize(64);
		this.setUnlocalizedName(block.getUnlocalizedName());
		this.names = names;
	}
	public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		if(itemIn.equals(Registers.tab) ||itemIn.equals(CreativeTabs.SEARCH))
			for (int i=0;i<names.length;i++)
			{
				items.add(new ItemStack(this, 1, i));
			}
	}
	/**
	 * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
	 * placed as a Block (mostly used with ItemBlocks).
	 */
	public int getMetadata(int damage)
	{
		return damage;
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
	 * different names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack stack)
	{

		int i = stack.getMetadata();
		return i >= 0 && i < this.names.length ? super.getUnlocalizedName(stack) + "." + this.names[i] : super.getUnlocalizedName(stack);
	}
}