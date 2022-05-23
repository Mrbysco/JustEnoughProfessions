package com.mrbysco.justenoughprofessions.profession.trade;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.justenoughprofessions.client.RenderHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;

import java.util.List;
import java.util.Map;

public class WanderingTradeWrapper implements IRecipeCategoryExtension {
	private final WanderingTradeEntry entry;

	public WanderingTradeWrapper(WanderingTradeEntry entry) {
		this.entry = entry;
	}

	public WanderingTradeWrapper(Int2ObjectMap<VillagerTrades.ItemListing[]> stacks) {
		this(new WanderingTradeEntry(stacks));
	}

	public Map<Integer, List<TradeListing>> getItemListings() {
		return this.entry.getItemListings();
	}

	public WanderingTrader getWanderingTrader() {
		return this.entry.getTraderEntity();
	}

	@Override
	public void drawInfo(int recipeWidth, int recipeHeight, PoseStack poseStack, double mouseX, double mouseY) {
		WanderingTrader wanderingTrader = entry.getTraderEntity();
		if (wanderingTrader != null) {
			RenderHelper.renderEntity(poseStack, 20, 80, 25.0F,
					38 - mouseX,
					80 - mouseY,
					wanderingTrader);
		}
	}
}