package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessWretchEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;


public class TaillessWretchModel<T extends TaillessWretchEntity> extends SegmentedModel<T> implements IHasArm {
    private final ModelRenderer BullMan;
    private final ModelRenderer Body;
    private final ModelRenderer BodyChest_r1;
    private final ModelRenderer RLegParts;
    private final ModelRenderer RThigh;
    private final ModelRenderer RThigh_r1;
    private final ModelRenderer RLeg;
    private final ModelRenderer RFoot;
    private final ModelRenderer LLegParts;
    private final ModelRenderer LThigh;
    private final ModelRenderer LThigh_r1;
    private final ModelRenderer LLeg;
    private final ModelRenderer LFoot;
    private final ModelRenderer RArmParts;
    private final ModelRenderer RUpperArm;
    private final ModelRenderer RArm;
    private final ModelRenderer Mace;
    private final ModelRenderer cube_1;
    private final ModelRenderer cube_2;
    private final ModelRenderer LArmParts;
    private final ModelRenderer LUpperArm;
    private final ModelRenderer LArm;
    private final ModelRenderer HeadParts;
    private final ModelRenderer Head_r1;
    private final ModelRenderer Jaw_r1;
    private final ModelRenderer REar;
    private final ModelRenderer REar_r1;
    private final ModelRenderer LEar;
    private final ModelRenderer LEar_r1;
    private final ModelRenderer horns;
    private final ModelRenderer lefthorns;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer righthorns;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;

    public TaillessWretchModel() {
        texWidth = 64;
        texHeight = 64;

        BullMan = new ModelRenderer(this);
        BullMan.setPos(0.0F, 24.0F, 0.0F);


        Body = new ModelRenderer(this);
        Body.setPos(0.0F, -12.1723F, 0.3765F);
        BullMan.addChild(Body);
        Body.texOffs(0, 16).addBox(-3.0F, -2.8277F, -1.3765F, 6.0F, 5.0F, 3.0F, 0.0F, false);
        Body.texOffs(1, 9).addBox(-3.0F, 2.1723F, -1.3765F, 6.0F, 2.0F, 4.0F, 0.0F, false);

        BodyChest_r1 = new ModelRenderer(this);
        BodyChest_r1.setPos(0.0F, -2.8447F, -0.7471F);
        Body.addChild(BodyChest_r1);
        setRotationAngle(BodyChest_r1, 0.2618F, 0.0F, 0.0F);
        BodyChest_r1.texOffs(0, 0).addBox(-4.0F, -2.0F, -2.5F, 8.0F, 4.0F, 5.0F, 0.0F, false);

        RLegParts = new ModelRenderer(this);
        RLegParts.setPos(-2.0F, -8.0F, 0.0F);
        BullMan.addChild(RLegParts);


        RThigh = new ModelRenderer(this);
        RThigh.setPos(0.0F, 1.5F, 0.5F);
        RLegParts.addChild(RThigh);


        RThigh_r1 = new ModelRenderer(this);
        RThigh_r1.setPos(0.0F, 0.0F, 0.0F);
        RThigh.addChild(RThigh_r1);
        setRotationAngle(RThigh_r1, -0.4363F, 0.0F, 0.0F);
        RThigh_r1.texOffs(17, 30).addBox(-1.0F, -2.5F, -1.5F, 2.0F, 5.0F, 3.0F, 0.0F, false);

        RLeg = new ModelRenderer(this);
        RLeg.setPos(0.0F, 5.0937F, 1.0774F);
        RLegParts.addChild(RLeg);
        setRotationAngle(RLeg, 0.4363F, 0.0F, 0.0F);
        RLeg.texOffs(0, 33).addBox(-1.0F, -2.0F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);

        RFoot = new ModelRenderer(this);
        RFoot.setPos(0.0F, 7.5F, 0.5F);
        RLegParts.addChild(RFoot);
        RFoot.texOffs(30, 12).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, 0.0F, false);

        LLegParts = new ModelRenderer(this);
        LLegParts.setPos(2.0F, -8.0F, 0.0F);
        BullMan.addChild(LLegParts);


        LThigh = new ModelRenderer(this);
        LThigh.setPos(0.0F, 1.5F, 0.5F);
        LLegParts.addChild(LThigh);


        LThigh_r1 = new ModelRenderer(this);
        LThigh_r1.setPos(0.0F, 0.0F, 0.0F);
        LThigh.addChild(LThigh_r1);
        setRotationAngle(LThigh_r1, -0.4363F, 0.0F, 0.0F);
        LThigh_r1.texOffs(30, 4).addBox(-1.0F, -2.5F, -1.5F, 2.0F, 5.0F, 3.0F, 0.0F, false);

        LLeg = new ModelRenderer(this);
        LLeg.setPos(0.0F, 5.0937F, 1.0774F);
        LLegParts.addChild(LLeg);
        setRotationAngle(LLeg, 0.4363F, 0.0F, 0.0F);
        LLeg.texOffs(32, 22).addBox(-1.0F, -2.0F, -0.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);

        LFoot = new ModelRenderer(this);
        LFoot.setPos(0.0F, 7.5F, 0.5F);
        LLegParts.addChild(LFoot);
        LFoot.texOffs(28, 18).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, 0.0F, false);

        RArmParts = new ModelRenderer(this);
        RArmParts.setPos(-4.0F, -15.0F, 0.0F);
        BullMan.addChild(RArmParts);


        RUpperArm = new ModelRenderer(this);
        RUpperArm.setPos(-1.0F, 1.0F, -0.5F);
        RArmParts.addChild(RUpperArm);
        RUpperArm.texOffs(10, 24).addBox(-1.0F, -3.0F, -1.5F, 2.0F, 6.0F, 3.0F, 0.0F, false);

        RArm = new ModelRenderer(this);
        RArm.setPos(-1.0F, 6.1805F, -1.1288F);
        RArmParts.addChild(RArm);
        setRotationAngle(RArm, -0.2618F, 0.0F, 0.0F);
        RArm.texOffs(0, 24).addBox(-1.0F, -3.0F, -1.5F, 2.0F, 6.0F, 3.0F, 0.0F, false);


        Mace = new ModelRenderer(this);
        Mace.setPos(-0.5F, 7.6282F, -6.1219F);
        RArmParts.addChild(Mace);


        cube_1 = new ModelRenderer(this);
        cube_1.setPos(-0.5F, -0.7765F, -2.8978F);
        Mace.addChild(cube_1);
        setRotationAngle(cube_1, -0.2618F, 0.0F, 0.0F);
        cube_1.texOffs(52, 4).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, false);

        cube_2 = new ModelRenderer(this);
        cube_2.setPos(-1.0F, 1.8718F, 3.1219F);
        Mace.addChild(cube_2);
        setRotationAngle(cube_2, -0.2618F, 0.0F, 0.0F);
        cube_2.texOffs(40, 5).addBox(0.0F, -1.5F, -5.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);

        LArmParts = new ModelRenderer(this);
        LArmParts.setPos(4.0F, -15.0F, 0.0F);
        BullMan.addChild(LArmParts);


        LUpperArm = new ModelRenderer(this);
        LUpperArm.setPos(1.0F, 1.0F, -0.5F);
        LArmParts.addChild(LUpperArm);
        LUpperArm.texOffs(25, 24).addBox(-1.0F, -3.0F, -1.5F, 2.0F, 6.0F, 3.0F, 0.0F, false);

        LArm = new ModelRenderer(this);
        LArm.setPos(1.0F, 6.1805F, -1.1288F);
        LArmParts.addChild(LArm);
        setRotationAngle(LArm, -0.2618F, 0.0F, 0.0F);
        LArm.texOffs(18, 18).addBox(-1.0F, -3.0F, -1.5F, 2.0F, 6.0F, 3.0F, 0.0F, false);

        HeadParts = new ModelRenderer(this);
        HeadParts.setPos(0.0F, -16.0F, -2.0F);
        BullMan.addChild(HeadParts);


        Head_r1 = new ModelRenderer(this);
        Head_r1.setPos(0.0F, -2.75F, -0.15F);
        HeadParts.addChild(Head_r1);
        setRotationAngle(Head_r1, 0.0F, 0.0F, 0.0F);
        Head_r1.texOffs(40, 0).addBox(-2.0F, -1.25F, -1.85F, 4.0F, 2.0F, 3.0F, 0.0F, false);

        Jaw_r1 = new ModelRenderer(this);
        Jaw_r1.setPos(0.0F, -2.75F, -0.15F);
        HeadParts.addChild(Jaw_r1);
        setRotationAngle(Jaw_r1, 0.0F, 0.0F, 0.0F);
        Jaw_r1.texOffs(17, 11).addBox(-2.0F, 0.75F, -3.85F, 4.0F, 2.0F, 5.0F, 0.0F, false);

        REar = new ModelRenderer(this);
        REar.setPos(0.0F, 17.0F, 0.0F);
        HeadParts.addChild(REar);


        REar_r1 = new ModelRenderer(this);
        REar_r1.setPos(0.0F, -19.75F, -0.15F);
        REar.addChild(REar_r1);
        setRotationAngle(REar_r1, 0.0F, 0.0F, 0.0F);
        REar_r1.texOffs(0, 0).addBox(-3.0F, -0.75F, -0.85F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        LEar = new ModelRenderer(this);
        LEar.setPos(0.0F, 17.0F, 0.0F);
        HeadParts.addChild(LEar);


        LEar_r1 = new ModelRenderer(this);
        LEar_r1.setPos(0.0F, -19.75F, -0.15F);
        LEar.addChild(LEar_r1);
        setRotationAngle(LEar_r1, 0.0F, 0.0F, 0.0F);
        LEar_r1.texOffs(0, 2).addBox(2.0F, -0.75F, -0.85F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        horns = new ModelRenderer(this);
        horns.setPos(0.0F, 16.0F, 2.0F);
        HeadParts.addChild(horns);


        lefthorns = new ModelRenderer(this);
        lefthorns.setPos(3.3276F, -19.66F, -1.8415F);
        horns.addChild(lefthorns);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.519F, -0.1981F, -0.3415F);
        lefthorns.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 1.0472F, -0.5672F);
        cube_r1.texOffs(32, 0).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(-0.519F, 0.1981F, 0.3415F);
        lefthorns.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, -0.3927F);
        cube_r2.texOffs(25, 18).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        righthorns = new ModelRenderer(this);
        righthorns.setPos(-3.3276F, -19.66F, -1.8415F);
        horns.addChild(righthorns);


        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(-0.519F, -0.1981F, -0.3415F);
        righthorns.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, -1.0472F, 0.5672F);
        cube_r3.texOffs(17, 9).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(0.519F, 0.1981F, 0.3415F);
        righthorns.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, 0.3927F);
        cube_r4.texOffs(7, 24).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
    }


    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        BullMan.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.RArmParts, this.LArmParts, this.RLegParts, this.LLegParts, this.HeadParts);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
            modelRenderer.xRot = x;
            modelRenderer.yRot = y;
            modelRenderer.zRot = z;
        }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = 2.0F;

        if (f < 2.0F) {
            f = 2.0F;
        }
        this.HeadParts.visible = true;
        this.HeadParts.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.HeadParts.xRot = headPitch * ((float)Math.PI / 180F);
        this.Body.xRot = 0.0F;
        this.RLegParts.xRot -= 0.0F;
        this.LLegParts.xRot -= 0.0F;
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

        float f3 = -14.0F;

        TaillessWretchEntity.ArmPose abstracttaillesswretchentity$armpose = entityIn.getArmPose();
        if (abstracttaillesswretchentity$armpose == TaillessWretchEntity.ArmPose.ATTACKING) {
            float f4 = MathHelper.sin(attackTime * (float)Math.PI);
            float f5 = MathHelper.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * (float)Math.PI);
            RArmParts.xRot = -1.8849558F + MathHelper.cos(ageInTicks * 0.09F) * 0.15F;
            RArmParts.xRot += f4 * 2.2F - f5 * 0.4F;
            this.LArmParts.xRot = (float)((double)this.LArmParts.xRot * 0.5D);
            this.LArmParts.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
            if (this.LArmParts.xRot > 0.4F) {
                this.LArmParts.xRot = 0.4F;
            }
            if (this.LArmParts.xRot < -0.4F) {
                this.LArmParts.xRot = -0.4F;
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
        }

    }

    @Override
    public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
        this.getArm(sideIn).translateAndRotate(matrixStackIn);
    }

    private ModelRenderer getArm(HandSide side) {
        return side == HandSide.LEFT ? this.LArmParts : this.RArmParts;
    }
}
