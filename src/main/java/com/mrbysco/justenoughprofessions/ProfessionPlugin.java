package com.mrbysco.justenoughprofessions;

import com.mrbysco.justenoughprofessions.bartering.BarterCategory;
import com.mrbysco.justenoughprofessions.bartering.BarterWrapper;
import com.mrbysco.justenoughprofessions.compat.CompatibilityHelper;
import com.mrbysco.justenoughprofessions.profession.trade.TradeCategory;
import com.mrbysco.justenoughprofessions.profession.trade.TradeWrapper;
import com.mrbysco.justenoughprofessions.profession.trade.WanderingTradeCategory;
import com.mrbysco.justenoughprofessions.profession.trade.WanderingTradeWrapper;
import com.mrbysco.justenoughprofessions.profession.workstation.WorkstationCategory;
import com.mrbysco.justenoughprofessions.profession.workstation.WorkstationWrapper;
import com.mrbysco.justenoughprofessions.util.LootTableUtil;
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
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JeiPlugin
public class ProfessionPlugin implements IModPlugin {
	private static final ResourceLocation UID = new ResourceLocation(JustEnoughProfessions.MOD_ID, "jei_plugin");

	public static final ResourceLocation WORKSTATION = new ResourceLocation(JustEnoughProfessions.MOD_ID, "workstation");
	public static final ResourceLocation TRADE = new ResourceLocation(JustEnoughProfessions.MOD_ID, "trade");
	public static final ResourceLocation WANDERING_TRADE = new ResourceLocation(JustEnoughProfessions.MOD_ID, "wandering_trade");
	public static final ResourceLocation BARTER = new ResourceLocation(JustEnoughProfessions.MOD_ID, "barter");
	public static final RecipeType<WorkstationWrapper> WORKSTATION_TYPE = RecipeType.create(JustEnoughProfessions.MOD_ID, "workstation", WorkstationWrapper.class);
	public static final RecipeType<TradeWrapper> TRADE_TYPE = RecipeType.create(JustEnoughProfessions.MOD_ID, "trade", TradeWrapper.class);
	public static final RecipeType<WanderingTradeWrapper> WANDERING_TRADE_TYPE = RecipeType.create(JustEnoughProfessions.MOD_ID, "wandering_trade", WanderingTradeWrapper.class);
	public static final RecipeType<BarterWrapper> BARTER_TYPE = RecipeType.create(JustEnoughProfessions.MOD_ID, "barter", BarterWrapper.class);

	@Nullable
	private IRecipeCategory<WorkstationWrapper> workstationCategory;
	@Nullable
	private IRecipeCategory<TradeWrapper> tradeCategory;
	@Nullable
	private IRecipeCategory<WanderingTradeWrapper> wanderingTradeCategory;
	@Nullable
	private IRecipeCategory<BarterWrapper> barterCategory;

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
				tradeCategory = new TradeCategory(guiHelper),
				wanderingTradeCategory = new WanderingTradeCategory<>(guiHelper),
				barterCategory = new BarterCategory<>(guiHelper));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(Items.EMERALD), WORKSTATION_TYPE);
		registration.addRecipeCatalyst(new ItemStack(Items.EMERALD), TRADE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(Items.VILLAGER_SPAWN_EGG), WORKSTATION_TYPE);
		registration.addRecipeCatalyst(new ItemStack(Items.VILLAGER_SPAWN_EGG), TRADE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(Items.WANDERING_TRADER_SPAWN_EGG), WANDERING_TRADE_TYPE);
		registration.addRecipeCatalyst(new ItemStack(Items.GOLD_INGOT), BARTER_TYPE);
		registration.addRecipeCatalyst(new ItemStack(Items.PIGLIN_SPAWN_EGG), BARTER_TYPE);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ErrorUtil.checkNotNull(WORKSTATION_TYPE, "workstationType");
		ErrorUtil.checkNotNull(TRADE_TYPE, "tradeType");
		ErrorUtil.checkNotNull(WANDERING_TRADE_TYPE, "wanderingTradeType");
		ErrorUtil.checkNotNull(BARTER_TYPE, "barterType");

		registration.addRecipes(WORKSTATION_TYPE, getWorkstations());
		registration.addRecipes(TRADE_TYPE, getTrades());
		registration.addRecipes(WANDERING_TRADE_TYPE, getWanderingTrades());
		registration.addRecipes(BARTER_TYPE, getBarters());
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
			if (profession.getRegistryName().equals(new ResourceLocation("none"))) {
				continue;
			}
			Int2ObjectMap<VillagerTrades.ItemListing[]> itemListings = entry.getValue();
			entries.add(new TradeWrapper(profession, itemListings));
		}
		return entries;
	}

	public List<WanderingTradeWrapper> getWanderingTrades() {
		return List.of(new WanderingTradeWrapper(VillagerTrades.WANDERING_TRADER_TRADES));
	}

	public List<BarterWrapper> getBarters() {
		LootTable barterTable = LootTableUtil.getLootTables(Minecraft.getInstance().level).get(BuiltInLootTables.PIGLIN_BARTERING);

		return LootTableUtil.getLootEntries(barterTable)
				.stream()
				.map(BarterWrapper::new)
				.collect(Collectors.toList());
	}
}