package com.diamssword.tesserakt.packets;

import com.diamssword.tesserakt.storage.TesseraktData;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendNames implements IMessage {

	private NBTTagCompound tag;
	public PacketSendNames() {}
	public PacketSendNames(NBTTagCompound tag) {
		this.tag = tag;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class HandlerSendNames implements IMessageHandler<PacketSendNames, IMessage> {

		@Override
		public IMessage onMessage(PacketSendNames message, MessageContext ctx) {
			System.out.println(message.tag);
			if(message.tag.hasKey("private"))
				TesseraktData.namesFromNBT(message.tag,true);	
			else
			TesseraktData.namesFromNBT(message.tag,false);
			return null;

		}
	}
}
