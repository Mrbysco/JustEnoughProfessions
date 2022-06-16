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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedList;
import java.util.List;

@JeiPlugin
public class ForgeProfessionPlugin implements IModPlugin {
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
		for (VillagerProfession profession : ForgeRegistries.PROFESSIONS) {
			if (profession == VillagerProfession.NONE) {
				continue;
			}
			for (PoiType poiType : ForgeRegistries.POI_TYPES.getValues()) {
				if (profession.acquirableJobSite().test(ForgeRegistries.POI_TYPES.getHolder(poiType).orElse(null))) {
					List<ItemStack> stacks = new LinkedList<>();
					List<ResourceLocation> knownItems = new LinkedList<>();
					for (BlockState state : poiType.matchingStates()) {
						Block block = ForgeRegistries.BLOCKS.getValue(ForgeRegistries.BLOCKS.getKey(state.getBlock()));
						if (block != null) {
							ItemStack stack = CompatibilityHelper.compatibilityCheck(new ItemStack(block), ForgeRegistries.PROFESSIONS.getKey(profession));
							ResourceLocation location = ForgeRegistries.ITEMS.getKey(stack.getItem());
							if (!stack.isEmpty() && !knownItems.contains(location)) {
								stacks.add(stack);
								knownItems.add(location);
							}
						}
					}
					if (!stacks.isEmpty()) {
						entries.add(new ProfessionWrapper(new ProfessionEntry(profession, stacks)));
					}
				}
			}
		}
		registration.addRecipes(PROFESSION_TYPE, entries);
	}
}