package com.mrbysco.justenoughprofessions.profession.workstation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.justenoughprofessions.JustEnoughProfessions;
import com.mrbysco.justenoughprofessions.ProfessionPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class WorkstationCategory<T extends IRecipeCategoryExtension> implements IRecipeCategory<WorkstationWrapper> {
	private final IDrawableStatic background;
	private final IDrawableStatic icon;
	private final IDrawableStatic slotDrawable;
	private final TranslatableComponent title;

	public WorkstationCategory(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation(JustEnoughProfessions.MOD_ID, "textures/gui/professions.png");
		this.background = guiHelper.drawableBuilder(location, 0, 0, 72, 62).addPadding(1, 0, 0, 50).build();

		ResourceLocation iconLocation = new ResourceLocation(JustEnoughProfessions.MOD_ID, "textures/gui/profession_icon.png");
		this.icon = guiHelper.createDrawable(iconLocation, 0, 0, 16, 16);

		this.slotDrawable = guiHelper.getSlotDrawable();
		this.title = new TranslatableComponent("justenoughprofessions.workstations.title");
	}

	@SuppressWarnings("removal")
	@Override
	public ResourceLocation getUid() {
		return ProfessionPlugin.WORKSTATION;
	}

	@SuppressWarnings("removal")
	@Override
	public Class<? extends WorkstationWrapper> getRecipeClass() {
		return WorkstationWrapper.class;
	}

	@Override
	public Component getTitle() {
		return this.title;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, WorkstationWrapper workstationWrapper, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 23).addItemStacks(workstationWrapper.getBlockStacks());
	}

	@Override
	public void draw(WorkstationWrapper workstationWrapper, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
		// Draw Drops
		this.slotDrawable.draw(poseStack, 75, 22);

		// Draw entity
		workstationWrapper.drawInfo(getBackground().getWidth(), getBackground().getHeight(), poseStack, mouseX, mouseY);
		// Draw entity name
		poseStack.pushPose();
		poseStack.translate(1, 0, 0);
		Font font = Minecraft.getInstance().font;
		String text = Screen.hasShiftDown() ? workstationWrapper.getProfessionName().toString() : workstationWrapper.getProfessionName().getPath();
		if (font.width(text) > 122) {
			poseStack.scale(0.75F, 0.75F, 0.75F);
		}
		font.draw(poseStack, text, 0, 0, 8);
		poseStack.popPose();
	}
}
