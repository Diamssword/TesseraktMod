package com.diamssword.tesserakt.packets;

import java.util.UUID;

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
	private UUID owner;
	public PacketSendNamesClient() {}
	public PacketSendNamesClient(int channel,String name) {
		this.channel=channel;
		this.name=name;
		this.delete=false;
	}
	public PacketSendNamesClient(int channel,String name,UUID owner) {
		this.channel=channel;
		this.name=name;
		this.delete=false;
		this.owner=owner;
	}
	public PacketSendNamesClient(int channel,String name,boolean delete) {
		this.channel=channel;
		this.name=name;
		this.delete = delete;
	}
	public PacketSendNamesClient(int channel,String name,boolean delete,UUID owner) {
		this.channel=channel;
		this.name=name;
		this.delete = delete;
		this.owner=owner;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag=ByteBufUtils.readTag(buf);
		this.channel=tag.getInteger("channel");
		this.delete=tag.getBoolean("delete");
		this.name=tag.getString("name");
		if(tag.hasKey("owner"))
			this.owner=UUID.fromString(tag.getString("owner"));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag=new NBTTagCompound();
		tag.setInteger("channel",channel);
		tag.setBoolean("delete", this.delete);
		tag.setString("name",this.name);
		if(this.owner != null)
			tag.setString("owner",this.owner.toString());
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class HandlerSendNamesClient implements IMessageHandler<PacketSendNamesClient, IMessage> {

		@Override
		public IMessage onMessage(PacketSendNamesClient message, MessageContext ctx) {
			if(message.owner != null)
			{
				TesseraktData.addNamePrivate(ctx.getServerHandler().player.world, message.owner, message.channel, message.name, message.delete);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setTag("namesPrivate",TesseraktData.getNBT(ctx.getServerHandler().player.world).getTag("namesPrivate"));
				tag.setBoolean("private", true);
				Main.network.sendToAll(new PacketSendNames(tag));
			}
			else
			{
			TesseraktData.addName(ctx.getServerHandler().player.world, message.channel, message.name, message.delete);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("names",TesseraktData.getNBT(ctx.getServerHandler().player.world).getTag("names"));
			Main.network.sendToAll(new PacketSendNames(tag));
			}
			return null;

		}
	}
}
