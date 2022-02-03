package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.tailless.BulletEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class BulletModel<T extends BulletEntity> extends EntityModel<T> {
    private final ModelRenderer Bullet;
    private final ModelRenderer HeadParts;
    private final ModelRenderer LowJaw;
    private final ModelRenderer UpJaw;
    private final ModelRenderer Head;
    private final ModelRenderer Body;
    private final ModelRenderer RLeg;
    private final ModelRenderer LLeg;
    private final ModelRenderer RHorn;
    private final ModelRenderer LHorn;
    private final ModelRenderer RWing;
    private final ModelRenderer LWing;

    public BulletModel() {
        texWidth = 32;
        texHeight = 32;

        Bullet = new ModelRenderer(this);
        Bullet.setPos(0.0F, 24.0F, -3.0F);


        HeadParts = new ModelRenderer(this);
        HeadParts.setPos(0.0F, 0.0F, 0.0F);
        Bullet.addChild(HeadParts);


        LowJaw = new ModelRenderer(this);
        LowJaw.setPos(0.0F, 0.0F, 0.0F);
        HeadParts.addChild(LowJaw);
        LowJaw.texOffs(0, 6).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

        UpJaw = new ModelRenderer(this);
        UpJaw.setPos(0.0F, 0.0F, 0.0F);
        HeadParts.addChild(UpJaw);
        UpJaw.texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

        Head = new ModelRenderer(this);
        Head.setPos(0.0F, 0.0F, 0.0F);
        HeadParts.addChild(Head);
        Head.texOffs(13, 14).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);

        Body = new ModelRenderer(this);
        Body.setPos(0.0F, 0.0F, 0.0F);
        Bullet.addChild(Body);
        Body.texOffs(15, 4).addBox(-2.0F, -2.0F, 2.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        RLeg = new ModelRenderer(this);
        RLeg.setPos(0.0F, 0.0F, 0.0F);
        Bullet.addChild(RLeg);
        RLeg.texOffs(0, 16).addBox(-2.0F, -1.0F, 4.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        LLeg = new ModelRenderer(this);
        LLeg.setPos(0.0F, 0.0F, 0.0F);
        Bullet.addChild(LLeg);
        LLeg.texOffs(15, 10).addBox(1.0F, -1.0F, 4.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        RHorn = new ModelRenderer(this);
        RHorn.setPos(0.0F, 0.0F, 0.0F);
        Bullet.addChild(RHorn);
        RHorn.texOffs(0, 20).addBox(-3.0F, -4.0F, 1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        LHorn = new ModelRenderer(this);
        LHorn.setPos(0.0F, 0.0F, 0.0F);
        Bullet.addChild(LHorn);
        LHorn.texOffs(7, 16).addBox(2.0F, -4.0F, 1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        RWing = new ModelRenderer(this);
        RWing.setPos(0.0F, 0.0F, 0.0F);
        Bullet.addChild(RWing);
        RWing.texOffs(13, 0).addBox(-6.0F, -1.0F, 2.0F, 4.0F, 0.0F, 3.0F, 0.0F, false);

        LWing = new ModelRenderer(this);
        LWing.setPos(0.0F, 0.0F, 0.0F);
        Bullet.addChild(LWing);
        LWing.texOffs(0, 12).addBox(2.0F, -1.0F, 2.0F, 4.0F, 0.0F, 3.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = ((float)(entityIn.getId() * 3) + ageInTicks) * 0.13F;
        this.LWing.zRot = MathHelper.cos(f) * 32.0F * ((float)Math.PI / 180F);
        this.RWing.zRot = -this.LWing.zRot;
    }


    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Bullet.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
