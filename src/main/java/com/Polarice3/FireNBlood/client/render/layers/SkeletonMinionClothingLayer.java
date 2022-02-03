package com.Polarice3.FireNBlood.client.render.layers;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.ZombieMinionModel;
import com.Polarice3.FireNBlood.entities.ally.SkeletonMinionEntity;
import com.Polarice3.FireNBlood.entities.ally.ZombieMinionEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

public class SkeletonMinionClothingLayer <T extends MobEntity & IRangedAttackMob, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/skeletonminion_overlay.png");
    private final SkeletonModel<T> layerModel = new SkeletonModel<>(0.25F, true);

    public SkeletonMinionClothingLayer(IEntityRenderer<T, M> p_i50919_1_) {
        super(p_i50919_1_);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, TEXTURES, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 1.0F, 1.0F, 1.0F);

    }
}
