package com.diamssword.tesserakt.tileentity;

import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class CheatedBatteryTile extends TileEntity implements ITickable{
	IEnergyStorage energy = new IEnergyStorage() {

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return Integer.MAX_VALUE;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return Integer.MAX_VALUE;
		}

		@Override
		public int getEnergyStored() {
			return Integer.MAX_VALUE;
		}

		@Override
		public int getMaxEnergyStored() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean canExtract() {
			return true;
		}

		@Override
		public boolean canReceive() {
			return true;
		}};
	@Override
	public void update() {
	
		if(!world.isRemote)
		{
		
			if(energy != null && this.energy.canExtract())
			{
				this.pushTo(pos.up(), EnumFacing.DOWN);
				this.pushTo(pos.down(), EnumFacing.UP);
				this.pushTo(pos.east(), EnumFacing.WEST);
				this.pushTo(pos.west(), EnumFacing.EAST);
				this.pushTo(pos.north(), EnumFacing.SOUTH);
				this.pushTo(pos.south(), EnumFacing.NORTH);
			}
		}
	}

	private void pushTo(BlockPos pos,EnumFacing facing )
	{
		TileEntity te = world.getTileEntity(pos);
		if(te != null)
		{
			if(te.hasCapability(CapabilityEnergy.ENERGY, facing))
			{
				IEnergyStorage en =te.getCapability(CapabilityEnergy.ENERGY, facing);
				if(en.canReceive())
				{
					int am=en.receiveEnergy(this.energy.getEnergyStored(), false);
					this.energy.extractEnergy(am, false);
				}
			}
		}
	}


	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if(this.isInvalid())
			return false;
	
		if(capability ==  CapabilityEnergy.ENERGY)
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {

		if(capability ==  CapabilityEnergy.ENERGY && energy != null)
		{
			return (T) energy;
		}
		return super.getCapability(capability, facing);
	}
}
