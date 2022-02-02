package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessHorrorEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class BulldrakeModel extends SegmentedModel<TaillessHorrorEntity> {
    private final ModelRenderer Bulldrake;
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
    private final ModelRenderer RWing;
    private final ModelRenderer LWing;
    private final ModelRenderer Arrows;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;

    public BulldrakeModel() {
        textureWidth = 128;
        textureHeight = 128;

        Bulldrake = new ModelRenderer(this);
        Bulldrake.setRotationPoint(0.0F, 16.0F, 2.0F);


        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, 0.0F, 0.0F);
        Bulldrake.addChild(Body);
        setRotationAngle(Body, 1.5708F, 0.0F, 0.0F);
        Body.setTextureOffset(78, 54).addBox(-4.0F, 4.0F, -2.0001F, 8.0F, 4.0F, 6.0F, 0.0F, false);
        Body.setTextureOffset(45, 61).addBox(-6.0F, -4.0F, -4.0001F, 12.0F, 8.0F, 8.0F, 0.0F, false);
        Body.setTextureOffset(0, 0).addBox(-8.0F, -12.0F, -6.0001F, 16.0F, 8.0F, 10.0F, 0.0F, false);
        Body.setTextureOffset(29, 78).addBox(-4.0F, 8.0F, -4.0001F, 8.0F, 6.0F, 8.0F, 0.0F, false);

        RLegParts = new ModelRenderer(this);
        RLegParts.setRotationPoint(-4.0F, 0.0F, 11.0F);
        Bulldrake.addChild(RLegParts);
        setRotationAngle(RLegParts, 1.5708F, 0.0F, 0.0F);


        RThigh = new ModelRenderer(this);
        RThigh.setRotationPoint(0.0F, 2.0F, -1.0F);
        RLegParts.addChild(RThigh);
        setRotationAngle(RThigh, -0.4363F, 0.0F, 0.0F);
        RThigh.setTextureOffset(62, 83).addBox(-4.0F, -5.0F, -3.0F, 4.0F, 8.0F, 6.0F, 0.0F, false);

        RKnee = new ModelRenderer(this);
        RKnee.setRotationPoint(-1.0F, 8.0F, -3.0F);
        RLegParts.addChild(RKnee);
        setRotationAngle(RKnee, -0.2618F, 0.0F, 0.0F);
        RKnee.setTextureOffset(17, 93).addBox(-3.0F, -4.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        RLeg = new ModelRenderer(this);
        RLeg.setRotationPoint(-1.0F, 14.0F, -3.0F);
        RLegParts.addChild(RLeg);
        RLeg.setTextureOffset(47, 93).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 6.0F, 2.0F, 0.0F, false);

        RFoot = new ModelRenderer(this);
        RFoot.setRotationPoint(-1.0F, 18.0F, -4.0F);
        RLegParts.addChild(RFoot);
        setRotationAngle(RFoot, 0.4363F, 0.0F, 0.0F);
        RFoot.setTextureOffset(86, 65).addBox(-3.0F, -1.0F, -3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

        LLegParts = new ModelRenderer(this);
        LLegParts.setRotationPoint(6.0F, 0.0F, 13.0F);
        Bulldrake.addChild(LLegParts);
        setRotationAngle(LLegParts, 1.5708F, 0.0F, 0.0F);


        LThigh = new ModelRenderer(this);
        LThigh.setRotationPoint(-2.0F, 0.0F, -1.0F);
        LLegParts.addChild(LThigh);
        setRotationAngle(LThigh, -0.4363F, 0.0F, 0.0F);
        LThigh.setTextureOffset(0, 81).addBox(0.0F, -5.0F, -3.0F, 4.0F, 8.0F, 6.0F, 0.0F, false);

        LKnee = new ModelRenderer(this);
        LKnee.setRotationPoint(1.0F, 6.0F, -3.0F);
        LLegParts.addChild(LKnee);
        setRotationAngle(LKnee, -0.2618F, 0.0F, 0.0F);
        LKnee.setTextureOffset(83, 92).addBox(-3.0F, -4.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        LLeg = new ModelRenderer(this);
        LLeg.setRotationPoint(1.0F, 12.0F, -3.0F);
        LLegParts.addChild(LLeg);
        LLeg.setTextureOffset(34, 93).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 6.0F, 2.0F, 0.0F, false);

        LFoot = new ModelRenderer(this);
        LFoot.setRotationPoint(1.0F, 16.0F, -4.0F);
        LLegParts.addChild(LFoot);
        setRotationAngle(LFoot, 0.4363F, 0.0F, 0.0F);
        LFoot.setTextureOffset(83, 83).addBox(-3.0F, -1.0F, -3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

        HeadParts = new ModelRenderer(this);
        HeadParts.setRotationPoint(0.3333F, 1.5F, -11.3333F);
        Bulldrake.addChild(HeadParts);


        Head_r1 = new ModelRenderer(this);
        Head_r1.setRotationPoint(0.6667F, -2.5F, -0.6667F);
        HeadParts.addChild(Head_r1);
        Head_r1.setTextureOffset(0, 66).addBox(-6.0F, -4.0F, -6.0F, 10.0F, 6.0F, 8.0F, 0.0F, false);

        JawUp = new ModelRenderer(this);
        JawUp.setRotationPoint(0.6667F, -0.5F, -0.6667F);
        HeadParts.addChild(JawUp);
        JawUp.setTextureOffset(0, 39).addBox(-6.0F, -3.0F, -10.0F, 10.0F, 5.0F, 12.0F, 0.0F, false);
        JawUp.setTextureOffset(0, 110).addBox(-6.0F, -3.0F, -10.0F, 10.0F, 4.0F, 10.0F, 0.0F, false);

        JawDown = new ModelRenderer(this);
        JawDown.setRotationPoint(0.6667F, 2.5F, -0.6667F);
        HeadParts.addChild(JawDown);
        JawDown.setTextureOffset(33, 45).addBox(-6.0F, -3.0F, -10.0F, 10.0F, 3.0F, 12.0F, 0.0F, false);

        REar = new ModelRenderer(this);
        REar.setRotationPoint(0.6667F, 46.5F, -6.6667F);
        HeadParts.addChild(REar);


        REar_r1 = new ModelRenderer(this);
        REar_r1.setRotationPoint(-7.0F, -49.5F, 3.0F);
        REar.addChild(REar_r1);
        REar_r1.setTextureOffset(98, 3).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        LEar = new ModelRenderer(this);
        LEar.setRotationPoint(2.6667F, 46.5F, -6.6667F);
        HeadParts.addChild(LEar);


        LEar_r1 = new ModelRenderer(this);
        LEar_r1.setRotationPoint(3.0F, -49.5F, 3.0F);
        LEar.addChild(LEar_r1);
        LEar_r1.setTextureOffset(33, 44).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        RHorns = new ModelRenderer(this);
        RHorns.setRotationPoint(2.6667F, 46.5F, -6.6667F);
        HeadParts.addChild(RHorns);


        RHorn2_r1 = new ModelRenderer(this);
        RHorn2_r1.setRotationPoint(-11.0F, -52.0F, 5.0F);
        RHorns.addChild(RHorn2_r1);
        RHorn2_r1.setTextureOffset(0, 96).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

        RHorn1_r1 = new ModelRenderer(this);
        RHorn1_r1.setRotationPoint(-12.0F, -49.5F, 5.0F);
        RHorns.addChild(RHorn1_r1);
        RHorn1_r1.setTextureOffset(95, 74).addBox(2.0F, -5.5F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        LHorns = new ModelRenderer(this);
        LHorns.setRotationPoint(2.6667F, 46.5F, -6.6667F);
        HeadParts.addChild(LHorns);


        LHorn2_r1 = new ModelRenderer(this);
        LHorn2_r1.setRotationPoint(5.0F, -52.0F, 5.0F);
        LHorns.addChild(LHorn2_r1);
        LHorn2_r1.setTextureOffset(0, 39).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

        LHorn1_r1 = new ModelRenderer(this);
        LHorn1_r1.setRotationPoint(3.0F, -49.5F, 5.0F);
        LHorns.addChild(LHorn1_r1);
        LHorn1_r1.setTextureOffset(0, 0).addBox(-1.0F, -5.5F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        RWing = new ModelRenderer(this);
        RWing.setRotationPoint(-7.0F, 1.0F, -9.0F);
        Bulldrake.addChild(RWing);
        RWing.setTextureOffset(0, 24).addBox(-31.0F, -1.0F, -3.0F, 30.0F, 2.0F, 2.0F, 0.0F, false);
        RWing.setTextureOffset(0, 34).addBox(-29.0F, -1.0F, -1.0F, 28.0F, 2.0F, 2.0F, 0.0F, false);
        RWing.setTextureOffset(43, 0).addBox(-27.0F, -1.0F, 1.0F, 26.0F, 2.0F, 2.0F, 0.0F, false);
        RWing.setTextureOffset(61, 29).addBox(-25.0F, -1.0F, 3.0F, 24.0F, 2.0F, 2.0F, 0.0F, false);
        RWing.setTextureOffset(0, 61).addBox(-23.0F, -1.0F, 5.0F, 24.0F, 2.0F, 2.0F, 0.0F, false);
        RWing.setTextureOffset(61, 34).addBox(-21.0F, -1.0F, 7.0F, 22.0F, 2.0F, 2.0F, 0.0F, false);
        RWing.setTextureOffset(65, 20).addBox(-19.0F, -1.0F, 9.0F, 20.0F, 2.0F, 2.0F, 0.0F, false);
        RWing.setTextureOffset(66, 49).addBox(-17.0F, -1.0F, 11.0F, 18.0F, 2.0F, 2.0F, 0.0F, false);

        LWing = new ModelRenderer(this);
        LWing.setRotationPoint(7.0F, 1.0F, -9.0F);
        Bulldrake.addChild(LWing);
        LWing.setTextureOffset(0, 19).addBox(1.0F, -1.0F, -3.0F, 30.0F, 2.0F, 2.0F, 0.0F, false);
        LWing.setTextureOffset(0, 29).addBox(1.0F, -1.0F, -1.0F, 28.0F, 2.0F, 2.0F, 0.0F, false);
        LWing.setTextureOffset(33, 39).addBox(1.0F, -1.0F, 1.0F, 26.0F, 2.0F, 2.0F, 0.0F, false);
        LWing.setTextureOffset(53, 10).addBox(1.0F, -1.0F, 3.0F, 24.0F, 2.0F, 2.0F, 0.0F, false);
        LWing.setTextureOffset(43, 5).addBox(-1.0F, -1.0F, 5.0F, 24.0F, 2.0F, 2.0F, 0.0F, false);
        LWing.setTextureOffset(63, 15).addBox(-1.0F, -1.0F, 7.0F, 22.0F, 2.0F, 2.0F, 0.0F, false);
        LWing.setTextureOffset(66, 44).addBox(-1.0F, -1.0F, 9.0F, 20.0F, 2.0F, 2.0F, 0.0F, false);
        LWing.setTextureOffset(54, 78).addBox(-1.0F, -1.0F, 11.0F, 18.0F, 2.0F, 2.0F, 0.0F, false);

        Arrows = new ModelRenderer(this);
        Arrows.setRotationPoint(0.0F, -5.0F, -6.0F);
        Bulldrake.addChild(Arrows);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-0.5F, -1.0F, 1.5F);
        Arrows.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, 0.3927F);
        cube_r1.setTextureOffset(9, 96).addBox(2.5F, -11.0F, -1.5F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-0.5F, -2.0F, 1.5F);
        Arrows.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.48F, 0.0F, 0.7854F);
        cube_r2.setTextureOffset(65, 98).addBox(2.5F, -8.0F, -4.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(-0.5F, -2.0F, 1.5F);
        Arrows.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.4363F, 0.0F, 0.7854F);
        cube_r3.setTextureOffset(37, 66).addBox(2.5F, -9.0F, 1.5F, 1.0F, 10.0F, 1.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(-4.0F, -2.0F, -2.0F);
        Arrows.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.3927F, 0.0F, -0.6981F);
        cube_r4.setTextureOffset(60, 98).addBox(-1.0F, -4.0F, -2.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(-0.5F, -2.0F, 1.5F);
        Arrows.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, -0.4363F);
        cube_r5.setTextureOffset(21, 81).addBox(-3.5F, -7.0F, -0.5F, 1.0F, 10.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(TaillessHorrorEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = ((float)(entityIn.getEntityId() * 3) + ageInTicks) * 0.13F;
        float f1 = 16.0F;
        this.LWing.rotateAngleZ = MathHelper.cos(f) * 32.0F * ((float)Math.PI / 180F);
        this.RWing.rotateAngleZ = -this.LWing.rotateAngleZ;
        if (entityIn.isRoaring()) {
            this.JawUp.rotateAngleX = -0.134F;
            this.JawDown.rotateAngleX = 0.278F;
        } else {
            this.JawUp.rotateAngleX = 0F;
            this.JawDown.rotateAngleX = 0F;
        }
    }

        @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Bulldrake.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.Body);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
