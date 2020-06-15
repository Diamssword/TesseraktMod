package com.diamssword.tesserakt.dimensional_bag;

import com.diamssword.tesserakt.Registers;

public class DimBagRefs{



	public static DimBagWallBlock BagWall = new DimBagWallBlock();
	public static DimBagOutBlock BagjerumOutBlock = new DimBagOutBlock();
	public static DimBagBlock BagjerumBlock = new DimBagBlock();
	public static DimBagControllerBlock BagjerumControllerBlock = new DimBagControllerBlock();
	public static DimBagItem BagjerumItem= new DimBagItem();
	public static void init()
	{
	Registers.blocks.add(BagWall);
	Registers.blocks.add(BagjerumOutBlock);
	Registers.blocks.add(BagjerumBlock);
	Registers.blocks.add(BagjerumControllerBlock);
	Registers.items.add(BagjerumItem);
	
	}
}
