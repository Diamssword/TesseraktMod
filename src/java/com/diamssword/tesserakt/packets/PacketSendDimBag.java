package com.diamssword.tesserakt.packets;

import com.diamssword.tesserakt.dimensional_bag.DimBagNBT;
import com.diamssword.tesserakt.tileentity.TesseraktTile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendDimBag implements IMessage {

	private DimBagNBT bag;
	public PacketSendDimBag() {}
	public PacketSendDimBag(DimBagNBT bag) {
		this.bag=bag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		bag=new DimBagNBT();
		bag.fromNBTClient(ByteBufUtils.readTag(buf));
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, bag.toNBTClient());
	}

	public static class HandlerSendDimBag implements IMessageHandler<PacketSendDimBag, IMessage> {

		@Override
		public IMessage onMessage(PacketSendDimBag message, MessageContext ctx) {
			//TODO
			return null;

		}
	}
}
;