package com.mrbysco.justenoughprofessions.jei;

import com.mrbysco.justenoughprofessions.VillagerCache;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A record to hold the professionHolder and the block stacks for the professionHolder
 * @param professionHolder The professionHolder
 * @param blockStacks The workstation blocks for the professionHolder
 */
public record ProfessionEntry(Holder<VillagerProfession> professionHolder, List<ItemStack> blockStacks) {

	/**
	 * Get the villager entity for the professionHolder
	 * @return The Villager entity
	 */
	@Nullable
	public Villager getVillagerEntity() {
		return VillagerCache.getVillagerEntity(this.professionHolder);
	}
}
