package com.diamssword.tesserakt.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.diamssword.tesserakt.Configs;
import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.Registers;
import com.diamssword.tesserakt.WrenchsCompat;
import com.diamssword.tesserakt.tileentity.FrameTile;
import com.diamssword.tesserakt.utils.IIngredientDisplay;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EmptyBlock extends Block implements IIngredientDisplay{
	public static PropertyInteger FILLED = PropertyInteger.create("filled", 0,3);
	public EmptyBlock(String name) {
		super(Material.IRON);
		this.setRegistryName(name);
		this.setHardness(5);
		this.setResistance(5000f);
		this.setHarvestLevel("pickaxe", 1);
		this.setUnlocalizedName(Main.MODID+"."+name);
		this.setCreativeTab(Registers.tab);

	}
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(stack.getItem() == Item.getItemFromBlock(this))
		{
			tooltip.add("Filled by clicking the block with a Resonant Ender Bucket or piping it in directly");
		}
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
	public FrameTile getTileEntity(IBlockAccess world, BlockPos pos) {
		return (FrameTile)world.getTileEntity(pos);
	}
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public FrameTile createTileEntity(World world, IBlockState state)
	{
		return new FrameTile();

	}
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FILLED);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{

		if(state.getBlock() == Registers.blockFrameEmpty)
		{
			ItemStack stack= playerIn.getHeldItem(hand);
			FrameTile tile =this.getTileEntity(worldIn, pos);
			if(stack != null && tile != null )
			{
				FluidStack fluid =FluidUtil.getFluidContained(stack);
				if(fluid != null && fluid.getFluid().getName().equals(Configs.enderFluid))
				{
					FluidActionResult res=FluidUtil.tryEmptyContainer(stack, tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing), 1000, playerIn, true);
					if(res.success)
					{
						worldIn.setBlockState(pos, Registers.blockFrameFull.getDefaultState());
						playerIn.setHeldItem(hand, res.getResult());
						return true;
					}
				}
			}
		}

		if(WrenchsCompat.isWrench(playerIn.getHeldItem(hand)))
		{
			if(playerIn.isSneaking())
			{
				if(worldIn.setBlockToAir(pos))
					state.getBlock().dropBlockAsItem(worldIn, pos, state, 0);
				return true;
			}
		}
		return false;
	}
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}

	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return blockState.getValue(FILLED);
	}
	private ItemStack bucket = null;
	@Override
	public ItemStack[][] toDisplay(IBlockState state) {
		if(state.getValue(FILLED)==0)
		{
			if(bucket == null)
			{
				bucket=FluidUtil.getFilledBucket(new FluidStack(FluidRegistry.getFluid(Configs.enderFluid), 1000));
			}
			return new ItemStack[][] {{bucket}};
		}
		return null;
	}

}
