package com.mrbysco.justenoughprofessions.jei;

import com.mrbysco.justenoughprofessions.RenderHelper;
import com.mrbysco.justenoughprofessions.platform.Services;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ProfessionWrapper(ProfessionEntry entry) implements IRecipeCategoryExtension<ProfessionWrapper> {

	public ResourceLocation getProfessionName() {
		return Services.PLATFORM.getProfessionKey(entry.profession());
	}

	public List<ItemStack> getBlockStacks() {
		return this.entry.blockStacks();
	}

	@Override
	public void drawInfo(ProfessionWrapper recipe, int recipeWidth, int recipeHeight, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		Villager entityVillager = entry.getVillagerEntity();
		if (entityVillager != null) {
			RenderHelper.renderEntity(guiGraphics, 22, 62, 25.0F,
					38 - mouseX,
					80 - mouseY,
					entityVillager);
		}
	}
}