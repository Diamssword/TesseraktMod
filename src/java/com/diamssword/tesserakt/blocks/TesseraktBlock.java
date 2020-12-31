package com.diamssword.tesserakt.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Configs;
import com.diamssword.tesserakt.Configs.LockItem;
import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.Registers;
import com.diamssword.tesserakt.WrenchsCompat;
import com.diamssword.tesserakt.storage.TesseraktData;
import com.diamssword.tesserakt.tileentity.TesseraktTile;
import com.diamssword.tesserakt.utils.IIngredientDisplay;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TesseraktBlock extends Block  implements IIngredientDisplay{
	public static PropertyBool CONNECTED = PropertyBool.create("connected");
	public static PropertyBool PRIVATE = PropertyBool.create("private");
	public TesseraktBlock() {
		super(Material.IRON);
		this.setRegistryName("tesserakt");	
		this.setHardness(5);
		this.setResistance(5000f);
		this.setHarvestLevel("pickaxe", 1);
		this.setUnlocalizedName(Main.MODID+".tesserakt");
		this.setCreativeTab(Registers.tab);

	}
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(stack.getItem() == Item.getItemFromBlock(this))
		{
			if(stack.hasTagCompound())
			{
				int chan =stack.getTagCompound().getInteger("channel");
				tooltip.add("Frequency : "+ chan);
				if(stack.getItemDamage()!=1)
				{
					if(TesseraktData.names != null && !TesseraktData.names.isEmpty())
					{
						String s =TesseraktData.names.get(chan);
						if(s != null)
						{
							tooltip.add("Label : "+ s);
						}
					}
				}
			}
			tooltip.add("Transfer stuff across any distance and dimensions");
			if(stack.getItemDamage()!=1)
			{
				tooltip.add("" );
				tooltip.add("Privatize by clicking the block with :" );
				for(LockItem st : Configs.locksItems)
				{
					ItemStack displayer=null;
					if(st.meta ==-1)
						displayer =new ItemStack(st.item,1,0);
					else
						displayer=new ItemStack(st.item,1,st.meta);
					tooltip.add("-"+displayer.getDisplayName());
				}
			}
		}
	}
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(PRIVATE, meta !=0);
	}
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(WrenchsCompat.isWrench(playerIn.getHeldItem(hand)))
		{
			if(playerIn.isSneaking())
			{
				if(worldIn.setBlockToAir(pos))
				{
					NonNullList<ItemStack> ret = NonNullList.create();
					this.getDrops(ret,worldIn, pos, state, 0);
					for(ItemStack drop : ret)
						Block.spawnAsEntity(worldIn, pos, drop);
				}
				return true;
			}
		}
		if(!worldIn.isRemote)
		{
			worldIn.notifyBlockUpdate(pos,state, state, 3);
			if(!state.getValue(PRIVATE))
			{
				ItemStack st = playerIn.getHeldItem(hand);
				if(st != null)
				{
					for(LockItem i : Configs.locksItems)
					{
						if(st.getItem() == i.item)
						{
							if(i.meta ==-1 || i.meta == st.getItemDamage())
							{
								st.shrink(1);
								worldIn.setBlockState(pos, state.withProperty(PRIVATE, true));
								TileEntity te =worldIn.getTileEntity(pos);
								if(te != null && te instanceof TesseraktTile)
								{
									TesseraktTile tile = (TesseraktTile) te;
									if(worldIn.getBlockState(pos).getValue(PRIVATE))
									{
										tile.setOwner(playerIn.getUniqueID());
									}
								}
								return true;
							}
						}
					}
				}
			}

		}
		else 
		{
			if(state.getValue(PRIVATE))
			{
				TesseraktTile t =this.getTileEntity(worldIn, pos);
				UUID id =t.getOwner();
				if(id != null && id.equals(playerIn.getUniqueID()))
				{
					playerIn.openGui(Main.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
					return true;
				}
				playerIn.sendStatusMessage(new TextComponentTranslation(Main.MODID+".text.private.tesserakt"), true);
				return false;
			}
			playerIn.openGui(Main.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}
	public TesseraktTile getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TesseraktTile)world.getTileEntity(pos);
	}
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool)
	{
		super.harvestBlock(world, player, pos, state, te, tool);
		world.setBlockToAir(pos);
	}
	@Override
	public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		TesseraktTile te = world.getTileEntity(pos) instanceof TesseraktTile ? (TesseraktTile)world.getTileEntity(pos) : null;
		if (te != null)
			drops.add(te.toItemNBT());
		else
			super.getDrops(drops, world, pos, state, fortune);
	}
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		if (willHarvest) return true; //If it will harvest, delay deletion of the block until after getDrops
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TesseraktTile createTileEntity(World world, IBlockState state)
	{
		return new TesseraktTile();

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
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, CONNECTED,PRIVATE);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(PRIVATE, meta!=0);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(PRIVATE) ? 1:0;
	}
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileEntity te =worldIn.getTileEntity(pos);
		if(te != null && te instanceof TesseraktTile)
		{
			TesseraktTile tile = (TesseraktTile) te;
			tile.fromItemNBT(stack);
			if(worldIn.getBlockState(pos).getValue(PRIVATE))
			{
				tile.setOwner(placer.getUniqueID());
			}
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
		TesseraktTile te=this.getTileEntity(worldIn,pos);
		if(te != null)
		{
			int mode = te.getIoRedstone();
			if(mode ==3)
			{
				if(flag)
					te.setChannelAndActivate(te.getChannel());
				else
					te.desactivate();
				worldIn.notifyNeighborsOfStateChange(pos, Registers.blockTesserakt, true);
			}
			if(mode == 1)
			{
				int pow=this.getMostPower(pos, worldIn);
				te.updateRedstone(pow);
				worldIn.notifyNeighborsOfStateChange(pos, Registers.blockTesserakt, true);
			}

		}
	}

	private int getMostPower(BlockPos pos, World world)
	{
		int res=0;
		for(EnumFacing face:EnumFacing.VALUES)
		{
			int lv =world.getRedstonePower(pos.add(face.getFrontOffsetX(),face.getFrontOffsetY(),face.getFrontOffsetZ()), face);
			if(lv > res)
				res=lv;
		}
		for(EnumFacing face:EnumFacing.VALUES)
		{
			int lv =world.getRedstonePower(pos.up().add(face.getFrontOffsetX(),face.getFrontOffsetY(),face.getFrontOffsetZ()), face);
			if(lv > res)
				res=lv;
		}
		return res;
	}

	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TesseraktTile te =this.getTileEntity(blockAccess,pos);
		if(te != null && te.getIoRedstone() == 2)
			return te.getRedstone();
		return 0;
	}

	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return this.getWeakPower(blockState, blockAccess, pos, side);
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}
	@Override
	public ItemStack[][] toDisplay(IBlockState state) {
		if(!state.getValue(PRIVATE))
		{
			List<ItemStack[]> stacks= new ArrayList<ItemStack[]>();
			for(LockItem it:Configs.locksItems)
			{
				int meta= it.meta;
						if(meta ==-1)
							meta = 0;
				stacks.add(new ItemStack[] {new ItemStack(it.item,1,meta)});
			}
			return stacks.toArray(new ItemStack[0][0]);
		}
		return new ItemStack[0][0];
	}
}
