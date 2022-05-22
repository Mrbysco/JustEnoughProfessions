package com.mrbysco.justenoughprofessions.util;

import com.google.common.collect.Maps;
import com.mrbysco.justenoughprofessions.bartering.LootEntry;
import com.mrbysco.justenoughprofessions.mixin.EnchantRandomlyFunctionAccessor;
import com.mrbysco.justenoughprofessions.mixin.LootPoolAccessor;
import com.mrbysco.justenoughprofessions.mixin.LootTableAccessor;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.resource.PathResourcePack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class LootTableUtil {
	public static final LootContext randomContext = new RandomContext();
	private static LootTables lootTables;

	public static LootTables getLootTables(Level level) {
		if (level != null && level.getServer() != null) {
			return level.getServer().getLootTables();
		}

		if(lootTables != null) {
			return lootTables;
		}

		lootTables = new LootTables(new PredicateManager());

		ReloadableResourceManager reloadableResourceManager = new ReloadableResourceManager(PackType.SERVER_DATA);

		List<PackResources> packs = new LinkedList<>();
		packs.add(new VanillaPackResources(ServerPacksSource.BUILT_IN_METADATA, "minecraft"));
		for (IModFileInfo mod : ModList.get().getModFiles()) {
			packs.add(new PathResourcePack(mod.getFile().getFileName(), mod.getFile().getFilePath()));
		}

		reloadableResourceManager.registerReloadListener(lootTables);
		ReloadInstance instance = reloadableResourceManager.createReload(
				Util.backgroundExecutor(),
				Minecraft.getInstance(),
				CompletableFuture.completedFuture(Unit.INSTANCE),
				packs);

		Minecraft.getInstance().managedBlock(instance::isDone);

		return lootTables;
	}

	public static List<LootEntry> getLootEntries(LootTable table) {
		List<LootEntry> drops = new ArrayList<>();

		final LootTables lootTables = getLootTables(Minecraft.getInstance().level);

		getPools(table).forEach(
				pool -> {
					final float totalWeight = getEntries(pool).stream()
							.filter(entry -> entry instanceof LootPoolSingletonContainer).map(entry -> (LootPoolSingletonContainer) entry)
							.mapToInt(entry -> entry.weight).sum();
					getEntries(pool).stream()
							.filter(entry -> entry instanceof LootItem).map(entry -> (LootItem) entry)
							.map(entry -> {
								ItemStack stack = new ItemStack(entry.item);
								LootEntry lootEntry = new LootEntry(entry.weight / totalWeight, stack);
								for (LootItemFunction function : entry.functions) {
									if (function instanceof SetNbtFunction setNbtFunction) {
										setNbtFunction.apply(stack, randomContext);
									} else if (function instanceof EnchantRandomlyFunction setEnchantFunction) {
										List<Enchantment> enchantments = getEnchantments(setEnchantFunction);
										for (Enchantment enchantment : enchantments) {
											int maxLevel = enchantment.getMaxLevel();
											for (int i = 0; i < maxLevel; i++) {
												ItemStack enchantedStack = new ItemStack(Items.ENCHANTED_BOOK);
												enchantedStack.enchant(enchantment, i + 1);
												lootEntry.addOutput(enchantedStack);
											}
										}
									} else if (function instanceof SetPotionFunction setPotionFunction) {
										setPotionFunction.apply(stack, randomContext);
									} else if (function instanceof SetItemCountFunction setCountFunction) {
										NumberProvider randomRange = setCountFunction.value;
										if (randomRange.getType() == NumberProviders.UNIFORM) {
											UniformGenerator uniformGenerator = (UniformGenerator) randomRange;
											if (uniformGenerator.min instanceof ConstantValue constantMin && uniformGenerator.max instanceof ConstantValue constantMax) {
												lootEntry.setMin((int) constantMin.getFloat(null));
												lootEntry.setMax((int) constantMax.getFloat(null));
											}
										}
									} else {
//										function.apply(stack, randomContext);
									}
								}
								lootEntry.setOutput(stack);
								return lootEntry;
							})
							.forEach(drops::add);

					getEntries(pool).stream()
							.filter(entry -> entry instanceof LootTableReference).map(entry -> (LootTableReference) entry)
							.map(entry -> getLootEntries(lootTables.get(entry.name))).forEach(drops::addAll);
				}
		);

		drops.removeIf(Objects::isNull);
		return drops;
	}

	public static List<LootPool> getPools(LootTable table) {
		return ((LootTableAccessor) table).getPools();
	}

	public static List<LootPoolEntryContainer> getEntries(LootPool pool) {
		return List.of(((LootPoolAccessor) pool).getEntries());
	}

	public static List<Enchantment> getEnchantments(EnchantRandomlyFunction enchantRandomlyFunction) {
		return ((EnchantRandomlyFunctionAccessor) enchantRandomlyFunction).getEnchantments();
	}

	public static class RandomContext extends LootContext {
		public RandomContext() {
			super(new Random(), 0F, null, null, null, Maps.newIdentityHashMap(), Maps.newHashMap());
		}
	}
}
