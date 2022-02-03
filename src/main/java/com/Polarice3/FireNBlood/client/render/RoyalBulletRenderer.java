package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.RoyalBulletModel;
import com.Polarice3.FireNBlood.entities.hostile.tailless.RoyalBulletEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class RoyalBulletRenderer extends MobRenderer<RoyalBulletEntity, RoyalBulletModel<RoyalBulletEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/royalbullet.png");

    public RoyalBulletRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new RoyalBulletModel<>(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(RoyalBulletEntity entity) {
        return TEXTURE;
    }

    protected void setupRotations(RoyalBulletEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float yRot, float partialTicks) {
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, yRot, partialTicks);
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(entityLiving.xRot));
    }

}
