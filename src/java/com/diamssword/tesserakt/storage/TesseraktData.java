package com.diamssword.tesserakt.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
	public static Map<UUID,Map<Integer,String>> privatenames;
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
			namesFromNBT(getNBT(w),false);
		return names;
	}
	public static Map<Integer,String> getNamesFor(World w,UUID owner)
	{
		if(privatenames == null)
			namesFromNBT(getNBT(w),true);
		Map<Integer,String> res= privatenames.get(owner);
		if(res == null)
			res= new HashMap<Integer,String>();
		return res;
	}
	public static void namesFromNBT(NBTTagCompound nbt,boolean privateNames)
	{
		if(privateNames)
		{
			Map<UUID,Map<Integer,String>> map = new HashMap<UUID,Map<Integer,String>>();
			NBTTagList list=(NBTTagList) nbt.getTag("namesPrivate");
			System.out.println(list);
			if(list != null)
			{
				for(int i= 0;i< list.tagCount();i++)
				{
					NBTTagCompound tag = (NBTTagCompound) list.get(i);
					String s =tag.getString("owner");
					try {
						UUID uuid=UUID.fromString(s);
						NBTTagList list1=(NBTTagList) tag.getTag("names");
						Map<Integer,String> namesOw=new HashMap<Integer,String>();
						if(list1 != null)
						{
							for(int j= 0;j< list1.tagCount();j++)
							{
								NBTTagCompound tag1 = (NBTTagCompound) list1.get(j);
								namesOw.put(tag1.getInteger("channel"),tag1.getString("name"));
							}
							map.put(uuid,namesOw);
						}
					}catch(IllegalArgumentException e)
					{}


				}
			}
			privatenames=map;
		}
		else
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
	}
	public static void addName(World w, int channel, String name, boolean remove)
	{
		if(names == null)
			namesFromNBT(getNBT(w),false);
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
	public static void addNamePrivate(World w,UUID owner, int channel, String name, boolean remove)
	{
		if(privatenames == null)
			namesFromNBT(getNBT(w),true);
		Map<Integer,String> list=privatenames.get(owner);
		if(list ==null)
		{
			privatenames.put(owner,list =new HashMap<Integer,String>());
		}

			if(remove)
				list.remove(channel);
			else
				list.put(channel, name);
			NBTTagList list1= new NBTTagList();
			for(UUID owners : privatenames.keySet())
			{
				NBTTagCompound tag =new  NBTTagCompound();
				tag.setString("owner", owners.toString());
				NBTTagList list2= new NBTTagList();
				for(Integer id : privatenames.get(owners).keySet())
				{
					NBTTagCompound tag1 =new  NBTTagCompound();
					tag1.setInteger("channel", id);
					tag1.setString("name", privatenames.get(owners).get(id));
					list2.appendTag(tag1);
				}
				tag.setTag("names", list2);
				list1.appendTag(tag);
			}
			
			getNBT(w).setTag("namesPrivate", list1);
			save(w);
		
	}
	public static NBTTagCompound getDimBag(World w)
	{
		NBTTagCompound tag= (NBTTagCompound) getNBT(w);

		if(!tag.hasKey("bags"))
			tag.setTag("bags", new NBTTagCompound());
		return (NBTTagCompound) tag.getTag("bags");
	}
	public static void setBags(World w,NBTTagCompound tag)
	{
		NBTTagCompound tag1= (NBTTagCompound) getNBT(w);
		tag1.setTag("bags", tag);
		save(w);
	}

}