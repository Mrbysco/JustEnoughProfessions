package com.mrbysco.justenoughprofessions.bartering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.justenoughprofessions.client.RenderHelper;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public record BarterWrapper(LootEntry entry) implements IRecipeCategoryExtension {

	public double getChance() {
		return entry().chance();
	}

	public List<ItemStack> getOutputs() {
		return entry().getOutputs();
	}

	public int getMin() {
		return entry().min();
	}

	public int getMax() {
		return entry().max();
	}

	@Override
	public void drawInfo(int recipeWidth, int recipeHeight, PoseStack poseStack, double mouseX, double mouseY) {
		Piglin piglin = getPiglin();
		if (piglin != null) {
			RenderHelper.renderEntity(poseStack, 20, 30, 25.0F,
					38 - mouseX,
					80 - mouseY,
					piglin);
		}
	}

	@Nullable
	public Piglin getPiglin() {
		CompoundTag nbt = new CompoundTag();
		nbt.putString("id", ForgeRegistries.ENTITIES.getKey(EntityType.PIGLIN).toString());
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.hasSingleplayerServer() && mc.getSingleplayerServer() != null ? mc.getSingleplayerServer().getAllLevels().iterator().next() : mc.level;
		if (level != null) {
			Piglin piglin = (Piglin) EntityType.loadEntityRecursive(nbt, level, Function.identity());
			if (piglin != null) {
				return piglin;
			}
		}
		return null;
	}
}
