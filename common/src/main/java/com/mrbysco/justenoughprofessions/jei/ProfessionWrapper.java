package com.mrbysco.justenoughprofessions.jei;

import com.mrbysco.justenoughprofessions.RenderHelper;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.joml.Vector2f;

import java.util.List;

/**
 * A wrapper class for the professionHolder recipe.
 *
 * @param entry The professionHolder entry for the recipe.
 */
public record ProfessionWrapper(ProfessionEntry entry) implements IRecipeCategoryExtension<ProfessionWrapper> {

	/**
	 * Get the professionHolder name for the recipe.
	 *
	 * @return the professionHolder name for the recipe.
	 */
	public Identifier getProfessionName() {
		return entry.professionHolder().unwrapKey().map(ResourceKey::identifier).orElse(null);
	}

	/**
	 * Get the professionHolder name for display.
	 *
	 * @return the professionHolder name for display.
	 */
	public Component getDisplayName() {
		Identifier professionKey = getProfessionName();
		String languageKey = professionKey.toLanguageKey();
		if (languageKey.startsWith("minecraft.")) languageKey = languageKey.replace("minecraft.", "");
		return Component.translatable("entity.minecraft.villager." + languageKey);
	}

	/**
	 * Get the ItemStacks that represent the blocks in the recipe.
	 *
	 * @return a list of ItemStacks for the blocks in the recipe.
	 */
	public List<ItemStack> getBlockStacks() {
		return this.entry.blockStacks();
	}

	/**
	 * Get the ItemStacks that represent the items in the recipe.
	 *
	 * @param recipe       The recipe to get the items from.
	 * @param recipeWidth  The width of the recipe.
	 * @param recipeHeight The height of the recipe.
	 * @param guiGraphics  The GuiGraphics instance.
	 * @param mouseX       the X position of the mouse, relative to the recipe.
	 * @param mouseY       the Y position of the mouse, relative to the recipe.
	 */
	@Override
	public void drawInfo(ProfessionWrapper recipe, int recipeWidth, int recipeHeight, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		final Matrix3x2fStack poseStack = guiGraphics.pose();

		Villager entityVillager = entry.getVillagerEntity();
		if (entityVillager != null) {
			Vector2f position = new Vector2f(26, 62);
			position = poseStack.transformPosition(position);
			int x = Math.round(position.x);
			int y = Math.round(position.y);
			RenderHelper.renderVillager(guiGraphics, x, y, 25.0F,
					mouseX + 12,
					mouseY - 12,
					entityVillager);
		}
	}
}