package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.BulletModel;
import com.Polarice3.FireNBlood.entities.hostile.BulletEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class BulletRenderer extends MobRenderer<BulletEntity, BulletModel<BulletEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/bullet.png");

    public BulletRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BulletModel<>(), 0.5F);
    }

    @Override
    public ResourceLocation getEntityTexture(BulletEntity entity) {
        return TEXTURE;
    }

    protected void applyRotations(BulletEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(entityLiving.rotationPitch));
    }

}
