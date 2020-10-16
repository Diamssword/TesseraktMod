package com.diamssword.tesserakt.events;

import com.diamssword.tesserakt.render.tileentity.ISkyRender;
import com.diamssword.tesserakt.utils.IIngredientDisplay;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
@EventBusSubscriber(Side.CLIENT)
public class ClientEvents {
	@SubscribeEvent
	public static void onBlockFrame(DrawBlockHighlightEvent event)
	{
		if(event.getTarget().getBlockPos() != null && mc.world.getBlockState(event.getTarget().getBlockPos()).getBlock() instanceof IIngredientDisplay)
		{
			IBlockState st =  mc.world.getBlockState(event.getTarget().getBlockPos());
			items = ((IIngredientDisplay)st.getBlock()).toDisplay(st);
		}
		else
		{
			items = null;
		}
	}
	static Minecraft mc = Minecraft.getMinecraft();
	private static ItemStack[][] items;
	public static void RenderBlocksDisplay()
	{
		if(items != null && items.length>=1 && items[0] != null && items[0].length>=1)
		{
			ScaledResolution res=new ScaledResolution(mc);

			int x=(res.getScaledWidth()/3)-8;
			int y=(res.getScaledHeight()/3)-8;
			GlStateManager.pushMatrix();	
			GlStateManager.scale(1.5, 1.5, 1.5);
			int posX = x - ((items[0].length/2)*16);
			int posY = y - ((items.length/2)*16);
			for(int i=0;i<items.length;i++)
			{
				for(int j=0;j<items[i].length;j++)
				{
					mc.getRenderItem().renderItemIntoGUI(items[i][j],  posX+(j*16),posY+(i*16));
					mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, items[i][j],posX+(j*16),posY+(i*16), null);

				}
			}
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			// Fixes opaque cooldown overlay a bit lower
			// TODO: check if enabled blending still screws things up down the line.
			GlStateManager.disableBlend();
			GlStateManager.scale(1, 1, 1);
			GlStateManager.popMatrix();
		}
	}

	@SubscribeEvent
	public static void onPostRenderOverlay(RenderGameOverlayEvent.Post event)
	{
		if(event.getType().equals(ElementType.CROSSHAIRS))
		{	
			RenderBlocksDisplay();
		}
	}
	public static ISkyRender sky= null;
	@SubscribeEvent
	public static void RenderWorldEvent(RenderWorldLastEvent e)
	{
		if(sky != null)
		{
			GlStateManager.pushMatrix();
			sky.render(Minecraft.getMinecraft().player);
			GlStateManager.popMatrix();
		}
	}

}
