package com.mrbysco.justenoughprofessions.platform;

import com.mrbysco.justenoughprofessions.FabricProfessionPlugin;
import com.mrbysco.justenoughprofessions.jei.ProfessionWrapper;
import com.mrbysco.justenoughprofessions.platform.services.IPlatformHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public ResourceLocation getEntityKey(EntityType entityType) {
		return Registry.ENTITY_TYPE.getKey(entityType);
	}

	@Override
	public ResourceLocation getProfessionKey(VillagerProfession villagerProfession) {
		return Registry.VILLAGER_PROFESSION.getKey(villagerProfession);
	}

	@Override
	public RecipeType<ProfessionWrapper> getProfessionType() {
		return FabricProfessionPlugin.PROFESSION_TYPE;
	}
}
