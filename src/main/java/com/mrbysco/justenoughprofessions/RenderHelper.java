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
        if (livingEntity.level == null) livingEntity.level = Minecraft.getInstance().level;
        matrixStack.pushPose();
        matrixStack.translate((float)x, (float)y, 50f);
        matrixStack.scale((float) scale, (float) scale, (float) scale);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        // Rotate entity
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(((float) Math.atan((-40 / 40.0F))) * 10.0F));

        livingEntity.yBodyRot = (float) -(yaw/40.F) * 20.0F;
        livingEntity.yRot = (float) -(yaw/40.F) * 20.0F;
        livingEntity.yHeadRot = livingEntity.yRot;
        livingEntity.yHeadRotO = livingEntity.yRot;

        matrixStack.translate(0.0F, livingEntity.getMyRidingOffset(), 0.0F);
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        entityrenderermanager.overrideCameraOrientation(Quaternion.ONE);
        entityrenderermanager.setRenderShadow(false);
        final IRenderTypeBuffer.Impl renderTypeBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack, renderTypeBuffer, 15728880);
        });
        renderTypeBuffer.endBatch();
        entityrenderermanager.setRenderShadow(true);
        matrixStack.popPose();
    }
}
