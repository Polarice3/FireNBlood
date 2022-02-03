package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.VizierModel;
import com.Polarice3.FireNBlood.client.render.layers.VizierAuraLayer;
import com.Polarice3.FireNBlood.entities.bosses.VizierEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class VizierRenderer extends MobRenderer<VizierEntity, VizierModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/vizier.png");

    public VizierRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new VizierModel(0.0F, 0.0F), 1.0F);
        this.addLayer(new VizierAuraLayer(this));
        this.addLayer(new HeldItemLayer<VizierEntity, VizierModel>(this) {
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, VizierEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entitylivingbaseIn.isCharging()) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }

            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(VizierEntity entity) {
        return TEXTURE;
    }
}
