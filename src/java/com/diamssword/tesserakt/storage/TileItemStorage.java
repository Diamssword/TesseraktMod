package com.diamssword.tesserakt.storage;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemStorage implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound>
{
	private Tesserakt tes;
	private boolean input = false;
	private boolean output = false;
	public TileItemStorage(Tesserakt tes)
	{
		super();
		this.tes=tes;
	}

	public void setIO(boolean in, boolean out)
	{
		this.input = in;
		this.output = out;
	}
	public void setSize(int size)
	{
		tes.item.setSize(size);
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack)
	{
		tes.item.setStackInSlot(slot, stack);
	}

	@Override
	public int getSlots()
	{
		return tes.item.getSlots();
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot(int slot)
	{
		return tes.item.getStackInSlot(slot);
	}

	@Override
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	{
		if (!input)
			return stack;
		return tes.item.insertItem(slot, stack, simulate);
	}

	@Override
	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (!output)
			return ItemStack.EMPTY;
		return tes.item.extractItem(slot, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return tes.item.getSlotLimit(slot);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	{
		return true;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{

		return tes.item.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		tes.item.deserializeNBT(nbt);
	}
}
