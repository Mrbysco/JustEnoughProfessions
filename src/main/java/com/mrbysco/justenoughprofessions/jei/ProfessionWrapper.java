package com.mrbysco.justenoughprofessions.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.justenoughprofessions.RenderHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ProfessionWrapper implements IRecipeCategoryExtension {
    private final ProfessionEntry entry;

    public ProfessionWrapper(ProfessionEntry entry) {
        this.entry = entry;
    }

    public ResourceLocation getProfessionName() {
        return entry.getProfession().getRegistryName();
    }

    public List<ItemStack> getBlockStacks() {
        return this.entry.getBlockStacks();
    }

    @Override
    public void setIngredients(IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, this.entry.getBlockStacks());
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, MatrixStack matrixStack, double mouseX, double mouseY) {
        VillagerEntity entityVillager = entry.getVillagerEntity();
        if(entityVillager != null) {
            RenderHelper.renderEntity(matrixStack, 22, 62, 25.0F,
                    38 - mouseX,
                    80 - mouseY,
                    entityVillager);
        }
    }
}
