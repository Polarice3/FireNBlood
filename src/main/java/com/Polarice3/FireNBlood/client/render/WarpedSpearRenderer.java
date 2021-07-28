package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.WarpedSpearModel;
import com.Polarice3.FireNBlood.entities.projectiles.WarpedSpearEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.TridentModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class WarpedSpearRenderer extends EntityRenderer<WarpedSpearEntity> {
    public static final ResourceLocation SPEAR = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/warped_spear.png");
    private final WarpedSpearModel spearModel = new WarpedSpearModel();

    public WarpedSpearRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(WarpedSpearEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw) - 90.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch) + 90.0F));
        IVertexBuilder ivertexbuilder = net.minecraft.client.renderer.ItemRenderer.getEntityGlintVertexBuilder(bufferIn, this.spearModel.getRenderType(this.getEntityTexture(entityIn)), false, entityIn.func_226572_w_());
        this.spearModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(WarpedSpearEntity entity) {
        return SPEAR;
    }
}
