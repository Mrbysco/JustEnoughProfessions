package com.mrbysco.justenoughprofessions.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.justenoughprofessions.RenderHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

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
    public void drawInfo(int recipeWidth, int recipeHeight, PoseStack matrixStack, double mouseX, double mouseY) {
        Villager entityVillager = entry.getVillagerEntity();
        if(entityVillager != null) {
            RenderHelper.renderEntity(matrixStack, 22, 62, 25.0F,
                    38 - mouseX,
                    80 - mouseY,
                    entityVillager);
        }
    }
}