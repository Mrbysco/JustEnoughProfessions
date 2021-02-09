package com.mrbysco.justenoughprofessions;

import com.mrbysco.justenoughprofessions.compat.CompatHelper;
import com.mrbysco.justenoughprofessions.jei.ProfessionCategory;
import com.mrbysco.justenoughprofessions.jei.ProfessionEntry;
import com.mrbysco.justenoughprofessions.jei.ProfessionWrapper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
@JeiPlugin
public class ProfessionPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(JustEnoughProfessions.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ProfessionCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Items.EMERALD), ProfessionCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(Items.VILLAGER_SPAWN_EGG), ProfessionCategory.UID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ProfessionEntry> entries = new LinkedList<>();
        for(VillagerProfession profession : ForgeRegistries.PROFESSIONS) {
            List<ItemStack> stacks = new LinkedList<>();
            List<ResourceLocation> knownItems = new LinkedList<>();
            PointOfInterestType poiType = profession.getPointOfInterest();

            for(BlockState state : poiType.blockStates) {
                Block block = state.getBlock();
                if(block != null) {
                    ItemStack stack = CompatHelper.compatCheck(new ItemStack(block), profession.getRegistryName());
                    ResourceLocation location = stack.getItem().getRegistryName();
                    if(!stack.isEmpty() && !knownItems.contains(location)) {
                        stacks.add(stack);
                        knownItems.add(stack.getItem().getRegistryName());
                    }
                }
            }
            if(!stacks.isEmpty()) {
                Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
                for(int i = 0; i < stacks.size(); i++) {
                    map.put(i, stacks.get(i));
                }
                entries.add(new ProfessionEntry(profession, map));
            }
        }
        registration.addRecipes(asRecipes(entries, ProfessionWrapper::new), ProfessionCategory.UID);
    }

    private static <T, R> Collection<R> asRecipes(Collection<T> collection, Function<T, R> transformer) {
        return collection.stream().map(transformer).collect(Collectors.toList());
    }
}
