package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.neutral.QuellEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class QuellModel extends EntityModel<QuellEntity> {
    private final ModelRenderer Head;
    private final ModelRenderer Body;
    private final ModelRenderer RightArm;
    private final ModelRenderer RightArm2;
    private final ModelRenderer LeftArm;
    private final ModelRenderer LeftArm2;
    private final ModelRenderer RightLeg;

    public QuellModel() {
        textureWidth = 64;
        textureHeight = 64;

        Head = new ModelRenderer(this);
        Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.setTextureOffset(0, 17).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        Head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);

        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, 7.0F, 0.0F);
        Body.setTextureOffset(29, 30).addBox(-6.0F, -7.0F, -2.0F, 12.0F, 8.0F, 4.0F, 0.0F, false);
        Body.setTextureOffset(32, 0).addBox(-6.0F, -7.0F, -2.0F, 12.0F, 8.0F, 4.0F, 0.5F, false);
        Body.setTextureOffset(29, 13).addBox(-5.0F, 1.0F, -2.0F, 10.0F, 4.0F, 4.0F, 0.0F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setRotationPoint(-7.0F, 2.0F, 0.0F);
        RightArm.setTextureOffset(17, 52).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
        RightArm.setTextureOffset(0, 47).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.25F, false);

        RightArm2 = new ModelRenderer(this);
        RightArm2.setRotationPoint(-1.0F, 10.0F, -2.0F);
        RightArm.addChild(RightArm2);
        setRotationAngle(RightArm2, -0.6109F, 0.0F, 0.0F);
        RightArm2.setTextureOffset(0, 34).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setRotationPoint(7.0F, 2.0F, 0.0F);
        LeftArm.setTextureOffset(17, 52).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
        LeftArm.setTextureOffset(0, 47).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.25F, false);

        LeftArm2 = new ModelRenderer(this);
        LeftArm2.setRotationPoint(1.0F, 10.0F, -2.0F);
        LeftArm.addChild(LeftArm2);
        setRotationAngle(LeftArm2, -0.6109F, 0.0F, 0.0F);
        LeftArm2.setTextureOffset(0, 34).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

        RightLeg = new ModelRenderer(this);
        RightLeg.setRotationPoint(0.1F, 14.0F, 0.0F);
        RightLeg.setTextureOffset(34, 43).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 4.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(QuellEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = 2.0F;

        if (f < 2.0F) {
            f = 2.0F;
        }
        this.Head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.Head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.RightArm.rotateAngleX = (float)((double)this.RightArm.rotateAngleX * 0.5D);
        this.LeftArm.rotateAngleX = (float)((double)this.LeftArm.rotateAngleX * 0.5D);
        this.RightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
        this.LeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Head.render(matrixStack, buffer, packedLight, packedOverlay);
        Body.render(matrixStack, buffer, packedLight, packedOverlay);
        RightArm.render(matrixStack, buffer, packedLight, packedOverlay);
        LeftArm.render(matrixStack, buffer, packedLight, packedOverlay);
        RightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

}
