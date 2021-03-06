package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.MinionModel;
import com.Polarice3.FireNBlood.entities.ally.FriendlyScorchEntity;
import com.Polarice3.FireNBlood.entities.hostile.ScorchEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class ScorchRenderer extends BipedRenderer<ScorchEntity, MinionModel<ScorchEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/scorch.png");
    protected static final ResourceLocation CHARGING_TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/scorch_charging.png");

    public ScorchRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new MinionModel<>(), 0.3F);
    }

    protected int getBlockLightLevel(ScorchEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    public ResourceLocation getTextureLocation(ScorchEntity entity) {
        return entity.isCharging() ? CHARGING_TEXTURE : TEXTURE;
    }

    protected void scale(ScorchEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.4F, 0.4F, 0.4F);
    }
}
