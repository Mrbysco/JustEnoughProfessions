package com.mrbysco.justenoughprofessions.jei;

import com.mrbysco.justenoughprofessions.RenderHelper;
import com.mrbysco.justenoughprofessions.platform.Services;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ProfessionWrapper(ProfessionEntry entry) implements IRecipeCategoryExtension<ProfessionWrapper> {

	/**
	 * Get the profession name for the recipe.
	 * @return
	 */
	public ResourceLocation getProfessionName() {
		return Services.PLATFORM.getProfessionKey(entry.profession());
	}

	/**
	 * Get the profession name for display.
	 *
	 * @return
	 */
	public Component getDisplayName() {
		ResourceLocation professionKey = getProfessionName();
		String languageKey = professionKey.toLanguageKey();
		if (languageKey.startsWith("minecraft.")) languageKey = languageKey.replace("minecraft.", "");
		return Component.translatable("entity.minecraft.villager." + languageKey);
	}

	/**
	 * Get the ItemStacks that represent the blocks in the recipe.
	 * @return a list of ItemStacks for the blocks in the recipe.
	 */
	public List<ItemStack> getBlockStacks() {
		return this.entry.blockStacks();
	}

	@Override
	public void drawInfo(ProfessionWrapper recipe, int recipeWidth, int recipeHeight, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		Villager entityVillager = entry.getVillagerEntity();
		if (entityVillager != null) {
			RenderHelper.renderVillager(guiGraphics, 22, 62, 25.0F,
					38 - mouseX,
					15 - mouseY,
					entityVillager);
		}
	}
}