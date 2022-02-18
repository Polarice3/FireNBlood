package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessHorrorEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class TaillessHorrorModel extends SegmentedModel<TaillessHorrorEntity> {
    private final ModelRenderer Bulldrake;
    private final ModelRenderer Body;
    private final ModelRenderer RLegParts;
    private final ModelRenderer RThigh;
    private final ModelRenderer RLeg;
    private final ModelRenderer RLeg_r1;
    private final ModelRenderer RFoot;
    private final ModelRenderer RFoot_r1;
    private final ModelRenderer LLegParts;
    private final ModelRenderer LThigh;
    private final ModelRenderer LLeg;
    private final ModelRenderer LLeg_r1;
    private final ModelRenderer LFoot;
    private final ModelRenderer LFoot_r1;
    private final ModelRenderer HeadParts;
    private final ModelRenderer Head_r1;
    private final ModelRenderer JawUp;
    private final ModelRenderer JawDown;
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
    private final ModelRenderer RWing;
    private final ModelRenderer LWing;
    private final ModelRenderer Arrows;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;

    public TaillessHorrorModel() {
        texWidth = 128;
        texHeight = 128;

        Bulldrake = new ModelRenderer(this);
        Bulldrake.setPos(0.0F, 24.0F, 0.0F);


        Body = new ModelRenderer(this);
        Body.setPos(2.0F, -3.0F, 34.0F);
        Bulldrake.addChild(Body);
        setRotationAngle(Body, 1.5708F, 0.0F, 0.0F);
        Body.texOffs(36, 65).addBox(-8.0F, -38.0F, -1.0F, 12.0F, 8.0F, 8.0F, 0.0F, false);
        Body.texOffs(0, 40).addBox(-10.0F, -46.0F, -3.0F, 16.0F, 8.0F, 10.0F, 0.0F, false);
        Body.texOffs(68, 73).addBox(-6.0F, -30.0F, -1.0F, 8.0F, 6.0F, 8.0F, 0.0F, false);

        RLegParts = new ModelRenderer(this);
        RLegParts.setPos(-4.0F, -6.0F, 9.0F);
        Bulldrake.addChild(RLegParts);
        setRotationAngle(RLegParts, 1.5708F, 0.0F, 0.0F);


        RThigh = new ModelRenderer(this);
        RThigh.setPos(0.0F, 2.0F, -1.0F);
        RLegParts.addChild(RThigh);
        setRotationAngle(RThigh, -0.4363F, 0.0F, 0.0F);
        RThigh.texOffs(0, 0).addBox(-4.0F, -6.0F, -3.0F, 4.0F, 9.0F, 6.0F, 0.0F, false);

        RLeg = new ModelRenderer(this);
        RLeg.setPos(-1.0F, 9.0F, -1.0F);
        RLegParts.addChild(RLeg);


        RLeg_r1 = new ModelRenderer(this);
        RLeg_r1.setPos(-1.0F, -1.0F, 0.0F);
        RLeg.addChild(RLeg_r1);
        setRotationAngle(RLeg_r1, 0.3491F, 0.0F, 0.0F);
        RLeg_r1.texOffs(80, 10).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 2.0F, 0.0F, false);

        RFoot = new ModelRenderer(this);
        RFoot.setPos(-1.0F, 18.0F, -4.0F);
        RLegParts.addChild(RFoot);
        setRotationAngle(RFoot, 0.4363F, 0.0F, 0.0F);


        RFoot_r1 = new ModelRenderer(this);
        RFoot_r1.setPos(-1.0F, -6.9971F, 4.1309F);
        RFoot.addChild(RFoot_r1);
        setRotationAngle(RFoot_r1, -0.0873F, 0.0F, 0.0F);
        RFoot_r1.texOffs(68, 65).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

        LLegParts = new ModelRenderer(this);
        LLegParts.setPos(6.0F, -6.0F, 10.0F);
        Bulldrake.addChild(LLegParts);
        setRotationAngle(LLegParts, 1.5708F, 0.0F, 0.0F);


        LThigh = new ModelRenderer(this);
        LThigh.setPos(-2.0F, 0.0F, -1.0F);
        LLegParts.addChild(LThigh);
        setRotationAngle(LThigh, -0.4363F, 0.0F, 0.0F);
        LThigh.texOffs(0, 15).addBox(0.0F, -5.0F, -3.0F, 4.0F, 8.0F, 6.0F, 0.0F, false);

        LLeg = new ModelRenderer(this);
        LLeg.setPos(1.0F, 12.0F, -3.0F);
        LLegParts.addChild(LLeg);


        LLeg_r1 = new ModelRenderer(this);
        LLeg_r1.setPos(-1.0F, -8.0F, -1.0F);
        LLeg.addChild(LLeg_r1);
        setRotationAngle(LLeg_r1, 0.3491F, 0.0F, 0.0F);
        LLeg_r1.texOffs(0, 58).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 6.0F, 2.0F, 0.0F, false);

        LFoot = new ModelRenderer(this);
        LFoot.setPos(1.0F, 16.0F, -4.0F);
        LLegParts.addChild(LFoot);
        setRotationAngle(LFoot, 0.4363F, 0.0F, 0.0F);


        LFoot_r1 = new ModelRenderer(this);
        LFoot_r1.setPos(-1.0F, -7.0F, 4.0F);
        LFoot.addChild(LFoot_r1);
        setRotationAngle(LFoot_r1, -0.0873F, 0.0F, 0.0F);
        LFoot_r1.texOffs(0, 29).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

        HeadParts = new ModelRenderer(this);
        HeadParts.setPos(0.3333F, -4.5F, -11.3333F);
        Bulldrake.addChild(HeadParts);


        Head_r1 = new ModelRenderer(this);
        Head_r1.setPos(0.6667F, -2.5F, -0.6667F);
        HeadParts.addChild(Head_r1);
        Head_r1.texOffs(0, 73).addBox(-6.0F, -4.0F, -6.0F, 10.0F, 6.0F, 8.0F, 0.0F, false);

        JawUp = new ModelRenderer(this);
        JawUp.setPos(0.6667F, -0.5F, -0.6667F);
        HeadParts.addChild(JawUp);
        JawUp.texOffs(52, 40).addBox(-6.0F, -3.0F, -10.0F, 10.0F, 5.0F, 12.0F, 0.0F, false);
        JawUp.texOffs(77, 19).addBox(-6.0F, -3.0F, -10.0F, 10.0F, 4.0F, 10.0F, 0.0F, false);

        JawDown = new ModelRenderer(this);
        JawDown.setPos(0.6667F, 2.5F, -0.6667F);
        HeadParts.addChild(JawDown);
        JawDown.texOffs(0, 58).addBox(-6.0F, -3.0F, -10.0F, 10.0F, 3.0F, 12.0F, 0.0F, false);

        REar = new ModelRenderer(this);
        REar.setPos(0.6667F, 46.5F, -6.6667F);
        HeadParts.addChild(REar);


        REar_r1 = new ModelRenderer(this);
        REar_r1.setPos(-7.0F, -49.5F, 3.0F);
        REar.addChild(REar_r1);
        REar_r1.texOffs(28, 73).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        LEar = new ModelRenderer(this);
        LEar.setPos(2.6667F, 46.5F, -6.6667F);
        HeadParts.addChild(LEar);


        LEar_r1 = new ModelRenderer(this);
        LEar_r1.setPos(3.0F, -49.5F, 3.0F);
        LEar.addChild(LEar_r1);
        LEar_r1.texOffs(0, 73).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        horns = new ModelRenderer(this);
        horns.setPos(-0.3333F, 13.5F, -3.6667F);
        HeadParts.addChild(horns);


        lefthorns = new ModelRenderer(this);
        lefthorns.setPos(6.3276F, -19.66F, 3.1585F);
        horns.addChild(lefthorns);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.519F, -0.1981F, -0.3415F);
        lefthorns.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 1.0472F, -0.5672F);
        cube_r1.texOffs(42, 48).addBox(0.5F, -0.5F, 0.5F, 4.0F, 1.0F, 1.0F, 1.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(-0.519F, 0.1981F, 0.3415F);
        lefthorns.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, -0.3927F);
        cube_r2.texOffs(0, 66).addBox(-1.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 1.0F, false);

        righthorns = new ModelRenderer(this);
        righthorns.setPos(-3.3276F, -19.66F, -1.8415F);
        horns.addChild(righthorns);


        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(-0.519F, -0.1981F, -0.3415F);
        righthorns.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, -1.0472F, 0.5672F);
        cube_r3.texOffs(8, 37).addBox(-1.5F, 1.5F, 5.5F, 4.0F, 1.0F, 1.0F, 1.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(0.519F, 0.1981F, 0.3415F);
        righthorns.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, 0.3927F);
        cube_r4.texOffs(40, 58).addBox(-5.0F, 0.5F, 4.5F, 3.0F, 1.0F, 1.0F, 1.0F, false);

        RWing = new ModelRenderer(this);
        RWing.setPos(-7.0F, -5.0F, -9.0F);
        Bulldrake.addChild(RWing);
        RWing.texOffs(0, 0).addBox(-29.0F, 0.0F, -11.0F, 30.0F, 0.0F, 20.0F, 0.0F, false);

        LWing = new ModelRenderer(this);
        LWing.setPos(7.0F, -5.0F, -9.0F);
        Bulldrake.addChild(LWing);
        LWing.texOffs(0, 20).addBox(-2.0F, 0.0F, -11.0F, 30.0F, 0.0F, 20.0F, 0.0F, false);

        Arrows = new ModelRenderer(this);
        Arrows.setPos(0.0F, 19.0F, -10.0F);
        Bulldrake.addChild(Arrows);


        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(-0.5F, -31.0F, 5.5F);
        Arrows.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, 0.3927F);
        cube_r5.texOffs(0, 37).addBox(2.5F, -11.0F, -1.5F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(-0.5F, -32.0F, 5.5F);
        Arrows.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.48F, 0.0F, 0.7854F);
        cube_r6.texOffs(80, 18).addBox(2.5F, -8.0F, -4.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        cube_r7 = new ModelRenderer(this);
        cube_r7.setPos(-0.5F, -32.0F, 5.5F);
        Arrows.addChild(cube_r7);
        setRotationAngle(cube_r7, -0.4363F, 0.0F, 0.7854F);
        cube_r7.texOffs(4, 37).addBox(2.5F, -9.0F, 1.5F, 1.0F, 10.0F, 1.0F, 0.0F, false);

        cube_r8 = new ModelRenderer(this);
        cube_r8.setPos(-4.0F, -32.0F, 2.0F);
        Arrows.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.3927F, 0.0F, -0.6981F);
        cube_r8.texOffs(36, 58).addBox(-1.0F, -4.0F, -2.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        cube_r9 = new ModelRenderer(this);
        cube_r9.setPos(-0.5F, -32.0F, 5.5F);
        Arrows.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.0F, -0.4363F);
        cube_r9.texOffs(32, 58).addBox(-3.5F, -7.0F, -0.5F, 1.0F, 10.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(TaillessHorrorEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = ((float)(entityIn.getId() * 3) + ageInTicks) * 0.13F;
        float f1 = 16.0F;
        this.LWing.zRot = MathHelper.cos(f) * 32.0F * ((float)Math.PI / 180F);
        this.RWing.zRot = -this.LWing.zRot;
        if (entityIn.isRoaring()) {
            this.JawUp.xRot = -0.134F;
            this.JawDown.xRot = 0.278F;
        } else {
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
        return ImmutableList.of(this.Body);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
