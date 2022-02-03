package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.projectiles.EtherealPunchEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EtherealPunchRenderer extends EntityRenderer<EtherealPunchEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/etherealpunch.png");
    private final GenericHeadModel skeletonHeadModel = new GenericHeadModel();

    public EtherealPunchRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    protected int getBlockLightLevel(EtherealPunchEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    public void render(EtherealPunchEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        float f = MathHelper.rotLerp(entityIn.yRotO, entityIn.yRot, partialTicks);
        float f1 = MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.skeletonHeadModel.renderType(this.getTextureLocation(entityIn)));
        this.skeletonHeadModel.setupAnim(0.0F, f, f1);
        this.skeletonHeadModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(EtherealPunchEntity entity) {
        return TEXTURE;
    }
}
