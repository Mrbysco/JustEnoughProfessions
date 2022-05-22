package com.mrbysco.justenoughprofessions.profession.trade;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.justenoughprofessions.client.RenderHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

public class TradeWrapper implements IRecipeCategoryExtension {
	private final TradeEntry entry;

	public TradeWrapper(TradeEntry entry) {
		this.entry = entry;
	}

	public TradeWrapper(VillagerProfession profession, Int2ObjectMap<VillagerTrades.ItemListing[]> stacks) {
		this.entry = new TradeEntry(profession, stacks);
	}

	public ResourceLocation getProfessionName() {
		return entry.profession().getRegistryName();
	}

	public Int2ObjectMap<VillagerTrades.ItemListing[]> getItemListings() {
		return this.entry.getItemListings();
	}

	public Villager getVillager() {
		return this.entry.getVillagerEntity();
	}

	@Override
	public void drawInfo(int recipeWidth, int recipeHeight, PoseStack poseStack, double mouseX, double mouseY) {
		Villager entityVillager = entry.getVillagerEntity();
		if (entityVillager != null) {
			RenderHelper.renderEntity(poseStack, 22, 62, 25.0F,
					38 - mouseX,
					80 - mouseY,
					entityVillager);
		}
	}
}