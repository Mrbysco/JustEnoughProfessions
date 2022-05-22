package com.mrbysco.justenoughprofessions.profession.trade;

import com.mrbysco.justenoughprofessions.profession.AbstractProfession;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

public class TradeEntry extends AbstractProfession {
	private final Int2ObjectMap<VillagerTrades.ItemListing[]> itemListings;

	public TradeEntry(VillagerProfession profession, Int2ObjectMap<VillagerTrades.ItemListing[]> listings) {
		super(profession);
		this.itemListings = listings;
	}

	public Int2ObjectMap<VillagerTrades.ItemListing[]> getItemListings() {
		return itemListings;
	}
}
