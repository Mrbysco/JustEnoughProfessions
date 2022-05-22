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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public class TradeCategory<T extends IRecipeCategoryExtension> implements IRecipeCategory<TradeWrapper> {
	private final IDrawableStatic background;
	private final IDrawableStatic icon;
	private final IDrawableStatic slotDrawable;
	private final TranslatableComponent title;

	public TradeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createBlankDrawable(120, 100);

		ResourceLocation iconLocation = new ResourceLocation(JustEnoughProfessions.MOD_ID, "textures/gui/profession_icon.png");
		this.icon = guiHelper.createDrawable(iconLocation, 0, 0, 16, 16);

		this.slotDrawable = guiHelper.getSlotDrawable();
		this.title = new TranslatableComponent("justenoughprofessions.trades.title");
	}

	@SuppressWarnings("removal")
	@Override
	public ResourceLocation getUid() {
		return ProfessionPlugin.TRADE;
	}

	@SuppressWarnings("removal")
	@Override
	public Class<? extends TradeWrapper> getRecipeClass() {
		return TradeWrapper.class;
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
	public void setRecipe(IRecipeLayoutBuilder builder, TradeWrapper tradeWrapper, IFocusGroup focuses) {
		IFocus<ItemStack> focus = focuses.getFocuses(VanillaTypes.ITEM).findFirst().orElse(null);
		ItemStack focusStack = focus == null ? ItemStack.EMPTY : focus.getTypedValue().getIngredient();

		Map<Integer, List<TradeListing>> itemListings = tradeWrapper.getItemListings();
		for (int i = 0; i < 5; i++) {
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
					}
				}
			}
			builder.addSlot(RecipeIngredientRole.INPUT, 61, 9 + (18 * i)).addItemStacks(inputStacks);
			builder.addSlot(RecipeIngredientRole.INPUT, 81, 9 + (18 * i)).addItemStacks(inputStacks2);
			builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 9 + (18 * i)).addItemStacks(outputStacks);
		}
	}

	@Override
	public void draw(TradeWrapper tradeWrapper, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
		// Draw Slots
		for (int i = 0; i < 5; i++) {
			this.slotDrawable.draw(poseStack, 60, 8 + (18 * i));
			this.slotDrawable.draw(poseStack, 80, 8 + (18 * i));
			this.slotDrawable.draw(poseStack, 100, 8 + (18 * i));
		}

		// Draw entity
		poseStack.pushPose();
		poseStack.translate(1, 0, 0);
		tradeWrapper.drawInfo(getBackground().getWidth(), getBackground().getHeight(), poseStack, mouseX, mouseY);
		poseStack.popPose();
		// Draw entity name
		poseStack.pushPose();
		poseStack.translate(1, 0, 0);
		String text = Screen.hasShiftDown() ? tradeWrapper.getProfessionName().toString() : tradeWrapper.getProfessionName().getPath();
		Font font = Minecraft.getInstance().font;
		if (font.width(text) > 122) {
			poseStack.scale(0.75F, 0.75F, 0.75F);
		}
		font.draw(poseStack, text, 0, 0, 8);

		poseStack.popPose();

		for (int i = 0; i < 5; i++) {
			font.draw(poseStack, "Lv. " + (i + 1), 34, 12 + (i * 18 + i), 8);
		}
	}
}
