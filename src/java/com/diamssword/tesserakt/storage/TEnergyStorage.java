package com.diamssword.tesserakt.storage;

import net.minecraftforge.energy.EnergyStorage;

public class TEnergyStorage extends EnergyStorage{

	private Tesserakt parent;
	public TEnergyStorage(int capacity,Tesserakt parent) {
		super(capacity);
		this.parent=parent;
	}
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		int res= super.extractEnergy(maxExtract, simulate);
		if(res != 0 && !simulate)
			Tesserakt.save(parent.world,parent);
		return res;

	}
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		int res= super.receiveEnergy(maxReceive, simulate);
		if(res != 0 && !simulate)
			Tesserakt.save(parent.world,parent);
		return res;

	}
}
