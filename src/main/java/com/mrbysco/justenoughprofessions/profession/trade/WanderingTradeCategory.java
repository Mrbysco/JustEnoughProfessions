package com.mrbysco.justenoughprofessions.profession.trade;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.justenoughprofessions.JustEnoughProfessions;
import com.mrbysco.justenoughprofessions.ProfessionPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public class WanderingTradeCategory<T extends IRecipeCategoryExtension> implements IRecipeCategory<WanderingTradeWrapper> {
	private final IDrawableStatic background;
	private final IDrawableStatic icon;
	private final IDrawableStatic slotDrawable;
	private final TranslatableComponent title;

	public WanderingTradeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createBlankDrawable(120, 100);

		ResourceLocation iconLocation = new ResourceLocation(JustEnoughProfessions.MOD_ID, "textures/gui/profession_icon.png");
		this.icon = guiHelper.createDrawable(iconLocation, 0, 0, 16, 16);

		this.slotDrawable = guiHelper.getSlotDrawable();
		this.title = new TranslatableComponent("justenoughprofessions.trades.title");
	}

	@SuppressWarnings("removal")
	@Override
	public ResourceLocation getUid() {
		return ProfessionPlugin.WANDERING_TRADE;
	}

	@SuppressWarnings("removal")
	@Override
	public Class<? extends WanderingTradeWrapper> getRecipeClass() {
		return WanderingTradeWrapper.class;
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
	public void setRecipe(IRecipeLayoutBuilder builder, WanderingTradeWrapper wanderingTradeWrapper, IFocusGroup focuses) {
		IFocus<?> focus = focuses.getFocuses(RecipeIngredientRole.CATALYST).findFirst().orElse(null);
		if (focus == null) {
			focus = focuses.getFocuses(VanillaTypes.ITEM).findFirst().orElse(null);
		}
		ItemStack focusStack = focus == null ? ItemStack.EMPTY : focus.checkedCast(VanillaTypes.ITEM).isPresent() ?
				focus.checkedCast(VanillaTypes.ITEM).get().getTypedValue().getIngredient() : ItemStack.EMPTY;

		Map<Integer, List<TradeListing>> itemListings = wanderingTradeWrapper.getItemListings();
		for (int i = 0; i < 2; i++) {
			NonNullList<ItemStack> inputStacks = NonNullList.create();
			NonNullList<ItemStack> inputStacks2 = NonNullList.create();
			NonNullList<ItemStack> outputStacks = NonNullList.create();
			List<TradeListing> listings = itemListings.get(i + 1);
			for (TradeListing listing : listings) {
				ItemStack costA = listing.input1();
				ItemStack costB = listing.input2();
				ItemStack result = listing.output();

				if (focusStack.isEmpty()) {
					inputStacks.add(costA);
					inputStacks2.add(costB);
					outputStacks.add(result);
				} else {
					if (focus.getRole() == RecipeIngredientRole.INPUT) {
						if (focusStack.sameItem(costA) || focusStack.sameItem(costB)) {
							inputStacks.add(costA);
							inputStacks2.add(costB);
							outputStacks.add(result);
						}
					} else if (focus.getRole() == RecipeIngredientRole.OUTPUT) {
						if (focusStack.sameItem(result)) {
							inputStacks.add(costA);
							inputStacks2.add(costB);
							outputStacks.add(result);
						}
					} else {
						inputStacks.add(costA);
						inputStacks2.add(costB);
						outputStacks.add(result);
					}
				}
			}
			builder.addSlot(RecipeIngredientRole.INPUT, 51, 32 + (36 * i)).addItemStacks(inputStacks);
			builder.addSlot(RecipeIngredientRole.INPUT, 71, 32 + (36 * i)).addItemStacks(inputStacks2);
			builder.addSlot(RecipeIngredientRole.OUTPUT, 91, 32 + (36 * i)).addItemStacks(outputStacks);
		}
	}

	@Override
	public void draw(WanderingTradeWrapper tradeWrapper, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
		// Draw Slots
		for (int i = 0; i < 2; i++) {
			this.slotDrawable.draw(poseStack, 50, 31 + (36 * i));
			this.slotDrawable.draw(poseStack, 70, 31 + (36 * i));
			this.slotDrawable.draw(poseStack, 90, 31 + (36 * i));
		}

		// Draw entity
		poseStack.pushPose();
		poseStack.translate(1, 0, 0);
		tradeWrapper.drawInfo(getBackground().getWidth(), getBackground().getHeight(), poseStack, mouseX, mouseY);
		poseStack.popPose();
		// Draw entity name
		poseStack.pushPose();
		poseStack.translate(1, 0, 0);
		poseStack.popPose();
		Font font = Minecraft.getInstance().font;
		for (int i = 0; i < 2; i++) {
			String text = i == 0 ? "Generic" : "Rare";
			font.draw(poseStack, text, 50, 18 + (i * 36 + i), 8);
		}
	}
}
