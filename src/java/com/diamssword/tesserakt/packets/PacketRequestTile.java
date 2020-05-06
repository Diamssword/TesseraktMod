package com.diamssword.tesserakt.packets;

import com.diamssword.tesserakt.tileentity.TesseraktTile;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestTile implements IMessage {

	private long pos;
	public PacketRequestTile() {}
	public PacketRequestTile(BlockPos pos) {
this.pos = pos.toLong();

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = Long.parseLong(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, new Long(pos).toString());
	}

	public static class HandlerRequestTile implements IMessageHandler<PacketRequestTile, IMessage> {

		@Override
		public IMessage onMessage(PacketRequestTile message, MessageContext ctx) {
			BlockPos pos=BlockPos.fromLong(message.pos);
			IBlockState st=ctx.getServerHandler().player.world.getBlockState(pos);
			ctx.getServerHandler().player.world.notifyBlockUpdate(pos, st,st, 3);
			return null;

		}
	}
}
