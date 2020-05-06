package com.diamssword.tesserakt;


import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Configs {


	public static String enderFluid;
	public static boolean FrameFill;
	public static void syncConfig(Configuration config) { // Gets called from preInit
		try {
			// Load config
			config.load();

			// Read props from config
			Property tesseraktFluid = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
					"Tesserakt_Fluid", // Property name
					"ender", // Default value
					"the name of the fluid used to fill the tesserakt"); // Comment
			enderFluid = tesseraktFluid.getString();
			Property frameFill = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
					"frame_fill", // Property name
					true, // Default value
					"set to false to disable the filling mekanism of the Empty Frame (totally remove tile entity and you will need to provide your own recipe)"); // Comment
			enderFluid = tesseraktFluid.getString();
			FrameFill=frameFill.getBoolean();
		} catch (Exception e) {
		} finally {
			if (config.hasChanged()) config.save();
		}
	}
}
