package com.mrbysco.justenoughprofessions.jei;

import com.mrbysco.justenoughprofessions.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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
		CompoundTag nbt = new CompoundTag();
		nbt.putString("id", Objects.requireNonNull(Services.PLATFORM.getEntityKey(EntityType.VILLAGER)).toString());
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.hasSingleplayerServer() && mc.getSingleplayerServer() != null ? mc.getSingleplayerServer().getAllLevels().iterator().next() : mc.level;
		if (level != null) {
			Villager villager = (Villager) EntityType.loadEntityRecursive(nbt, level, Function.identity());
			if (villager != null) {
				villager.setVillagerData(villager.getVillagerData().setProfession(this.profession));
				return villager;
			}
		}
		return null;
	}
}
