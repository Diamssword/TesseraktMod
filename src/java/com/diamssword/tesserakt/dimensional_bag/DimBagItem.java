package com.diamssword.tesserakt.dimensional_bag;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Configs;
import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.Registers;

import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DimBagItem extends Item{

	public DimBagItem() {
		this.setUnlocalizedName(Main.MODID+".dimensional_bag");
		this.setRegistryName("dimensional_bag");
		this.setMaxStackSize(1);
		this.setCreativeTab(Registers.tab);
	}
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(stack.getItem() == this)
		{
			tooltip.add("A Traveler's best friend!");
			tooltip.add("A Tesserakt Frame filled with wool, surounded by obsidian and powered by a nether star");
			if(stack.hasTagCompound())
			{
				if(stack.getTagCompound().hasKey("id"))
					tooltip.add("ID:"+stack.getTagCompound().getString("id"));
			}
		}
	}
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		BlockPos pos= playerIn.getPosition();
		if(worldIn.provider.getDimension() != Configs.DimTPDimensionID)
		{
			if(playerIn.world.isAirBlock(pos))
			{
				if(!worldIn.isRemote && worldIn instanceof WorldServer)
				{
					ItemStack stack=playerIn.getHeldItem(handIn);
					UUID id = null;
					if(stack.hasTagCompound())
					{
						if(stack.getTagCompound().hasKey("id"))
						{
							try {
								id= UUID.fromString(stack.getTagCompound().getString("id"));
							}catch(IllegalArgumentException e)
							{
								id = UUID.randomUUID();
							}
						}

					}
					if(id == null)
					{
						id = UUID.randomUUID();
					}
					playerIn.getHeldItem(handIn).shrink(1);
					this.useBag(worldIn, playerIn,id, pos);	
				}
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
									ItemStack stack=playerIn.getHeldItem(handIn);
									UUID id = null;
									if(stack.hasTagCompound())
									{
										if(stack.getTagCompound().hasKey("id"))
										{
											try {
												id= UUID.fromString(stack.getTagCompound().getString("id"));
											}catch(IllegalArgumentException e)
											{
												id = UUID.randomUUID();
											}
										}

									}
									if(id == null)
									{
										id = UUID.randomUUID();
									}
									playerIn.getHeldItem(handIn).shrink(1);
									this.useBag(worldIn, playerIn,id, p1);
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
	private void useBag(World worldIn, EntityPlayer playerIn,UUID id, BlockPos pos)
	{
		DimBagNBT bag=DimBagNBT.get(worldIn, id);
		if(bag.bagPos != null)
		{
			World w =worldIn.getMinecraftServer().getWorld(bag.bagDim);
			if(w != null)
				w.setBlockToAir(bag.bagPos);
		}
		bag.bagPos = pos;
		bag.bagDim  =worldIn.provider.getDimension();
		bag.id=id;
		DimBagNBT.save(worldIn, bag);
		worldIn.setBlockState(pos, DimBagRefs.BagjerumBlock.getDefaultState().withProperty(DimBagBlock.FACING, EnumFacing.getFront(2+((int)rand.nextInt(4)))));
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile != null && tile instanceof DimBagTile)
		{
			((DimBagTile) tile).setOwner(id);
		}
		DimBagLogic.TeleportPlayerToRoom(playerIn, id);
	}
}
