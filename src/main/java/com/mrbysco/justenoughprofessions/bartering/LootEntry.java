package com.mrbysco.justenoughprofessions.bartering;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class LootEntry {
	private final float chance;
	private ItemStack output;
	private final List<ItemStack> outputs = new ArrayList<>();
	private int min = 0;
	private int max = 0;

	public LootEntry(float chance, ItemStack output) {
		this.chance = chance;
		this.output = output;
	}

	public float chance() {
		return chance;
	}

	public ItemStack output() {
		return output.copy();
	}

	public List<ItemStack> getOutputs() {
		return outputs.isEmpty() ? List.of(output()) : outputs;
	}

	public void addOutput(ItemStack output) {
		outputs.add(output);
	}

	public void setOutput(ItemStack output) {
		this.output = output;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int min() {
		return min;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int max() {
		return max;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (LootEntry) obj;
		return Float.floatToIntBits(this.chance) == Float.floatToIntBits(that.chance) &&
				Objects.equals(this.output, that.output) &&
				this.min == that.min &&
				this.max == that.max;
	}

	@Override
	public int hashCode() {
		return Objects.hash(chance, output, min, max);
	}

	@Override
	public String toString() {
		return "LootEntry[" +
				"chance=" + chance + ", " +
				"output=" + output + ", " +
				"min=" + min + ", " +
				"max=" + max + ']';
	}


}
