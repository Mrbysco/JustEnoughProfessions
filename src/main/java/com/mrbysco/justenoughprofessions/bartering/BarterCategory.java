package com.mrbysco.justenoughprofessions.bartering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.justenoughprofessions.ProfessionPlugin;
import mezz.jei.api.constants.VanillaTypes;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BarterCategory<T extends IRecipeCategoryExtension> implements IRecipeCategory<BarterWrapper> {
	private final IDrawableStatic background;
	private final IDrawable icon;
	private final IDrawableStatic slotDrawable;
	private final TranslatableComponent title;

	public BarterCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createBlankDrawable(100, 60);

		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(Items.GOLD_INGOT));

		this.slotDrawable = guiHelper.getSlotDrawable();
		this.title = new TranslatableComponent("justenoughprofessions.barter.title");
	}

	@SuppressWarnings("removal")
	@Override
	public ResourceLocation getUid() {
		return ProfessionPlugin.BARTER;
	}

	@SuppressWarnings("removal")
	@Override
	public Class<? extends BarterWrapper> getRecipeClass() {
		return BarterWrapper.class;
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
	public void setRecipe(IRecipeLayoutBuilder builder, BarterWrapper barterWrapper, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 37, 21).addItemStack(new ItemStack(Items.GOLD_INGOT));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 71, 21).addItemStacks(barterWrapper.getOutputs());
	}

	@Override
	public void draw(BarterWrapper barterWrapper, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
		// Draw Slots
		this.slotDrawable.draw(poseStack, 36, 20);
		this.slotDrawable.draw(poseStack, 70, 20);

		// Draw entity
		poseStack.pushPose();
		poseStack.translate(-6, 18, 0);
		barterWrapper.drawInfo(getBackground().getWidth(), getBackground().getHeight(), poseStack, mouseX, mouseY);
		poseStack.popPose();
		// Draw entity name
		poseStack.pushPose();
		poseStack.translate(1, 0, 0);
		Font font = Minecraft.getInstance().font;
		String text = Math.round((barterWrapper.getChance() * 100) * 100.0) / 100.0 + "%";
		font.draw(poseStack, text, 0, 0, 8);
		String amount = "";
		if (barterWrapper.getMin() > 0 && barterWrapper.getMax() > 0) {
			amount = barterWrapper.getMin() + "-" + barterWrapper.getMax();
		} else {
			amount = "1";
		}
		font.draw(poseStack, amount, 78 - (font.width(amount) / 2), 40, 8);

		poseStack.popPose();
	}
}
