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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
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
		Identifier iconLocation = Constants.modLoc("textures/gui/profession_icon.png");
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
		Minecraft mc = Minecraft.getInstance();
		Font font = mc.font;
		String text = mc.hasShiftDown() ? professionWrapper.getProfessionName().toString() : professionWrapper.getDisplayName().getString();
		if (font.width(text) > 122) {
			poseStack.scale(0.75F, 0.75F);
		}
		renderScrollingString(guiGraphics, font, Component.literal(text), 0, 0, 0, 74, 9, ARGB.opaque(8));
		poseStack.popMatrix();
	}

	private void renderScrollingString(
			GuiGraphics guiGraphics, Font font, Component text, int centerX, int minX, int minY, int maxX, int maxY, int color
	) {
		int i = font.width(text);
		int j = (minY + maxY - 9) / 2 + 1;
		int k = maxX - minX;
		if (i > k) {
			int l = i - k;
			double d0 = Util.getMillis() / 1000.0;
			double d1 = Math.max(l * 0.5, 3.0);
			double d2 = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * d0 / d1)) / 2.0 + 0.5;
			double d3 = Mth.lerp(d2, 0.0, (double)l);
			guiGraphics.enableScissor(minX, minY, maxX, maxY);
			guiGraphics.drawString(font, text, minX - (int)d3, j, color, false);
			guiGraphics.disableScissor();
		} else {
			guiGraphics.drawString(font, text, 0, 0, ARGB.opaque(8), false);
		}
	}
}
