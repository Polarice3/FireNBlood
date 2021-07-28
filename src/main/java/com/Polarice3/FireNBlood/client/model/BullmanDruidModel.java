package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.TaillessDruidEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class BullmanDruidModel<T extends TaillessDruidEntity> extends EntityModel<T> {
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
    private final ModelRenderer RArm;
    private final ModelRenderer RHand;
    private final ModelRenderer Staff;
    private final ModelRenderer StaffHandle;
    private final ModelRenderer StaffEnd;
    private final ModelRenderer StaffHead;
    private final ModelRenderer LArmParts;
    private final ModelRenderer LUpperArm;
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
    private final ModelRenderer RHorn1_r1;
    private final ModelRenderer LHorns;
    private final ModelRenderer LHorn2_r1;
    private final ModelRenderer LHorn1_r1;

    public BullmanDruidModel(float layer) {
        textureWidth = 128;
        textureHeight = 128;

        BullMan = new ModelRenderer(this);
        BullMan.setRotationPoint(2.0F, 34.0F, -4.0F);


        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, 0.0F, 0.0F);
        BullMan.addChild(Body);
        Body.setTextureOffset(36, 40).addBox(-6.0F, -34.0F, 1.0F, 8.0F, 4.0F, 6.0F, 0.0F, false);
        Body.setTextureOffset(76, 84).addBox(-9.0F, -44.0F, -1.0F, 14.0F, 18.0F, 10.0F, 0.0F, false);
        Body.setTextureOffset(0, 86).addBox(-10.0F, -44.0F, -2.0F, 16.0F, 10.0F, 12.0F, 0.0F, false);
        Body.setTextureOffset(21, 106).addBox(-9.0F, -29.0F, 0.0F, 14.0F, 14.0F, 8.0F, 0.0F, false);

        RLegParts = new ModelRenderer(this);
        RLegParts.setRotationPoint(-6.0F, -26.0F, 4.0F);
        BullMan.addChild(RLegParts);


        RThigh = new ModelRenderer(this);
        RThigh.setRotationPoint(4.0F, 19.5F, -3.5F);
        RLegParts.addChild(RThigh);
        RThigh.setTextureOffset(44, 52).addBox(-6.0F, -19.5F, 0.5F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        RKnee = new ModelRenderer(this);
        RKnee.setRotationPoint(4.0F, 22.0F, -3.0F);
        RLegParts.addChild(RKnee);
        RKnee.setTextureOffset(0, 74).addBox(-6.0F, -16.0F, 2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        RLeg = new ModelRenderer(this);
        RLeg.setRotationPoint(4.0F, 24.0F, -2.5F);
        RLegParts.addChild(RLeg);
        RLeg.setTextureOffset(72, 74).addBox(-6.0F, -14.0F, 3.5F, 4.0F, 4.0F, 2.0F, 0.0F, false);

        RFoot = new ModelRenderer(this);
        RFoot.setRotationPoint(4.0F, 25.5F, -3.5F);
        RLegParts.addChild(RFoot);
        RFoot.setTextureOffset(60, 46).addBox(-6.0F, -11.5F, 0.5F, 4.0F, 2.0F, 6.0F, 0.0F, false);

        LLegParts = new ModelRenderer(this);
        LLegParts.setRotationPoint(2.0F, -26.0F, 4.0F);
        BullMan.addChild(LLegParts);


        LThigh = new ModelRenderer(this);
        LThigh.setRotationPoint(0.0F, 19.5F, -3.5F);
        LLegParts.addChild(LThigh);
        LThigh.setTextureOffset(60, 60).addBox(-2.0F, -19.5F, 0.5F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        LKnee = new ModelRenderer(this);
        LKnee.setRotationPoint(0.0F, 22.0F, -3.0F);
        LLegParts.addChild(LKnee);
        LKnee.setTextureOffset(54, 74).addBox(-2.0F, -16.0F, 2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        LLeg = new ModelRenderer(this);
        LLeg.setRotationPoint(0.0F, 24.0F, -2.5F);
        LLegParts.addChild(LLeg);
        LLeg.setTextureOffset(76, 16).addBox(-2.0F, -14.0F, 3.5F, 4.0F, 4.0F, 2.0F, 0.0F, false);

        LFoot = new ModelRenderer(this);
        LFoot.setRotationPoint(0.0F, 25.5F, -3.5F);
        LLegParts.addChild(LFoot);
        LFoot.setTextureOffset(48, 12).addBox(-2.0F, -11.5F, 0.5F, 4.0F, 2.0F, 6.0F, 0.0F, false);

        RArmParts = new ModelRenderer(this);
        RArmParts.setRotationPoint(-10.0F, -40.0F, 2.0F);
        BullMan.addChild(RArmParts);


        RUpperArm = new ModelRenderer(this);
        RUpperArm.setRotationPoint(5.0F, 25.0F, -2.5F);
        RArmParts.addChild(RUpperArm);
        RUpperArm.setTextureOffset(104, 29).addBox(-9.0F, -29.0F, -0.5F, 4.0F, 12.0F, 8.0F, 0.0F, false);

        RArm = new ModelRenderer(this);
        RArm.setRotationPoint(5.0F, 30.5F, -2.0F);
        RArmParts.addChild(RArm);
        RArm.setTextureOffset(0, 116).addBox(-9.0F, -22.5F, 0.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        RHand = new ModelRenderer(this);
        RHand.setRotationPoint(5.0F, 33.0F, -2.0F);
        RArmParts.addChild(RHand);
        RHand.setTextureOffset(68, 34).addBox(-9.0F, -19.0F, 1.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        Staff = new ModelRenderer(this);
        Staff.setRotationPoint(9.0F, 0.0F, 30.0F);
        RArmParts.addChild(Staff);
        setRotationAngle(Staff, 1.5708F, 0.0F, 0.0F);


        StaffHandle = new ModelRenderer(this);
        StaffHandle.setRotationPoint(0.0F, 0.0F, 0.0F);
        Staff.addChild(StaffHandle);
        StaffHandle.setTextureOffset(19, 0).addBox(-11.0F, -48.0F, -18.0F, 1.0F, 45.0F, 1.0F, 0.0F, false);

        StaffEnd = new ModelRenderer(this);
        StaffEnd.setRotationPoint(0.0F, 0.0F, 0.0F);
        Staff.addChild(StaffEnd);
        StaffEnd.setTextureOffset(29, 1).addBox(-12.0F, -3.0F, -19.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);

        StaffHead = new ModelRenderer(this);
        StaffHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        Staff.addChild(StaffHead);
        StaffHead.setTextureOffset(0, 52).addBox(-13.0F, -49.0F, -20.0F, 5.0F, 5.0F, 5.0F, 0.0F, false);

        LArmParts = new ModelRenderer(this);
        LArmParts.setRotationPoint(6.0F, -40.0F, 2.0F);
        BullMan.addChild(LArmParts);


        LUpperArm = new ModelRenderer(this);
        LUpperArm.setRotationPoint(-1.0F, 25.0F, -2.5F);
        LArmParts.addChild(LUpperArm);
        LUpperArm.setTextureOffset(104, 0).addBox(1.0F, -29.0F, -0.5F, 4.0F, 12.0F, 8.0F, 0.0F, false);

        LArm = new ModelRenderer(this);
        LArm.setRotationPoint(-1.0F, 30.5F, -2.0F);
        LArmParts.addChild(LArm);
        LArm.setTextureOffset(108, 116).addBox(1.0F, -22.5F, 0.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        LHand = new ModelRenderer(this);
        LHand.setRotationPoint(-1.0F, 33.0F, -2.0F);
        LArmParts.addChild(LHand);
        LHand.setTextureOffset(62, 22).addBox(1.0F, -19.0F, 1.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        HeadParts = new ModelRenderer(this);
        HeadParts.setRotationPoint(-2.6667F, -43.5F, 0.6667F);
        BullMan.addChild(HeadParts);


        Head_r1 = new ModelRenderer(this);
        Head_r1.setRotationPoint(0.6667F, -5.5F, 2.3333F);
        HeadParts.addChild(Head_r1);
        Head_r1.setTextureOffset(44, 0).addBox(-4.0F, -2.0F, -3.0F, 8.0F, 4.0F, 6.0F, 0.0F, false);

        Jaw_r1 = new ModelRenderer(this);
        Jaw_r1.setRotationPoint(0.6667F, -2.5F, 0.3333F);
        HeadParts.addChild(Jaw_r1);
        Jaw_r1.setTextureOffset(34, 22).addBox(-4.0F, -2.0F, -5.0F, 8.0F, 4.0F, 10.0F, 0.0F, false);

        REar = new ModelRenderer(this);
        REar.setRotationPoint(2.6667F, 43.5F, -0.6667F);
        HeadParts.addChild(REar);


        REar_r1 = new ModelRenderer(this);
        REar_r1.setRotationPoint(-7.0F, -49.5F, 3.0F);
        REar.addChild(REar_r1);
        REar_r1.setTextureOffset(26, 46).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        LEar = new ModelRenderer(this);
        LEar.setRotationPoint(2.6667F, 43.5F, -0.6667F);
        HeadParts.addChild(LEar);


        LEar_r1 = new ModelRenderer(this);
        LEar_r1.setRotationPoint(3.0F, -49.5F, 3.0F);
        LEar.addChild(LEar_r1);
        LEar_r1.setTextureOffset(16, 46).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        RHorns = new ModelRenderer(this);
        RHorns.setRotationPoint(2.6667F, 43.5F, -0.6667F);
        HeadParts.addChild(RHorns);


        RHorn2_r1 = new ModelRenderer(this);
        RHorn2_r1.setRotationPoint(-9.0F, -52.0F, 5.0F);
        RHorns.addChild(RHorn2_r1);
        RHorn2_r1.setTextureOffset(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        RHorn1_r1 = new ModelRenderer(this);
        RHorn1_r1.setRotationPoint(-7.0F, -49.5F, 5.0F);
        RHorns.addChild(RHorn1_r1);
        RHorn1_r1.setTextureOffset(34, 18).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        LHorns = new ModelRenderer(this);
        LHorns.setRotationPoint(2.6667F, 43.5F, -0.6667F);
        HeadParts.addChild(LHorns);


        LHorn2_r1 = new ModelRenderer(this);
        LHorn2_r1.setRotationPoint(5.0F, -52.0F, 5.0F);
        LHorns.addChild(LHorn2_r1);
        LHorn2_r1.setTextureOffset(76, 56).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        LHorn1_r1 = new ModelRenderer(this);
        LHorn1_r1.setRotationPoint(3.0F, -49.5F, 5.0F);
        LHorns.addChild(LHorn1_r1);
        LHorn1_r1.setTextureOffset(76, 44).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        BullMan.render(matrixStack, buffer, packedLight, packedOverlay);
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

        this.RArmParts.rotationPointZ = 0.0F;
        this.LArmParts.rotationPointZ = 0.0F;
        this.RLegParts.rotationPointZ = 4.0F;
        this.LLegParts.rotationPointZ = 4.0F;
        this.RLegParts.rotationPointY = -27.0F;
        this.LLegParts.rotationPointY = -27.0F;
        this.HeadParts.rotationPointZ = -0.0F;
        this.HeadParts.rotationPointY = -43.0F;

        float f3 = -14.0F;
        this.RArmParts.setRotationPoint(-9.0F, -40.0F, 4.0F);
        this.LArmParts.setRotationPoint(5.0F, -40.0F, 4.0F);

        TaillessDruidEntity.ArmPose abstracttaillessdruidentity$armpose = entityIn.getArmPose();
        if (abstracttaillessdruidentity$armpose == TaillessDruidEntity.ArmPose.SPELLCASTING){
            float f4 = MathHelper.sin(swingProgress * (float)Math.PI);
            float f5 = MathHelper.sin((1.0F - (1.0F - swingProgress) * (1.0F - swingProgress)) * (float)Math.PI);
            RArmParts.rotateAngleX = -1.8849558F + MathHelper.cos(ageInTicks * 0.09F) * 0.15F;
            RArmParts.rotateAngleX += f4 * 2.2F - f5 * 0.4F;
/*            this.RArmParts.rotateAngleX = (float)((double)this.RArmParts.rotateAngleX * 0.5D);
            this.RArmParts.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;*/
            this.LArmParts.rotateAngleX = (float)((double)this.LArmParts.rotateAngleX * 0.5D);
            this.LArmParts.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
            if (this.LArmParts.rotateAngleX > 0.4F) {
                this.LArmParts.rotateAngleX = 0.4F;
            }
            if (this.LArmParts.rotateAngleX < -0.4F) {
                this.LArmParts.rotateAngleX = -0.4F;
            }
        }
        if (abstracttaillessdruidentity$armpose == AbstractTaillessEntity.ArmPose.NEUTRAL){
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
}

