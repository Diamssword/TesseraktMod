package com.diamssword.tesserakt.render.tileentity;

import javax.vecmath.Vector3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.math.BlockPos;

public abstract class ISkyRender {

	public static TextureManager man = Minecraft.getMinecraft().getTextureManager();
	public double x;
	public double y;
	public double z;
	public float width;
	public void setProperties(double x,double y,double z,float width)
	{
		this.x=x;
		this.y=y;
		this.z=z;
		this.width=width;
	}
	public void setProperties(BlockPos pos,float width)
	{
		this.x=pos.getX()+0.5;
		this.y=pos.getY()-0.5;
		this.z=pos.getZ()+0.5;
		this.width=width;
	}
	
	public Vector3d getRelativePos(EntityPlayerSP player)
	{
		return new Vector3d(x-player.posX, y-player.posY, z-player.posZ);
	}
	public abstract void render(EntityPlayerSP player);

}
