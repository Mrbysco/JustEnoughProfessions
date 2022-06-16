package com.mrbysco.justenoughprofessions.compat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class CompatibilityHelper {
	public static ItemStack compatibilityCheck(ItemStack stack, @Nullable ResourceLocation profession) {
		if (profession != null) {
			if (profession.equals(new ResourceLocation("immersiveengineering", "outfitter"))) {
				CompoundTag tag = stack.hasTag() ? stack.getTag() : new CompoundTag();
				tag.putBoolean("JEP_outfitter", true);
				stack.setTag(tag);
				return stack;
			}
		}
		return stack;
	}
}