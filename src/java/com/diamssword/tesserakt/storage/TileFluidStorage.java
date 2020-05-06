package com.diamssword.tesserakt.storage;

import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileFluidStorage implements IFluidTank, IFluidHandler
{

	protected Tesserakt tes;

	private boolean input = false;
	private boolean output = false;

	private IFluidTankProperties[] tankProperties;
	public TileFluidStorage(Tesserakt tess)
	{
		this.tes=tess;
	}
	public void setIO(boolean in, boolean out)
	{
		this.input = in;
		this.output = out;
	} 
	/* IFluidTank */
	@Override
	@Nullable
	public FluidStack getFluid()
	{
		return tes.fluid.getFluid();
	}

	public void setFluid(@Nullable FluidStack fluid)
	{
		tes.fluid.setFluid(fluid);
	}

	@Override
	public int getFluidAmount()
	{

		return tes.fluid.getFluidAmount();
	}

	@Override
	public int getCapacity()
	{
		return tes.fluid.getCapacity();
	}

	public void setCapacity(int capacity)
	{
		tes.fluid.setCapacity(capacity);
	}

	public void setTileEntity(TileEntity tile)
	{
		tes.fluid.setTileEntity(tile);
	}

	@Override
	public FluidTankInfo getInfo()
	{
		return tes.fluid.getInfo();
	}

	@Override
	public IFluidTankProperties[] getTankProperties()
	{
		if (this.tankProperties == null)
		{
			this.tankProperties = new IFluidTankProperties[] { new IFluidTankProperties() {

				@Override
				public FluidStack getContents() {
					return tes.fluid.getFluid();
				}
				@Override
				public int getCapacity() {
					return this.getCapacity();
				}
				@Override
				public boolean canFill() {
					return input;
				}
				@Override
				public boolean canDrain() {
					return output;
				}
				@Override
				public boolean canFillFluidType(FluidStack fluidStack) {
					return canFillFluidType(fluidStack);
				}
				@Override
				public boolean canDrainFluidType(FluidStack fluidStack) {
					return canDrainFluidType(fluidStack);
				}} };
		}
		return this.tankProperties;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		if (!this.canFill())
		{
			return 0;
		}
		int res= tes.fluid.fill(resource, doFill);
		return res;
	}

	/**
	 * Use this method to bypass the restrictions from {@link #canFillFluidType(FluidStack)}
	 * Meant for use by the owner of the tank when they have {@link #canFill() set to false}.
	 */
	public int fillInternal(FluidStack resource, boolean doFill)
	{
		return tes.fluid.fillInternal(resource, doFill);
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		if (!this.canDrain())
		{
			return null;
		}
		return tes.fluid.drain(resource, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		if (!this.canDrain())
		{
			return null;
		}
		return tes.fluid.drain(maxDrain, doDrain);
	}

	/**
	 * Use this method to bypass the restrictions from {@link #canDrainFluidType(FluidStack)}
	 * Meant for use by the owner of the tank when they have {@link #canDrain()} set to false}.
	 */
	@Nullable
	public FluidStack drainInternal(FluidStack resource, boolean doDrain)
	{
		return tes.fluid.drainInternal(resource, doDrain);
	}

	/**
	 * Use this method to bypass the restrictions from {@link #canDrainFluidType(FluidStack)}
	 * Meant for use by the owner of the tank when they have {@link #canDrain()} set to false}.
	 */
	@Nullable
	public FluidStack drainInternal(int maxDrain, boolean doDrain)
	{
		return tes.fluid.drainInternal(maxDrain, doDrain);
	}

	/**
	 * Whether this tank can be filled with {@link IFluidHandler}
	 *
	 * @see IFluidTankProperties#canFill()
	 */
	public boolean canFill()
	{
		return this.input;
	}

	/**
	 * Whether this tank can be drained with {@link IFluidHandler}
	 *
	 * @see IFluidTankProperties#canDrain()
	 */
	public boolean canDrain()
	{
		return this.output;
	}

	/**
	 * Set whether this tank can be filled with {@link IFluidHandler}
	 *
	 * @see IFluidTankProperties#canFill()
	 */
	public void setCanFill(boolean canFill)
	{

	}

	/**
	 * Set whether this tank can be drained with {@link IFluidHandler}
	 *
	 * @see IFluidTankProperties#canDrain()
	 */
	public void setCanDrain(boolean canDrain)
	{

	}

	/**
	 * Returns true if the tank can be filled with this type of fluid.
	 * Used as a filter for fluid types.
	 * Does not consider the current contents or capacity of the tank,
	 * only whether it could ever fill with this type of fluid.
	 *
	 * @see IFluidTankProperties#canFillFluidType(FluidStack)
	 */
	public boolean canFillFluidType(FluidStack fluid)
	{
		return canFill();
	}

	/**
	 * Returns true if the tank can drain out this type of fluid.
	 * Used as a filter for fluid types.
	 * Does not consider the current contents or capacity of the tank,
	 * only whether it could ever drain out this type of fluid.
	 *
	 * @see IFluidTankProperties#canDrainFluidType(FluidStack)
	 */
	public boolean canDrainFluidType(@Nullable FluidStack fluid)
	{
		return fluid != null && canDrain();
	}

	protected void onContentsChanged()
	{

	}
}