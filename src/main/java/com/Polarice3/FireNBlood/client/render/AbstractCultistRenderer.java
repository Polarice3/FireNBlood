package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.client.model.AbstractCultistModel;
import com.Polarice3.FireNBlood.entities.hostile.cultists.AbstractCultistEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;

public abstract class AbstractCultistRenderer<T extends AbstractCultistEntity> extends MobRenderer<T, AbstractCultistModel<T>> {
    protected AbstractCultistRenderer(EntityRendererManager p_i50966_1_, AbstractCultistModel<T> p_i50966_2_, float p_i50966_3_) {
        super(p_i50966_1_, p_i50966_2_, p_i50966_3_);
        this.addLayer(new HeadLayer<>(this));
    }

    protected void scale(T entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }

}
