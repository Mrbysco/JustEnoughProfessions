package com.mrbysco.justenoughprofessions.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.justenoughprofessions.RenderHelper;
import com.mrbysco.justenoughprofessions.platform.Services;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ProfessionWrapper(ProfessionEntry entry) implements IRecipeCategoryExtension {

	public ResourceLocation getProfessionName() {
		return Services.PLATFORM.getProfessionKey(entry.profession());
	}

	public List<ItemStack> getBlockStacks() {
		return this.entry.blockStacks();
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