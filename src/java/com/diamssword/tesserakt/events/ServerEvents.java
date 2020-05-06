package com.diamssword.tesserakt.events;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.packets.PacketSendNames;
import com.diamssword.tesserakt.storage.TesseraktData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

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
}
