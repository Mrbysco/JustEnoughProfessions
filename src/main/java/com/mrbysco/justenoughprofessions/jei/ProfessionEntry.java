package com.mrbysco.justenoughprofessions.jei;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class ProfessionEntry {
    private final VillagerProfession profession;
    private final List<ItemStack> blockStacks;
    private final List<ResourceLocation> knownItems;

    public ProfessionEntry(VillagerProfession profession, Int2ObjectMap<ItemStack> stacks) {
        this.profession = profession;
        this.blockStacks = new LinkedList<>();
        this.knownItems = new LinkedList<>();
        addProfessionStacks(stacks);
    }

    public void addProfessionStacks(Int2ObjectMap<ItemStack> stackList) {
        for (int i = 0; i < stackList.size(); i++) {
            ItemStack stack = stackList.get(i);
            ResourceLocation location = stack.getItem().getRegistryName();
            if(!stack.isEmpty() && !knownItems.contains(location)) {
                this.blockStacks.add(stack);
                this.knownItems.add(stack.getItem().getRegistryName());
            }
        }
    }

    public VillagerProfession getProfession() {
        return profession;
    }

    public List<ItemStack> getBlockStacks() {
        return blockStacks;
    }

    @Nullable
    public VillagerEntity getVillagerEntity() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("id", ForgeRegistries.ENTITIES.getKey(EntityType.VILLAGER).toString());
        Minecraft mc = Minecraft.getInstance();
        World world;
        if(mc.isSingleplayer()) {
            world = mc.getIntegratedServer().getWorlds().iterator().next();
        } else {
            world = mc.world;
        }
        if(world != null) {
            VillagerEntity villagerEntity = (VillagerEntity)EntityType.loadEntityAndExecute(nbt, world, Function.identity());
            if(villagerEntity != null) {
                villagerEntity.setVillagerData(villagerEntity.getVillagerData().withProfession(this.profession));
            }
            return villagerEntity;
        }
        return null;
    }
}
