package com.diamssword.tesserakt.dimensional_bag;

import java.util.Random;

import com.diamssword.tesserakt.Configs;
import com.diamssword.tesserakt.Main;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class DimBagItem extends Item{

	public DimBagItem() {
		this.setUnlocalizedName(Main.MODID+".dimensional_bag");
		this.setRegistryName("dimensional_bag");
		this.setMaxStackSize(1);
	}
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		BlockPos pos= playerIn.getPosition();
		if(worldIn.provider.getDimension() != Configs.DimTPDimensionID)
		{
			if(playerIn.world.isAirBlock(pos))
			{
				if(!worldIn.isRemote && worldIn instanceof WorldServer)
					this.useBag(worldIn, playerIn, pos);
			}
			else
			{
				for(int x=pos.getX()-4;x<pos.getX()+4;x++)
				{
					for(int y=pos.getY()-4;y<pos.getY()+4;y++)
					{
						for(int z=pos.getZ()-4;z<pos.getZ()+4;z++)
						{
							BlockPos p1=new BlockPos(x,y,z);
							if(worldIn.isAirBlock(p1))
							{
								if(!worldIn.isRemote && worldIn instanceof WorldServer)
								{
									playerIn.getHeldItem(handIn).shrink(1);
									this.useBag(worldIn, playerIn, p1);
								}
								return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
							}
						}
					}
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
	}
	private Random rand = new Random();
	private void useBag(World worldIn, EntityPlayer playerIn, BlockPos pos)
	{
		DimBagNBT bag=DimBagNBT.get(worldIn, playerIn.getUniqueID());
		if(bag.bagPos != null)
		{
		World w =worldIn.getMinecraftServer().getWorld(bag.bagDim);
		if(w != null)
			w.setBlockToAir(bag.bagPos);
		}
		bag.bagPos = pos;
		bag.bagDim  =worldIn.provider.getDimension();
		bag.owner=playerIn.getUniqueID();
		DimBagNBT.save(worldIn, bag);
		worldIn.setBlockState(pos, DimBagRefs.BagjerumBlock.getDefaultState().withProperty(DimBagBlock.FACING, EnumFacing.getFront(2+((int)rand.nextInt(4)))));
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile != null && tile instanceof DimBagTile)
		{
			((DimBagTile) tile).setOwner(playerIn.getUniqueID());
		}
		DimBagLogic.TeleportPlayerToRoom(playerIn, playerIn.getUniqueID());
	}
}