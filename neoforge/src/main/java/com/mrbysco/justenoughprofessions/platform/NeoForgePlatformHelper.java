package com.mrbysco.justenoughprofessions.platform;

import com.mrbysco.justenoughprofessions.NeoForgeProfessionPlugin;
import com.mrbysco.justenoughprofessions.jei.ProfessionWrapper;
import com.mrbysco.justenoughprofessions.platform.services.IPlatformHelper;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public Identifier getEntityKey(EntityType<?> entityType) {
		return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
	}

	@Override
	public IRecipeType<ProfessionWrapper> getProfessionType() {
		return NeoForgeProfessionPlugin.PROFESSION_TYPE;
	}
}
