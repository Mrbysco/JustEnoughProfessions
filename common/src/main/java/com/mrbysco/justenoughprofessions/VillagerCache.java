package com.mrbysco.justenoughprofessions;

import com.mrbysco.justenoughprofessions.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

/**
 * This class is used to cache the villager entity used for rendering the villager profession
 */
public class VillagerCache {
	/**
	 * The cached villager entity
	 */
	private static Villager cachedVillager;

	/**
	 * Get a villager entity with a specific profession
	 * If the entity is not cached, it will create a new one and cache it
	 * @param profession The profession to set the villager to
	 * @return The villager entity
	 */
	@Nullable
	public static Villager getVillagerEntity(VillagerProfession profession) {
		if (cachedVillager == null) {
			CompoundTag nbt = new CompoundTag();
			nbt.putString("id", Objects.requireNonNull(Services.PLATFORM.getEntityKey(EntityType.VILLAGER)).toString());
			ClientLevel level = Minecraft.getInstance().level;
			if (level != null) {
				Villager villager = (Villager) EntityType.loadEntityRecursive(nbt, level, Function.identity());
				if (villager != null) {
					cachedVillager = villager;
				}
			}
		} else {
			cachedVillager.setVillagerData(cachedVillager.getVillagerData().setProfession(profession));
			return cachedVillager;
		}

		return null;
	}

}
