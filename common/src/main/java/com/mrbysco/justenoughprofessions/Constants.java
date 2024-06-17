package com.mrbysco.justenoughprofessions;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {

	public static final String MOD_ID = "justenoughprofessions";
	public static final String MOD_NAME = "Just Enough Professions";
	public static final Logger LOG = LogManager.getLogger(MOD_NAME);

	public static ResourceLocation modLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}