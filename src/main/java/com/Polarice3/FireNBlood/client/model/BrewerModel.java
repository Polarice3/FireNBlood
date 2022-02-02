package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.neutral.protectors.BrewerEntity;
import net.minecraft.client.renderer.entity.model.WitchModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BrewerModel<T extends BrewerEntity> extends WitchModel<T> {
    private final ModelRenderer head;
    private final ModelRenderer all;

    public BrewerModel(float scale) {
        super(scale);
        this.all = new ModelRenderer(this);
        this.all.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head = (new ModelRenderer(this)).setTextureSize(64, 128);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.villagerHead.addChild(this.head);
        this.head.setTextureOffset(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, scale);
/*        this.all.addChild(this.villagerHead);
        this.all.addChild(this.villagerBody);
        this.all.addChild(this.villagerArms);
        this.all.addChild(this.rightVillagerLeg);
        this.all.addChild(this.leftVillagerLeg);*/
    }

    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (this.isSitting || entityIn.isEntitySleeping() || entityIn.isDying()) {
            this.rightVillagerLeg.rotateAngleX = -1.4137167F;
            this.rightVillagerLeg.rotateAngleY = ((float)Math.PI / 10F);
            this.rightVillagerLeg.rotateAngleZ = 0.07853982F;
            this.leftVillagerLeg.rotateAngleX = -1.4137167F;
            this.leftVillagerLeg.rotateAngleY = (-(float)Math.PI / 10F);
            this.leftVillagerLeg.rotateAngleZ = -0.07853982F;
//            this.all.rotationPointY = 10.0F;
        } else {
            this.rightVillagerLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
            this.rightVillagerLeg.rotateAngleY = 0.0F;
            this.rightVillagerLeg.rotateAngleZ = 0.0F;
            this.leftVillagerLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
            this.leftVillagerLeg.rotateAngleY = 0.0F;
            this.leftVillagerLeg.rotateAngleZ = 0.0F;
//            this.all.rotationPointY = 0.0F;
        }

    }
}
