package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.MutatedChickenModel;
import com.Polarice3.FireNBlood.client.model.MutatedCowModel;
import com.Polarice3.FireNBlood.entities.neutral.MutatedChickenEntity;
import com.Polarice3.FireNBlood.entities.neutral.MutatedCowEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class MutatedChickenRenderer extends MobRenderer<MutatedChickenEntity, MutatedChickenModel<MutatedChickenEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/mutatedchicken.png");

    public MutatedChickenRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new MutatedChickenModel<>(), 0.5F);
    }

    protected void scale(MutatedChickenEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entitylivingbaseIn.getMutatedFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        matrixStackIn.scale(f2, f3, f2);
    }

    protected float getWhiteOverlayProgress(MutatedChickenEntity livingEntityIn, float partialTicks) {
        float f = livingEntityIn.getMutatedFlashIntensity(partialTicks);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(MutatedChickenEntity entity) {
        return TEXTURE;
    }
}
