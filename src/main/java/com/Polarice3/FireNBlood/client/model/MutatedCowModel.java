package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.neutral.MutatedCowEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class MutatedCowModel<T extends MutatedCowEntity> extends EntityModel<T> {
        private final ModelRenderer body;
        private final ModelRenderer head;
        private final ModelRenderer head2;
        private final ModelRenderer leg0;
        private final ModelRenderer leg1;
        private final ModelRenderer leg2;
        private final ModelRenderer leg3;

        public MutatedCowModel() {
            textureWidth = 64;
            textureHeight = 64;

            body = new ModelRenderer(this);
            body.setRotationPoint(0.0F, 5.0F, 2.0F);
            body.setTextureOffset(0, 30).addBox(-8.0F, -10.0F, -7.0F, 16.0F, 18.0F, 16.0F, 0.0F, false);
            body.setTextureOffset(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F, 0.0F, false);

            head = new ModelRenderer(this);
            head.setRotationPoint(0.0F, 7.0F, -8.0F);
            head.setTextureOffset(0, 0).addBox(0.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F, 0.0F, false);
            head.setTextureOffset(22, 0).addBox(-1.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            head.setTextureOffset(22, 0).addBox(8.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);

            head2 = new ModelRenderer(this);
            head2.setRotationPoint(-6.0F, 0.0F, 0.0F);
            head.addChild(head2);
            head2.setTextureOffset(28, 0).addBox(-2.0F, -4.0F, -6.0F, 6.0F, 8.0F, 6.0F, 0.0F, false);
            head2.setTextureOffset(50, 0).addBox(-2.0F, -5.0F, -4.0F, -1.0F, 3.0F, 1.0F, 0.0F, false);
            head2.setTextureOffset(50, 0).addBox(5.0F, -5.0F, -4.0F, -1.0F, 3.0F, 1.0F, 0.0F, false);

            leg0 = new ModelRenderer(this);
            leg0.setRotationPoint(-4.0F, 12.0F, 7.0F);
            leg0.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

            leg1 = new ModelRenderer(this);
            leg1.setRotationPoint(4.0F, 12.0F, 7.0F);
            leg1.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

            leg2 = new ModelRenderer(this);
            leg2.setRotationPoint(-4.0F, 12.0F, -6.0F);
            leg2.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

            leg3 = new ModelRenderer(this);
            leg3.setRotationPoint(4.0F, 12.0F, -6.0F);
            leg3.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
        }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        leg0.render(matrixStack, buffer, packedLight, packedOverlay);
        leg1.render(matrixStack, buffer, packedLight, packedOverlay);
        leg2.render(matrixStack, buffer, packedLight, packedOverlay);
        leg3.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.body.rotateAngleX = ((float)Math.PI / 2F);
        this.leg0.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }
}
