package com.mrbysco.justenoughprofessions;

import com.mrbysco.justenoughprofessions.compat.CompatibilityHelper;
import com.mrbysco.justenoughprofessions.jei.ProfessionCategory;
import com.mrbysco.justenoughprofessions.jei.ProfessionEntry;
import com.mrbysco.justenoughprofessions.jei.ProfessionWrapper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JeiPlugin
public class FabricProfessionPlugin implements IModPlugin {
	private static final Identifier UID = Constants.modLoc("jei_plugin");

	public static final IRecipeType<ProfessionWrapper> PROFESSION_TYPE = IRecipeType.create(Constants.MOD_ID, "professions", ProfessionWrapper.class);

	@Override
	public Identifier getPluginUid() {
		return UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new ProfessionCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addCraftingStation(PROFESSION_TYPE, new ItemStack(Items.EMERALD));
		registration.addCraftingStation(PROFESSION_TYPE, new ItemStack(Items.VILLAGER_SPAWN_EGG));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		List<ProfessionWrapper> entries = new LinkedList<>();
		for (Map.Entry<ResourceKey<VillagerProfession>, VillagerProfession> entry : BuiltInRegistries.VILLAGER_PROFESSION.entrySet()) {
			ResourceKey<VillagerProfession> resourceKey = entry.getKey();
			VillagerProfession profession = entry.getValue();
			if (entry.getKey().identifier().equals(VillagerProfession.NONE.identifier())) {
				continue;
			}
			List<ItemStack> stacks = new LinkedList<>();
			List<Identifier> knownItems = new LinkedList<>();
			List<PoiType> types = BuiltInRegistries.POINT_OF_INTEREST_TYPE.stream().toList();
			for (PoiType poiType : types) {
				Optional<ResourceKey<PoiType>> poiKey = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getResourceKey(poiType);
				if (poiKey.isPresent() && profession.acquirableJobSite().test(BuiltInRegistries.POINT_OF_INTEREST_TYPE.get(poiKey.get()).orElse(null))) {
					for (BlockState state : poiType.matchingStates()) {
						Block block = state.getBlock();
						if (block != null) {
							ItemStack stack = CompatibilityHelper.compatibilityCheck(new ItemStack(block), BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession));
							Identifier location = BuiltInRegistries.ITEM.getKey(stack.getItem());
							if (!stack.isEmpty() && !knownItems.contains(location)) {
								stacks.add(stack);
								knownItems.add(location);
							}
						}
					}
				}
			}
			if (!stacks.isEmpty()) {
				entries.add(new ProfessionWrapper(new ProfessionEntry(BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(resourceKey), stacks)));
			}
		}
		registration.addRecipes(PROFESSION_TYPE, entries);
	}
}