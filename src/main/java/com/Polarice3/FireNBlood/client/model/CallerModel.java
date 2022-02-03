package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.tailless.CallerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class CallerModel<T extends CallerEntity> extends EntityModel<T> {
    private final ModelRenderer Caller;
    private final ModelRenderer Pike;
    private final ModelRenderer HeadParts;
    private final ModelRenderer Head_r1;
    private final ModelRenderer Jaw_r1;
    private final ModelRenderer RHorns;
    private final ModelRenderer RHorn2_r1;
    private final ModelRenderer RHorn1_r1;
    private final ModelRenderer LHorns;
    private final ModelRenderer LHorn2_r1;
    private final ModelRenderer LHorn1_r1;
    private final ModelRenderer Stacks;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer Base;

    public CallerModel(float layer) {
        texWidth = 64;
        texHeight = 64;

        Caller = new ModelRenderer(this);
        Caller.setPos(0.0F, 24.0F, 1.0F);


        Pike = new ModelRenderer(this);
        Pike.setPos(0.0F, 0.0F, 0.0F);
        Caller.addChild(Pike);
        Pike.texOffs(0, 26).addBox(-1.0F, -28.0F, -2.0F, 2.0F, 28.0F, 2.0F, 0.0F, false);

        HeadParts = new ModelRenderer(this);
        HeadParts.setPos(-0.6667F, -18.5F, -2.3333F);
        Caller.addChild(HeadParts);
        setRotationAngle(HeadParts, 0.4363F, 0.0F, 0.0F);


        Head_r1 = new ModelRenderer(this);
        Head_r1.setPos(0.6667F, -5.5F, 2.3333F);
        HeadParts.addChild(Head_r1);
        Head_r1.texOffs(0, 15).addBox(-4.0F, -2.0F, -3.0F, 8.0F, 4.0F, 6.0F, 0.0F, false);

        Jaw_r1 = new ModelRenderer(this);
        Jaw_r1.setPos(0.6667F, -2.5F, 0.3333F);
        HeadParts.addChild(Jaw_r1);
        Jaw_r1.texOffs(0, 0).addBox(-4.0F, -2.0F, -5.0F, 8.0F, 4.0F, 10.0F, 0.0F, false);

        RHorns = new ModelRenderer(this);
        RHorns.setPos(2.6667F, 43.5F, -0.6667F);
        HeadParts.addChild(RHorns);


        RHorn2_r1 = new ModelRenderer(this);
        RHorn2_r1.setPos(-9.0F, -52.0F, 5.0F);
        RHorns.addChild(RHorn2_r1);
        RHorn2_r1.texOffs(27, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        RHorn1_r1 = new ModelRenderer(this);
        RHorn1_r1.setPos(-7.0F, -49.5F, 5.0F);
        RHorns.addChild(RHorn1_r1);
        RHorn1_r1.texOffs(39, 38).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        LHorns = new ModelRenderer(this);
        LHorns.setPos(2.6667F, 43.5F, -0.6667F);
        HeadParts.addChild(LHorns);


        LHorn2_r1 = new ModelRenderer(this);
        LHorn2_r1.setPos(5.0F, -52.0F, 5.0F);
        LHorns.addChild(LHorn2_r1);
        LHorn2_r1.texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        LHorn1_r1 = new ModelRenderer(this);
        LHorn1_r1.setPos(3.0F, -49.5F, 5.0F);
        LHorns.addChild(LHorn1_r1);
        LHorn1_r1.texOffs(39, 31).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        Stacks = new ModelRenderer(this);
        Stacks.setPos(0.0F, 0.0F, 0.0F);
        Caller.addChild(Stacks);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0F, 0.0F, 0.0F);
        Stacks.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.2182F, -1.789F, -1.0036F);
        cube_r1.texOffs(19, 30).addBox(-1.0F, -12.0F, -1.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(0.0F, 0.0F, 0.0F);
        Stacks.addChild(cube_r2);
        setRotationAngle(cube_r2, 1.8326F, -1.3963F, -0.829F);
        cube_r2.texOffs(24, 31).addBox(-1.0F, -12.0F, 0.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(0.0F, 0.0F, 0.0F);
        Stacks.addChild(cube_r3);
        setRotationAngle(cube_r3, 1.8326F, -0.6545F, -1.0908F);
        cube_r3.texOffs(29, 31).addBox(-1.0F, -14.0F, 0.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(0.0F, 0.0F, 0.0F);
        Stacks.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0436F, -2.138F, -0.9599F);
        cube_r4.texOffs(34, 31).addBox(-1.0F, -12.0F, -2.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(0.0F, 0.0F, 0.0F);
        Stacks.addChild(cube_r5);
        setRotationAngle(cube_r5, 1.0472F, -2.138F, -0.48F);
        cube_r5.texOffs(37, 0).addBox(-1.0F, -12.0F, 0.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(0.0F, 0.0F, 0.0F);
        Stacks.addChild(cube_r6);
        setRotationAngle(cube_r6, -0.829F, 0.0F, -0.48F);
        cube_r6.texOffs(9, 26).addBox(-1.0F, -16.0F, -2.0F, 1.0F, 16.0F, 1.0F, 0.0F, false);

        cube_r7 = new ModelRenderer(this);
        cube_r7.setPos(0.0F, 0.0F, 0.0F);
        Stacks.addChild(cube_r7);
        setRotationAngle(cube_r7, 1.0472F, 0.0F, -0.48F);
        cube_r7.texOffs(14, 26).addBox(-1.0F, -16.0F, 1.0F, 1.0F, 16.0F, 1.0F, 0.0F, false);

        Base = new ModelRenderer(this);
        Base.setPos(0.0F, 0.0F, 0.0F);
        Caller.addChild(Base);
        Base.texOffs(23, 20).addBox(-3.0F, -4.0F, -4.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Caller.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}

