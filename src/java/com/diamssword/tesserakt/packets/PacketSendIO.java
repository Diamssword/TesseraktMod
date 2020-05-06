package com.diamssword.tesserakt.packets;

import com.diamssword.tesserakt.storage.TesseraktData;
import com.diamssword.tesserakt.tileentity.TesseraktTile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendIO implements IMessage {

	private BlockPos pos;
	private int type;
	private int mode;
	public PacketSendIO() {}
	public PacketSendIO(BlockPos pos,int type,int mode ) {
		this.pos = pos;
		this.type =type;
		this.mode = mode;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		NBTTagCompound test = ByteBufUtils.readTag(buf);
		this.pos = BlockPos.fromLong(test.getLong("pos"));
		this.type = test.getInteger("type");
		this.mode=test.getInteger("mode");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound test= new NBTTagCompound();
		test.setLong("pos", pos.toLong());
		test.setInteger("type", type);
		test.setInteger("mode", mode);
		ByteBufUtils.writeTag(buf, test);
	}

	public static class HandlerSendIO implements IMessageHandler<PacketSendIO, IMessage> {

		@Override
		public IMessage onMessage(PacketSendIO message, MessageContext ctx) {
			TileEntity te=ctx.getServerHandler().player.world.getTileEntity(message.pos);
			if(te != null && te instanceof TesseraktTile)
			{
				TesseraktTile tile=(TesseraktTile) te;
				tile.setIO(message.type, message.mode);

			}
			return null;

		}
	}
}
;