package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.TaillessWretchModel;
import com.Polarice3.FireNBlood.client.render.layers.TaillessHeldItemLayer;
import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessWretchEntity;
import com.Polarice3.FireNBlood.entities.neutral.MutatedRabbitEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TaillessWretchRenderer extends MobRenderer<TaillessWretchEntity, TaillessWretchModel<TaillessWretchEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/taillesswretch.png");

    public TaillessWretchRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new TaillessWretchModel<>(), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(TaillessWretchEntity entity) {
        return TEXTURE;
    }

    protected void scale(TaillessWretchEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(2.0F, 2.0F, 2.0F);
    }
}
