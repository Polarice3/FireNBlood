package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.client.model.MinionModel;
import com.Polarice3.FireNBlood.entities.ally.FriendlyVexEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class FriendlyVexRenderer extends BipedRenderer<FriendlyVexEntity, MinionModel<FriendlyVexEntity>> {
    private static final ResourceLocation VEX_TEXTURE = new ResourceLocation("textures/entity/illager/vex.png");
    private static final ResourceLocation VEX_CHARGING_TEXTURE = new ResourceLocation("textures/entity/illager/vex_charging.png");

    public FriendlyVexRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new MinionModel<>(), 0.3F);
    }

    protected int getBlockLightLevel(FriendlyVexEntity entityIn, BlockPos pos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(FriendlyVexEntity entity) {
        return entity.isCharging() ? VEX_CHARGING_TEXTURE : VEX_TEXTURE;
    }

    protected void scale(FriendlyVexEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.4F, 0.4F, 0.4F);
    }
}
