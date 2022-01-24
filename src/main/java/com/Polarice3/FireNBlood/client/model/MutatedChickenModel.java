package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.neutral.MutatedChickenEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class MutatedChickenModel<T extends MutatedChickenEntity> extends EntityModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer comb;
    private final ModelRenderer beak;
    private final ModelRenderer legfrontright;
    private final ModelRenderer legfrontleft;
    private final ModelRenderer legbackright;
    private final ModelRenderer legbackleft;
    private final ModelRenderer wingright;
    private final ModelRenderer wingleft;

    public MutatedChickenModel() {
        textureWidth = 64;
        textureHeight = 32;

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 16.0F, 0.0F);
        body.setTextureOffset(0, 13).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 15.0F, -4.0F);
        head.setTextureOffset(0, 0).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 3.0F, 0.0F, false);

        comb = new ModelRenderer(this);
        comb.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(comb);
        comb.setTextureOffset(14, 5).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        beak = new ModelRenderer(this);
        beak.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(beak);
        beak.setTextureOffset(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        legfrontright = new ModelRenderer(this);
        legfrontright.setRotationPoint(-4.0F, 19.0F, -3.0F);
        legfrontright.setTextureOffset(26, 0).addBox(0.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        legfrontleft = new ModelRenderer(this);
        legfrontleft.setRotationPoint(2.0F, 19.0F, -3.0F);
        legfrontleft.setTextureOffset(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        legbackright = new ModelRenderer(this);
        legbackright.setRotationPoint(-7.0F, 19.0F, -2.0F);
        legbackright.setTextureOffset(26, 0).addBox(2.0F, 0.0F, 3.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        legbackleft = new ModelRenderer(this);
        legbackleft.setRotationPoint(-7.0F, 19.0F, -2.0F);
        legbackleft.setTextureOffset(26, 0).addBox(5.0F, 0.0F, 3.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        wingright = new ModelRenderer(this);
        wingright.setRotationPoint(-3.0F, 17.0F, 0.0F);
        wingright.setTextureOffset(38, 9).addBox(-1.0F, -4.0F, -3.0F, 1.0F, 8.0F, 6.0F, 0.0F, false);

        wingleft = new ModelRenderer(this);
        wingleft.setRotationPoint(3.0F, 13.0F, 0.0F);
        wingleft.setTextureOffset(24, 13).addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        legbackright.render(matrixStack, buffer, packedLight, packedOverlay);
        legbackleft.render(matrixStack, buffer, packedLight, packedOverlay);
        legfrontright.render(matrixStack, buffer, packedLight, packedOverlay);
        legfrontleft.render(matrixStack, buffer, packedLight, packedOverlay);
        wingright.render(matrixStack, buffer, packedLight, packedOverlay);
        wingleft.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.body.rotateAngleX = ((float)Math.PI / 2F);
        this.legbackright.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.legbackleft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.legfrontright.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.legfrontleft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }
}
