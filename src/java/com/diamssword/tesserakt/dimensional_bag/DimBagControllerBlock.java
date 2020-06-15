package com.diamssword.tesserakt.dimensional_bag;

import java.util.UUID;

import com.diamssword.tesserakt.Main;
import com.diamssword.tesserakt.utils.TEBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class DimBagControllerBlock extends Block implements TEBlock<DimBagControllerTile> {

	public DimBagControllerBlock() {
		super(Material.BARRIER);
		this.setUnlocalizedName(Main.MODID+".dim_bag_controller");
		this.setRegistryName("dim_bag_controller");
		this.setBlockUnbreakable();
		this.setResistance(999999999);
	}
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			TileEntity tile = worldIn.getTileEntity(pos);
			if(tile != null && tile instanceof DimBagControllerTile)
			{
				UUID id =((DimBagControllerTile) tile).getOwner();
				if(id!= null)
				{
					DimBagNBT bag=DimBagNBT.get(worldIn, id);
					if(bag!= null)
					{
						ItemStack stack = playerIn.getHeldItem(hand);
						if(stack != null && stack.getItem()== Items.EMERALD && bag.size*2 <=64)
						{


							bag.size = bag.size*2;
							Chunk c = worldIn.getChunkFromBlockCoords(bag.dimPos.east(5));
							DimBagNBT.save(worldIn, bag);
							DimBagLogic.buildRoom(c, bag.size+3);
							DimBagLogic.fill(new BlockPos(bag.dimPos.getX()+2,128,bag.dimPos.getZ()-2),new BlockPos(bag.dimPos.getX()+2,131,bag.dimPos.getZ()+2),worldIn,Blocks.AIR.getDefaultState());
						}
					}

				}
			}
		}


		return true;
	}

	@Override
	public DimBagControllerTile createTileEntity(World world, IBlockState state) {
		return new DimBagControllerTile();
	}
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	@Override
	public Class<DimBagControllerTile> getTileEntityClass() {
		return DimBagControllerTile.class;
	}
}
