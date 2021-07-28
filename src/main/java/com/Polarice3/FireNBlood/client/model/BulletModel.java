package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.BulletEntity;
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
        textureWidth = 32;
        textureHeight = 32;

        Bullet = new ModelRenderer(this);
        Bullet.setRotationPoint(0.0F, 24.0F, -3.0F);


        HeadParts = new ModelRenderer(this);
        HeadParts.setRotationPoint(0.0F, 0.0F, 0.0F);
        Bullet.addChild(HeadParts);


        LowJaw = new ModelRenderer(this);
        LowJaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        HeadParts.addChild(LowJaw);
        LowJaw.setTextureOffset(0, 6).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

        UpJaw = new ModelRenderer(this);
        UpJaw.setRotationPoint(0.0F, 0.0F, 0.0F);
        HeadParts.addChild(UpJaw);
        UpJaw.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

        Head = new ModelRenderer(this);
        Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        HeadParts.addChild(Head);
        Head.setTextureOffset(13, 14).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);

        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, 0.0F, 0.0F);
        Bullet.addChild(Body);
        Body.setTextureOffset(15, 4).addBox(-2.0F, -2.0F, 2.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        RLeg = new ModelRenderer(this);
        RLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        Bullet.addChild(RLeg);
        RLeg.setTextureOffset(0, 16).addBox(-2.0F, -1.0F, 4.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        LLeg = new ModelRenderer(this);
        LLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        Bullet.addChild(LLeg);
        LLeg.setTextureOffset(15, 10).addBox(1.0F, -1.0F, 4.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        RHorn = new ModelRenderer(this);
        RHorn.setRotationPoint(0.0F, 0.0F, 0.0F);
        Bullet.addChild(RHorn);
        RHorn.setTextureOffset(0, 20).addBox(-3.0F, -4.0F, 1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        LHorn = new ModelRenderer(this);
        LHorn.setRotationPoint(0.0F, 0.0F, 0.0F);
        Bullet.addChild(LHorn);
        LHorn.setTextureOffset(7, 16).addBox(2.0F, -4.0F, 1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        RWing = new ModelRenderer(this);
        RWing.setRotationPoint(0.0F, 0.0F, 0.0F);
        Bullet.addChild(RWing);
        RWing.setTextureOffset(13, 0).addBox(-6.0F, -1.0F, 2.0F, 4.0F, 0.0F, 3.0F, 0.0F, false);

        LWing = new ModelRenderer(this);
        LWing.setRotationPoint(0.0F, 0.0F, 0.0F);
        Bullet.addChild(LWing);
        LWing.setTextureOffset(0, 12).addBox(2.0F, -1.0F, 2.0F, 4.0F, 0.0F, 3.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = ((float)(entityIn.getEntityId() * 3) + ageInTicks) * 0.13F;
        this.LWing.rotateAngleZ = MathHelper.cos(f) * 32.0F * ((float)Math.PI / 180F);
        this.RWing.rotateAngleZ = -this.LWing.rotateAngleZ;
    }


    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Bullet.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
