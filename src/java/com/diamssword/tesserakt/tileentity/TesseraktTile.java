package com.diamssword.tesserakt.tileentity;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.Registers;
import com.diamssword.tesserakt.blocks.TesseraktBlock;
import com.diamssword.tesserakt.packets.PacketRequestTile;
import com.diamssword.tesserakt.storage.Tesserakt;
import com.diamssword.tesserakt.storage.TileEnergyStorage;
import com.diamssword.tesserakt.storage.TileFluidStorage;
import com.diamssword.tesserakt.storage.TileItemStorage;

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

public class TesseraktTile extends TileEntity implements ITickable{
	private Tesserakt tesseract;
	TileEnergyStorage energy;
	TileFluidStorage fluid;
	TileItemStorage item;
	private int oldRedstoneVal=0;
	private int id=0;
	private boolean connected=false;
	private  int ioEnergy=3;
	private  int ioItem=3;
	private  int ioFluid=3;
	private  int ioRedstone=0;
	@Override
	public void update() {
		if(this.world != null)
		{
			if(this.world.getBlockState(pos).getBlock() != Registers.blockTesserakt)
				this.invalidate();
		}
		if(!world.isRemote)
		{
			if(tesseract == null && this.connected)
			{
				this.tesseract =Tesserakt.get(this.world, id);
				Tesserakt.save(this.world, tesseract);
				if(tesseract != null)
				{
					energy = new TileEnergyStorage(tesseract);
					energy.setIO(this.canInput(0), this.canOutput(0));
					fluid = new TileFluidStorage(tesseract);
					fluid.setIO(this.canInput(2), this.canOutput(2));
					item = new TileItemStorage(tesseract);
					item.setIO(this.canInput(1), this.canOutput(1));

					IBlockState st =world.getBlockState(pos);
					world.notifyBlockUpdate(pos, st,st.withProperty(TesseraktBlock.CONNECTED, this.connected), 3);
					world.setBlockState(pos,st.withProperty(TesseraktBlock.CONNECTED, false));
					world.notifyNeighborsOfStateChange(pos, blockType, true);
				}
			}
			if(tesseract != null)
			{
				this.pushTo(pos.up(), EnumFacing.DOWN);
				this.pushTo(pos.down(), EnumFacing.UP);
				this.pushTo(pos.east(), EnumFacing.WEST);
				this.pushTo(pos.west(), EnumFacing.EAST);
				this.pushTo(pos.north(), EnumFacing.SOUTH);
				this.pushTo(pos.south(), EnumFacing.NORTH);
			}
			if(tesseract != null && tesseract.redstonePowered != this.oldRedstoneVal && world != null)
			{
				this.oldRedstoneVal = tesseract.redstonePowered;
				IBlockState st =world.getBlockState(pos);
				world.notifyBlockUpdate(pos,st,st, 3);
				world.notifyNeighborsOfStateChange(pos,Registers.blockTesserakt,true);
			}
		}
	}

	private void pushTo(BlockPos pos,EnumFacing facing )
	{
		TileEntity te = world.getTileEntity(pos);
		if(te != null)
		{
			if(energy != null && this.energy.canExtract() && te.hasCapability(CapabilityEnergy.ENERGY, facing))
			{
				IEnergyStorage en =te.getCapability(CapabilityEnergy.ENERGY, facing);
				if(en.canReceive())
				{
					int am=en.receiveEnergy(this.energy.getEnergyStored(), false);
					this.energy.extractEnergy(am, false);
				}
			}
			if(fluid != null && this.fluid.canDrain() && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing))
			{
				IFluidHandler en =te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
				int am=en.fill(this.fluid.getFluid(), false);
				this.fluid.drain(am, true);
			}
			if(item != null && this.canOutput(1) && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing))
			{
				IItemHandler en =te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
				for(int slot=0;slot<en.getSlots();slot++)
				{
					ItemStack am=en.insertItem(slot, this.item.getStackInSlot(0), false);
					this.item.setStackInSlot(0, am);
					if(am.isEmpty())
						break;
				}
			}
		}
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("channel", this.id);
		tag.setBoolean("connected", this.connected);
		tag.setInteger("ioEnergy", this.ioEnergy);
		tag.setInteger("ioItem", this.ioItem);
		tag.setInteger("ioFluid", this.ioFluid);
		tag.setInteger("ioRedstone", this.ioRedstone);
		return super.writeToNBT(tag);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		this.id = nbt.getInteger("channel");
		this.ioEnergy = nbt.getInteger("ioEnergy");
		this.ioItem = nbt.getInteger("ioItem");
		this.ioFluid = nbt.getInteger("ioFluid");
		this.connected = nbt.getBoolean("connected");
		this.ioRedstone =  nbt.getInteger("ioRedstone");
		super.readFromNBT(nbt);
	}

	public int getIoEnergy() {
		return ioEnergy;
	}
	public int getIoItem() {
		return ioItem;
	}
	public int getIoFluid() {
		return ioFluid;
	}
	public int getIoRedstone() {
		return ioRedstone;
	}
	public void setIO(int type,int mode)
	{
		if(type==0)
			this.ioEnergy=mode;
		if(type==1)
			this.ioItem=mode;
		if(type==2)
			this.ioFluid=mode;
		if(type==3)
			this.ioRedstone=mode;
		if(!world.isRemote)
		{
			if(this.energy != null)
				this.energy.setIO(this.canInput(0), this.canOutput(0));
			if(this.item != null)
				this.item.setIO(this.canInput(1), this.canOutput(1));
			if(this.fluid != null)
				this.fluid.setIO(this.canInput(2), this.canOutput(2));
			this.markDirty();
		}
		IBlockState st = world.getBlockState(pos);
		world.notifyBlockUpdate(pos,st, st.withProperty(TesseraktBlock.CONNECTED, this.connected), 3);
		world.notifyNeighborsOfStateChange(pos, Registers.blockTesserakt, true);
	}
	public void updateRedstone(int power)
	{
		if(this.tesseract != null && this.connected)
		{
			this.tesseract.redstonePowered = power;
			Tesserakt.save(world, tesseract);
		}
	}
	public int getRedstone()
	{
		if(this.tesseract != null && this.connected)
		{
			return this.tesseract.redstonePowered;
		}
		return 0;
	}
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if(!this.connected || this.isInvalid())
			return false;
		if(world.isRemote)
		{
			return (capability ==  CapabilityEnergy.ENERGY && this.ioEnergy!=0)||(capability ==  CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.ioItem!=0)||(capability ==  CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this.ioFluid!=0);
		}
		if(capability ==  CapabilityEnergy.ENERGY && this.ioEnergy!=0 && energy!= null)
		{
			return true;
		}
		if(capability ==  CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this.ioFluid!=0&& fluid!= null)
		{
			return true;
		}
		if(capability ==  CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.ioItem!=0 && item!= null)
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	public boolean canInput(int type)
	{
		int val =0; 
		if(type==0)
			val = this.ioEnergy;
		if(type==1)
			val = this.ioItem;
		if(type==2)
			val = this.ioFluid;
		return (val == 1 || val== 3);
	}
	public boolean canOutput(int type)
	{
		int val =0; 
		if(type==0)
			val = this.ioEnergy;
		if(type==1)
			val = this.ioItem;
		if(type==2)
			val = this.ioFluid;
		return (val == 2 || val== 3);
	}
	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {

		if(capability ==  CapabilityEnergy.ENERGY && energy != null)
		{
			return (T) energy;
		}
		if(capability ==  CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && fluid != null)
		{
			return (T) fluid;
		}
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && item != null)
		{
			return (T) item;
		}
		return super.getCapability(capability, facing);
	}
	public void setChannelAndActivate(int channel)
	{
		this.id = channel;
		this.tesseract = null;
		this.connected=true;
		this.markDirty();

		IBlockState st = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, st.withProperty(TesseraktBlock.CONNECTED, false),st.withProperty(TesseraktBlock.CONNECTED, this.connected), 3);
		world.setBlockState(pos, st.withProperty(TesseraktBlock.CONNECTED, true));
		world.notifyNeighborsOfStateChange(pos, blockType, true);
	}
	public void desactivate()
	{
		this.connected = false;
		this.tesseract = null;
		this.energy = null;
		this.fluid = null;
		this.item= null;
		this.markDirty();

		IBlockState st = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, st,st.withProperty(TesseraktBlock.CONNECTED, this.connected), 3);
		world.setBlockState(pos,st.withProperty(TesseraktBlock.CONNECTED, false));
		world.notifyNeighborsOfStateChange(pos, blockType, true);
	}
	public ItemStack toItemNBT()
	{
		ItemStack stack=new ItemStack(Registers.blockTesserakt,1);
		NBTTagCompound tag = new NBTTagCompound (); 
		tag.setInteger("channel", this.id);
		tag.setInteger("ioEnergy", this.ioEnergy);
		tag.setInteger("ioItem", this.ioItem);
		tag.setInteger("ioFluid", this.ioFluid);
		tag.setInteger("ioRedstone", this.ioRedstone);
		tag.setBoolean("connected", this.connected);
		stack.setTagCompound(tag);
		return stack;
	}
	public void fromItemNBT(ItemStack stack)
	{
		NBTTagCompound tag = stack.getTagCompound();
		if(tag != null)
		{
			this.id = tag.getInteger("channel");
			this.ioEnergy=tag.getInteger("ioEnergy");
			this.ioFluid=tag.getInteger("ioItem");
			this.ioItem=tag.getInteger("ioFluid");
			this.ioRedstone = tag.getInteger("ioRedstone");
			this.connected = tag.getBoolean("connected");
			this.markDirty();
		}
	}
	public int getChannel()
	{
		return this.id;
	}
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return false;
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

		IBlockState st = world.getBlockState(pos);
		world.notifyBlockUpdate(pos,st, st.withProperty(TesseraktBlock.CONNECTED, this.connected), 3);
		world.setBlockState(pos, st.withProperty(TesseraktBlock.CONNECTED, this.connected));
		world.notifyNeighborsOfStateChange(pos, blockType, true);
	}
	@Override
	public void onLoad()
	{
		if(this.world != null && !this.world.isRemote)
		{

			IBlockState st = world.getBlockState(pos);
			world.notifyBlockUpdate(pos,st, st.withProperty(TesseraktBlock.CONNECTED, this.connected), 3);
			world.setBlockState(pos, st.withProperty(TesseraktBlock.CONNECTED, this.connected));
			world.notifyNeighborsOfStateChange(pos, Registers.blockTesserakt, true);
		}
		if(this.world.isRemote)
		{
			Main.network.sendToServer(new PacketRequestTile(this.getPos()));
		}
	}
}
