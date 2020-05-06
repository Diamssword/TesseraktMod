package com.diamssword.tesserakt.packets;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.storage.TesseraktData;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendNamesClient implements IMessage {

	private int channel;
	private String name;
	private boolean delete;
	public PacketSendNamesClient() {}
	public PacketSendNamesClient(int channel,String name) {
		this.channel=channel;
		this.name=name;
		this.delete=false;
	}
	public PacketSendNamesClient(int channel,String name,boolean delete) {
		this.channel=channel;
		this.name=name;
		this.delete = delete;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		channel = ByteBufUtils.readVarInt(buf, 4);
		delete = ByteBufUtils.readVarInt(buf, 1) == 1? true : false;
		this.name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, channel, 4);
		ByteBufUtils.writeVarInt(buf, delete?1:0, 1);
		ByteBufUtils.writeUTF8String(buf, this.name);
	}

	public static class HandlerSendNamesClient implements IMessageHandler<PacketSendNamesClient, IMessage> {

		@Override
		public IMessage onMessage(PacketSendNamesClient message, MessageContext ctx) {
			TesseraktData.addName(ctx.getServerHandler().player.world, message.channel, message.name, message.delete);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("names",TesseraktData.getNBT(ctx.getServerHandler().player.world).getTag("names"));
			Main.network.sendToAll(new PacketSendNames(tag));
			return null;

		}
	}
}
