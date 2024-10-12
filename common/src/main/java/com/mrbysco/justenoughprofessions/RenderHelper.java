package com.mrbysco.justenoughprofessions;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.npc.Villager;
import org.joml.Quaternionf;

public class RenderHelper {
	/**
	 * Render the Villager entity on the screen
	 * @param guiGraphics The GuiGraphics instance
	 * @param x The x position
	 * @param y The y position
	 * @param scale The scale of the entity
	 * @param yaw The yaw of the entity
	 * @param pitch The pitch of the entity
	 * @param villager The Villager entity to render
	 */
	public static void renderVillager(GuiGraphics guiGraphics, int x, int y, double scale, double yaw, double pitch, Villager villager) {
		if (villager.level() == null) return;
		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		poseStack.translate((float) x, (float) y, 50f);
		poseStack.scale((float) scale, (float) scale, (float) scale);
		poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		// Rotate entity
		poseStack.mulPose(Axis.XP.rotationDegrees(((float) Math.atan((-40 / 40.0F))) * 10.0F));

		villager.yBodyRot = (float) -(yaw / 40.F) * 20.0F;
		villager.setYRot((float) -(yaw / 40.F) * 20.0F);
		villager.yHeadRot = villager.getYRot();
		villager.yHeadRotO = villager.getYRot();
		villager.setXRot((float) -(pitch / 5.F));

		poseStack.translate(0.0F, villager.getVehicleAttachmentPoint(villager).y(), 0.0F);
		EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		entityRenderDispatcher.overrideCameraOrientation(new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F));
		entityRenderDispatcher.setRenderShadow(false);
		final MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
		RenderSystem.runAsFancy(() -> {
			entityRenderDispatcher.render(villager, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, poseStack, bufferSource, 15728880);
		});
		bufferSource.endBatch();
		entityRenderDispatcher.setRenderShadow(true);
		poseStack.popPose();
	}
}
