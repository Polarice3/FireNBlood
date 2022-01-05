package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.AcolyteModel;
import com.Polarice3.FireNBlood.client.render.layers.AcolyteHeldItemLayer;
import com.Polarice3.FireNBlood.entities.neutral.AcolyteEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class AcolyteRenderer extends MobRenderer<AcolyteEntity, AcolyteModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/acolyte.png");

    public AcolyteRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new AcolyteModel(), 0.5F);
        this.addLayer(new AcolyteHeldItemLayer(this));
    }

    public void render(AcolyteEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        this.entityModel.func_205074_a(!entityIn.getHeldItemMainhand().isEmpty());
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(AcolyteEntity entity) {
        return TEXTURE;
    }

    protected void preRenderCallback(AcolyteEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
