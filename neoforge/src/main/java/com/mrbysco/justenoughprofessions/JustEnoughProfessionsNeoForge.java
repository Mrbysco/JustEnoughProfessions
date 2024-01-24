package com.mrbysco.justenoughprofessions;

import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.IExtensionPoint.DisplayTest;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@Mod(Constants.MOD_ID)
public class JustEnoughProfessionsNeoForge {

	public JustEnoughProfessionsNeoForge() {
		//Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () ->
				new IExtensionPoint.DisplayTest(() -> "Trans Rights Are Human Rights",
						(remoteVersionString, networkBool) -> networkBool));

		if (FMLEnvironment.dist.isClient()) {
			NeoForge.EVENT_BUS.addListener(this::handleTooltips);
		}
	}

	public void handleTooltips(ItemTooltipEvent event) {
		if (Minecraft.getInstance().screen instanceof IRecipesGui) {
			ItemStack stack = event.getItemStack();
			if (stack.hasTag() && stack.getTag().getBoolean("JEP_outfitter")) {
				event.getToolTip().add(Component.literal("Needs to have a shader applied").withStyle(ChatFormatting.GOLD));
			}
		}
	}
}