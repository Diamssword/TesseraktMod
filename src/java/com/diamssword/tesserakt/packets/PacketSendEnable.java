package com.diamssword.tesserakt.packets;

import com.diamssword.tesserakt.tileentity.TesseraktTile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendEnable implements IMessage {

	private NBTTagCompound tag;
	public PacketSendEnable() {}
	public PacketSendEnable(int channel,boolean activate, BlockPos pos) {

		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong("pos", pos.toLong());
		tag.setInteger("channel", channel);
		tag.setBoolean("activate", activate);
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

	public static class HandlerSendEnable implements IMessageHandler<PacketSendEnable, IMessage> {

		@Override
		public IMessage onMessage(PacketSendEnable message, MessageContext ctx) {
			TileEntity te=ctx.getServerHandler().player.world.getTileEntity(BlockPos.fromLong(message.tag.getLong("pos")));
			if(te != null && te instanceof TesseraktTile)
			{
				TesseraktTile tile=(TesseraktTile) te;
				if(message.tag.getBoolean("activate"))
					tile.setChannelAndActivate(message.tag.getInteger("channel"));
				else
					tile.desactivate();

			}
			return null;

		}
	}
}
