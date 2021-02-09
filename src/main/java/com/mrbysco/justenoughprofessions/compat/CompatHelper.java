package com.mrbysco.justenoughprofessions.compat;

import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

public class CompatHelper {
	public static ItemStack compatCheck(ItemStack stack, @Nullable ResourceLocation profession) {
		if(profession != null) {
			if(profession.equals(new ResourceLocation("immersiveengineering", "outfitter"))) {
				CompoundNBT tag = stack.hasTag() ? stack.getTag() : new CompoundNBT();
				tag.putBoolean("JEP_outfitter", true);
				stack.setTag(tag);
				return stack;
			}
		}
		return stack;
	}

	@SubscribeEvent
	public static void handleTooltips(ItemTooltipEvent event) {
		if(Minecraft.getInstance().currentScreen instanceof IRecipesGui) {
			ItemStack stack = event.getItemStack();
			if(stack.hasTag() && stack.getTag().getBoolean("JEP_outfitter")) {
				event.getToolTip().add(new StringTextComponent("Needs to have a shader applied").mergeStyle(TextFormatting.GOLD));
			}
		}
	}
}
