package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.TaillessDruidModel;
import com.Polarice3.FireNBlood.client.render.layers.BullmanDruidAuraLayer;
import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessDruidEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessWretchEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class TaillessDruidRenderer extends MobRenderer<TaillessDruidEntity, TaillessDruidModel<TaillessDruidEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/taillessdruid.png");


    public TaillessDruidRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new TaillessDruidModel<>(0.0F), 1.0F);
        this.addLayer(new BullmanDruidAuraLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(TaillessDruidEntity entity) {
        return TEXTURE;
    }

    protected void scale(TaillessDruidEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(2.0F, 2.0F, 2.0F);
    }
}
