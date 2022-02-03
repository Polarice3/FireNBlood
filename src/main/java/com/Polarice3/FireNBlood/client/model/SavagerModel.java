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
        texWidth = 128;
        texHeight = 128;

        Savager = new ModelRenderer(this);
        Savager.setPos(0.0F, 24.0F + p_i47227_2_, 0.0F);


        body = new ModelRenderer(this);
        body.setPos(0.0F, -21.0F + p_i47227_2_, 10.0F);
        Savager.addChild(body);
        setRotationAngle(body, 0.9599F, 0.0F, 0.0F);
        body.texOffs(0, 32).addBox(-8.0F, -14.0F, 0.0F, 16.0F, 20.0F, 10.0F, scaleFactor, false);

        body2 = new ModelRenderer(this);
        body2.setPos(0.0F, 0.0F + p_i47227_2_, 4.0F);
        body.addChild(body2);
        setRotationAngle(body2, 0.6109F, 0.0F, 0.0F);
        body2.texOffs(0, 0).addBox(-12.0F, -23.0F, 0.0F, 24.0F, 16.0F, 16.0F, scaleFactor, false);

        neck = new ModelRenderer(this);
        neck.setPos(0.0F, -32.0F + p_i47227_2_, -11.0F);
        Savager.addChild(neck);
        setRotationAngle(neck, 0.0F, 0.0436F, 0.0F);
        neck.texOffs(71, 102).addBox(-5.0F, -6.0F, -1.0F, 10.0F, 10.0F, 16.0F, scaleFactor, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        neck.addChild(head);
        head.texOffs(0, 94).addBox(-8.0F, -10.0F, -16.0F, 16.0F, 18.0F, 16.0F, scaleFactor, false);
        head.texOffs(0, 94).addBox(-2.0F, 2.0F, -20.0F, 4.0F, 8.0F, 4.0F, scaleFactor, false);

        mouth = new ModelRenderer(this);
        mouth.setPos(0.0F, 8.0F + p_i47227_2_, 0.0F);
        head.addChild(mouth);
        mouth.texOffs(0, 74).addBox(-8.0F, -2.0F, -16.0F, 16.0F, 3.0F, 16.0F, scaleFactor, false);

        horns = new ModelRenderer(this);
        horns.setPos(-5.0F, 8.0F + p_i47227_2_, -11.0F);
        head.addChild(horns);
        setRotationAngle(horns, 1.0472F, 0.0F, 0.0F);
        horns.texOffs(64, 35).addBox(-5.0F, -19.0F, 10.8564F, 2.0F, 14.0F, 4.0F, scaleFactor, false);
        horns.texOffs(64, 35).addBox(13.0F, -19.0F, 10.8564F, 2.0F, 14.0F, 4.0F, scaleFactor, false);

        RightLeg = new ModelRenderer(this);
        RightLeg.setPos(-8.0F, -27.0F + p_i47227_2_, 17.0F);
        Savager.addChild(RightLeg);
        RightLeg.texOffs(81, 54).addBox(-8.0F, -10.0F, -5.0F, 8.0F, 37.0F, 8.0F, scaleFactor, false);

        LeftLeg = new ModelRenderer(this);
        LeftLeg.setPos(8.0F, -27.0F + p_i47227_2_, 17.0F);
        Savager.addChild(LeftLeg);
        LeftLeg.texOffs(81, 54).addBox(0.0F, -10.0F, -5.0F, 8.0F, 37.0F, 8.0F, scaleFactor, false);

        RightArm = new ModelRenderer(this);
        RightArm.setPos(-11.0F, -33.0F, -7.0F);
        Savager.addChild(RightArm);
        setRotationAngle(RightArm, 0.5236F, 0.0F, 0.0F);
        RightArm.texOffs(80, 0).addBox(-9.0F, -2.9019F, -3.9019F, 8.0F, 17.0F, 8.0F, 0.0F, false);

        LRA = new ModelRenderer(this);
        LRA.setPos(-5.0F, 16.0981F, 0.0981F);
        RightArm.addChild(LRA);
        setRotationAngle(LRA, -0.7854F, 0.0F, 0.0F);
        LRA.texOffs(81, 26).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 20.0F, 8.0F, 0.0F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setPos(11.0F, -33.0F, -7.0F);
        Savager.addChild(LeftArm);
        setRotationAngle(LeftArm, 0.5236F, 0.0F, 0.0F);
        LeftArm.texOffs(80, 0).addBox(1.0F, -2.9019F, -3.9019F, 8.0F, 17.0F, 8.0F, 0.0F, false);

        LLA = new ModelRenderer(this);
        LLA.setPos(5.0F, 16.0981F, 0.0981F);
        LeftArm.addChild(LLA);
        setRotationAngle(LLA, -0.7854F, 0.0F, 0.0F);
        LLA.texOffs(81, 26).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 20.0F, 8.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(SavagerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = 2.0F;

        if (f < 2.0F) {
            f = 2.0F;
        }
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        if (this.RightLeg.xRot > 0.4F) {
            this.RightLeg.xRot = 0.4F;
        }

        if (this.LeftLeg.xRot > 0.4F) {
            this.LeftLeg.xRot = 0.4F;
        }

        if (this.RightLeg.xRot < -0.4F) {
            this.RightLeg.xRot = -0.4F;
        }

        if (this.LeftLeg.xRot < -0.4F) {
            this.LeftLeg.xRot = -0.4F;
        }
        if (this.riding || entityIn.isEntitySleeping() || entityIn.isDying()) {
            this.RightLeg.xRot = -1.4137167F;
            this.RightLeg.yRot = ((float)Math.PI / 10F);
            this.RightLeg.zRot = 0.07853982F;
            this.LeftLeg.xRot = -1.4137167F;
            this.LeftLeg.yRot = (-(float)Math.PI / 10F);
            this.LeftLeg.zRot = -0.07853982F;
            this.Savager.y = 44.0F;
        } else {
            this.RightLeg.xRot -= 0.0F;
            this.LeftLeg.xRot -= 0.0F;
            this.RightLeg.xRot = (float)((double)this.RightLeg.xRot * 0.5D);
            this.LeftLeg.xRot = (float)((double)this.LeftLeg.xRot * 0.5D);
            this.RightLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
            this.LeftLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / f;
            this.Savager.y = 24.0F;
        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Savager.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.neck, this.body, this.RightLeg, this.LeftLeg, this.RightArm, this.LeftArm);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public void prepareMobModel(SavagerEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        float f = 2.0F;

        if (f < 2.0F) {
            f = 2.0F;
        }
        int j = entityIn.func_213687_eg();
        int i = entityIn.func_213683_l();
        if (i > 0) {
            float f0 = MathHelper.triangleWave((float)i - partialTick, 10.0F);
            float f1 = (1.0F + f0) * 0.5F;
            float f2 = f1 * f1 * f1 * 12.0F;
            float f3 = f2 * MathHelper.sin(this.neck.xRot);
            this.neck.z = -21.0F + f2;
            this.neck.y = -32.0F - f3;
            float f4 = MathHelper.sin(((float)i - partialTick) / 10.0F * (float)Math.PI * 0.25F);
            this.mouth.xRot = ((float)Math.PI / 2F) * f4;
            if (i > 5) {
                this.mouth.xRot = MathHelper.sin(((float)(-4 + i) - partialTick) / 4.0F) * (float)Math.PI * 0.4F;
            } else {
                this.mouth.xRot = 0.15707964F * MathHelper.sin((float)Math.PI * ((float)i - partialTick) / 10.0F);
            }
            this.RightArm.xRot = -2.0F + 1.5F * MathHelper.triangleWave((float)i - partialTick, 10.0F);
            this.LeftArm.xRot = -2.0F + 1.5F * MathHelper.triangleWave((float)i - partialTick, 10.0F);
        } else {
            if (this.riding || entityIn.isEntitySleeping() || entityIn.isDying()) {
                this.RightArm.xRot = (-(float)Math.PI / 5F);
                this.RightArm.zRot = 0.0F;
                this.LeftArm.xRot = (-(float)Math.PI / 5F);
                this.LeftArm.zRot = 0.0F;
            }
            if (this.RightArm.xRot > 0.4F) {
                this.RightArm.xRot = 0.4F;
            }

            if (this.LeftArm.xRot > 0.4F) {
                this.LeftArm.xRot = 0.4F;
            }

            if (this.RightArm.xRot < -0.4F) {
                this.RightArm.xRot = -0.4F;
            }

            if (this.LeftArm.xRot < -0.4F) {
                this.LeftArm.xRot = -0.4F;
            }
            this.RightArm.xRot = (float)((double)this.RightArm.xRot * 0.5D);
            this.LeftArm.xRot = (float)((double)this.LeftArm.xRot * 0.5D);
            this.RightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
            this.LeftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
            float f6 = -1.0F * MathHelper.sin(this.neck.xRot);
            this.neck.x = 0.0F;
            this.neck.y = -32.0F - f6;
            this.neck.z = -11.0F;
            this.mouth.xRot = 0.0F;
        }

        if (j > 0) {
            float f7 = MathHelper.sin(((float)(20 - j) - partialTick) / 20.0F * (float)Math.PI * 0.25F);
            this.mouth.xRot = ((float)Math.PI / 2F) * f7;
        }
    }
}
