package com.diamssword.tesserakt.dimensional_bag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.diamssword.tesserakt.utils.ModTeleporter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class DimBagOutTile extends DimBagTile/* implements ITickable*/{
	/*private Random r = new Random();
	Map<EntityPlayer,Integer> list = new HashMap<EntityPlayer,Integer>();
	@Override
	public void update() {
		if(!world.isRemote &&  world.getTotalWorldTime() %10==0 && this.getOwner() != null)
		{
			List<EntityPlayer> ls = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(new BlockPos(this.pos.getX()+1,this.pos.getY()-4,this.pos.getZ()+1),this.pos));

			for(EntityPlayer e : ls)
			{
				if(!list.containsKey(e))
					list.put(e, 0);
			}
			for(EntityPlayer ent : list.keySet())
			{
				if(!ls.contains(ent) )
				{
					list.remove(ent);
				}
				else if(list.get(ent) <= 4)
				{
					list.put(ent, list.get(ent)+1);
					ent.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 11, 0, true, false));
				}
				else  if(list.get(ent) > 3){
					DimBagNBT bag=DimBagNBT.get(world, getOwner());
					if(bag != null && bag.bagPos != null)
					{
						list.remove(ent);
						if(bag.bagDim != ent.world.provider.getDimension())
						{
							ent.removePotionEffect(MobEffects.LEVITATION);
							ent.changeDimension(bag.bagDim, new ModTeleporter((WorldServer) ent.world));
						}
						ent.setPositionAndUpdate(bag.bagPos.getX()+0.5, bag.bagPos.getY()+0.5, bag.bagPos.getZ()+0.5);
						ent.motionX += r.nextDouble()*2;
						ent.motionY += r.nextDouble()*2;
						ent.motionZ += r.nextDouble()*2;
					}
				}
			}
		}
	}*/

}
