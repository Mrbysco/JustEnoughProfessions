package com.mrbysco.justenoughprofessions;

import com.mrbysco.justenoughprofessions.compat.CompatibilityHelper;
import com.mrbysco.justenoughprofessions.jei.ProfessionCategory;
import com.mrbysco.justenoughprofessions.jei.ProfessionEntry;
import com.mrbysco.justenoughprofessions.jei.ProfessionWrapper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedList;
import java.util.List;

@JeiPlugin
public class FabricProfessionPlugin implements IModPlugin {
	private static final ResourceLocation UID = new ResourceLocation(Constants.MOD_ID, "jei_plugin");

	public static final RecipeType<ProfessionWrapper> PROFESSION_TYPE = RecipeType.create(Constants.MOD_ID, "professions", ProfessionWrapper.class);

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new ProfessionCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(Items.EMERALD), PROFESSION_TYPE);
		registration.addRecipeCatalyst(new ItemStack(Items.VILLAGER_SPAWN_EGG), PROFESSION_TYPE);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		List<ProfessionWrapper> entries = new LinkedList<>();
		for (VillagerProfession profession : Registry.VILLAGER_PROFESSION.stream().toList()) {
			if (profession == VillagerProfession.NONE) {
				continue;
			}
			List<ItemStack> stacks = new LinkedList<>();
			List<ResourceLocation> knownItems = new LinkedList<>();
			for (PoiType poiType : Registry.POINT_OF_INTEREST_TYPE.stream().toList()) {
				ResourceKey<PoiType> typeResourceKey = Registry.POINT_OF_INTEREST_TYPE.getResourceKey(poiType).orElse(null);
				if (typeResourceKey != null && profession.acquirableJobSite().test(Registry.POINT_OF_INTEREST_TYPE.getHolder(typeResourceKey).orElse(null))) {
					for (BlockState state : poiType.matchingStates()) {
						Block block = Registry.BLOCK.get(Registry.BLOCK.getKey(state.getBlock()));
						if (block != null) {
							ItemStack stack = CompatibilityHelper.compatibilityCheck(new ItemStack(block), Registry.VILLAGER_PROFESSION.getKey(profession));
							ResourceLocation location = Registry.ITEM.getKey(stack.getItem());
							if (!stack.isEmpty() && !knownItems.contains(location)) {
								stacks.add(stack);
								knownItems.add(location);
							}
						}
					}
				}
			}
			if (!stacks.isEmpty()) {
				entries.add(new ProfessionWrapper(new ProfessionEntry(profession, stacks)));
			}
		}
		registration.addRecipes(PROFESSION_TYPE, entries);
	}
}