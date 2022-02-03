package com.Polarice3.FireNBlood.client.render.layers;

import com.Polarice3.FireNBlood.client.model.NeophyteModel;
import com.Polarice3.FireNBlood.entities.hostile.NeophyteEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3f;

public class NeophyteHeldItemLayer extends CrossedArmsItemLayer<NeophyteEntity, NeophyteModel> {
    public NeophyteHeldItemLayer(IEntityRenderer<NeophyteEntity, NeophyteModel> p_i50916_1_) {
        super(p_i50916_1_);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, NeophyteEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entitylivingbaseIn.getMainHandItem();
        matrixStackIn.pushPose();
        if (itemstack.getItem() == Items.POTION) {
            this.getParentModel().getHead().translateAndRotate(matrixStackIn);
            this.getParentModel().getNose().translateAndRotate(matrixStackIn);
            matrixStackIn.translate(0.0625D, 0.25D, 0.0D);
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(140.0F));
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(10.0F));
            matrixStackIn.translate(0.0D, (double)-0.4F, (double)0.4F);
        }

        super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        matrixStackIn.popPose();
    }
}
