package com.diamssword.tesserakt.storage;

import net.minecraftforge.fluids.FluidTank;

public class TFluidStorage extends FluidTank{

	private Tesserakt parent;
	public TFluidStorage(int capacity,Tesserakt parent) {
		super(capacity);
		this.parent = parent;
	}
	@Override
	protected void onContentsChanged()
	{
		Tesserakt.save(parent.world,parent);
	}
}
