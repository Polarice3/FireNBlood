package com.Polarice3.FireNBlood.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class RobeModel<T extends LivingEntity> extends BipedModel<T> {
    private final ModelRenderer darkrobe;
    public final ModelRenderer Headwear;
    public final ModelRenderer Helmet_r1;
    public final ModelRenderer Helmet_r2;
    public final ModelRenderer Mask;
    public final ModelRenderer Body;
    public final ModelRenderer RightArm;
    public final ModelRenderer Pauldron;
    public final ModelRenderer LeftArm;
    public final ModelRenderer Pauldron2;
    private final ModelRenderer bipedRightLeg;
    private final ModelRenderer bipedLeftLeg;

    public RobeModel(float modelSize) {
        super(modelSize, 0, 64, 128);
        textureWidth = 64;
        textureHeight = 128;

        darkrobe = new ModelRenderer(this);
        darkrobe.setRotationPoint(0.0F, 0.0F, 0.0F);

        Headwear = new ModelRenderer(this);
        Headwear.setRotationPoint(0.0F, -4.0F, 0.0F);
        darkrobe.addChild(Headwear);
        Headwear.setTextureOffset(0, 32).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

        Helmet_r1 = new ModelRenderer(this);
        Helmet_r1.setRotationPoint(0.0F, 0.8995F, 7.1213F);
        Headwear.addChild(Helmet_r1);
        setRotationAngle(Helmet_r1, -0.7854F, 0.0F, 0.0F);
        Helmet_r1.setTextureOffset(40, 32).addBox(-2.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, 1.0F, false);

        Helmet_r2 = new ModelRenderer(this);
        Helmet_r2.setRotationPoint(0.0F, -0.2629F, 5.3561F);
        Headwear.addChild(Helmet_r2);
        setRotationAngle(Helmet_r2, -0.3927F, 0.0F, 0.0F);
        Helmet_r2.setTextureOffset(24, 32).addBox(-3.0F, -2.5F, -1.0F, 6.0F, 5.0F, 2.0F, 1.0F, false);

        Mask = new ModelRenderer(this);
        Mask.setRotationPoint(0.0F, 0.0F, 0.0F);
        Headwear.addChild(Mask);
        Mask.setTextureOffset(0, 76).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, 13.0F, 0.0F);
        darkrobe.addChild(Body);
        Body.setTextureOffset(16, 48).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 24.0F, 4.0F, 1.01F, false);
        Body.setTextureOffset(0, 92).addBox(-5.0F, -13.0F, -4.0F, 10.0F, 6.0F, 8.0F, 0.0F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setRotationPoint(-1.5F, 5.0F, 0.0F);
        darkrobe.addChild(RightArm);
        RightArm.setTextureOffset(40, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        Pauldron = new ModelRenderer(this);
        Pauldron.setRotationPoint(-2.0F, -5.0F, 0.0F);
        RightArm.addChild(Pauldron);
        Pauldron.setTextureOffset(44, 64).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 6.0F, 1.0F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setRotationPoint(1.5F, 5.0F, 0.0F);
        darkrobe.addChild(LeftArm);
        LeftArm.setTextureOffset(40, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

        Pauldron2 = new ModelRenderer(this);
        Pauldron2.setRotationPoint(2.0F, -5.0F, 0.0F);
        LeftArm.addChild(Pauldron2);
        Pauldron2.setTextureOffset(36, 35).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 6.0F, 1.0F, false);

        bipedRightLeg = new ModelRenderer(this);
        bipedRightLeg.setRotationPoint(-1.9F, -7.0F, 0.0F);
        darkrobe.addChild(bipedRightLeg);
        bipedRightLeg.setTextureOffset(0, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        bipedLeftLeg = new ModelRenderer(this);
        bipedLeftLeg.setRotationPoint(1.9F, -7.0F, 0.0F);
        darkrobe.addChild(bipedLeftLeg);
        bipedLeftLeg.setTextureOffset(0, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

        this.bipedHead.addChild(Headwear);
        this.bipedBody.addChild(Body);
        this.bipedLeftArm.addChild(LeftArm);
        this.bipedRightArm.addChild(RightArm);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.Headwear.copyModelAngles(this.bipedHead);
        this.Body.copyModelAngles(this.bipedBody);
        this.RightArm.copyModelAngles(this.bipedRightArm);
        this.LeftArm.copyModelAngles(this.bipedLeftArm);
    }


    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        super.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
