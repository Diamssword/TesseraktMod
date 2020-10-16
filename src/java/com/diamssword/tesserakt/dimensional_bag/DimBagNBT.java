package com.diamssword.tesserakt.dimensional_bag;

import java.util.UUID;

import com.diamssword.tesserakt.storage.TesseraktData;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DimBagNBT {

	public BlockPos bagPos;
	public BlockPos dimPos;
	public int bagDim=0;
	public UUID id;
	public int size=4;
	public int upgrades=0;

	public DimBagNBT()
	{
	}
	public NBTTagCompound toNBT(NBTTagCompound tag)
	{
		NBTTagCompound tag1=new NBTTagCompound();
		if(bagPos != null)
		tag1.setLong("bagPos", bagPos.toLong());
		if(dimPos != null)
		tag1.setLong("dimPos", dimPos.toLong());
		tag1.setInteger("bagDim", this.bagDim);
		tag1.setInteger("bagSize", this.size);
		tag1.setInteger("upgrades", this.upgrades);
		if(id != null)
		tag.setTag(id.toString(), tag1);
		return tag;
	}
	public NBTTagCompound toNBTClient()
	{
		NBTTagCompound tag1=new NBTTagCompound();
		if(bagPos != null)
		tag1.setLong("bagPos", bagPos.toLong());
		if(dimPos != null)
		tag1.setLong("dimPos", dimPos.toLong());
		tag1.setInteger("bagDim", this.bagDim);
		tag1.setInteger("bagSize", this.size);
		tag1.setInteger("upgrades", this.upgrades);
		tag1.setString("id", this.id.toString());
		return tag1;
	}
	public void fromNBT(NBTTagCompound tag,UUID owner)
	{
		NBTTagCompound tag1=(NBTTagCompound) tag.getTag(owner.toString());
		if(tag1 == null || tag1.hasNoTags())
		{
			return;
		}
		this.id = owner;
		if(tag1.hasKey("bagPos"))
		this.bagPos=BlockPos.fromLong(tag1.getLong("bagPos"));
		if(tag1.hasKey("dimPos"))
		this.dimPos=BlockPos.fromLong(tag1.getLong("dimPos"));
		this.bagDim=tag1.getInteger("bagDim");
		this.size = tag1.getInteger("bagSize");
		this.upgrades = tag1.getInteger("upgrades");
	}
	public void fromNBTClient(NBTTagCompound tag)
	{
		this.id = UUID.fromString(tag.getString("id"));
		if(tag.hasKey("bagPos"))
		this.bagPos=BlockPos.fromLong(tag.getLong("bagPos"));
		if(tag.hasKey("dimPos"))
		this.dimPos=BlockPos.fromLong(tag.getLong("dimPos"));
		this.bagDim=tag.getInteger("bagDim");
		this.size = tag.getInteger("bagSize");
		this.upgrades = tag.getInteger("upgrades");
	}
	
	public static DimBagNBT get(World world, UUID playerID)
	{
		NBTTagCompound tag=(NBTTagCompound) TesseraktData.getDimBag(world);
		if(tag == null)
		{
			tag = new NBTTagCompound();
		}
		DimBagNBT bag=new DimBagNBT();
		bag.fromNBT(tag, playerID);
		return bag;
	}
	public static void save(World world, DimBagNBT bag)
	{
		NBTTagCompound tag=(NBTTagCompound) TesseraktData.getDimBag(world);
		if(tag == null)
		{
			tag = new NBTTagCompound();
		}
		bag.toNBT(tag);
		TesseraktData.setBags(world, tag);
	}
	public static class Room
	{
		public Room(int id)
		{
			this.id=id;
		}
		int id;
		boolean max=false;
	}
}
