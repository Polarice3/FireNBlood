package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.APArmorModel;
import com.Polarice3.FireNBlood.client.model.AbstractProtectorModel;
import com.Polarice3.FireNBlood.client.render.layers.ProtectorLoyalLayer;
import com.Polarice3.FireNBlood.entities.neutral.RedemptorEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class RedemptorRenderer extends AbstractProtectorRenderer<RedemptorEntity>{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/redemptor.png");

    public RedemptorRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new AbstractProtectorModel<>(0.0F, 0.5F), 0.5F);
        this.addLayer(new HeldItemLayer<RedemptorEntity, AbstractProtectorModel<RedemptorEntity>>(this) {
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, RedemptorEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entitylivingbaseIn.isAggressive()) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }

            }
        });
        this.addLayer(new BipedArmorLayer<>(this, new APArmorModel<>(0.5F), new APArmorModel<>(1.0F)));
        this.addLayer(new ProtectorLoyalLayer<>(this));
    }

    @Override
    public ResourceLocation getEntityTexture(RedemptorEntity entity) {
        return TEXTURE;
    }
}
