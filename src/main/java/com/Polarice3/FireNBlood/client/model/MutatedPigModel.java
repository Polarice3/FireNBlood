package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.neutral.MutatedPigEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class MutatedPigModel<T extends MutatedPigEntity> extends EntityModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer head2;
    private final ModelRenderer head3;
    private final ModelRenderer head4;
    private final ModelRenderer head5;
    private final ModelRenderer legbackright;
    private final ModelRenderer legbackleft;
    private final ModelRenderer legfrontright;
    private final ModelRenderer legfrontleft;

    public MutatedPigModel() {
        textureWidth = 64;
        textureHeight = 64;

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 11.0F, 2.0F);
        body.setTextureOffset(0, 20).addBox(-8.0F, -10.0F, -7.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 12.0F, -3.0F);
        head.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        head.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        head2 = new ModelRenderer(this);
        head2.setRotationPoint(-3.0F, 0.0F, 3.0F);
        head.addChild(head2);
        head2.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        head2.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        head3 = new ModelRenderer(this);
        head3.setRotationPoint(3.0F, 0.0F, 3.0F);
        head.addChild(head3);
        head3.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        head3.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        head4 = new ModelRenderer(this);
        head4.setRotationPoint(0.0F, 0.0F, 6.0F);
        head.addChild(head4);
        head4.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        head4.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        head5 = new ModelRenderer(this);
        head5.setRotationPoint(0.0F, -5.0F, 3.0F);
        head.addChild(head5);
        head5.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        head5.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        legbackright = new ModelRenderer(this);
        legbackright.setRotationPoint(-3.0F, 18.0F, 7.0F);
        legbackright.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        legbackleft = new ModelRenderer(this);
        legbackleft.setRotationPoint(3.0F, 18.0F, 7.0F);
        legbackleft.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, true);

        legfrontright = new ModelRenderer(this);
        legfrontright.setRotationPoint(-3.0F, 18.0F, -5.0F);
        legfrontright.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        legfrontleft = new ModelRenderer(this);
        legfrontleft.setRotationPoint(3.0F, 18.0F, -5.0F);
        legfrontleft.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, true);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        legbackright.render(matrixStack, buffer, packedLight, packedOverlay);
        legbackleft.render(matrixStack, buffer, packedLight, packedOverlay);
        legfrontright.render(matrixStack, buffer, packedLight, packedOverlay);
        legfrontleft.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.body.rotateAngleX = ((float)Math.PI / 2F);
        this.legbackright.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.legbackleft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.legfrontright.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.legfrontleft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }
}
