package com.mrbysco.justenoughprofessions.profession;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

public class AbstractProfession {
	private final VillagerProfession profession;

	public AbstractProfession(VillagerProfession profession) {
		this.profession = profession;
	}

	public VillagerProfession profession() {
		return profession;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (AbstractProfession) obj;
		return Objects.equals(this.profession, that.profession);
	}

	@Override
	public int hashCode() {
		return Objects.hash(profession);
	}

	@Override
	public String toString() {
		return "AbstractProfession[" +
				"profession=" + profession + ']';
	}

	@Nullable
	public Villager getVillagerEntity() {
		CompoundTag nbt = new CompoundTag();
		nbt.putString("id", ForgeRegistries.ENTITIES.getKey(EntityType.VILLAGER).toString());
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
