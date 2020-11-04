package com.mrbysco.justenoughprofessions.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.justenoughprofessions.JeiProfessions;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class ProfessionCategory<T extends IRecipeCategoryExtension> implements IRecipeCategory<ProfessionWrapper> {
    public static final ResourceLocation UID = new ResourceLocation(JeiProfessions.MOD_ID, "professions");

    protected static final int X_FIRST_ITEM = 75;
    protected static final int Y_ITEM_DISTANCE = 22;
    private final IDrawableStatic background;
    private final IDrawableStatic icon;
    private final IDrawableStatic slotDrawable;

    public ProfessionCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(JeiProfessions.MOD_ID, "textures/gui/professions.png");
        this.background = guiHelper.drawableBuilder(location, 0, 0, 72, 62).addPadding(1, 0, 0, 50).build();

        ResourceLocation iconLocation = new ResourceLocation(JeiProfessions.MOD_ID, "textures/gui/profession_icon.png");
        this.icon = guiHelper.createDrawable(iconLocation, 0, 0, 16, 16);

        this.slotDrawable = guiHelper.getSlotDrawable();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends ProfessionWrapper> getRecipeClass() {
        return ProfessionWrapper.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("justenoughprofessions.professions.title");
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
    public void setIngredients(ProfessionWrapper professionWrapper, IIngredients iIngredients) {
        professionWrapper.setIngredients(iIngredients);
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, ProfessionWrapper professionWrapper, IIngredients iIngredients) {
        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();

        guiItemStacks.init(0, true, X_FIRST_ITEM, Y_ITEM_DISTANCE);
        guiItemStacks.set(0, professionWrapper.getBlockStacks());
    }

    @Override
    public void draw(ProfessionWrapper recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        // Draw Drops
        this.slotDrawable.draw(matrixStack, X_FIRST_ITEM, Y_ITEM_DISTANCE);

        // Draw entity
        recipe.drawInfo(getBackground().getWidth(), getBackground().getHeight(), matrixStack, mouseX, mouseY);
        // Draw entity name
        matrixStack.push();
        matrixStack.translate(1, 0, 0);
        FontRenderer font = Minecraft.getInstance().fontRenderer;
        String text = Screen.hasShiftDown() ? recipe.getProfessionName().toString() : recipe.getProfessionName().getPath();
        if(font.getStringWidth(text) > 122) {
            matrixStack.scale(0.75F, 0.75F, 0.75F);
        }
        font.drawString(matrixStack, text, 0, 0, 8);
        matrixStack.pop();
    }
}
