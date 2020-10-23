package com.diamssword.tesserakt.tileentity;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.Registers;
import com.diamssword.tesserakt.blocks.ExponentialBatteryBlock;
import com.diamssword.tesserakt.packets.PacketRequestTile;
import com.diamssword.tesserakt.utils.BatteryEnergyStorage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ExponentialBatteryTile extends TileEntity implements ITickable{
	BatteryEnergyStorage storage = new BatteryEnergyStorage(100, 0,this.world,this.pos);
	@Override
	public void update() {
		if(!this.world.isRemote && storage != null && storage.changed)
		{
			storage.changed=false;
			IBlockState st = world.getBlockState(pos);
			st = st.withProperty(ExponentialBatteryBlock.LEVEL, this.getLevel());
			world.setBlockState(pos, st,3);
			world.notifyBlockUpdate(pos, st,st, 3);
			world.notifyNeighborsOfStateChange(pos, blockType, true);
			this.pushTo(pos.up(), EnumFacing.DOWN);
			this.pushTo(pos.down(), EnumFacing.UP);
		}
	}
	private void pushTo(BlockPos pos,EnumFacing facing )
	{
		TileEntity te = world.getTileEntity(pos);
		if(te != null)
		{
			if(storage != null && te.hasCapability(CapabilityEnergy.ENERGY, facing))
			{
				IEnergyStorage en =te.getCapability(CapabilityEnergy.ENERGY, facing);
				if(en.canReceive())
				{
					int am=en.receiveEnergy(this.storage.getEnergyStored(), false);
					this.storage.extractEnergy(am, false);
				}
			}
		}
	}
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock();
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("stored",this.storage.getEnergyStored());
		tag.setInteger("additional",this.storage.getAdditionalEnergy());
		tag.setInteger("max", this.storage.getMaxEnergyStored());
		return super.writeToNBT(tag);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		int max= nbt.getInteger("max");
		if( max>0)
		{
			storage = new BatteryEnergyStorage(max, nbt.getInteger("stored"),nbt.getInteger("additional"),this.world,this.pos);
		}
		super.readFromNBT(nbt);
	}
	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {

		if(capability ==  CapabilityEnergy.ENERGY && storage != null)
		{
			return (T) storage;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if(capability ==  CapabilityEnergy.ENERGY)
		{
			return storage != null;
		}
		return super.hasCapability(capability, facing);
	}

	public double getAdditionalPercentFilled()
	{
		return (double)this.storage.getAdditionalEnergy()/(double)this.storage.getMaxEnergyStored();
	}
	public double getPercentFilled()
	{
		return (double)this.storage.getEnergyStored()/(double)this.storage.getMaxEnergyStored();
	}
	public int getMaxIO()
	{
		return this.storage.getMaxIO();
	}
	public int getMaxEnergy()
	{
		return this.storage.getMaxEnergyStored();
	}
	public int getEnergy()
	{
		return this.storage.getEnergyStored();
	}
	public int getAdditionalEnergy()
	{
		return this.storage.getAdditionalEnergy();
	}
	public void setEnergy(BatteryEnergyStorage st)
	{
		this.storage = st;
	}
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new SPacketUpdateTileEntity(this.pos, 1, nbtTag);
	}

	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
		world.notifyNeighborsOfStateChange(pos, blockType, true);
		IBlockState st = world.getBlockState(pos);
		st = st.withProperty(ExponentialBatteryBlock.LEVEL, this.getLevel());
		world.setBlockState(pos, st,3);
		world.notifyBlockUpdate(pos, st,st, 3);
		world.notifyNeighborsOfStateChange(pos, blockType, true);
	}
	public int getLevel()
	{
		if(this.storage != null)
		{
			int i= Math.round(Math.round(this.getPercentFilled() * 7d));
			if(this.getPercentFilled()<=0 && i>0)
				i=0;
			if(this.getPercentFilled()<1 && i>=7)
				i=6;
			return i;
		}
		return 0;
	}
	@Override
	public void onLoad()
	{
		if(this.world != null && !this.world.isRemote)
		{

			IBlockState st = world.getBlockState(pos);
			world.notifyBlockUpdate(pos,st, st.withProperty(ExponentialBatteryBlock.LEVEL, this.getLevel()), 3);
			world.setBlockState(pos, st.withProperty(ExponentialBatteryBlock.LEVEL, this.getLevel()));
			world.notifyNeighborsOfStateChange(pos, Registers.exponentialBattery, true);
		}
		if(this.world.isRemote)
		{
			Main.network.sendToServer(new PacketRequestTile(this.getPos()));
		}
	}
}
