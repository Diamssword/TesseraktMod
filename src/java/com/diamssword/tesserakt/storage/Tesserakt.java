package com.diamssword.tesserakt.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Tesserakt {

	public static Map<Integer,Tesserakt> globals = new HashMap<Integer,Tesserakt>();
	public static Map<UUID,Map<Integer,Tesserakt>> privates = new HashMap<UUID,Map<Integer,Tesserakt>>();
	public int id;
	public TEnergyStorage energy = new TEnergyStorage(1000000,this);
	public TItemStorage item = new TItemStorage(1,this);
	public TFluidStorage fluid = new TFluidStorage(1000,this);
	public int redstonePowered=0;
	public final World world;
	public final UUID owner;
	public Tesserakt(int id,World w)
	{
		this.id=id;
		this.world = w;
		this.owner = null;
	}
	public Tesserakt(int id,World w,UUID owner)
	{
		this.id=id;
		this.world = w;
		this.owner=owner;
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
		if(globals.containsKey(id))
		{
			Tesserakt t = globals.get(id);
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
		globals.put(id, bag);
		return bag;
	}
	public static Tesserakt getPrivate(World world,UUID owner, int id)
	{
		Map<Integer,Tesserakt> map = privates.get(owner);
		if(map == null)
		{
			privates.put(owner, map = new HashMap<Integer,Tesserakt>());
		}
		if(map.containsKey(id))
		{
			Tesserakt t = map.get(id);
			if(t != null)
				return t;
		}
		NBTTagCompound tag=(NBTTagCompound) TesseraktData.getNBT(world).getTag("tesseraktsPrivates");
		if(tag == null)
		{
			tag = new NBTTagCompound();
		}
		NBTTagCompound tag1 =(NBTTagCompound) tag.getTag(owner.toString());
		if(tag1 == null)
		{
			tag1 = new NBTTagCompound();
		}
		Tesserakt bag=new Tesserakt(id,world,owner);
		bag.fromNBT(tag1, id);
		privates.get(owner).put(id, bag);
		return bag;
	}
	public static void save(World world, Tesserakt tesseract)
	{
		if(tesseract.owner != null)
		{
			NBTTagCompound tag=(NBTTagCompound) TesseraktData.getNBT(world).getTag("tesseraktsPrivates");
			if(tag == null)
			{
				tag = new NBTTagCompound();
			}
			NBTTagCompound tag1 =(NBTTagCompound) tag.getTag(tesseract.owner.toString());
			if(tag1 == null)
			{
				tag1 = new NBTTagCompound();
			}
			tesseract.toNBT(tag1);
			TesseraktData.getNBT(world).setTag("tesseraktsPrivates", tag);
		}
		else
		{
			NBTTagCompound tag=(NBTTagCompound) TesseraktData.getNBT(world).getTag("tesserakts");
			if(tag == null)
			{
				tag = new NBTTagCompound();
			}
			tesseract.toNBT(tag);
			TesseraktData.getNBT(world).setTag("tesserakts", tag);
		}
		TesseraktData.save(world);
	}
}
