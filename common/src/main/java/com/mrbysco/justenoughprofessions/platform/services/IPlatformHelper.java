package com.mrbysco.justenoughprofessions.platform.services;

import com.mrbysco.justenoughprofessions.jei.ProfessionWrapper;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public interface IPlatformHelper {

	/**
	 * Get the registry name of the entitytype
	 *
	 * @param entityType The entity type
	 * @return The registry name
	 */
	ResourceLocation getEntityKey(EntityType<?> entityType);

	/**
	 * Get the RecipeType for JEI
	 *
	 * @return The professionHolder RecipeType
	 */
	IRecipeType<ProfessionWrapper> getProfessionType();
}
