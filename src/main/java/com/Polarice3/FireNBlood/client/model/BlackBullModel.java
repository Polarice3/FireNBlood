package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.tailless.BlackBullEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class BlackBullModel<T extends BlackBullEntity> extends EntityModel<T> {
    private final ModelRenderer BlackBull;
    private final ModelRenderer Body;
    private final ModelRenderer Legs;
    private final ModelRenderer LFLeg;
    private final ModelRenderer RFLeg;
    private final ModelRenderer LBLeg;
    private final ModelRenderer RBLeg;
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

    public BlackBullModel() {
        texWidth = 64;
        texHeight = 64;

        BlackBull = new ModelRenderer(this);
        BlackBull.setPos(0.0F, 25.0F, -3.0F);


        Body = new ModelRenderer(this);
        Body.setPos(2.0F, -7.0F, 0.0F);
        BlackBull.addChild(Body);
        Body.texOffs(0, 0).addBox(-8.0F, -16.0F, -6.0F, 12.0F, 12.0F, 18.0F, 0.0F, false);

        Legs = new ModelRenderer(this);
        Legs.setPos(2.0F, -5.0F, 0.0F);
        BlackBull.addChild(Legs);


        LFLeg = new ModelRenderer(this);
        LFLeg.setPos(2.0F, -6.0F, -4.0F);
        Legs.addChild(LFLeg);
        LFLeg.texOffs(0, 46).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);

        RFLeg = new ModelRenderer(this);
        RFLeg.setPos(-6.0F, -6.0F, -4.0F);
        Legs.addChild(RFLeg);
        RFLeg.texOffs(43, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);

        LBLeg = new ModelRenderer(this);
        LBLeg.setPos(2.0F, -6.0F, 10.0F);
        Legs.addChild(LBLeg);
        LBLeg.texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);

        RBLeg = new ModelRenderer(this);
        RBLeg.setPos(-6.0F, -6.0F, 10.0F);
        Legs.addChild(RBLeg);
        RBLeg.texOffs(33, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);

        HeadParts = new ModelRenderer(this);
        HeadParts.setPos(0.0F, -19.0F, -6.0F);
        BlackBull.addChild(HeadParts);


        Head_r1 = new ModelRenderer(this);
        Head_r1.setPos(0.0F, -2.0F, -3.0F);
        HeadParts.addChild(Head_r1);
        Head_r1.texOffs(37, 31).addBox(-4.0F, -2.0F, -3.0F, 8.0F, 4.0F, 6.0F, 0.0F, false);

        Jaw_r1 = new ModelRenderer(this);
        Jaw_r1.setPos(0.0F, 1.0F, -5.0F);
        HeadParts.addChild(Jaw_r1);
        Jaw_r1.texOffs(0, 31).addBox(-4.0F, -2.0F, -5.0F, 8.0F, 4.0F, 10.0F, 0.0F, false);

        REar = new ModelRenderer(this);
        REar.setPos(2.0F, 47.0F, -6.0F);
        HeadParts.addChild(REar);


        REar_r1 = new ModelRenderer(this);
        REar_r1.setPos(-7.0F, -49.5F, 3.0F);
        REar.addChild(REar_r1);
        REar_r1.texOffs(24, 51).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        LEar = new ModelRenderer(this);
        LEar.setPos(2.0F, 47.0F, -6.0F);
        HeadParts.addChild(LEar);


        LEar_r1 = new ModelRenderer(this);
        LEar_r1.setPos(3.0F, -49.5F, 3.0F);
        LEar.addChild(LEar_r1);
        LEar_r1.texOffs(50, 49).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        RHorns = new ModelRenderer(this);
        RHorns.setPos(2.0F, 47.0F, -6.0F);
        HeadParts.addChild(RHorns);


        RHorn2_r1 = new ModelRenderer(this);
        RHorn2_r1.setPos(-9.0F, -52.0F, 5.0F);
        RHorns.addChild(RHorn2_r1);
        setRotationAngle(RHorn2_r1, 0.7854F, 0.0F, 0.0F);
        RHorn2_r1.texOffs(27, 31).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        RHorn1_r1 = new ModelRenderer(this);
        RHorn1_r1.setPos(-7.0F, -49.5F, 5.0F);
        RHorns.addChild(RHorn1_r1);
        RHorn1_r1.texOffs(50, 42).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        LHorns = new ModelRenderer(this);
        LHorns.setPos(2.0F, 47.0F, -6.0F);
        HeadParts.addChild(LHorns);


        LHorn2_r1 = new ModelRenderer(this);
        LHorn2_r1.setPos(5.0F, -52.0F, 5.0F);
        LHorns.addChild(LHorn2_r1);
        setRotationAngle(LHorn2_r1, 0.7854F, 0.0F, 0.0F);
        LHorn2_r1.texOffs(0, 31).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        LHorn1_r1 = new ModelRenderer(this);
        LHorn1_r1.setPos(3.0F, -49.5F, 5.0F);
        LHorns.addChild(LHorn1_r1);
        LHorn1_r1.texOffs(17, 46).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.HeadParts.xRot = headPitch * ((float)Math.PI / 180F);
        this.HeadParts.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.RBLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.LBLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.RFLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.LFLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }


    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        BlackBull.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
