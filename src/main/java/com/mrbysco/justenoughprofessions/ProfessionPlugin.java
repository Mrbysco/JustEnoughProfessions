package com.mrbysco.justenoughprofessions;

import com.mrbysco.justenoughprofessions.compat.CompatibilityHelper;
import com.mrbysco.justenoughprofessions.profession.trade.TradeCategory;
import com.mrbysco.justenoughprofessions.profession.trade.TradeWrapper;
import com.mrbysco.justenoughprofessions.profession.workstation.WorkstationCategory;
import com.mrbysco.justenoughprofessions.profession.workstation.WorkstationWrapper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.util.ErrorUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class ProfessionPlugin implements IModPlugin {
	private static final ResourceLocation UID = new ResourceLocation(JustEnoughProfessions.MOD_ID, "jei_plugin");

	public static final ResourceLocation WORKSTATION = new ResourceLocation(JustEnoughProfessions.MOD_ID, "workstation");
	public static final ResourceLocation TRADE = new ResourceLocation(JustEnoughProfessions.MOD_ID, "trade");
	public static final RecipeType<WorkstationWrapper> WORKSTATION_TYPE = RecipeType.create(JustEnoughProfessions.MOD_ID, "workstation", WorkstationWrapper.class);
	public static final RecipeType<TradeWrapper> TRADE_TYPE = RecipeType.create(JustEnoughProfessions.MOD_ID, "trade", TradeWrapper.class);

	@Nullable
	private IRecipeCategory<WorkstationWrapper> workstationCategory;
	private IRecipeCategory<TradeWrapper> tradeCategory;

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registration.addRecipeCategories(
				workstationCategory = new WorkstationCategory(guiHelper),
				tradeCategory = new TradeCategory(guiHelper));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(Items.EMERALD), WORKSTATION_TYPE);
		registration.addRecipeCatalyst(new ItemStack(Items.EMERALD), TRADE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(Items.VILLAGER_SPAWN_EGG), WORKSTATION_TYPE);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ErrorUtil.checkNotNull(WORKSTATION_TYPE, "workstationType");
		ErrorUtil.checkNotNull(TRADE_TYPE, "tradeType");


		registration.addRecipes(WORKSTATION_TYPE, getWorkstations());
		registration.addRecipes(TRADE_TYPE, getTrades());
	}

	public List<WorkstationWrapper> getWorkstations() {
		List<WorkstationWrapper> entries = new LinkedList<>();
		for (VillagerProfession profession : ForgeRegistries.PROFESSIONS) {
			List<ItemStack> stacks = new LinkedList<>();
			List<ResourceLocation> knownItems = new LinkedList<>();
			PoiType poiType = profession.getJobPoiType();

			for (BlockState state : poiType.matchingStates) {
				Block block = ForgeRegistries.BLOCKS.getValue(state.getBlock().getRegistryName());
				if (block != null) {
					ItemStack stack = CompatibilityHelper.compatibilityCheck(new ItemStack(block), profession.getRegistryName());
					ResourceLocation location = stack.getItem().getRegistryName();
					if (!stack.isEmpty() && !knownItems.contains(location)) {
						stacks.add(stack);
						knownItems.add(stack.getItem().getRegistryName());
					}
				}
			}
			if (!stacks.isEmpty()) {
				Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
				for (int i = 0; i < stacks.size(); i++) {
					map.put(i, stacks.get(i));
				}
				entries.add(new WorkstationWrapper(profession, map));
			}
		}
		return entries;
	}

	public List<TradeWrapper> getTrades() {
		List<TradeWrapper> entries = new LinkedList<>();
		for (Map.Entry<VillagerProfession, Int2ObjectMap<VillagerTrades.ItemListing[]>> entry : VillagerTrades.TRADES.entrySet()) {
			VillagerProfession profession = entry.getKey();
			Int2ObjectMap<VillagerTrades.ItemListing[]> itemListings = entry.getValue();
			entries.add(new TradeWrapper(profession, itemListings));
		}
		return entries;
	}
}