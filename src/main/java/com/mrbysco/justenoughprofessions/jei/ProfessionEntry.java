package com.mrbysco.justenoughprofessions.jei;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class ProfessionEntry {
    private final VillagerProfession profession;
    private final List<ItemStack> blockStacks;

    public ProfessionEntry(VillagerProfession profession, Int2ObjectMap<ItemStack> stacks) {
        this.profession = profession;
        this.blockStacks = new LinkedList<>();
        addProfessionStacks(stacks);
    }

    public void addProfessionStacks(Int2ObjectMap<ItemStack> stackList) {
        for (int i = 0; i < stackList.size(); i++) {
            this.blockStacks.add(stackList.get(i));
        }
    }

    public VillagerProfession getProfession() {
        return profession;
    }

    public List<ItemStack> getBlockStacks() {
        return blockStacks;
    }

    @Nullable
    public Villager getVillagerEntity() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("id", ForgeRegistries.ENTITIES.getKey(EntityType.VILLAGER).toString());
        Minecraft mc = Minecraft.getInstance();
        Level world = mc.hasSingleplayerServer() && mc.getSingleplayerServer() != null ? mc.getSingleplayerServer().getAllLevels().iterator().next() : mc.level;
        if(world != null) {
            Villager villagerEntity = (Villager)EntityType.loadEntityRecursive(nbt, world, Function.identity());
            if(villagerEntity != null) {
                villagerEntity.setVillagerData(villagerEntity.getVillagerData().setProfession(this.profession));
                return villagerEntity;
            }
        }
        return null;
    }
}
