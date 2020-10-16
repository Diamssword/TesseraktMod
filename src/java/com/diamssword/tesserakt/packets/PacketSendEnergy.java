package com.diamssword.tesserakt.packets;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.tileentity.ExponentialBatteryTile;
import com.diamssword.tesserakt.utils.BatteryEnergyStorage;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendEnergy implements IMessage {

	private NBTTagCompound data ;
	public PacketSendEnergy() {}
	public PacketSendEnergy(BatteryEnergyStorage energy,BlockPos pos) {
		this.data = new NBTTagCompound();
		this.data.setInteger("max", energy.getMaxEnergyStored());
		this.data.setInteger("stored", energy.getEnergyStored());
		this.data.setInteger("alt", energy.getAdditionalEnergy());
		this.data.setLong("pos", pos.toLong());

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		data = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
	}

	public static class HandlerSendEnergy implements IMessageHandler<PacketSendEnergy, IMessage> {

		@Override
		public IMessage onMessage(PacketSendEnergy message, MessageContext ctx) {
			BlockPos pos=BlockPos.fromLong(message.data.getLong("pos"));
			TileEntity te= Main.proxy.getClientWorld().getTileEntity(pos);
			if(te instanceof ExponentialBatteryTile)
			{
				((ExponentialBatteryTile) te).setEnergy(new BatteryEnergyStorage(message.data.getInteger("max"), message.data.getInteger("stored"), message.data.getInteger("alt"), null, null));
			}
			return null;

		}
	}
}
