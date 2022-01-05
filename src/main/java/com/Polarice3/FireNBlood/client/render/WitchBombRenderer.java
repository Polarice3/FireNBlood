package com.Polarice3.FireNBlood.client.render;


import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.projectiles.SoulFireballEntity;
import com.Polarice3.FireNBlood.entities.projectiles.WitchBombEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WitchBombRenderer extends EntityRenderer<WitchBombEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID,"textures/entity/witchbomb.png");
    private static final RenderType field_229044_e_ = RenderType.getEntityCutoutNoCull(TEXTURE);

    public WitchBombRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    protected int getBlockLight(WitchBombEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    public void render(WitchBombEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.ticksExisted >= 2 || !(this.renderManager.info.getRenderViewEntity().getDistanceSq(entityIn) < 12.25D)) {
            matrixStackIn.push();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.rotate(this.renderManager.getCameraOrientation());
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
            MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
            Matrix4f matrix4f = matrixstack$entry.getMatrix();
            Matrix3f matrix3f = matrixstack$entry.getNormal();
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(field_229044_e_);
            func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 0, 0, 1);
            func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 0, 1, 1);
            func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 1, 1, 0);
            func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 1, 0, 0);
            matrixStackIn.pop();
        }
    }

    private static void func_229045_a_(IVertexBuilder p_229045_0_, Matrix4f p_229045_1_, Matrix3f p_229045_2_, int p_229045_3_, float p_229045_4_, int p_229045_5_, int p_229045_6_, int p_229045_7_) {
        p_229045_0_.pos(p_229045_1_, p_229045_4_ - 0.5F, (float)p_229045_5_ - 0.25F, 0.0F).color(255, 255, 255, 255).tex((float)p_229045_6_, (float)p_229045_7_).overlay(OverlayTexture.NO_OVERLAY).lightmap(p_229045_3_).normal(p_229045_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getEntityTexture(WitchBombEntity entity) {
        return TEXTURE;
    }
}
