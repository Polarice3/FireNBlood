package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.client.model.FriendlyVexModel;
import com.Polarice3.FireNBlood.client.model.IrkModel;
import com.Polarice3.FireNBlood.entities.ally.FriendlyVexEntity;
import com.Polarice3.FireNBlood.entities.hostile.IrkEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.VexModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class FriendlyVexRenderer extends BipedRenderer<FriendlyVexEntity, FriendlyVexModel> {
    private static final ResourceLocation VEX_TEXTURE = new ResourceLocation("textures/entity/illager/vex.png");

    public FriendlyVexRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new FriendlyVexModel(), 0.3F);
    }

    protected int getBlockLight(FriendlyVexEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    public ResourceLocation getEntityTexture(FriendlyVexEntity entity) {
        return VEX_TEXTURE;
    }

    protected void preRenderCallback(FriendlyVexEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.4F, 0.4F, 0.4F);
    }
}
