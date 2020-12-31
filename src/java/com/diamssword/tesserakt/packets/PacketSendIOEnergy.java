package com.diamssword.tesserakt.packets;

import com.diamssword.tesserakt.utils.IOTileInterface;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendIOEnergy implements IMessage {

	private BlockPos pos;
	private EnumFacing face;
	private int mode;
	public PacketSendIOEnergy() {}
	public PacketSendIOEnergy(BlockPos pos,int mode,EnumFacing face ) {
		this.pos = pos;
		this.mode = mode;
		this.face=face;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound test = ByteBufUtils.readTag(buf);
		this.pos = BlockPos.fromLong(test.getLong("pos"));
		this.mode = test.getInteger("mode");
		this.face=EnumFacing.VALUES[test.getInteger("face")];
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound test= new NBTTagCompound();
		test.setLong("pos", pos.toLong());
		test.setInteger("face", face.getIndex());
		test.setInteger("mode", mode);
		ByteBufUtils.writeTag(buf, test);
	}

	public static class HandlerSendIOEnergy implements IMessageHandler<PacketSendIOEnergy, IMessage> {

		@Override
		public IMessage onMessage(PacketSendIOEnergy message, MessageContext ctx) {
			TileEntity te=ctx.getServerHandler().player.world.getTileEntity(message.pos);
			if(te != null && te instanceof IOTileInterface)
			{
				IOTileInterface tile=(IOTileInterface) te;
				tile.setMode(message.face, message.mode);
			}
			return null;

		}
	}
}
;