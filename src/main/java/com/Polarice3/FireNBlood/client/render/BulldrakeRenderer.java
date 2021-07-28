package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.BulldrakeModel;
import com.Polarice3.FireNBlood.entities.hostile.TaillessHorrorEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class BulldrakeRenderer extends MobRenderer<TaillessHorrorEntity, BulldrakeModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/taillesshorror.png");

    public BulldrakeRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BulldrakeModel(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(TaillessHorrorEntity entity) {
        return TEXTURE;
    }

    protected void applyRotations(TaillessHorrorEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(entityLiving.rotationPitch));
    }
}
