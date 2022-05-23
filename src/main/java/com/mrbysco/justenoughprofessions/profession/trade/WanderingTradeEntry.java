package com.mrbysco.justenoughprofessions.profession.trade;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class WanderingTradeEntry {
	private final Map<Integer, List<TradeListing>> itemListings;

	public WanderingTradeEntry(Int2ObjectMap<VillagerTrades.ItemListing[]> listings) {
		this.itemListings = new HashMap<>();
		this.populateListings(listings);
	}

	public void populateListings(Int2ObjectMap<VillagerTrades.ItemListing[]> listings) {
		WanderingTrader trader = getTraderEntity();
		for (int i = 0; i < listings.size(); i++) {
			int level = i + 1;
			List<TradeListing> levelListings = new ArrayList<>();
			VillagerTrades.ItemListing[] currentListings = listings.get(level);
			for (VillagerTrades.ItemListing listing : currentListings) {
				MerchantOffer offer = listing.getOffer(trader, trader.getRandom());
				if (offer != null) {
					levelListings.add(new TradeListing(offer.getCostA(), offer.getCostB(), offer.getResult()));
				}
			}
			itemListings.put(level, levelListings);
		}
	}

	public Map<Integer, List<TradeListing>> getItemListings() {
		return itemListings;
	}

	@Nullable
	public WanderingTrader getTraderEntity() {
		CompoundTag nbt = new CompoundTag();
		nbt.putString("id", ForgeRegistries.ENTITIES.getKey(EntityType.WANDERING_TRADER).toString());
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.hasSingleplayerServer() && mc.getSingleplayerServer() != null ? mc.getSingleplayerServer().getAllLevels().iterator().next() : mc.level;
		if (level != null) {
			WanderingTrader wanderingTrader = (WanderingTrader) EntityType.loadEntityRecursive(nbt, level, Function.identity());
			if (wanderingTrader != null) {
				return wanderingTrader;
			}
		}
		return null;
	}
}
