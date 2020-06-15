package com.diamssword.tesserakt;


import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Configs {


	public static String enderFluid;
	public static boolean FrameFill;
	public static int DimTPDimensionID;
	public static int DimBagspacing;
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
			
			Property DimBag_DimensionID = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
					"Dimensional_Bag_dimension_id", // Property name
					"7", // Default value
					"The id of the dimension used for the dimensional bag dimension"); // Comment
			DimTPDimensionID =DimBag_DimensionID.getInt();
			Property DimBag_spacing = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
					"Dimensional_Bag_spacing", // Property name
					"512", // Default value
					"The space beetween each room in the dimension"); // Comment
			DimBagspacing =DimBag_spacing.getInt();
			enderFluid = tesseraktFluid.getString();
		} catch (Exception e) {
		} finally {
			if (config.hasChanged()) config.save();
		}
	}
}
