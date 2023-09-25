package com.mrbysco.justenoughprofessions.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.justenoughprofessions.Constants;
import com.mrbysco.justenoughprofessions.platform.Services;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ProfessionCategory implements IRecipeCategory<ProfessionWrapper> {

	private final IDrawableStatic background;
	private final IDrawableStatic icon;
	private final IDrawableStatic slotDrawable;

	public ProfessionCategory(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation(Constants.MOD_ID, "textures/gui/professions.png");
		this.background = guiHelper.drawableBuilder(location, 0, 0, 72, 62).addPadding(1, 0, 0, 50).build();

		ResourceLocation iconLocation = new ResourceLocation(Constants.MOD_ID, "textures/gui/profession_icon.png");
		this.icon = guiHelper.createDrawable(iconLocation, 0, 0, 16, 16);

		this.slotDrawable = guiHelper.getSlotDrawable();
	}

	@Override
	public RecipeType<ProfessionWrapper> getRecipeType() {
		return Services.PLATFORM.getProfessionType();
	}

	@Override
	public Component getTitle() {
		return Component.translatable("justenoughprofessions.professions.title");
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
	public void setRecipe(IRecipeLayoutBuilder builder, ProfessionWrapper recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 23).addItemStacks(recipe.getBlockStacks());
	}
	@Override
	public void draw(ProfessionWrapper professionWrapper, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		// Draw Drops
		this.slotDrawable.draw(guiGraphics, 75, 22);

		// Draw entity
		professionWrapper.drawInfo(professionWrapper, getBackground().getWidth(), getBackground().getHeight(), guiGraphics, mouseX, mouseY);
		// Draw entity name
		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		poseStack.translate(1, 0, 0);
		Font font = Minecraft.getInstance().font;
		String text = Screen.hasShiftDown() ? professionWrapper.getProfessionName().toString() : professionWrapper.getProfessionName().getPath();
		if (font.width(text) > 122) {
			poseStack.scale(0.75F, 0.75F, 0.75F);
		}
		guiGraphics.drawString(font, text, 0, 0, 8, false);
		poseStack.popPose();
	}
}
