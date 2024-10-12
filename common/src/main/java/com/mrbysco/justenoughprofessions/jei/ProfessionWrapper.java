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

/**
 * A wrapper class for the profession recipe.
 * @param entry The profession entry for the recipe.
 */
public record ProfessionWrapper(ProfessionEntry entry) implements IRecipeCategoryExtension<ProfessionWrapper> {

	/**
	 * Get the profession name for the recipe.
	 * @return the profession name for the recipe.
	 */
	public ResourceLocation getProfessionName() {
		return Services.PLATFORM.getProfessionKey(entry.profession());
	}

	/**
	 * Get the profession name for display.
	 *
	 * @return the profession name for display.
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

	/**
	 * Get the ItemStacks that represent the items in the recipe.
	 * @param recipe The recipe to get the items from.
	 * @param recipeWidth The width of the recipe.
	 * @param recipeHeight The height of the recipe.
	 * @param guiGraphics The GuiGraphics instance.
	 * @param mouseX the X position of the mouse, relative to the recipe.
	 * @param mouseY the Y position of the mouse, relative to the recipe.
	 */
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