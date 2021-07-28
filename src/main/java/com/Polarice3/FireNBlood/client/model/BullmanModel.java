package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.TaillessWretchEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;


public class BullmanModel<T extends TaillessWretchEntity> extends SegmentedModel<T> implements IHasArm {

    private final ModelRenderer BullMan;
    private final ModelRenderer Body;
    private final ModelRenderer RLegParts;
    private final ModelRenderer RThigh;
    private final ModelRenderer RKnee;
    private final ModelRenderer RLeg;
    private final ModelRenderer RFoot;
    private final ModelRenderer LLegParts;
    private final ModelRenderer LThigh;
    private final ModelRenderer LKnee;
    private final ModelRenderer LLeg;
    private final ModelRenderer LFoot;
    private final ModelRenderer RArmParts;
    private final ModelRenderer RUpperArm;
    private final ModelRenderer RElbow;
    private final ModelRenderer RArm;
    private final ModelRenderer RHand;
    private final ModelRenderer LArmParts;
    private final ModelRenderer LUpperArm;
    private final ModelRenderer LElbow;
    private final ModelRenderer LArm;
    private final ModelRenderer LHand;
    private final ModelRenderer HeadParts;
    private final ModelRenderer Head_r1;
    private final ModelRenderer Jaw_r1;
    private final ModelRenderer REar;
    private final ModelRenderer REar_r1;
    private final ModelRenderer LEar;
    private final ModelRenderer LEar_r1;
    private final ModelRenderer RHorns;
    private final ModelRenderer RHorn2_r1;
    private final ModelRenderer LHorns;
    private final ModelRenderer LHorn2_r1;

    public BullmanModel() {
        textureWidth = 128;
        textureHeight = 128;

        BullMan = new ModelRenderer(this);
        BullMan.setRotationPoint(0.0F, 24.0F, 0.0F);


        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, 0.0F, 0.0F);
        BullMan.addChild(Body);
        Body.setTextureOffset(36, 40).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 4.0F, 6.0F, 0.0F, false);
        Body.setTextureOffset(0, 32).addBox(-6.0F, -28.0F, -4.0F, 12.0F, 4.0F, 8.0F, 0.0F, false);
        Body.setTextureOffset(0, 0).addBox(-8.0F, -34.0F, -6.0F, 16.0F, 6.0F, 10.0F, 0.0F, false);
        Body.setTextureOffset(0, 18).addBox(-6.0F, -20.0F, -4.0F, 12.0F, 4.0F, 8.0F, 0.0F, false);

        RLegParts = new ModelRenderer(this);
        RLegParts.setRotationPoint(-4.0F, -16.0F, 0.0F);
        BullMan.addChild(RLegParts);


        RThigh = new ModelRenderer(this);
        RThigh.setRotationPoint(0.0F, 3.0F, 1.0F);
        RLegParts.addChild(RThigh);
        RThigh.setTextureOffset(60, 60).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        RKnee = new ModelRenderer(this);
        RKnee.setRotationPoint(0.0F, 8.0F, 2.0F);
        RLegParts.addChild(RKnee);
        RKnee.setTextureOffset(0, 74).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        RLeg = new ModelRenderer(this);
        RLeg.setRotationPoint(0.0F, 12.0F, 3.0F);
        RLegParts.addChild(RLeg);
        RLeg.setTextureOffset(72, 74).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.0F, false);

        RFoot = new ModelRenderer(this);
        RFoot.setRotationPoint(0.0F, 15.0F, 1.0F);
        RLegParts.addChild(RFoot);
        RFoot.setTextureOffset(60, 46).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

        LLegParts = new ModelRenderer(this);
        LLegParts.setRotationPoint(4.0F, -16.0F, 0.0F);
        BullMan.addChild(LLegParts);


        LThigh = new ModelRenderer(this);
        LThigh.setRotationPoint(0.0F, 3.0F, 1.0F);
        LLegParts.addChild(LThigh);
        LThigh.setTextureOffset(60, 60).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        LKnee = new ModelRenderer(this);
        LKnee.setRotationPoint(0.0F, 8.0F, 2.0F);
        LLegParts.addChild(LKnee);
        LKnee.setTextureOffset(54, 74).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        LLeg = new ModelRenderer(this);
        LLeg.setRotationPoint(0.0F, 12.0F, 3.0F);
        LLegParts.addChild(LLeg);
        LLeg.setTextureOffset(72, 74).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.0F, false);

        LFoot = new ModelRenderer(this);
        LFoot.setRotationPoint(0.0F, 15.0F, 1.0F);
        LLegParts.addChild(LFoot);
        LFoot.setTextureOffset(60, 46).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

        RArmParts = new ModelRenderer(this);
        RArmParts.setRotationPoint(-8.0F, -30.0F, 0.0F);
        BullMan.addChild(RArmParts);


        RUpperArm = new ModelRenderer(this);
        RUpperArm.setRotationPoint(-2.0F, 0.0F, -1.0F);
        RArmParts.addChild(RUpperArm);
        RUpperArm.setTextureOffset(22, 52).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 8.0F, 6.0F, 0.0F, false);

        RElbow = new ModelRenderer(this);
        RElbow.setRotationPoint(-2.0F, 6.0F, 0.0F);
        RArmParts.addChild(RElbow);
        RElbow.setTextureOffset(70, 6).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        RArm = new ModelRenderer(this);
        RArm.setRotationPoint(-2.0F, 11.0F, 0.0F);
        RArmParts.addChild(RArm);
        RArm.setTextureOffset(0, 62).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        RHand = new ModelRenderer(this);
        RHand.setRotationPoint(-2.0F, 16.0F, 0.0F);
        RArmParts.addChild(RHand);
        RHand.setTextureOffset(62, 22).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        LArmParts = new ModelRenderer(this);
        LArmParts.setRotationPoint(8.0F, -30.0F, 0.0F);
        BullMan.addChild(LArmParts);


        LUpperArm = new ModelRenderer(this);
        LUpperArm.setRotationPoint(2.0F, 0.0F, -1.0F);
        LArmParts.addChild(LUpperArm);
        LUpperArm.setTextureOffset(0, 46).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 8.0F, 6.0F, 0.0F, false);

        LElbow = new ModelRenderer(this);
        LElbow.setRotationPoint(2.0F, 6.0F, 0.0F);
        LArmParts.addChild(LElbow);
        LElbow.setTextureOffset(70, 6).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        LArm = new ModelRenderer(this);
        LArm.setRotationPoint(2.0F, 11.0F, 0.0F);
        LArmParts.addChild(LArm);
        LArm.setTextureOffset(0, 62).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        LHand = new ModelRenderer(this);
        LHand.setRotationPoint(2.0F, 16.0F, 0.0F);
        LArmParts.addChild(LHand);
        LHand.setTextureOffset(68, 34).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        HeadParts = new ModelRenderer(this);
        HeadParts.setRotationPoint(0.0F, -34.0F, 0.0F);
        BullMan.addChild(HeadParts);


        Head_r1 = new ModelRenderer(this);
        Head_r1.setRotationPoint(0.0F, -3.5F, -0.3F);
        HeadParts.addChild(Head_r1);
        Head_r1.setTextureOffset(44, 0).addBox(-4.0F, -3.5F, -3.7F, 8.0F, 4.0F, 6.0F, 0.0F, false);

        Jaw_r1 = new ModelRenderer(this);
        Jaw_r1.setRotationPoint(0.0F, -5.5F, -0.3F);
        HeadParts.addChild(Jaw_r1);
        Jaw_r1.setTextureOffset(34, 22).addBox(-4.0F, 1.5F, -7.7F, 8.0F, 4.0F, 10.0F, 0.0F, false);

        REar = new ModelRenderer(this);
        REar.setRotationPoint(0.0F, 34.0F, 0.0F);
        HeadParts.addChild(REar);


        REar_r1 = new ModelRenderer(this);
        REar_r1.setRotationPoint(0.0F, -39.5F, -0.3F);
        REar.addChild(REar_r1);
        REar_r1.setTextureOffset(26, 46).addBox(-6.0F, -1.5F, -1.7F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        LEar = new ModelRenderer(this);
        LEar.setRotationPoint(0.0F, 34.0F, 0.0F);
        HeadParts.addChild(LEar);


        LEar_r1 = new ModelRenderer(this);
        LEar_r1.setRotationPoint(0.0F, -39.5F, -0.3F);
        LEar.addChild(LEar_r1);
        LEar_r1.setTextureOffset(16, 46).addBox(4.0F, -1.5F, -1.7F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        RHorns = new ModelRenderer(this);
        RHorns.setRotationPoint(0.0F, 34.0F, 0.0F);
        HeadParts.addChild(RHorns);


        RHorn2_r1 = new ModelRenderer(this);
        RHorn2_r1.setRotationPoint(0.0F, -39.5F, -0.3F);
        RHorns.addChild(RHorn2_r1);
        RHorn2_r1.setTextureOffset(0, 0).addBox(-8.0F, -6.5F, 0.3F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        RHorn2_r1.setTextureOffset(34, 18).addBox(-6.0F, -2.5F, 0.3F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        LHorns = new ModelRenderer(this);
        LHorns.setRotationPoint(0.0F, 34.0F, 0.0F);
        HeadParts.addChild(LHorns);


        LHorn2_r1 = new ModelRenderer(this);
        LHorn2_r1.setRotationPoint(0.0F, -39.5F, -0.3F);
        LHorns.addChild(LHorn2_r1);
        LHorn2_r1.setTextureOffset(0, 0).addBox(6.0F, -6.5F, 0.3F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        LHorn2_r1.setTextureOffset(34, 18).addBox(4.0F, -2.5F, 0.3F, 2.0F, 4.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        BullMan.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.RArmParts, this.LArmParts, this.RLegParts, this.LLegParts, this.HeadParts);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = 2.0F;

        if (f < 2.0F) {
            f = 2.0F;
        }
        this.HeadParts.showModel = true;
        this.HeadParts.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.HeadParts.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.Body.rotateAngleX = 0.0F;
        this.Body.rotationPointY = 0.0F;
        this.Body.rotationPointZ = -0.0F;
        this.RLegParts.rotateAngleX -= 0.0F;
        this.LLegParts.rotateAngleX -= 0.0F;
        this.RLegParts.rotateAngleX = (float)((double)this.RLegParts.rotateAngleX * 0.5D);
        this.LLegParts.rotateAngleX = (float)((double)this.LLegParts.rotateAngleX * 0.5D);
        this.RLegParts.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.LLegParts.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / f;
        if (this.RLegParts.rotateAngleX > 0.4F) {
            this.RLegParts.rotateAngleX = 0.4F;
        }

        if (this.LLegParts.rotateAngleX > 0.4F) {
            this.LLegParts.rotateAngleX = 0.4F;
        }

        if (this.RLegParts.rotateAngleX < -0.4F) {
            this.RLegParts.rotateAngleX = -0.4F;
        }

        if (this.LLegParts.rotateAngleX < -0.4F) {
            this.LLegParts.rotateAngleX = -0.4F;
        }

        float f3 = -14.0F;

        TaillessWretchEntity.ArmPose abstracttaillesswretchentity$armpose = entityIn.getArmPose();
        if (abstracttaillesswretchentity$armpose == TaillessWretchEntity.ArmPose.ATTACKING) {
            float f4 = MathHelper.sin(swingProgress * (float)Math.PI);
            float f5 = MathHelper.sin((1.0F - (1.0F - swingProgress) * (1.0F - swingProgress)) * (float)Math.PI);
            RArmParts.rotateAngleX = -1.8849558F + MathHelper.cos(ageInTicks * 0.09F) * 0.15F;
            RArmParts.rotateAngleX += f4 * 2.2F - f5 * 0.4F;
            this.LArmParts.rotateAngleX = (float)((double)this.LArmParts.rotateAngleX * 0.5D);
            this.LArmParts.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
            if (this.LArmParts.rotateAngleX > 0.4F) {
                this.LArmParts.rotateAngleX = 0.4F;
            }
            if (this.LArmParts.rotateAngleX < -0.4F) {
                this.LArmParts.rotateAngleX = -0.4F;
            }
        }
        if (abstracttaillesswretchentity$armpose == AbstractTaillessEntity.ArmPose.NEUTRAL){
            this.RArmParts.rotateAngleX = (float)((double)this.RArmParts.rotateAngleX * 0.5D);
            this.LArmParts.rotateAngleX = (float)((double)this.LArmParts.rotateAngleX * 0.5D);
            this.RArmParts.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
            this.LArmParts.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
            if (this.RArmParts.rotateAngleX > 0.4F) {
                this.RArmParts.rotateAngleX = 0.4F;
            }

            if (this.LArmParts.rotateAngleX > 0.4F) {
                this.LArmParts.rotateAngleX = 0.4F;
            }

            if (this.RArmParts.rotateAngleX < -0.4F) {
                this.RArmParts.rotateAngleX = -0.4F;
            }

            if (this.LArmParts.rotateAngleX < -0.4F) {
                this.LArmParts.rotateAngleX = -0.4F;
            }
        }

    }

    @Override
    public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
        this.getArm(sideIn).translateRotate(matrixStackIn);
    }

    private ModelRenderer getArm(HandSide side) {
        return side == HandSide.LEFT ? this.LArmParts : this.RArmParts;
    }
}
