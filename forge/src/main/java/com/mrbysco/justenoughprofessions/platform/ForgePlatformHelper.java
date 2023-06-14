package com.mrbysco.justenoughprofessions.platform;

import com.mrbysco.justenoughprofessions.ForgeProfessionPlugin;
import com.mrbysco.justenoughprofessions.jei.ProfessionWrapper;
import com.mrbysco.justenoughprofessions.platform.services.IPlatformHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgePlatformHelper implements IPlatformHelper {

	@Override
	public ResourceLocation getEntityKey(EntityType entityType) {
		return ForgeRegistries.ENTITY_TYPES.getKey(entityType);
	}

	@Override
	public ResourceLocation getProfessionKey(VillagerProfession villagerProfession) {
		return ForgeRegistries.VILLAGER_PROFESSIONS.getKey(villagerProfession);
	}

	@Override
	public RecipeType<ProfessionWrapper> getProfessionType() {
		return ForgeProfessionPlugin.PROFESSION_TYPE;
	}
}
