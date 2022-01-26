package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.hostile.ChannellerEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ChannellerModel extends SegmentedModel<ChannellerEntity> implements IHasArm, IHasHead {
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer hat;
    private final ModelRenderer nose;
    private final ModelRenderer halo;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer arms;
    private final ModelRenderer leg0;
    private final ModelRenderer leg1;
    private final ModelRenderer rightArm;
    private final ModelRenderer rightItem;
    private final ModelRenderer leftArm;
    public ChannellerModel.ArmPose leftArmPose = ChannellerModel.ArmPose.EMPTY;
    public ChannellerModel.ArmPose rightArmPose = ChannellerModel.ArmPose.EMPTY;

    public ChannellerModel() {
        textureWidth = 64;
        textureHeight = 64;

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);
        body.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.5F, false);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.addChild(head);
        head.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);

        hat = new ModelRenderer(this);
        hat.setRotationPoint(0.0F, -5.0F, 0.0F);
        head.addChild(hat);
        hat.setTextureOffset(32, 0).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.2F, false);

        nose = new ModelRenderer(this);
        nose.setRotationPoint(0.0F, -2.0F, 0.0F);
        head.addChild(nose);
        nose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        halo = new ModelRenderer(this);
        halo.setRotationPoint(0.0F, 1.0F, -4.0F);
        head.addChild(halo);
        halo.setTextureOffset(35, 39).addBox(-5.0F, -11.0F, 4.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        halo.setTextureOffset(35, 39).addBox(4.0F, -11.0F, 4.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        halo.setTextureOffset(42, 62).addBox(-5.0F, -12.0F, 4.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
        halo.setTextureOffset(29, 59).addBox(-1.0F, -15.0F, 4.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        halo.setTextureOffset(29, 39).addBox(5.0F, -8.0F, 4.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        halo.setTextureOffset(29, 39).addBox(-7.0F, -8.0F, 4.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        halo.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, -0.48F);
        cube_r1.setTextureOffset(29, 39).addBox(9.0F, -9.0F, 4.0F, 6.0F, 2.0F, 1.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
        halo.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, 0.48F);
        cube_r2.setTextureOffset(29, 39).addBox(-15.0F, -9.0F, 4.0F, 6.0F, 2.0F, 1.0F, 0.0F, false);

        arms = new ModelRenderer(this);
        arms.setRotationPoint(0.0F, 2.0F, 0.0F);
        body.addChild(arms);
        arms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
        arms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
        arms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.0F, false);

        leg0 = new ModelRenderer(this);
        leg0.setRotationPoint(-2.0F, 12.0F, 0.0F);
        body.addChild(leg0);
        leg0.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        leg1 = new ModelRenderer(this);
        leg1.setRotationPoint(2.0F, 12.0F, 0.0F);
        body.addChild(leg1);
        leg1.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

        rightArm = new ModelRenderer(this);
        rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        body.addChild(rightArm);
        rightArm.setTextureOffset(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        rightItem = new ModelRenderer(this);
        rightItem.setRotationPoint(-0.5F, 6.0F, 0.5F);
        rightArm.addChild(rightItem);


        leftArm = new ModelRenderer(this);
        leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        body.addChild(leftArm);
        leftArm.setTextureOffset(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
    }

    @Override
    public void setRotationAngles(ChannellerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.arms.rotationPointY = 3.0F;
        this.arms.rotationPointZ = -1.0F;
        this.arms.rotateAngleX = -0.75F;
        this.nose.setRotationPoint(0.0F, -2.0F, 0.0F);
        float f = 0.01F * (float)(entity.getEntityId() % 10);
        this.nose.rotateAngleX = MathHelper.sin((float)entity.ticksExisted * f) * 4.5F * ((float)Math.PI / 180F);
        this.nose.rotateAngleY = 0.0F;
        this.nose.rotateAngleZ = MathHelper.cos((float)entity.ticksExisted * f) * 2.5F * ((float)Math.PI / 180F);
        if (this.isSitting) {
            this.rightArm.rotateAngleX = (-(float)Math.PI / 5F);
            this.rightArm.rotateAngleY = 0.0F;
            this.rightArm.rotateAngleZ = 0.0F;
            this.leftArm.rotateAngleX = (-(float)Math.PI / 5F);
            this.leftArm.rotateAngleY = 0.0F;
            this.leftArm.rotateAngleZ = 0.0F;
            this.leg0.rotateAngleX = -1.4137167F;
            this.leg0.rotateAngleY = ((float)Math.PI / 10F);
            this.leg0.rotateAngleZ = 0.07853982F;
            this.leg1.rotateAngleX = -1.4137167F;
            this.leg1.rotateAngleY = (-(float)Math.PI / 10F);
            this.leg1.rotateAngleZ = -0.07853982F;
        } else {
            this.rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            this.rightArm.rotateAngleY = 0.0F;
            this.rightArm.rotateAngleZ = 0.0F;
            this.leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
            this.leftArm.rotateAngleY = 0.0F;
            this.leftArm.rotateAngleZ = 0.0F;
            this.leg0.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
            this.leg0.rotateAngleY = 0.0F;
            this.leg0.rotateAngleZ = 0.0F;
            this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
            this.leg1.rotateAngleY = 0.0F;
            this.leg1.rotateAngleZ = 0.0F;
        }
        ChannellerEntity.ArmPose cultistentity$armpose = entity.getArmPose();
        switch (cultistentity$armpose){
            case CROSSED:
                this.rightArm.rotateAngleX = 0;
                this.leftArm.rotateAngleX = 0;
                break;
            case SPELLCASTING:
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
        }

        boolean flag = cultistentity$armpose == ChannellerEntity.ArmPose.CROSSED;
        this.arms.showModel = flag;
        this.leftArm.showModel = !flag;
        this.rightArm.showModel = !flag;
    }

    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.arms, this.leg0, this.leg1, this.leftArm, this.rightArm);
    }

    public void setLivingAnimations(ChannellerEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
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

    private void RightArmPoses (Hand hand, ChannellerEntity entityIn){
        ItemStack itemstack = entityIn.getHeldItem(hand);
        UseAction useAction = itemstack.getUseAction();
        if (entityIn.getArmPose() != ChannellerEntity.ArmPose.CROSSED){
            this.rightArmPose = ArmPose.EMPTY;
            if (!itemstack.isEmpty()) {
                this.rightArmPose = ArmPose.ITEM;
            }
        }
    }

    private void LeftArmPoses (Hand hand, ChannellerEntity entityIn){
        ItemStack itemstack = entityIn.getHeldItem(hand);
        UseAction useAction = itemstack.getUseAction();
        if (entityIn.getArmPose() != ChannellerEntity.ArmPose.CROSSED){
            this.leftArmPose = ArmPose.EMPTY;
            if (!itemstack.isEmpty()) {
                this.leftArmPose = ArmPose.ITEM;
            }
        }
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    private ModelRenderer getArm(HandSide p_191216_1_) {
        return p_191216_1_ == HandSide.LEFT ? this.leftArm : this.rightArm;
    }

    public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
        this.getArm(sideIn).translateRotate(matrixStackIn);
    }

    public ModelRenderer func_205073_b() {
        return this.nose;
    }

    @Override
    public ModelRenderer getModelHead() {
        return head;
    }

    @OnlyIn(Dist.CLIENT)
    public static enum ArmPose {
        EMPTY(false),
        ITEM(false);

        private final boolean field_241656_h_;

        private ArmPose(boolean p_i241257_3_) {
            this.field_241656_h_ = p_i241257_3_;
        }

        public boolean func_241657_a_() {
            return this.field_241656_h_;
        }
    }
}