package com.mrbysco.justenoughprofessions.profession.workstation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.justenoughprofessions.client.RenderHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class WorkstationWrapper implements IRecipeCategoryExtension {
	private final WorkstationEntry entry;

	public WorkstationWrapper(WorkstationEntry entry) {
		this.entry = entry;
	}

	public WorkstationWrapper(VillagerProfession profession, Int2ObjectMap<ItemStack> stacks) {
		this.entry = new WorkstationEntry(profession, stacks);
	}

	public ResourceLocation getProfessionName() {
		return entry.profession().getRegistryName();
	}

	public List<ItemStack> getBlockStacks() {
		return this.entry.getBlockStacks();
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