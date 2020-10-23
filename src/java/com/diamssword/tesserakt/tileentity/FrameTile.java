package com.diamssword.tesserakt.tileentity;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Configs;
import com.diamssword.tesserakt.Registers;
import com.diamssword.tesserakt.blocks.CraftBlock;
import com.diamssword.tesserakt.blocks.EmptyBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FrameTile extends TileEntity {
	private int amount=0;
	private static Fluid ender=null;
	private static Fluid red=null;
	private boolean isRedstone=false;
	public FrameTile()
	{
		if(ender == null)
		{
			ender=FluidRegistry.getFluid(Configs.enderFluid);
			if(ender == null)
			{
				ender = FluidRegistry.LAVA;
				Configs.enderFluid=  FluidRegistry.LAVA.getName();
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("amount", this.amount);
		tag.setBoolean("alt", this.isRedstone);
		return super.writeToNBT(tag);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.amount = nbt.getInteger("amount");
		this.isRedstone = nbt.getBoolean("alt");
		super.readFromNBT(nbt);
	}
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {

		if(capability ==  CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {

		if(capability ==  CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return (T) handler;
		}
		return super.getCapability(capability, facing);
	}
	IFluidHandler handler=	new IFluidHandler() {

		@Override
		public IFluidTankProperties[] getTankProperties() {
			return new IFluidTankProperties[] {new IFluidTankProperties() {

				@Override
				public FluidStack getContents() {
					return new FluidStack(isRedstone?red:ender,amount);
				}

				@Override
				public int getCapacity() {
					return 1000;
				}

				@Override
				public boolean canFill() {
					return true;
				}

				@Override
				public boolean canDrain() {
					return false;
				}

				@Override
				public boolean canFillFluidType(FluidStack fluidStack) {
					if(amount==0)
						return fluidStack.getFluid().equals(ender) ||fluidStack.getFluid().equals(red);
					return fluidStack.getFluid().equals(isRedstone?red:ender);
				}

				@Override
				public boolean canDrainFluidType(FluidStack fluidStack) {
					return false;
				}}};
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if(amount ==0)
				if(resource.getFluid().equals(red))
					isRedstone=true;
				else
					isRedstone=false;
			int res= resource.amount +amount;
			int ret = resource.amount;
			if(res >1000)
			{
				res=1000;
				ret =1000;
			}
			if(doFill)
			{
				amount = res;
				markDirty();
				transform();
			}
			return ret;
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			FluidStack stack = resource.copy();
			stack.amount=0;
			return stack;
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			return new FluidStack(isRedstone?red:ender, 0);
		}};
		public void transform()
		{
			if(!this.world.isRemote)
			{
				if(this.amount>=1000)
				{
					this.world.setBlockState(pos, Registers.blockFrameFull.getDefaultState().withProperty(CraftBlock.TYPE, this.isRedstone));
					this.invalidate();
				}
				else
				{
					IBlockState st = this.blockType.getDefaultState().withProperty(EmptyBlock.FILLED, this.amount/250);
					this.world.setBlockState(pos, st,2);
					world.notifyBlockUpdate(pos, st,st, 3);
					world.notifyNeighborsOfStateChange(pos, blockType, true);
				}
			}
		}
		@Override
		public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
		{
			return oldState.getBlock() != newSate.getBlock();
		}
}
