package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.TaillessHorrorModel;
import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessHorrorEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class TaillessHorrorRenderer extends MobRenderer<TaillessHorrorEntity, TaillessHorrorModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/taillesshorror.png");

    public TaillessHorrorRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new TaillessHorrorModel(), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(TaillessHorrorEntity entity) {
        return TEXTURE;
    }

    protected void setupRotations(TaillessHorrorEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float yRot, float partialTicks) {
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, yRot, partialTicks);
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(entityLiving.xRot));
    }
}
