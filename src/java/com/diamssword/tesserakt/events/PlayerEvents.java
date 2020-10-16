package com.diamssword.tesserakt.events;

import com.diamssword.tesserakt.Registers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
@EventBusSubscriber
public class PlayerEvents {

	@SubscribeEvent
	public static void onServerConnect(LivingEvent.LivingJumpEvent e)
	{
		if(!e.getEntity().world.isRemote)
		{
			BlockPos pos = e.getEntity().getPosition().down();
			IBlockState st=	e.getEntity().world.getBlockState(pos);
			if(st.getBlock() == Registers.blockEnderWool)
			{

				EnumFacing f = e.getEntityLiving().getHorizontalFacing();
				if(e.getEntityLiving().rotationPitch>60 && e.getEntityLiving().rotationPitch<91)
					f=EnumFacing.DOWN;
				if(e.getEntityLiving().rotationPitch<-60 && e.getEntityLiving().rotationPitch>-91)
					f=EnumFacing.UP;
				for(int i=1;i<33;i++)
				{
					if(e.getEntity().world.getBlockState(pos.offset(f, i)).getBlock()== Registers.blockEnderWool)
					{
						BlockPos p1= pos.offset(f, i);
						for(int j=0;j<10;j++)
						{
							((WorldServer)e.getEntity().world).spawnParticle(EnumParticleTypes.PORTAL, p1.getX()+Math.random(), p1.getY()+1+Math.random(), p1.getZ()+Math.random(), 1, 0, 0, 0, 0,new int[0]);
						}
						e.getEntityLiving().setPositionAndUpdate(p1.getX()+0.5, p1.getY()+1.1, p1.getZ()+0.5);
						((WorldServer)e.getEntity().world).playSound(null, p1.getX(),p1.getY()+1.5,p1.getZ(), new SoundEvent(new ResourceLocation("minecraft:entity.endermen.teleport")), SoundCategory.BLOCKS, 1, (float) (Math.random()*2));
						for(int j=0;j<10;j++)
						{
							((WorldServer)e.getEntity().world).spawnParticle(EnumParticleTypes.PORTAL, p1.getX()+Math.random(), p1.getY()+1+Math.random(), p1.getZ()+Math.random(), 1, 0, 0, 0, 0,new int[0]);
						}
						break;
					}
				}
			}

		}

	}
}
