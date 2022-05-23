package com.mrbysco.justenoughprofessions.profession.trade;

import com.mrbysco.justenoughprofessions.profession.AbstractProfession;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeEntry extends AbstractProfession {
	private final Map<Integer, List<TradeListing>> itemListings;

	public TradeEntry(VillagerProfession profession, Int2ObjectMap<VillagerTrades.ItemListing[]> listings) {
		super(profession);
		this.itemListings = new HashMap<>();
		this.populateListings(listings);
	}

	public void populateListings(Int2ObjectMap<VillagerTrades.ItemListing[]> listings) {
		Villager villager = getVillagerEntity();
		for (int i = 0; i < listings.size(); i++) {
			int level = i + 1;
			List<TradeListing> levelListings = new ArrayList<>();
			VillagerTrades.ItemListing[] currentListings = listings.get(level);
			for (VillagerTrades.ItemListing listing : currentListings) {
				MerchantOffer offer = listing.getOffer(villager, villager.getRandom());
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
}
