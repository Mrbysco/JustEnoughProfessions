package com.mrbysco.justenoughprofessions.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CompatibilityHelper {
	public static ItemStack compatibilityCheck(ItemStack stack, @Nullable ResourceLocation profession) {
//		if (profession != null) { TODO: Re-implement if IE is back
//			if (profession.equals(new ResourceLocation("immersiveengineering", "outfitter"))) {
//				CompoundTag tag = stack.hasTag() ? stack.getTag() : new CompoundTag();
//				tag.putBoolean("JEP_outfitter", true);
//				stack.setTag(tag);
//				return stack;
//			}
//		}
		return stack;
	}
}