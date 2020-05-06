package com.diamssword.tesserakt.storage;

import java.util.HashMap;
import java.util.Map;

import com.diamssword.tesserakt.Main;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
@EventBusSubscriber
public class TesseraktData extends WorldSavedData {
	private static final String DATA_NAME = Main.MODID;
	private NBTTagCompound tag= new NBTTagCompound();
	public static Map<Integer,String> names;
	public static TesseraktData get(World world) {
		MapStorage storage = world.getMapStorage();
		TesseraktData instance = (TesseraktData) storage.getOrLoadData(TesseraktData.class, DATA_NAME);

		if (instance == null) {
			instance = new TesseraktData();
			storage.setData(DATA_NAME, instance);
		}
		return instance;
	}	
	public static void save(World world) {
		MapStorage storage = world.getMapStorage();
		TesseraktData instance = (TesseraktData) storage.getOrLoadData(TesseraktData.class, DATA_NAME);
		if(instance != null)
			instance.markDirty();
	}
	public static NBTTagCompound  getNBT(World world) {
		return get(world).tag;
	}	
	public TesseraktData() {
		super(DATA_NAME);
	}
	public TesseraktData(String s) {
		super(s);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if(nbt != null)
			tag= nbt;
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.merge(tag);
		return compound;
	}
	public static Map<Integer,String> getNames(World w)
	{
		if(names == null)
			namesFromNBT(getNBT(w));
		return names;
	}
	public static void namesFromNBT(NBTTagCompound nbt)
	{
		Map<Integer,String> map = new HashMap<Integer,String>();
		NBTTagList list=(NBTTagList) nbt.getTag("names");
		if(list != null)
		{
			for(int i= 0;i< list.tagCount();i++)
			{
				NBTTagCompound tag = (NBTTagCompound) list.get(i);
				map.put(tag.getInteger("channel"),tag.getString("name"));
			}
		}
		names=map;
	}
	public static void addName(World w, int channel, String name, boolean remove)
	{
		if(names == null)
			namesFromNBT(getNBT(w));
		if(remove)
			names.remove(channel);
		else
			names.put(channel, name);
		NBTTagList list= new NBTTagList();

		for(Integer id : names.keySet())
		{
			NBTTagCompound tag =new  NBTTagCompound();
			tag.setInteger("channel", id);
			tag.setString("name", names.get(id));
			list.appendTag(tag);
		}
		getNBT(w).setTag("names", list);
		save(w);
	}

}