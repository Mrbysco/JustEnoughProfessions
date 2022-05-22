package com.mrbysco.justenoughprofessions.profession.workstation;

import com.mrbysco.justenoughprofessions.profession.AbstractProfession;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class WorkstationEntry extends AbstractProfession {
	private final List<ItemStack> blockStacks;

	public WorkstationEntry(VillagerProfession profession, Int2ObjectMap<ItemStack> stacks) {
		super(profession);
		this.blockStacks = new LinkedList<>();
		addProfessionStacks(stacks);
	}

	public void addProfessionStacks(Int2ObjectMap<ItemStack> stackList) {
		for (int i = 0; i < stackList.size(); i++) {
			this.blockStacks.add(stackList.get(i));
		}
	}

	public List<ItemStack> getBlockStacks() {
		return blockStacks;
	}
}
