package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.BrewerModel;
import com.Polarice3.FireNBlood.client.render.layers.BrewerHeldItemLayer;
import com.Polarice3.FireNBlood.entities.neutral.protectors.BrewerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BrewerRenderer extends MobRenderer<BrewerEntity, BrewerModel<BrewerEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/brewer.png");

    public BrewerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BrewerModel<>(0.0F), 0.5F);
        this.addLayer(new BrewerHeldItemLayer<>(this));
    }

    public void render(BrewerEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        this.model.setHoldingItem(!entityIn.getMainHandItem().isEmpty());
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(BrewerEntity entity) {
        return TEXTURE;
    }

    protected void scale(BrewerEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }

}
