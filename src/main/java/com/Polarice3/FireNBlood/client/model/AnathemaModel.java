package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.tailless.masters.TaillessAnathemaEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class AnathemaModel extends SegmentedModel<TaillessAnathemaEntity> {
    private final ModelRenderer Bulldrake;
    private final ModelRenderer Body;
    private final ModelRenderer Body_r1;
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
    private final ModelRenderer HeadParts;
    private final ModelRenderer Head_r1;
    private final ModelRenderer JawUp;
    private final ModelRenderer JawDown;
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
    private final ModelRenderer Arrows;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer RWings;
    private final ModelRenderer RWing;
    private final ModelRenderer RWing2;
    private final ModelRenderer LWings;
    private final ModelRenderer LWing;
    private final ModelRenderer LWing2;
    private final ModelRenderer Neck;

    public AnathemaModel() {
        texWidth = 512;
        texHeight = 512;

        Bulldrake = new ModelRenderer(this);
        Bulldrake.setPos(0.0F, -1.0F, 2.0F);


        Body = new ModelRenderer(this);
        Body.setPos(0.0F, -17.0F, 3.0F);
        Bulldrake.addChild(Body);
        setRotationAngle(Body, 1.309F, 0.0F, 0.0F);
        Body.texOffs(170, 141).addBox(-8.0F, 16.0F, -6.0002F, 16.0F, 8.0F, 12.0F, 0.0F, false);
        Body.texOffs(153, 58).addBox(-12.0F, 0.0F, -10.0002F, 24.0F, 16.0F, 16.0F, 0.0F, false);
        Body.texOffs(0, 169).addBox(-8.0F, 24.0F, -10.0002F, 16.0F, 12.0F, 16.0F, 0.0F, false);

        Body_r1 = new ModelRenderer(this);
        Body_r1.setPos(0.0F, 24.0F, -6.0F);
        Body.addChild(Body_r1);
        setRotationAngle(Body_r1, 0.1745F, 0.0F, 0.0F);
        Body_r1.texOffs(0, 132).addBox(-16.0F, -38.0F, -2.0002F, 32.0F, 16.0F, 20.0F, 0.0F, false);

        RLegParts = new ModelRenderer(this);
        RLegParts.setPos(-8.0F, -7.0F, 30.0F);
        Bulldrake.addChild(RLegParts);
        setRotationAngle(RLegParts, -0.0436F, 0.0F, 0.0F);


        RThigh = new ModelRenderer(this);
        RThigh.setPos(0.0F, -4.2541F, 3.6454F);
        RLegParts.addChild(RThigh);
        setRotationAngle(RThigh, -0.4363F, 0.0F, 0.0F);
        RThigh.texOffs(106, 176).addBox(-8.0F, -2.0F, -8.0F, 8.0F, 16.0F, 12.0F, 0.0F, false);

        RKnee = new ModelRenderer(this);
        RKnee.setPos(-2.0F, 7.7459F, -0.3546F);
        RLegParts.addChild(RKnee);
        setRotationAngle(RKnee, -0.0436F, 0.0F, 0.0F);
        RKnee.texOffs(0, 198).addBox(-6.0F, 0.0F, -6.0F, 8.0F, 12.0F, 8.0F, 0.0F, false);

        RLeg = new ModelRenderer(this);
        RLeg.setPos(-2.0F, 18.7459F, 1.6454F);
        RLegParts.addChild(RLeg);
        setRotationAngle(RLeg, 0.4363F, 0.0F, 0.0F);
        RLeg.texOffs(0, 33).addBox(-6.0F, 0.0F, -4.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);

        RFoot = new ModelRenderer(this);
        RFoot.setPos(-2.0F, 24.7459F, 2.6454F);
        RLegParts.addChild(RFoot);
        RFoot.texOffs(176, 181).addBox(-6.0F, 6.0F, -8.0F, 8.0F, 4.0F, 12.0F, 0.0F, false);

        LLegParts = new ModelRenderer(this);
        LLegParts.setPos(8.0F, -7.0F, 30.0F);
        Bulldrake.addChild(LLegParts);
        setRotationAngle(LLegParts, -0.0436F, 0.0F, 0.0F);


        LThigh = new ModelRenderer(this);
        LThigh.setPos(0.0F, -5.0F, 6.0F);
        LLegParts.addChild(LThigh);
        setRotationAngle(LThigh, -0.4363F, 0.0F, 0.0F);
        LThigh.texOffs(65, 169).addBox(0.0F, -2.0F, -8.0F, 8.0F, 16.0F, 12.0F, 0.0F, false);

        LKnee = new ModelRenderer(this);
        LKnee.setPos(6.0F, 7.0F, 2.0F);
        LLegParts.addChild(LKnee);
        setRotationAngle(LKnee, -0.0436F, 0.0F, 0.0F);
        LKnee.texOffs(147, 193).addBox(-6.0F, 0.0F, -6.0F, 8.0F, 12.0F, 8.0F, 0.0F, false);

        LLeg = new ModelRenderer(this);
        LLeg.setPos(6.0F, 19.0F, 3.0F);
        LLegParts.addChild(LLeg);
        setRotationAngle(LLeg, 0.4363F, 0.0F, 0.0F);
        LLeg.texOffs(0, 0).addBox(-6.0F, 0.0F, -4.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);

        LFoot = new ModelRenderer(this);
        LFoot.setPos(6.0F, 25.0F, 3.0F);
        LLegParts.addChild(LFoot);
        LFoot.texOffs(147, 176).addBox(-6.0F, 6.0F, -8.0F, 8.0F, 4.0F, 12.0F, 0.0F, false);

        HeadParts = new ModelRenderer(this);
        HeadParts.setPos(0.6666F, -18.0F, -20.6666F);

        Head_r1 = new ModelRenderer(this);
        Head_r1.setPos(1.3334F, -9.0F, -1.3334F);
        HeadParts.addChild(Head_r1);
        Head_r1.texOffs(153, 91).addBox(-12.0F, 0.0F, -14.0F, 20.0F, 12.0F, 16.0F, 0.0F, false);

        JawUp = new ModelRenderer(this);
        JawUp.setPos(1.3334F, -5.0F, -1.3334F);
        HeadParts.addChild(JawUp);
        JawUp.texOffs(105, 141).addBox(-12.0F, 2.0F, -22.0F, 20.0F, 10.0F, 24.0F, 0.0F, false);

        JawDown = new ModelRenderer(this);
        JawDown.setPos(1.3334F, 1.0F, -1.3334F);
        HeadParts.addChild(JawDown);
        JawDown.texOffs(145, 27).addBox(-12.0F, 2.0F, -22.0F, 20.0F, 6.0F, 24.0F, 0.0F, false);

        REar = new ModelRenderer(this);
        REar.setPos(1.3334F, 89.0F, -13.3334F);
        HeadParts.addChild(REar);


        REar_r1 = new ModelRenderer(this);
        REar_r1.setPos(-14.0F, -99.0F, 6.0F);
        REar.addChild(REar_r1);
        REar_r1.texOffs(194, 162).addBox(-2.0F, 1.0F, -4.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

        LEar = new ModelRenderer(this);
        LEar.setPos(5.3334F, 89.0F, -13.3334F);
        HeadParts.addChild(LEar);


        LEar_r1 = new ModelRenderer(this);
        LEar_r1.setPos(6.0F, -99.0F, 6.0F);
        LEar.addChild(LEar_r1);
        LEar_r1.texOffs(105, 141).addBox(-2.0F, 1.0F, -4.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

        RHorns = new ModelRenderer(this);
        RHorns.setPos(5.3334F, 89.0F, -13.3334F);
        HeadParts.addChild(RHorns);


        RHorn2_r1 = new ModelRenderer(this);
        RHorn2_r1.setPos(-22.0F, -104.0F, 10.0F);
        RHorns.addChild(RHorn2_r1);
        RHorn2_r1.texOffs(0, 99).addBox(-2.0F, -6.0F, -4.0F, 4.0F, 16.0F, 4.0F, 0.0F, false);

        RHorn1_r1 = new ModelRenderer(this);
        RHorn1_r1.setPos(-24.0F, -99.0F, 10.0F);
        RHorns.addChild(RHorn1_r1);
        RHorn1_r1.texOffs(141, 33).addBox(4.0F, -3.0F, -4.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        LHorns = new ModelRenderer(this);
        LHorns.setPos(5.3334F, 89.0F, -13.3334F);
        HeadParts.addChild(LHorns);


        LHorn2_r1 = new ModelRenderer(this);
        LHorn2_r1.setPos(10.0F, -104.0F, 10.0F);
        LHorns.addChild(LHorn2_r1);
        LHorn2_r1.texOffs(0, 66).addBox(-2.0F, -6.0F, -4.0F, 4.0F, 16.0F, 4.0F, 0.0F, false);

        LHorn1_r1 = new ModelRenderer(this);
        LHorn1_r1.setPos(6.0F, -99.0F, 10.0F);
        LHorns.addChild(LHorn1_r1);
        LHorn1_r1.texOffs(0, 132).addBox(-2.0F, -3.0F, -4.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        Arrows = new ModelRenderer(this);
        Arrows.setPos(0.0F, -29.0F, 3.0F);
        Bulldrake.addChild(Arrows);
        setRotationAngle(Arrows, -0.4363F, 0.0F, 0.0F);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-1.0F, -2.0F, 3.0F);
        Arrows.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, 0.3927F);
        cube_r1.texOffs(17, 66).addBox(5.0F, -14.0F, -5.0F, 2.0F, 24.0F, 2.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(6.0F, -5.0F, 3.0F);
        Arrows.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.48F, 0.0F, 0.7854F);
        cube_r2.texOffs(33, 198).addBox(5.0F, -8.0F, -11.0F, 2.0F, 16.0F, 2.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(-1.0F, -4.0F, 3.0F);
        Arrows.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.4363F, 0.0F, 0.7854F);
        cube_r3.texOffs(141, 66).addBox(5.0F, -10.0F, 1.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-8.0F, -4.0F, -4.0F);
        Arrows.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.3927F, 0.0F, -0.6981F);
        cube_r4.texOffs(141, 99).addBox(-2.0F, 0.0F, -6.0F, 2.0F, 16.0F, 2.0F, 0.0F, false);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(-1.0F, -4.0F, 3.0F);
        Arrows.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, -0.4363F);
        cube_r5.texOffs(17, 99).addBox(-7.0F, -6.0F, -3.0F, 2.0F, 20.0F, 2.0F, 0.0F, false);

        RWings = new ModelRenderer(this);
        RWings.setPos(-16.0F, -14.0F, -5.0F);
        Bulldrake.addChild(RWings);


        RWing = new ModelRenderer(this);
        RWing.setPos(2.0F, -10.0F, 1.0F);
        RWings.addChild(RWing);
        setRotationAngle(RWing, 0.0F, 0.0F, -0.7854F);
        RWing.texOffs(141, 18).addBox(-62.0F, 6.0F, -8.0F, 60.0F, 4.0F, 4.0F, 0.0F, false);
        RWing.texOffs(0, 99).addBox(-58.0F, 6.0F, -4.0F, 56.0F, 4.0F, 28.0F, 0.0F, false);

        RWing2 = new ModelRenderer(this);
        RWing2.setPos(-27.0F, 33.0F, -3.0F);
        RWings.addChild(RWing2);
        setRotationAngle(RWing2, 0.0F, 0.7854F, 0.6109F);
        RWing2.texOffs(85, 132).addBox(-62.0F, 6.0F, -8.0F, 60.0F, 4.0F, 4.0F, 0.0F, false);
        RWing2.texOffs(0, 0).addBox(-58.0F, 6.0F, -4.0F, 56.0F, 4.0F, 28.0F, 0.0F, false);

        LWings = new ModelRenderer(this);
        LWings.setPos(16.0F, -14.0F, -5.0F);
        Bulldrake.addChild(LWings);


        LWing = new ModelRenderer(this);
        LWing.setPos(-2.0F, -11.0F, 1.0F);
        LWings.addChild(LWing);
        setRotationAngle(LWing, 0.0F, 0.0F, 0.7854F);
        LWing.texOffs(141, 9).addBox(2.0F, 6.0F, -8.0F, 60.0F, 4.0F, 4.0F, 0.0F, false);
        LWing.texOffs(0, 66).addBox(2.0F, 6.0F, -4.0F, 56.0F, 4.0F, 28.0F, 0.0F, false);

        LWing2 = new ModelRenderer(this);
        LWing2.setPos(27.0F, 32.0F, -3.0F);
        LWings.addChild(LWing2);
        setRotationAngle(LWing2, 0.0F, -0.7854F, -0.6109F);
        LWing2.texOffs(141, 0).addBox(2.0F, 6.0F, -8.0F, 60.0F, 4.0F, 4.0F, 0.0F, false);
        LWing2.texOffs(0, 33).addBox(2.0F, 6.0F, -4.0F, 56.0F, 4.0F, 28.0F, 0.0F, false);

        Neck = new ModelRenderer(this);
        Neck.setPos(0.0F, 0.0F, 0.0F);
        Bulldrake.addChild(Neck);
        Neck.texOffs(0, 226).addBox(-7.0F, -25.0F, -20.0F, 14.0F, 14.0F, 20.0F, 0.0F, false);
        Neck.addChild(HeadParts);
    }

    @Override
    public void setupAnim(TaillessAnathemaEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = 2.0F;

        if (f < 2.0F) {
            f = 2.0F;
        }
        this.HeadParts.visible = true;
        this.HeadParts.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.HeadParts.xRot = headPitch * ((float)Math.PI / 180F);
        if (entityIn.isRoaring()) {
            this.JawUp.xRot = -this.JawDown.xRot;
            this.JawDown.xRot = 0.278F;
        }
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
        this.RWings.xRot = (float)((double)this.RWings.xRot * 0.5D);
        this.LWings.xRot = (float)((double)this.LWings.xRot * 0.5D);
        this.RWings.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
        this.LWings.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
        if (this.RWings.xRot > 0.4F) {
            this.RWings.xRot = 0.4F;
        }

        if (this.LWings.xRot > 0.4F) {
            this.LWings.xRot = 0.4F;
        }

        if (this.RWings.xRot < -0.4F) {
            this.RWings.xRot = -0.4F;
        }

        if (this.LWings.xRot < -0.4F) {
            this.LWings.xRot = -0.4F;
        }
    }

    @Override
    public void prepareMobModel(TaillessAnathemaEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick){
        super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
        int l = entityIn.Attacking();
        if (l > 0) {
            float f = MathHelper.triangleWave((float)l - partialTick, 10.0F);
            float f1 = (1.0F + f) * 0.5F;
            float f2 = f1 * f1 * f1 * 12.0F;
            float f3 = f2 * MathHelper.sin(this.Neck.xRot);
            this.Neck.z = -6.5F + f2;
            this.Neck.y = -0.5F - f3;
            float f4 = MathHelper.sin(((float)50 - partialTick) / 50.0F * (float)Math.PI * 0.25F);
            this.JawUp.xRot = -this.JawDown.xRot;
            this.JawDown.xRot = ((float)Math.PI / 8F) * f4;
        } else {
            float f6 = -1.0F * MathHelper.sin(this.Neck.xRot);
            this.Neck.x = 0.0F;
            this.Neck.y = -0.5F - f6;
            this.Neck.z = 5.5F;
            this.JawUp.xRot = 0F;
            this.JawDown.xRot = 0F;
        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Bulldrake.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.Neck, this.Body, this.RLegParts, this.LLegParts, this.RWings, this.LWings);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
