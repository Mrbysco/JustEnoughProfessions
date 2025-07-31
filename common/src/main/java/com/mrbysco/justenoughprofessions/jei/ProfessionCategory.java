package com.mrbysco.justenoughprofessions.jei;

import com.mrbysco.justenoughprofessions.Constants;
import com.mrbysco.justenoughprofessions.platform.Services;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.joml.Matrix3x2fStack;

/**
 * The JEI recipe category for the professions
 */
public class ProfessionCategory implements IRecipeCategory<ProfessionWrapper> {

	private final IDrawableStatic icon;

	/**
	 * Create the professionHolder category
	 * @param guiHelper The gui helper instance
	 */
	public ProfessionCategory(IGuiHelper guiHelper) {
		ResourceLocation iconLocation = Constants.modLoc("textures/gui/profession_icon.png");
		this.icon = guiHelper.createDrawable(iconLocation, 0, 0, 16, 16);
	}

	@Override
	public IRecipeType<ProfessionWrapper> getRecipeType() {
		return Services.PLATFORM.getProfessionType();
	}

	@Override
	public Component getTitle() {
		return Component.translatable("justenoughprofessions.professions.title");
	}

	@Override
	public int getHeight() {
		return 62;
	}

	@Override
	public int getWidth() {
		return 72;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ProfessionWrapper recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.OUTPUT, 56, 23)
				.addItemStacks(recipe.getBlockStacks())
				.setStandardSlotBackground();
	}

	@Override
	public void draw(ProfessionWrapper professionWrapper, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		// Draw entity
		professionWrapper.drawInfo(professionWrapper, getWidth(), getHeight(), guiGraphics, mouseX, mouseY);
		// Draw entity name
		Matrix3x2fStack poseStack = guiGraphics.pose();
		poseStack.pushMatrix();
		Font font = Minecraft.getInstance().font;
		String text = Screen.hasShiftDown() ? professionWrapper.getProfessionName().toString() : professionWrapper.getDisplayName().getString();
		if (font.width(text) > 122) {
			poseStack.scale(0.75F, 0.75F);
		}
		guiGraphics.drawString(font, text, 0, 0, ARGB.opaque(8), false);
		poseStack.popMatrix();
	}
}
