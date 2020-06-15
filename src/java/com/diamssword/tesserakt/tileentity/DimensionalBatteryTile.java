package com.diamssword.tesserakt.tileentity;

import java.util.List;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.packets.PacketRequestTile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DimensionalBatteryTile extends TileEntity implements ITickable{

	public static DamageSource dmg = new DamageSource("dimensional_squish"){
		 public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
		    {
		        EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
		        String s = "death.attack." + this.damageType;
		        String s1 = s + ".player";
		        return entitylivingbase != null && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName()}) : new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName()});
		    }
	};
	static {
		dmg.setDamageBypassesArmor().setDamageIsAbsolute();
	}
		public double size= 1;
		@Override
		public void update() {
			if(!this.world.isRemote)
			{
			List<Entity> entities=	world.getEntitiesInAABBexcluding((Entity)null, this.getBound(),null);
			for(Entity e : entities)
			{
				if(e instanceof EntityLiving)
				{
					EntityLiving liv =((EntityLiving) e);
					if(liv.attackable())
					{
						if(liv.attackEntityFrom(dmg, 1))
						{
							this.setSize(this.size+0.01d);
						}
					}
					
				}
				if(e instanceof EntityPlayer)
				{
					EntityPlayer liv =((EntityPlayer) e);
					if(liv.attackable())
					{
						if(liv.attackEntityFrom(dmg, 1))
						{
							this.setSize(this.size+0.01d);
						}
					}
					
				}
			}
			}
			
		}
		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound tag) {
			tag.setDouble("size", this.size);
			return super.writeToNBT(tag);
		}
		@Override
		public void readFromNBT(NBTTagCompound nbt) {

			this.size = nbt.getInteger("size");
			super.readFromNBT(nbt);
		}
		@Nullable
		public SPacketUpdateTileEntity getUpdatePacket()
		{
			NBTTagCompound nbtTag = new NBTTagCompound();
			this.writeToNBT(nbtTag);
			return new SPacketUpdateTileEntity(this.pos, 1, nbtTag);
		}

		public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
			readFromNBT(packet.getNbtCompound());

		}
		public AxisAlignedBB getBound()		
		{
			
			double size1 =Math.sqrt((size*size)/2);
			return  new AxisAlignedBB(pos.getX()+0.5f+(size1/2),pos.getY()+0.5f+(size1/2),pos.getZ()+0.5f+(size1/2),pos.getX()+0.5f-(size1/2),pos.getY()+0.5f-(size1/2),pos.getZ()+0.5f-(size1/2));
		}
		  public void onLoad()
		    {
		        if(this.world.isRemote)
		        {
		        	Main.network.sendToServer(new PacketRequestTile(this.pos));
		        }
		    }
		public void setSize(double d) {
			this.size = d;
			this.markDirty();

			IBlockState st =world.getBlockState(pos);
			world.notifyBlockUpdate(pos, st,st, 3);
			world.setBlockState(pos,st);
			world.notifyNeighborsOfStateChange(pos, blockType, true);
		}
		   @SideOnly(Side.CLIENT)
		    public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox()
		    {
		        net.minecraft.util.math.AxisAlignedBB bb = INFINITE_EXTENT_AABB;
		     /*   Block type = getBlockType();
		        BlockPos pos = getPos();
		        if (type == Blocks.ENCHANTING_TABLE)
		        {
		            bb = new net.minecraft.util.math.AxisAlignedBB(pos, pos.add(1, 1, 1));
		        }
		        else if (type == Blocks.CHEST || type == Blocks.TRAPPED_CHEST)
		        {
		            bb = new net.minecraft.util.math.AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 2, 2));
		        }
		        else if (type == Blocks.STRUCTURE_BLOCK)
		        {
		            bb = INFINITE_EXTENT_AABB;
		        }
		        else if (type != null && type != Blocks.BEACON)
		        {
		            net.minecraft.util.math.AxisAlignedBB cbb = null;
		            try
		            {
		                cbb = world.getBlockState(getPos()).getCollisionBoundingBox(world, pos).offset(pos);
		            }
		            catch (Exception e)
		            {
		                // We have to capture any exceptions that may occur here because BUKKIT servers like to send
		                // the tile entity data BEFORE the chunk data, you know, the OPPOSITE of what vanilla does!
		                // So we can not GARENTEE that the world state is the real state for the block...
		                // So, once again in the long line of US having to accommodate BUKKIT breaking things,
		                // here it is, assume that the TE is only 1 cubic block. Problem with this is that it may
		                // cause the TileEntity renderer to error further down the line! But alas, nothing we can do.
		                cbb = new net.minecraft.util.math.AxisAlignedBB(getPos().add(-1, 0, -1), getPos().add(1, 1, 1));
		            }
		            if (cbb != null) bb = cbb;
		        }*/
		        return bb;
		    }
		
}
