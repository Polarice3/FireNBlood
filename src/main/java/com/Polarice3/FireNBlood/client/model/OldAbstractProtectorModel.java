package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.neutral.AbstractProtectorEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OldAbstractProtectorModel<T extends AbstractProtectorEntity> extends SegmentedModel<T> implements IHasArm, IHasHead {
    public final ModelRenderer head;
    public final ModelRenderer hat;
    public final ModelRenderer body;
    public final ModelRenderer clothes;
    public final ModelRenderer arms;
    public final ModelRenderer rightleg;
    public final ModelRenderer leftleg;
    public final ModelRenderer rightArm;
    public final ModelRenderer leftArm;
    public final ModelRenderer all;

    public OldAbstractProtectorModel(float scaleFactor, float p_i47227_2_) {
        textureWidth = 64;
        textureHeight = 64;
        this.all = new ModelRenderer(this);
        this.all.setRotationPoint(0.0F, 0.0F, 0.0F);

        this.head = new ModelRenderer(this);
        this.head.setRotationPoint(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.head.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, scaleFactor);

        this.hat = new ModelRenderer(this, 32, 0);
        this.hat.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, scaleFactor + 0.45F);
        this.head.addChild(this.hat);
        this.hat.showModel = false;

        ModelRenderer modelrenderer = new ModelRenderer(this);
        modelrenderer.setRotationPoint(0.0F, p_i47227_2_ - 2.0F, 0.0F);
        modelrenderer.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, scaleFactor);
        this.head.addChild(modelrenderer);
        this.all.addChild(this.head);

        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.body.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, scaleFactor);
        this.clothes = new ModelRenderer(this);
        this.clothes.setRotationPoint(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.clothes.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, scaleFactor + 0.5F);
        this.all.addChild(this.body);
        this.all.addChild(this.clothes);

        this.arms = new ModelRenderer(this);
        this.arms.setRotationPoint(0.0F, 0.0F + p_i47227_2_ + 2.0F, 0.0F);
        this.arms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, scaleFactor);
        ModelRenderer modelrenderer1 = new ModelRenderer(this, 44, 22);
        modelrenderer1.mirror = true;
        modelrenderer1.addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, scaleFactor);
        this.arms.addChild(modelrenderer1);
        this.arms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, scaleFactor);

        this.rightleg = new ModelRenderer(this, 0, 22);
        this.rightleg.setRotationPoint(-2.0F, 12.0F + p_i47227_2_, 0.0F);
        this.rightleg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scaleFactor);
        this.all.addChild(this.rightleg);

        this.leftleg = new ModelRenderer(this, 0, 22);
        this.leftleg.mirror = true;
        this.leftleg.setRotationPoint(2.0F, 12.0F + p_i47227_2_, 0.0F);
        this.leftleg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scaleFactor);
        this.all.addChild(this.leftleg);

        this.rightArm = new ModelRenderer(this, 40, 46);
        this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scaleFactor);
        this.rightArm.setRotationPoint(-5.0F, 2.0F + p_i47227_2_, 0.0F);

        this.leftArm = new ModelRenderer(this, 40, 46);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scaleFactor);
        this.leftArm.setRotationPoint(5.0F, 2.0F + p_i47227_2_, 0.0F);
    }

    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.all, this.arms, this.leftArm, this.rightArm);
    }

    /**
     * Sets this entity's model rotation angles
     */
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.arms.rotationPointZ = -1.0F;
        this.arms.rotateAngleX = -0.75F;
        if (this.isSitting || entityIn.isEntitySleeping() || entityIn.isDying()) {
            this.rightArm.rotateAngleX = (-(float)Math.PI / 5F);
            this.rightArm.rotateAngleY = 0.0F;
            this.rightArm.rotateAngleZ = 0.0F;
            this.leftArm.rotateAngleX = (-(float)Math.PI / 5F);
            this.leftArm.rotateAngleY = 0.0F;
            this.leftArm.rotateAngleZ = 0.0F;
            this.rightleg.rotateAngleX = -1.4137167F;
            this.rightleg.rotateAngleY = ((float)Math.PI / 10F);
            this.rightleg.rotateAngleZ = 0.07853982F;
            this.leftleg.rotateAngleX = -1.4137167F;
            this.leftleg.rotateAngleY = (-(float)Math.PI / 10F);
            this.leftleg.rotateAngleZ = -0.07853982F;
            this.arms.rotationPointY = 13.0F;
            this.rightArm.rotationPointY = 12.0F;
            this.leftArm.rotationPointY = 12.0F;
            this.all.rotationPointY = 10.0F;
        } else {
            this.arms.rotationPointY = 3.0F;
            this.rightArm.rotationPointY = 2.0F;
            this.leftArm.rotationPointY = 2.0F;
            this.rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            this.rightArm.rotateAngleY = 0.0F;
            this.rightArm.rotateAngleZ = 0.0F;
            this.leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
            this.leftArm.rotateAngleY = 0.0F;
            this.leftArm.rotateAngleZ = 0.0F;
            this.rightleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
            this.rightleg.rotateAngleY = 0.0F;
            this.rightleg.rotateAngleZ = 0.0F;
            this.leftleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
            this.leftleg.rotateAngleY = 0.0F;
            this.leftleg.rotateAngleZ = 0.0F;
            this.all.rotationPointY = 0.0F;
        }

        AbstractProtectorEntity.ArmPose abstractprotectorentity$armpose = entityIn.getArmPose();
        if (abstractprotectorentity$armpose == AbstractProtectorEntity.ArmPose.ATTACKING) {
            if (entityIn.getHeldItemMainhand().isEmpty()) {
                ModelHelper.func_239105_a_(this.leftArm, this.rightArm, true, this.swingProgress, ageInTicks);
            } else {
                ModelHelper.func_239103_a_(this.rightArm, this.leftArm, entityIn, this.swingProgress, ageInTicks);
            }
        } else if (abstractprotectorentity$armpose == AbstractProtectorEntity.ArmPose.SPELLCASTING) {
            this.rightArm.rotationPointZ = 0.0F;
            this.rightArm.rotationPointX = -5.0F;
            this.leftArm.rotationPointZ = 0.0F;
            this.leftArm.rotationPointX = 5.0F;
            this.rightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
            this.leftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
            this.rightArm.rotateAngleZ = 2.3561945F;
            this.leftArm.rotateAngleZ = -2.3561945F;
            this.rightArm.rotateAngleY = 0.0F;
            this.leftArm.rotateAngleY = 0.0F;
        } else if (abstractprotectorentity$armpose == AbstractProtectorEntity.ArmPose.BOW_AND_ARROW) {
            this.rightArm.rotateAngleY = -0.1F + this.head.rotateAngleY;
            this.rightArm.rotateAngleX = (-(float)Math.PI / 2F) + this.head.rotateAngleX;
            this.leftArm.rotateAngleX = -0.9424779F + this.head.rotateAngleX;
            this.leftArm.rotateAngleY = this.head.rotateAngleY - 0.4F;
            this.leftArm.rotateAngleZ = ((float)Math.PI / 2F);
        } else if (abstractprotectorentity$armpose == AbstractProtectorEntity.ArmPose.CROSSBOW_HOLD) {
            ModelHelper.func_239104_a_(this.rightArm, this.leftArm, this.head, true);
        } else if (abstractprotectorentity$armpose == AbstractProtectorEntity.ArmPose.CROSSBOW_CHARGE) {
            ModelHelper.func_239102_a_(this.rightArm, this.leftArm, entityIn, true);
        }

        boolean flag = abstractprotectorentity$armpose == AbstractProtectorEntity.ArmPose.CROSSED;
        this.arms.showModel = flag;
        this.leftArm.showModel = !flag;
        this.rightArm.showModel = !flag;
    }

    private ModelRenderer getArm(HandSide p_191216_1_) {
        return p_191216_1_ == HandSide.LEFT ? this.leftArm : this.rightArm;
    }

    public ModelRenderer func_205062_a() {
        return this.hat;
    }

    public ModelRenderer getModelHead() {
        return this.head;
    }

    public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
        this.getArm(sideIn).translateRotate(matrixStackIn);
    }

/*    public void setVisible(boolean visible) {
        this.head.showModel = visible;
        this.body.showModel = visible;
        this.rightArm.showModel = visible;
        this.leftArm.showModel = visible;
        this.rightleg.showModel = visible;
        this.leftleg.showModel = visible;
    }

    public void setModelAttributes(AbstractProtectorModel<T> modelIn) {
        super.copyModelAttributesTo(modelIn);
        modelIn.head.copyModelAngles(this.head);
        modelIn.body.copyModelAngles(this.body);
        modelIn.rightArm.copyModelAngles(this.rightArm);
        modelIn.leftArm.copyModelAngles(this.leftArm);
        modelIn.rightleg.copyModelAngles(this.rightleg);
        modelIn.leftleg.copyModelAngles(this.leftleg);
    }*/
}