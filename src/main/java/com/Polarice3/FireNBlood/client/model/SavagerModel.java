package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.neutral.protectors.SavagerEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class SavagerModel extends SegmentedModel<SavagerEntity> {
    private final ModelRenderer Savager;
    private final ModelRenderer body;
    private final ModelRenderer body2;
    private final ModelRenderer neck;
    private final ModelRenderer head;
    private final ModelRenderer mouth;
    private final ModelRenderer horns;
    private final ModelRenderer RightLeg;
    private final ModelRenderer LeftLeg;
    private final ModelRenderer RightArm;
    private final ModelRenderer LRA;
    private final ModelRenderer LeftArm;
    private final ModelRenderer LLA;

    public SavagerModel(float scaleFactor, float p_i47227_2_) {
        textureWidth = 128;
        textureHeight = 128;

        Savager = new ModelRenderer(this);
        Savager.setRotationPoint(0.0F, 24.0F + p_i47227_2_, 0.0F);


        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, -21.0F + p_i47227_2_, 10.0F);
        Savager.addChild(body);
        setRotationAngle(body, 0.9599F, 0.0F, 0.0F);
        body.setTextureOffset(0, 32).addBox(-8.0F, -14.0F, 0.0F, 16.0F, 20.0F, 10.0F, scaleFactor, false);

        body2 = new ModelRenderer(this);
        body2.setRotationPoint(0.0F, 0.0F + p_i47227_2_, 4.0F);
        body.addChild(body2);
        setRotationAngle(body2, 0.6109F, 0.0F, 0.0F);
        body2.setTextureOffset(0, 0).addBox(-12.0F, -23.0F, 0.0F, 24.0F, 16.0F, 16.0F, scaleFactor, false);

        neck = new ModelRenderer(this);
        neck.setRotationPoint(0.0F, -32.0F + p_i47227_2_, -11.0F);
        Savager.addChild(neck);
        setRotationAngle(neck, 0.0F, 0.0436F, 0.0F);
        neck.setTextureOffset(71, 102).addBox(-5.0F, -6.0F, -1.0F, 10.0F, 10.0F, 16.0F, scaleFactor, false);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 0.0F + p_i47227_2_, 0.0F);
        neck.addChild(head);
        head.setTextureOffset(0, 94).addBox(-8.0F, -10.0F, -16.0F, 16.0F, 18.0F, 16.0F, scaleFactor, false);
        head.setTextureOffset(0, 94).addBox(-2.0F, 2.0F, -20.0F, 4.0F, 8.0F, 4.0F, scaleFactor, false);

        mouth = new ModelRenderer(this);
        mouth.setRotationPoint(0.0F, 8.0F + p_i47227_2_, 0.0F);
        head.addChild(mouth);
        mouth.setTextureOffset(0, 74).addBox(-8.0F, -2.0F, -16.0F, 16.0F, 3.0F, 16.0F, scaleFactor, false);

        horns = new ModelRenderer(this);
        horns.setRotationPoint(-5.0F, 8.0F + p_i47227_2_, -11.0F);
        head.addChild(horns);
        setRotationAngle(horns, 1.0472F, 0.0F, 0.0F);
        horns.setTextureOffset(64, 35).addBox(-5.0F, -19.0F, 10.8564F, 2.0F, 14.0F, 4.0F, scaleFactor, false);
        horns.setTextureOffset(64, 35).addBox(13.0F, -19.0F, 10.8564F, 2.0F, 14.0F, 4.0F, scaleFactor, false);

        RightLeg = new ModelRenderer(this);
        RightLeg.setRotationPoint(-8.0F, -27.0F + p_i47227_2_, 17.0F);
        Savager.addChild(RightLeg);
        RightLeg.setTextureOffset(81, 54).addBox(-8.0F, -10.0F, -5.0F, 8.0F, 37.0F, 8.0F, scaleFactor, false);

        LeftLeg = new ModelRenderer(this);
        LeftLeg.setRotationPoint(8.0F, -27.0F + p_i47227_2_, 17.0F);
        Savager.addChild(LeftLeg);
        LeftLeg.setTextureOffset(81, 54).addBox(0.0F, -10.0F, -5.0F, 8.0F, 37.0F, 8.0F, scaleFactor, false);

        RightArm = new ModelRenderer(this);
        RightArm.setRotationPoint(-11.0F, -33.0F, -7.0F);
        Savager.addChild(RightArm);
        setRotationAngle(RightArm, 0.5236F, 0.0F, 0.0F);
        RightArm.setTextureOffset(80, 0).addBox(-9.0F, -2.9019F, -3.9019F, 8.0F, 17.0F, 8.0F, 0.0F, false);

        LRA = new ModelRenderer(this);
        LRA.setRotationPoint(-5.0F, 16.0981F, 0.0981F);
        RightArm.addChild(LRA);
        setRotationAngle(LRA, -0.7854F, 0.0F, 0.0F);
        LRA.setTextureOffset(81, 26).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 20.0F, 8.0F, 0.0F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setRotationPoint(11.0F, -33.0F, -7.0F);
        Savager.addChild(LeftArm);
        setRotationAngle(LeftArm, 0.5236F, 0.0F, 0.0F);
        LeftArm.setTextureOffset(80, 0).addBox(1.0F, -2.9019F, -3.9019F, 8.0F, 17.0F, 8.0F, 0.0F, false);

        LLA = new ModelRenderer(this);
        LLA.setRotationPoint(5.0F, 16.0981F, 0.0981F);
        LeftArm.addChild(LLA);
        setRotationAngle(LLA, -0.7854F, 0.0F, 0.0F);
        LLA.setTextureOffset(81, 26).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 20.0F, 8.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(SavagerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = 2.0F;

        if (f < 2.0F) {
            f = 2.0F;
        }
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        if (this.RightLeg.rotateAngleX > 0.4F) {
            this.RightLeg.rotateAngleX = 0.4F;
        }

        if (this.LeftLeg.rotateAngleX > 0.4F) {
            this.LeftLeg.rotateAngleX = 0.4F;
        }

        if (this.RightLeg.rotateAngleX < -0.4F) {
            this.RightLeg.rotateAngleX = -0.4F;
        }

        if (this.LeftLeg.rotateAngleX < -0.4F) {
            this.LeftLeg.rotateAngleX = -0.4F;
        }
        if (this.isSitting || entityIn.isEntitySleeping() || entityIn.isDying()) {
            this.RightLeg.rotateAngleX = -1.4137167F;
            this.RightLeg.rotateAngleY = ((float)Math.PI / 10F);
            this.RightLeg.rotateAngleZ = 0.07853982F;
            this.LeftLeg.rotateAngleX = -1.4137167F;
            this.LeftLeg.rotateAngleY = (-(float)Math.PI / 10F);
            this.LeftLeg.rotateAngleZ = -0.07853982F;
            this.Savager.rotationPointY = 44.0F;
        } else {
            this.RightLeg.rotateAngleX -= 0.0F;
            this.LeftLeg.rotateAngleX -= 0.0F;
            this.RightLeg.rotateAngleX = (float)((double)this.RightLeg.rotateAngleX * 0.5D);
            this.LeftLeg.rotateAngleX = (float)((double)this.LeftLeg.rotateAngleX * 0.5D);
            this.RightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
            this.LeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / f;
            this.Savager.rotationPointY = 24.0F;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Savager.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.neck, this.body, this.RightLeg, this.LeftLeg, this.RightArm, this.LeftArm);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setLivingAnimations(SavagerEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        float f = 2.0F;

        if (f < 2.0F) {
            f = 2.0F;
        }
        int j = entityIn.func_213687_eg();
        int i = entityIn.func_213683_l();
        if (i > 0) {
            float f0 = MathHelper.func_233021_e_((float)i - partialTick, 10.0F);
            float f1 = (1.0F + f0) * 0.5F;
            float f2 = f1 * f1 * f1 * 12.0F;
            float f3 = f2 * MathHelper.sin(this.neck.rotateAngleX);
            this.neck.rotationPointZ = -21.0F + f2;
            this.neck.rotationPointY = -32.0F - f3;
            float f4 = MathHelper.sin(((float)i - partialTick) / 10.0F * (float)Math.PI * 0.25F);
            this.mouth.rotateAngleX = ((float)Math.PI / 2F) * f4;
            if (i > 5) {
                this.mouth.rotateAngleX = MathHelper.sin(((float)(-4 + i) - partialTick) / 4.0F) * (float)Math.PI * 0.4F;
            } else {
                this.mouth.rotateAngleX = 0.15707964F * MathHelper.sin((float)Math.PI * ((float)i - partialTick) / 10.0F);
            }
            this.RightArm.rotateAngleX = -2.0F + 1.5F * MathHelper.func_233021_e_((float)i - partialTick, 10.0F);
            this.LeftArm.rotateAngleX = -2.0F + 1.5F * MathHelper.func_233021_e_((float)i - partialTick, 10.0F);
        } else {
            if (this.isSitting || entityIn.isEntitySleeping() || entityIn.isDying()) {
                this.RightArm.rotateAngleX = (-(float)Math.PI / 5F);
                this.RightArm.rotateAngleZ = 0.0F;
                this.LeftArm.rotateAngleX = (-(float)Math.PI / 5F);
                this.LeftArm.rotateAngleZ = 0.0F;
            }
            if (this.RightArm.rotateAngleX > 0.4F) {
                this.RightArm.rotateAngleX = 0.4F;
            }

            if (this.LeftArm.rotateAngleX > 0.4F) {
                this.LeftArm.rotateAngleX = 0.4F;
            }

            if (this.RightArm.rotateAngleX < -0.4F) {
                this.RightArm.rotateAngleX = -0.4F;
            }

            if (this.LeftArm.rotateAngleX < -0.4F) {
                this.LeftArm.rotateAngleX = -0.4F;
            }
            this.RightArm.rotateAngleX = (float)((double)this.RightArm.rotateAngleX * 0.5D);
            this.LeftArm.rotateAngleX = (float)((double)this.LeftArm.rotateAngleX * 0.5D);
            this.RightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
            this.LeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
            float f6 = -1.0F * MathHelper.sin(this.neck.rotateAngleX);
            this.neck.rotationPointX = 0.0F;
            this.neck.rotationPointY = -32.0F - f6;
            this.neck.rotationPointZ = -11.0F;
            this.mouth.rotateAngleX = 0.0F;
        }

        if (j > 0) {
            float f7 = MathHelper.sin(((float)(20 - j) - partialTick) / 20.0F * (float)Math.PI * 0.25F);
            this.mouth.rotateAngleX = ((float)Math.PI / 2F) * f7;
        }
    }
}
