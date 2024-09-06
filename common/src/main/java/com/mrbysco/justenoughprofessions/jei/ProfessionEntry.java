package com.mrbysco.justenoughprofessions.jei;

import com.mrbysco.justenoughprofessions.VillagerCache;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A record to hold the profession and the block stacks for the profession
 * @param profession The profession
 * @param blockStacks The workstation blocks for the profession
 */
public record ProfessionEntry(VillagerProfession profession, List<ItemStack> blockStacks) {

	/**
	 * Get the villager entity for the profession
	 * @return The Villager entity
	 */
	@Nullable
	public Villager getVillagerEntity() {
		return VillagerCache.getVillagerEntity(this.profession);
	}
}
