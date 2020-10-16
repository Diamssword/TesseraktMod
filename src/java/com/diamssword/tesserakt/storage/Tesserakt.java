package com.diamssword.tesserakt.storage;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Tesserakt {

	public static Map<Integer,Tesserakt> loadeds = new HashMap<Integer,Tesserakt>();
	public int id;
	public TEnergyStorage energy = new TEnergyStorage(1000000,this);
	public TItemStorage item = new TItemStorage(1,this);
	public TFluidStorage fluid = new TFluidStorage(1000,this);
	public int redstonePowered=0;
	public final World world;
	
	public Tesserakt(int id,World w)
	{
		this.id=id;
		this.world = w;
	}
	public NBTTagCompound toNBT(NBTTagCompound tag)
	{
		NBTTagCompound tag1=new NBTTagCompound();
		if(energy != null)
		tag1.setInteger("energy", energy.getEnergyStored());
		if(item != null)
		tag1.setTag("item", item.serializeNBT());
		if(fluid != null)
			tag1.setTag("fluid", fluid.writeToNBT(new NBTTagCompound()));
		tag1.setInteger("redstone", this.redstonePowered);
		tag.setTag(this.id+"", tag1);
		return tag;
	}
	
	public void fromNBT(NBTTagCompound tag,int id)
	{
		NBTTagCompound tag1=(NBTTagCompound) tag.getTag(id+"");
		if(tag1 == null || tag1.hasNoTags())
		{
			return;
		}
		this.id= id;
		if(tag1.hasKey("energy"))
		this.energy.receiveEnergy(tag1.getInteger("energy"), false);
		if(tag1.hasKey("item"))
			this.item.deserializeNBT((NBTTagCompound) tag1.getTag("item"));
		if(tag1.hasKey("fluid"))
			this.fluid.readFromNBT((NBTTagCompound) tag1.getTag("fluid"));
		this.redstonePowered=tag1.getInteger("redstone");
	}
	public static Tesserakt get(World world, int id)
	{
		if(loadeds.containsKey(id))
		{
			Tesserakt t = loadeds.get(id);
			if(t != null)
				return t;
		}
		NBTTagCompound tag=(NBTTagCompound) TesseraktData.getNBT(world).getTag("tesserakts");
		if(tag == null)
		{
			tag = new NBTTagCompound();
		}
		Tesserakt bag=new Tesserakt(id,world);
		bag.fromNBT(tag, id);
		loadeds.put(id, bag);
		return bag;
	}
	public static void save(World world, Tesserakt tesseract)
	{
		NBTTagCompound tag=(NBTTagCompound) TesseraktData.getNBT(world).getTag("tesserakts");
		if(tag == null)
		{
			tag = new NBTTagCompound();
		}
		tesseract.toNBT(tag);
		TesseraktData.getNBT(world).setTag("tesserakts", tag);
		TesseraktData.save(world);
	}
}
