package com.diamssword.tesserakt.storage;

import net.minecraftforge.energy.IEnergyStorage;

public class TileEnergyStorage implements IEnergyStorage
{
    protected Tesserakt tes;

	private boolean input = false;
	private boolean output = false;
    public TileEnergyStorage(Tesserakt tess)
    {
    	this.tes=tess;
    }
    public void setIO(boolean in, boolean out)
    {
    	this.input = in;
    	this.output = out;
    }
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
    	if(!this.input)
    		return 0;
        return tes.energy.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
    	if(!this.output)
    		return 0;
      return tes.energy.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored()
    {
 
        return this.tes.energy.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored()
    {
        return this.tes.energy.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract()
    {
        return this.output;
    }

    @Override
    public boolean canReceive()
    {
        return this.input;
    }
}