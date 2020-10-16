package com.diamssword.tesserakt.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.vecmath.Vector3d;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.packets.PacketSendNames;
import com.diamssword.tesserakt.storage.TesseraktData;
import com.diamssword.tesserakt.utils.ModTeleporter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@EventBusSubscriber
public class ServerEvents {
	@SubscribeEvent
	public static void onServerConnect(PlayerLoggedInEvent e)
	{
		if(TesseraktData.getNBT(e.player.world).hasKey("names"))
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("names",TesseraktData.getNBT(e.player.world).getTag("names"));
			Main.network.sendTo(new PacketSendNames(tag), (EntityPlayerMP) e.player);
		}
	}
	private static Random r = new Random();
	public static List<EntityPacket> tooTP =  Collections.synchronizedList(new ArrayList<EntityPacket>());
	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent e)
	{
		List<EntityPacket> ls =new ArrayList<EntityPacket>();
		ls.addAll(tooTP);
		tooTP.clear();
		for(EntityPacket pack : ls)
		{
			try {
				if(pack.entity != null && !pack.entity.isDead && pack.entity.world != null)
				{
					World w1=e.world.getMinecraftServer().getWorld(pack.dim);
					BlockPos p1= new BlockPos(pack.pos.x,pack.pos.y,pack.pos.z);
					if(!w1.isAirBlock(p1));
					{
						if(w1.isAirBlock(p1.north()))
							p1=p1.north();
						else if(w1.isAirBlock(p1.south()))
							p1=p1.south();
						else if(w1.isAirBlock(p1.east()))
							p1=p1.east();
						else if(w1.isAirBlock(p1.west()))
							p1=p1.west();
						else
							p1=p1.north();
						p1 = p1.subtract(new Vec3i(pack.pos.x, pack.pos.y, pack.pos.z));
						pack.pos=new Vector3d(pack.pos.getX()+p1.getX(),pack.pos.getY()+p1.getY(),pack.pos.getZ()+p1.getZ());
					}
					pack.entity.setPositionAndUpdate(pack.pos.x,pack.pos.y, pack.pos.z);
					pack.entity.motionX += r.nextDouble();
					pack.entity.motionY += r.nextDouble()*0.5;
					pack.entity.motionZ += r.nextDouble();
					if(pack.dim != pack.entity.dimension)
					{
						if(pack.entity instanceof EntityPlayer)
						{
							EntityPlayerMP p =e.world.getMinecraftServer().getPlayerList().getPlayerByUUID(pack.entity.getUniqueID());
							WorldServer w = p.getServer().getWorld(pack.dim);
							if(w!=null && p != null)
							{
								p.setPortal(new BlockPos(pack.pos.x,pack.pos.y,pack.pos.z));
								e.world.getMinecraftServer().getPlayerList().transferPlayerToDimension(p, pack.dim,(ITeleporter) new ModTeleporter(w));
							}
						}
						else
						{
							pack.entity.changeDimension(pack.dim, (ITeleporter) new ModTeleporter(pack.entity.getServer().getWorld(pack.dim)));
						}
					}
				}
			}catch(NullPointerException e1)
			{
			}
		}
	}
	public static class EntityPacket
	{
		public final Entity entity;
		public Vector3d pos;
		public final int dim;
		public EntityPacket(Entity ent, Vector3d pos, int dim)
		{
			this.entity=ent;
			this.pos=pos;
			this.dim=dim;
		}
	}
}
