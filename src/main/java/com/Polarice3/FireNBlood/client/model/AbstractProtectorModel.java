package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.neutral.AbstractProtectorEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
public class AbstractProtectorModel<T extends AbstractProtectorEntity> extends BipedModel<T> {
    public final ModelRenderer clothes;
    public final ModelRenderer arms;
    public final ModelRenderer all;

    public AbstractProtectorModel(float modelSize, float p_i47227_2_) {
        super(modelSize);
        textureWidth = 64;
        textureHeight = 64;

        this.all = new ModelRenderer(this);
        this.all.setRotationPoint(0.0F, 0.0F, 0.0F);

        this.bipedHead = new ModelRenderer(this);
        this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.bipedHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, modelSize);

        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, modelSize + 0.45F);
        this.bipedHeadwear.showModel = false;

        ModelRenderer modelrenderer = new ModelRenderer(this);
        modelrenderer.setRotationPoint(0.0F, p_i47227_2_ - 2.0F, 0.0F);
        modelrenderer.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, modelSize);
        this.bipedHead.addChild(modelrenderer);
        this.all.addChild(this.bipedHead);

        this.bipedBody = new ModelRenderer(this);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.bipedBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, modelSize);
        this.clothes = new ModelRenderer(this);
        this.clothes.setRotationPoint(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.clothes.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, modelSize + 0.5F);
        this.all.addChild(this.bipedBody);
        this.all.addChild(this.clothes);

        this.arms = new ModelRenderer(this);
        this.arms.setRotationPoint(0.0F, 0.0F + p_i47227_2_ + 2.0F, 0.0F);
        this.arms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, modelSize);
        ModelRenderer modelrenderer1 = new ModelRenderer(this, 44, 22);
        modelrenderer1.mirror = true;
        modelrenderer1.addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, modelSize);
        this.arms.addChild(modelrenderer1);
        this.arms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, modelSize);

        this.bipedRightLeg = new ModelRenderer(this, 0, 22);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + p_i47227_2_, 0.0F);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.all.addChild(this.bipedRightLeg);

        this.bipedLeftLeg = new ModelRenderer(this, 0, 22);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + p_i47227_2_, 0.0F);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.all.addChild(this.bipedLeftLeg);

        this.bipedRightArm = new ModelRenderer(this, 40, 46);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i47227_2_, 0.0F);

        this.bipedLeftArm = new ModelRenderer(this, 40, 46);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i47227_2_, 0.0F);
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.arms, this.clothes, this.all));
    }

    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.bipedHead.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.bipedHead.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.arms.rotationPointZ = -1.0F;
        this.arms.rotateAngleX = -0.75F;
        if (this.isSitting/* || entityIn.isEntitySleeping() || entityIn.isDying()*/) {
            this.bipedRightArm.rotateAngleX = (-(float)Math.PI / 5F);
            this.bipedRightArm.rotateAngleY = 0.0F;
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleX = (-(float)Math.PI / 5F);
            this.bipedLeftArm.rotateAngleY = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightLeg.rotateAngleX = -1.4137167F;
            this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
            this.bipedRightLeg.rotateAngleZ = 0.07853982F;
            this.bipedLeftLeg.rotateAngleX = -1.4137167F;
            this.bipedLeftLeg.rotateAngleY = (-(float)Math.PI / 10F);
            this.bipedLeftLeg.rotateAngleZ = -0.07853982F;
//            this.all.rotationPointY = 10.0F;
        } else {
            this.arms.rotationPointY = 3.0F;
            this.bipedRightArm.rotationPointY = 2.0F;
            this.bipedLeftArm.rotationPointY = 2.0F;
            this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            this.bipedRightArm.rotateAngleY = 0.0F;
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
            this.bipedLeftArm.rotateAngleY = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
            this.bipedRightLeg.rotateAngleY = 0.0F;
            this.bipedRightLeg.rotateAngleZ = 0.0F;
            this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
            this.bipedLeftLeg.rotateAngleY = 0.0F;
            this.bipedLeftLeg.rotateAngleZ = 0.0F;
//            this.all.rotationPointY = 0.0F;
        }

        AbstractProtectorEntity.ArmPose abstractprotectorentity$armpose = entityIn.getArmPose();
        switch (abstractprotectorentity$armpose){
            case CROSSED:
                this.bipedRightArm.rotateAngleX = 0;
                this.bipedLeftArm.rotateAngleX = 0;
                break;
            case ATTACKING:
                if (!entityIn.getHeldItemMainhand().isEmpty() && !(entityIn.getHeldItemMainhand().getItem() instanceof ShootableItem))
                    ModelHelper.func_239103_a_(this.bipedRightArm, this.bipedLeftArm, entityIn, this.swingProgress, ageInTicks);
                break;
            case CROSSBOW_CHARGE:
                ModelHelper.func_239102_a_(this.bipedRightArm, this.bipedLeftArm, entityIn, true);
                break;
            case CROSSBOW_HOLD:
                ModelHelper.func_239104_a_(this.bipedRightArm, this.bipedLeftArm, this.bipedHead, true);
                break;
            case SPELLCASTING:
                this.bipedRightArm.rotationPointZ = 0.0F;
                this.bipedRightArm.rotationPointX = -5.0F;
                this.bipedLeftArm.rotationPointZ = 0.0F;
                this.bipedLeftArm.rotationPointX = 5.0F;
                this.bipedRightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
                this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
                this.bipedRightArm.rotateAngleZ = 2.3561945F;
                this.bipedLeftArm.rotateAngleZ = -2.3561945F;
                this.bipedRightArm.rotateAngleY = 0.0F;
                this.bipedLeftArm.rotateAngleY = 0.0F;
        }

        boolean flag = abstractprotectorentity$armpose == AbstractProtectorEntity.ArmPose.CROSSED;
        this.arms.showModel = flag;
        this.bipedLeftArm.showModel = !flag;
        this.bipedRightArm.showModel = !flag;
        boolean flag2 = entityIn.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ArmorItem
                || entityIn.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() instanceof ArmorItem;
        this.clothes.showModel = !flag2;
    }

    public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;
        if (entityIn.getPrimaryHand() == HandSide.RIGHT) {
            this.RightArmPoses(Hand.MAIN_HAND, entityIn);
            this.LeftArmPoses(Hand.OFF_HAND, entityIn);
        } else {
            this.RightArmPoses(Hand.OFF_HAND, entityIn);
            this.LeftArmPoses(Hand.MAIN_HAND, entityIn);
        }
        super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
    }

    private void RightArmPoses (Hand hand, T entityIn){
        ItemStack itemstack = entityIn.getHeldItem(hand);
        UseAction useAction = itemstack.getUseAction();
        if (entityIn.getArmPose() != AbstractProtectorEntity.ArmPose.CROSSED){
            switch (useAction){
                case CROSSBOW:
                    this.rightArmPose = ArmPose.CROSSBOW_HOLD;
                    if (entityIn.isHandActive()) {
                        this.rightArmPose = BipedModel.ArmPose.CROSSBOW_CHARGE;
                    }
                    break;
                case BOW:
                    this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
                    break;
                default:
                    this.rightArmPose = BipedModel.ArmPose.EMPTY;
                    if (!itemstack.isEmpty()) {
                        this.rightArmPose = BipedModel.ArmPose.ITEM;
                    }
                    break;
            }
        }
    }

    private void LeftArmPoses (Hand hand, T entityIn){
        ItemStack itemstack = entityIn.getHeldItem(hand);
        UseAction useAction = itemstack.getUseAction();
        if (entityIn.getArmPose() != AbstractProtectorEntity.ArmPose.CROSSED){
            switch (useAction){
                case CROSSBOW:
                    this.leftArmPose = ArmPose.CROSSBOW_HOLD;
                    if (entityIn.isHandActive()) {
                        this.leftArmPose = BipedModel.ArmPose.CROSSBOW_CHARGE;
                    }
                    break;
                case BOW:
                    this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
                    break;
                default:
                    this.leftArmPose = BipedModel.ArmPose.EMPTY;
                    if (!itemstack.isEmpty()) {
                        this.leftArmPose = BipedModel.ArmPose.ITEM;
                    }
                    break;
            }
        }
    }

    private ModelRenderer getArm(HandSide p_191216_1_) {
        return p_191216_1_ == HandSide.LEFT ? this.bipedLeftArm : this.bipedRightArm;
    }

    public ModelRenderer func_205062_a() {
        return this.bipedHeadwear;
    }

    public ModelRenderer getModelHead() {
        return this.bipedHead;
    }

    public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
        this.getArm(sideIn).translateRotate(matrixStackIn);
    }

}