package com.diamssword.tesserakt.utils;


import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class ModTeleporter extends Teleporter{




	public ModTeleporter(WorldServer worldServer)

	{
		super(worldServer);


        }



	public void placeInPortal(Entity entity, double x, double y, double z, float rotationYaw)

	{



        }



	public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float rotationYaw)

	{

		return true;

        }



	public boolean makePortal(Entity entity)

        {

		return true;

        }


}
