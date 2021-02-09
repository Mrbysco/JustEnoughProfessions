package com.mrbysco.justenoughprofessions;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class RenderHelper {
    public static void renderEntity(MatrixStack matrixStack, int x, int y, double scale, double yaw, double pitch, LivingEntity livingEntity) {
        if (livingEntity.world == null) livingEntity.world = Minecraft.getInstance().world;
        matrixStack.push();
        matrixStack.translate((float)x, (float)y, 50f);
        matrixStack.scale((float) scale, (float) scale, (float) scale);
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(180.0F));
        // Rotate entity
        matrixStack.rotate(Vector3f.XP.rotationDegrees(((float) Math.atan((-40 / 40.0F))) * 10.0F));

        livingEntity.renderYawOffset = (float) -(yaw/40.F) * 20.0F;
        livingEntity.rotationYaw = (float) -(yaw/40.F) * 20.0F;
        livingEntity.rotationYawHead = livingEntity.rotationYaw;
        livingEntity.prevRotationYawHead = livingEntity.rotationYaw;

        matrixStack.translate(0.0F, livingEntity.getYOffset(), 0.0F);
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        entityrenderermanager.setCameraOrientation(Quaternion.ONE);
        entityrenderermanager.setRenderShadow(false);
        final IRenderTypeBuffer.Impl renderTypeBuffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.renderEntityStatic(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack, renderTypeBuffer, 15728880);
        });
        renderTypeBuffer.finish();
        entityrenderermanager.setRenderShadow(true);
        matrixStack.pop();
    }
}
