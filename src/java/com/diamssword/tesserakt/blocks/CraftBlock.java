package com.diamssword.tesserakt.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.Registers;
import com.diamssword.tesserakt.dimensional_bag.DimBagRefs;
import com.diamssword.tesserakt.utils.IIngredientDisplay;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CraftBlock extends Block implements IIngredientDisplay{
	public static PropertyInteger COVERED = PropertyInteger.create("covered", 0,6);
	public CraftBlock(String name) {
		super(Material.IRON);
		this.setRegistryName(name);
		this.setHardness(5);
		this.setResistance(5000f);
		this.setHarvestLevel("pickaxe", 1);
		this.setUnlocalizedName(Main.MODID+"."+name);
		this.setCreativeTab(Registers.tab);

	}
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, COVERED);
	}
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(stack.getItem() == Item.getItemFromBlock(this))
		{
			tooltip.add("Filled by clicking the block with a Resonant Ender Bucket or piping it in directly");
		}
	}
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		super.getDrops(drops, world, pos, state, fortune);
		int val=state.getValue(COVERED);
		if(val>0)
			drops.add(new ItemStack(Registers.blockEnderWool, val));

	}
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		if(stack.getItem() == Item.getItemFromBlock(Registers.blockEnderWool))
		{
			if( state.getValue(COVERED) <6)
			{
				worldIn.setBlockState(pos, state.withProperty(COVERED, state.getValue(COVERED)+1));
				stack.shrink(1);
				return true;
			}
		}
		else if(stack.getItem() == Items.NETHER_STAR)
		{
			if(!worldIn.isRemote)
			{
				boolean flag = true;
				for(int i =-1;i<2;i++)
				{
					for(int j =-1;j<2;j++)
					{
						if(!(i == 0 && j== 0))
						{
							BlockPos pos1 = pos.add(i, 0, j);
							if(worldIn.getBlockState(pos1).getBlock() != Blocks.OBSIDIAN)
							{
								flag = false;
								break;
							}
						}
					}
				}
				if(flag)
				{
					for(int i =-1;i<2;i++)
					{
						for(int j =-1;j<2;j++)
						{
							BlockPos pos1 = pos.add(i, 0, j);
							worldIn.createExplosion(playerIn, pos1.getX(),pos1.getY(),pos1.getZ(), 0.2f, true);
							worldIn.setBlockToAir(pos1);
							stack.shrink(1);
						}
					}
					EntityItem item =new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(DimBagRefs.BagjerumItem));
					item.setNoGravity(true);
					item.setEntityInvulnerable(true);
					item.setGlowing(true);
					item.motionY=-1;
					worldIn.spawnEntity(item);
				}
				else
				{
					playerIn.sendMessage(new TextComponentTranslation(Main.MODID+".bag_craft.error"));
				}
			}
		}
		return false;
	}
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	public boolean isTopSolid(IBlockState state)
	{
		return false;
	}
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}

	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return 4+blockState.getValue(COVERED);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(COVERED, meta);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(COVERED);
	}
	private ItemStack obsi =new ItemStack(Blocks.OBSIDIAN);
	private ItemStack star =new ItemStack(Items.NETHER_STAR);
	private ItemStack[][] ingrs = new ItemStack[][] {{obsi,obsi,obsi},{obsi,star,obsi},{obsi,obsi,obsi}};
	@Override
	public ItemStack[][] toDisplay(IBlockState state) {
		int i = state.getValue(COVERED);
		if(i<6)
			return new ItemStack[][] {{new ItemStack(Registers.blockEnderWool,6-state.getValue(COVERED))}};
			else
			{
				return ingrs;
			}
	}
}
