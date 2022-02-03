package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.masters.MinotaurEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessWretchEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class MinotaurModel <T extends MinotaurEntity> extends EntityModel<T> implements IHasArm {
    private final ModelRenderer BullMan;
    private final ModelRenderer Body;
    private final ModelRenderer Body_r1;
    private final ModelRenderer Body_r2;
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
    private final ModelRenderer Mace;
    private final ModelRenderer LArmParts;
    private final ModelRenderer LUpperArm;
    private final ModelRenderer LElbow;
    private final ModelRenderer LArm;
    private final ModelRenderer LHand;
    private final ModelRenderer Cannon;
    private final ModelRenderer Handle_r1;
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

    public MinotaurModel() {
        texWidth = 128;
        texHeight = 128;

        BullMan = new ModelRenderer(this);
        BullMan.setPos(2.0F, 34.0F, -4.0F);


        Body = new ModelRenderer(this);
        Body.setPos(0.0F, -12.0F, 6.0F);
        BullMan.addChild(Body);
        Body.texOffs(74, 45).addBox(-6.0F, -32.0F, -2.0F, 8.0F, 6.0F, 6.0F, 0.0F, false);
        Body.texOffs(27, 0).addBox(-10.0F, -26.0F, -4.0F, 16.0F, 6.0F, 8.0F, 0.0F, false);

        Body_r1 = new ModelRenderer(this);
        Body_r1.setPos(-2.0F, -10.0F, 4.0F);
        Body.addChild(Body_r1);
        setRotationAngle(Body_r1, 0.1309F, 0.0F, 0.0F);
        Body_r1.texOffs(0, 25).addBox(-8.0F, -36.0F, -7.0F, 16.0F, 8.0F, 10.0F, 0.0F, false);

        Body_r2 = new ModelRenderer(this);
        Body_r2.setPos(-2.0F, -10.0F, 4.0F);
        Body.addChild(Body_r2);
        setRotationAngle(Body_r2, 0.0873F, 0.0F, 0.0F);
        Body_r2.texOffs(43, 17).addBox(-6.0F, -28.0F, -6.0F, 12.0F, 6.0F, 8.0F, 0.0F, false);

        RLegParts = new ModelRenderer(this);
        RLegParts.setPos(-7.0F, -32.0F, 7.0F);
        BullMan.addChild(RLegParts);


        RThigh = new ModelRenderer(this);
        RThigh.setPos(5.0F, 21.5F, -3.5F);
        RLegParts.addChild(RThigh);
        RThigh.texOffs(0, 77).addBox(-8.0F, -21.5F, 0.5F, 6.0F, 8.0F, 6.0F, 0.0F, false);

        RKnee = new ModelRenderer(this);
        RKnee.setPos(4.0F, 24.0F, -3.0F);
        RLegParts.addChild(RKnee);
        RKnee.texOffs(80, 90).addBox(-6.0F, -16.0F, 2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        RLeg = new ModelRenderer(this);
        RLeg.setPos(5.0F, 28.0F, -4.5F);
        RLegParts.addChild(RLeg);
        RLeg.texOffs(0, 11).addBox(-8.0F, -16.0F, 3.5F, 6.0F, 6.0F, 4.0F, 0.0F, false);

        RFoot = new ModelRenderer(this);
        RFoot.setPos(5.0F, 31.5F, -5.5F);
        RLegParts.addChild(RFoot);
        RFoot.texOffs(21, 70).addBox(-8.0F, -13.5F, 0.5F, 6.0F, 4.0F, 8.0F, 0.0F, false);

        LLegParts = new ModelRenderer(this);
        LLegParts.setPos(3.0F, -32.0F, 7.0F);
        BullMan.addChild(LLegParts);


        LThigh = new ModelRenderer(this);
        LThigh.setPos(1.0F, 21.5F, -3.5F);
        LLegParts.addChild(LThigh);
        LThigh.texOffs(76, 0).addBox(-4.0F, -21.5F, 0.5F, 6.0F, 8.0F, 6.0F, 0.0F, false);

        LKnee = new ModelRenderer(this);
        LKnee.setPos(0.0F, 24.0F, -3.0F);
        LLegParts.addChild(LKnee);
        LKnee.texOffs(63, 90).addBox(-2.0F, -16.0F, 2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        LLeg = new ModelRenderer(this);
        LLeg.setPos(1.0F, 28.0F, -4.5F);
        LLegParts.addChild(LLeg);
        LLeg.texOffs(0, 0).addBox(-4.0F, -16.0F, 3.5F, 6.0F, 6.0F, 4.0F, 0.0F, false);

        LFoot = new ModelRenderer(this);
        LFoot.setPos(1.0F, 31.5F, -5.5F);
        LLegParts.addChild(LFoot);
        LFoot.texOffs(0, 63).addBox(-4.0F, -13.5F, 0.5F, 6.0F, 4.0F, 8.0F, 0.0F, false);

        RArmParts = new ModelRenderer(this);
        RArmParts.setPos(-10.0F, -52.0F, 3.0F);
        BullMan.addChild(RArmParts);


        RUpperArm = new ModelRenderer(this);
        RUpperArm.setPos(5.0F, 26.0F, -2.5F);
        RArmParts.addChild(RUpperArm);
        RUpperArm.texOffs(33, 51).addBox(-13.0F, -31.0F, -0.5F, 8.0F, 10.0F, 8.0F, 0.0F, false);

        RElbow = new ModelRenderer(this);
        RElbow.setPos(3.0F, 29.0F, -2.0F);
        RArmParts.addChild(RElbow);
        RElbow.texOffs(46, 90).addBox(-9.0F, -24.0F, 1.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        RArm = new ModelRenderer(this);
        RArm.setPos(4.0F, 33.5F, -3.0F);
        RArmParts.addChild(RArm);
        RArm.texOffs(75, 75).addBox(-11.0F, -24.5F, 1.0F, 6.0F, 8.0F, 6.0F, 0.0F, false);

        RHand = new ModelRenderer(this);
        RHand.setPos(4.0F, 36.0F, -3.0F);
        RArmParts.addChild(RHand);
        RHand.texOffs(83, 58).addBox(-11.0F, -19.0F, 1.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);

        Mace = new ModelRenderer(this);
        Mace.setPos(-4.0F, 19.0F, -15.1111F);
        RArmParts.addChild(Mace);
        Mace.texOffs(0, 0).addBox(-1.0F, -1.0F, 0.1111F, 2.0F, 2.0F, 22.0F, 0.0F, false);
        Mace.texOffs(68, 0).addBox(-1.0F, -1.0F, 22.1111F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        Mace.texOffs(27, 15).addBox(-2.0F, -2.0F, -1.8889F, 4.0F, 4.0F, 2.0F, 0.0F, false);
        Mace.texOffs(78, 26).addBox(-3.0F, -3.0F, -7.8889F, 6.0F, 6.0F, 6.0F, 0.0F, false);
        Mace.texOffs(65, 54).addBox(-5.0F, -1.0F, -5.8889F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        Mace.texOffs(21, 63).addBox(3.0F, -1.0F, -5.8889F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        Mace.texOffs(58, 51).addBox(-1.0F, -5.0F, -5.8889F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        Mace.texOffs(34, 44).addBox(-1.0F, 3.0F, -5.8889F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        Mace.texOffs(25, 44).addBox(-1.0F, -1.0F, -9.8889F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        LArmParts = new ModelRenderer(this);
        LArmParts.setPos(6.0F, -52.0F, 3.0F);
        BullMan.addChild(LArmParts);


        LUpperArm = new ModelRenderer(this);
        LUpperArm.setPos(3.0F, 26.0F, -2.5F);
        LArmParts.addChild(LUpperArm);
        LUpperArm.texOffs(0, 44).addBox(-3.0F, -31.0F, -0.5F, 8.0F, 10.0F, 8.0F, 0.0F, false);

        LElbow = new ModelRenderer(this);
        LElbow.setPos(1.0F, 29.0F, -2.0F);
        LArmParts.addChild(LElbow);
        LElbow.texOffs(76, 15).addBox(1.0F, -24.0F, 1.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        LArm = new ModelRenderer(this);
        LArm.setPos(2.0F, 33.5F, -3.0F);
        LArmParts.addChild(LArm);
        LArm.texOffs(50, 75).addBox(-1.0F, -24.5F, 1.0F, 6.0F, 8.0F, 6.0F, 0.0F, false);

        LHand = new ModelRenderer(this);
        LHand.setPos(2.0F, 36.0F, -3.0F);
        LArmParts.addChild(LHand);
        LHand.texOffs(25, 83).addBox(-1.0F, -19.0F, 1.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);

        Cannon = new ModelRenderer(this);
        Cannon.setPos(4.0F, 27.0F, -1.0F);
        LArmParts.addChild(Cannon);
        setRotationAngle(Cannon, 1.5708F, 0.0F, 0.0F);
        Cannon.texOffs(0, 110).addBox(-3.0F, -7.0F, -6.0F, 6.0F, 6.0F, 12.0F, 0.0F, false);

        Handle_r1 = new ModelRenderer(this);
        Handle_r1.setPos(3.0F, 16.0F, 13.0F);
        Cannon.addChild(Handle_r1);
        setRotationAngle(Handle_r1, -0.4363F, 0.0F, 0.0F);
        Handle_r1.texOffs(37, 120).addBox(-4.0F, -13.0F, -16.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);

        HeadParts = new ModelRenderer(this);
        HeadParts.setPos(-3.0F, -59.5F, 2.0F);
        BullMan.addChild(HeadParts);


        Head_r1 = new ModelRenderer(this);
        Head_r1.setPos(1.0F, -5.5F, -3.0F);
        HeadParts.addChild(Head_r1);
        Head_r1.texOffs(58, 62).addBox(-4.0F, -2.0F, -3.0F, 8.0F, 4.0F, 8.0F, 0.0F, false);

        Jaw_r1 = new ModelRenderer(this);
        Jaw_r1.setPos(1.0F, 0.5F, -3.0F);
        HeadParts.addChild(Jaw_r1);
        Jaw_r1.texOffs(43, 34).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 6.0F, 10.0F, 0.0F, false);

        REar = new ModelRenderer(this);
        REar.setPos(3.0F, 45.5F, -6.0F);
        HeadParts.addChild(REar);


        REar_r1 = new ModelRenderer(this);
        REar_r1.setPos(-7.0F, -49.5F, 3.0F);
        REar.addChild(REar_r1);
        REar_r1.texOffs(40, 15).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        LEar = new ModelRenderer(this);
        LEar.setPos(3.0F, 45.5F, -6.0F);
        HeadParts.addChild(LEar);


        LEar_r1 = new ModelRenderer(this);
        LEar_r1.setPos(3.0F, -49.5F, 3.0F);
        LEar.addChild(LEar_r1);
        LEar_r1.texOffs(0, 25).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        RHorns = new ModelRenderer(this);
        RHorns.setPos(3.0F, 43.5F, -4.0F);
        HeadParts.addChild(RHorns);


        RHorn2_r1 = new ModelRenderer(this);
        RHorn2_r1.setPos(-9.0F, -51.0F, 3.0F);
        RHorns.addChild(RHorn2_r1);
        setRotationAngle(RHorn2_r1, 0.7854F, 0.0F, 0.0F);
        RHorn2_r1.texOffs(11, 92).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 8.0F, 3.0F, 0.0F, false);

        RHorn1_r1 = new ModelRenderer(this);
        RHorn1_r1.setPos(-7.0F, -47.5F, 3.0F);
        RHorns.addChild(RHorn1_r1);
        RHorn1_r1.texOffs(22, 94).addBox(-1.0F, -5.5F, -1.0F, 2.0F, 6.0F, 3.0F, 0.0F, false);

        LHorns = new ModelRenderer(this);
        LHorns.setPos(3.0F, 43.5F, -4.0F);
        HeadParts.addChild(LHorns);


        LHorn2_r1 = new ModelRenderer(this);
        LHorn2_r1.setPos(5.0F, -51.0F, 3.0F);
        LHorns.addChild(LHorn2_r1);
        setRotationAngle(LHorn2_r1, 0.7854F, 0.0F, 0.0F);
        LHorn2_r1.texOffs(0, 92).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 8.0F, 3.0F, 0.0F, false);

        LHorn1_r1 = new ModelRenderer(this);
        LHorn1_r1.setPos(3.0F, -47.5F, 3.0F);
        LHorns.addChild(LHorn1_r1);
        LHorn1_r1.texOffs(93, 15).addBox(-1.0F, -5.5F, -1.0F, 2.0F, 6.0F, 3.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = 2.0F;
        int c = entityIn.charging();

        if (f < 2.0F) {
            f = 2.0F;
        }
        this.HeadParts.visible = true;
        this.HeadParts.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.HeadParts.xRot = headPitch * ((float)Math.PI / 180F);
        this.RLegParts.xRot = (float)((double)this.RLegParts.xRot * 0.5D);
        this.LLegParts.xRot = (float)((double)this.LLegParts.xRot * 0.5D);
        this.RLegParts.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.LLegParts.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / f;
        if (this.RLegParts.xRot > 0.4F) {
            this.RLegParts.xRot = 0.4F;
        }

        if (this.LLegParts.xRot > 0.4F) {
            this.LLegParts.xRot = 0.4F;
        }

        if (this.RLegParts.xRot < -0.4F) {
            this.RLegParts.xRot = -0.4F;
        }

        if (this.LLegParts.xRot < -0.4F) {
            this.LLegParts.xRot = -0.4F;
        }

        TaillessWretchEntity.ArmPose abstracttaillesswretchentity$armpose = entityIn.getArmPose();
        if (abstracttaillesswretchentity$armpose == TaillessWretchEntity.ArmPose.ATTACKING) {
            float f4 = MathHelper.sin(attackTime * (float)Math.PI);
            float f5 = MathHelper.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * (float)Math.PI);
            RArmParts.xRot = -1.8849558F + MathHelper.cos(ageInTicks * 0.09F) * 0.15F;
            RArmParts.xRot += f4 * 2.2F - f5 * 0.4F;
            LArmParts.xRot = -1.4F + MathHelper.cos(ageInTicks * 0.09F) * 0.15F;
        }

        if (abstracttaillesswretchentity$armpose == TaillessWretchEntity.ArmPose.SHOOT) {
            float f4 = MathHelper.sin(attackTime * (float)Math.PI);
            float f5 = MathHelper.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * (float)Math.PI);
            LArmParts.xRot = -1.8849558F + MathHelper.cos(ageInTicks * 0.09F) * 0.15F;
            LArmParts.xRot += f4 * 2.2F - f5 * 0.4F;
            this.RArmParts.xRot = (float)((double)this.RArmParts.xRot * 0.5D);
            this.RArmParts.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
            if (this.RArmParts.xRot > 0.4F) {
                this.RArmParts.xRot = 0.4F;
            }
            if (this.RArmParts.xRot < -0.4F) {
                this.RArmParts.xRot = -0.4F;
            }
        }

        if (abstracttaillesswretchentity$armpose == AbstractTaillessEntity.ArmPose.NEUTRAL){
            this.RArmParts.xRot = (float)((double)this.RArmParts.xRot * 0.5D);
            this.LArmParts.xRot = (float)((double)this.LArmParts.xRot * 0.5D);
            this.RArmParts.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
            this.LArmParts.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
            if (this.RArmParts.xRot > 0.4F) {
                this.RArmParts.xRot = 0.4F;
            }

            if (this.LArmParts.xRot > 0.4F) {
                this.LArmParts.xRot = 0.4F;
            }

            if (this.RArmParts.xRot < -0.4F) {
                this.RArmParts.xRot = -0.4F;
            }

            if (this.LArmParts.xRot < -0.4F) {
                this.LArmParts.xRot = -0.4F;
            }

            if (c > 0){
                float d = ageInTicks / 60.0F;
                this.RLegParts.xRot = (float)((double)this.RLegParts.xRot * 0.5D);
                this.LLegParts.xRot = (float)((double)this.LLegParts.xRot * 0.5D);
                this.RLegParts.xRot = MathHelper.cos(d * 0.6662F) * 1.4F * d / f;
                this.LLegParts.xRot = MathHelper.cos(d * 0.6662F + (float)Math.PI) * 1.4F * d / f;
                if (this.RLegParts.xRot > 0.4F) {
                    this.RLegParts.xRot = 0.4F;
                }

                if (this.LLegParts.xRot > 0.4F) {
                    this.LLegParts.xRot = 0.4F;
                }

                if (this.RLegParts.xRot < -0.4F) {
                    this.RLegParts.xRot = -0.4F;
                }

                if (this.LLegParts.xRot < -0.4F) {
                    this.LLegParts.xRot = -0.4F;
                }
            }
        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        BullMan.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    @Override
    public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
        this.getArm(sideIn).translateAndRotate(matrixStackIn);
    }

    private ModelRenderer getArm(HandSide side) {
        return side == HandSide.LEFT ? this.LHand : this.RHand;
    }
}
