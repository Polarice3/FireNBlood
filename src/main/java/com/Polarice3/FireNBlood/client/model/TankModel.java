package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.TankEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class TankModel <T extends LivingEntity> extends EntityModel<T> {
    private final ModelRenderer Tank;
    private final ModelRenderer Body;
    private final ModelRenderer Turret;
    private final ModelRenderer Bottom;
    private final ModelRenderer BottomParts;
    private final ModelRenderer LeftWheels;
    private final ModelRenderer BackLW;
    private final ModelRenderer UBackLW;
    private final ModelRenderer UFrontLW;
    private final ModelRenderer FrontLW;
    private final ModelRenderer RightWheels;
    private final ModelRenderer BackRW;
    private final ModelRenderer UBackRW;
    private final ModelRenderer UFrontRW;
    private final ModelRenderer FrontRW;

    public TankModel() {
        textureWidth = 256;
        textureHeight = 256;

        Tank = new ModelRenderer(this);
        Tank.setRotationPoint(0.0F, 19.0F, 0.0F);


        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, -3.0F, 0.0F);
        Tank.addChild(Body);
        Body.setTextureOffset(0, 0).addBox(-16.0F, -32.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, false);
        Body.setTextureOffset(98, 0).addBox(-16.0F, -8.0F, -24.0F, 32.0F, 8.0F, 8.0F, 0.0F, false);
        Body.setTextureOffset(130, 30).addBox(-16.0F, -4.0F, -28.0F, 32.0F, 4.0F, 4.0F, 0.0F, false);
        Body.setTextureOffset(58, 126).addBox(-16.0F, -12.0F, -20.0F, 32.0F, 4.0F, 4.0F, 0.0F, false);
        Body.setTextureOffset(0, 0).addBox(-8.0F, -40.0F, 16.0F, 4.0F, 24.0F, 4.0F, 0.0F, false);
        Body.setTextureOffset(0, 66).addBox(4.0F, -40.0F, 16.0F, 4.0F, 24.0F, 4.0F, 0.0F, false);
        Body.setTextureOffset(0, 0).addBox(4.0F, -40.0F, 20.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        Body.setTextureOffset(0, 66).addBox(-8.0F, -40.0F, 20.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        Turret = new ModelRenderer(this);
        Turret.setRotationPoint(0.0F, 14.0F, 0.0F);
        Body.addChild(Turret);
        Turret.setTextureOffset(116, 126).addBox(-4.0F, -34.0F, -32.0F, 8.0F, 8.0F, 16.0F, 0.0F, false);

        Bottom = new ModelRenderer(this);
        Bottom.setRotationPoint(0.0F, 11.0F, 0.0F);
        Tank.addChild(Bottom);
        Bottom.setTextureOffset(0, 66).addBox(-16.0F, -14.0F, -16.0F, 32.0F, 2.0F, 32.0F, 0.0F, false);

        BottomParts = new ModelRenderer(this);
        BottomParts.setRotationPoint(0.0F, 11.0F, 0.0F);
        Tank.addChild(BottomParts);
        BottomParts.setTextureOffset(98, 18).addBox(-20.0F, -12.0F, -16.0F, 40.0F, 2.0F, 2.0F, 0.0F, false);
        BottomParts.setTextureOffset(98, 18).addBox(-20.0F, -12.0F, -6.0F, 40.0F, 2.0F, 2.0F, 0.0F, false);
        BottomParts.setTextureOffset(98, 18).addBox(-20.0F, -12.0F, 4.0F, 40.0F, 2.0F, 2.0F, 0.0F, false);
        BottomParts.setTextureOffset(98, 18).addBox(-20.0F, -12.0F, 14.0F, 40.0F, 2.0F, 2.0F, 0.0F, false);
        BottomParts.setTextureOffset(90, 62).addBox(-26.0F, -16.0F, -20.0F, 8.0F, 10.0F, 40.0F, 0.0F, false);
        BottomParts.setTextureOffset(90, 62).addBox(18.0F, -16.0F, -20.0F, 8.0F, 10.0F, 40.0F, 0.0F, false);

        LeftWheels = new ModelRenderer(this);
        LeftWheels.setRotationPoint(0.0F, 7.0F, 0.0F);
        Tank.addChild(LeftWheels);


        BackLW = new ModelRenderer(this);
        BackLW.setRotationPoint(22.0F, -7.0F, 15.0F);
        LeftWheels.addChild(BackLW);
        BackLW.setTextureOffset(98, 66).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        UBackLW = new ModelRenderer(this);
        UBackLW.setRotationPoint(22.0F, -7.0F, 5.0F);
        LeftWheels.addChild(UBackLW);
        UBackLW.setTextureOffset(98, 66).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        UFrontLW = new ModelRenderer(this);
        UFrontLW.setRotationPoint(22.0F, -7.0F, -5.0F);
        LeftWheels.addChild(UFrontLW);
        UFrontLW.setTextureOffset(98, 66).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        FrontLW = new ModelRenderer(this);
        FrontLW.setRotationPoint(22.0F, -7.0F, -15.0F);
        LeftWheels.addChild(FrontLW);
        FrontLW.setTextureOffset(98, 66).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        RightWheels = new ModelRenderer(this);
        RightWheels.setRotationPoint(0.0F, 7.0F, 0.0F);
        Tank.addChild(RightWheels);


        BackRW = new ModelRenderer(this);
        BackRW.setRotationPoint(-22.0F, -7.0F, 15.0F);
        RightWheels.addChild(BackRW);
        BackRW.setTextureOffset(0, 118).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        UBackRW = new ModelRenderer(this);
        UBackRW.setRotationPoint(-22.0F, -7.0F, 5.0F);
        RightWheels.addChild(UBackRW);
        UBackRW.setTextureOffset(0, 118).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        UFrontRW = new ModelRenderer(this);
        UFrontRW.setRotationPoint(-22.0F, -7.0F, -5.0F);
        RightWheels.addChild(UFrontRW);
        UFrontRW.setTextureOffset(0, 118).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        FrontRW = new ModelRenderer(this);
        FrontRW.setRotationPoint(-22.0F, -7.0F, -15.0F);
        RightWheels.addChild(FrontRW);
        FrontRW.setTextureOffset(0, 118).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = ageInTicks / 60.0F;
        float wheel = MathHelper.cos(limbSwing * 2F) * 1.4F;
        this.Body.rotateAngleY = netHeadYaw * ((float)Math.PI /180F);
        this.Body.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.BackRW.rotateAngleX = wheel;
        this.UBackRW.rotateAngleX = wheel;
        this.UFrontRW.rotateAngleX = wheel;
        this.FrontRW.rotateAngleX = wheel;
        this.BackLW.rotateAngleX = wheel;
        this.UBackLW.rotateAngleX = wheel;
        this.UFrontLW.rotateAngleX = wheel;
        this.FrontLW.rotateAngleX = wheel;
        this.Body.rotationPointY = MathHelper.sin(f * 40.0F) + 0.4F;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Tank.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
