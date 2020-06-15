package com.diamssword.tesserakt.dimension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;

public class NoRenderHandler extends IRenderHandler
{
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc)
	{
		// No render = Empty method.
	}
}
