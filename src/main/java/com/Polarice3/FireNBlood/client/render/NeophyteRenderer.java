package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.NeophyteModel;
import com.Polarice3.FireNBlood.client.render.layers.NeophyteHeldItemLayer;
import com.Polarice3.FireNBlood.entities.hostile.NeophyteEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class NeophyteRenderer extends MobRenderer<NeophyteEntity, NeophyteModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/neophyte.png");

    public NeophyteRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new NeophyteModel(), 0.5F);
        this.addLayer(new NeophyteHeldItemLayer(this));
    }

    public void render(NeophyteEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        this.entityModel.func_205074_a(!entityIn.getHeldItemMainhand().isEmpty());
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(NeophyteEntity entity) {
        return TEXTURE;
    }

    protected void preRenderCallback(NeophyteEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
