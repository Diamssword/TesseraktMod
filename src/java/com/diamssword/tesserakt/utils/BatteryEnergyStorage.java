package com.diamssword.tesserakt.utils;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.EnergyStorage;

public class BatteryEnergyStorage extends EnergyStorage{
	public int aditionalenergy=0;
	public final World world;
	public final BlockPos pos;
	public boolean changed= true;
	public BatteryEnergyStorage(int capacity,int stored,@Nullable World w,@Nullable BlockPos pos) {
		super(capacity,capacity/100,capacity/100,stored);
		this.world=w;
		this.pos=pos;
	}
	public BatteryEnergyStorage(int capacity,int stored,int aditional,@Nullable World w,@Nullable BlockPos pos) {
		super(capacity,capacity/100,capacity/100,stored);
		this.aditionalenergy = aditional;
		this.world=w;
		this.pos=pos;
	}	
	public int getMaxIO()
	{
		return this.maxExtract;
	}
	public void upgradeCapacity()
	{
		this.capacity*=2;
		if(this.capacity<0)
			this.capacity = Integer.MAX_VALUE;
		this.maxExtract = this.capacity/100;
		this.maxReceive = this.capacity/100;
		this.aditionalenergy = 0;
	}
	public int getAdditionalEnergy()
	{
		return this.aditionalenergy;
	}
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		if (!canExtract())
			return 0;

		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
		if (!simulate)
		{
			energy -= energyExtracted;
			this.sendChange();
		}
		return energyExtracted;
	}
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		if (!canReceive())
			return 0;
		if(energy == capacity)
		{
			if(this.capacity == Integer.MAX_VALUE)
				return 0;
			int energyReceived = Math.min(capacity - aditionalenergy, Math.min(this.maxReceive, maxReceive));
			if (!simulate)
			{
				aditionalenergy += energyReceived;
				if(aditionalenergy == this.capacity)
				{
					this.upgradeCapacity();
				}
				this.sendChange();
			}
			return energyReceived;
		}
		else
		{
			int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
			if (!simulate)
			{
				this.sendChange();
				energy += energyReceived;
			}
			return energyReceived;
		}
	}

	private void sendChange()
	{
		this.changed = true;
	}
	

}
