package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.neutral.AbstractProtectorEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class APArmorModel <T extends AbstractProtectorEntity> extends BipedModel<T> {
    public final ModelRenderer all;

    public APArmorModel(float modelSizeIn) {
        super(modelSizeIn);
        this.all = new ModelRenderer(this);
        this.all.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSizeIn);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
/*        this.all.addChild(this.bipedHead);
        this.all.addChild(this.bipedBody);
        this.all.addChild(this.bipedLeftArm);
        this.all.addChild(this.bipedRightArm);
        this.all.addChild(this.bipedLeftLeg);
        this.all.addChild(this.bipedRightLeg);*/
    }

/*    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.all));
    }

    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.isSitting || entityIn.isEntitySleeping() || entityIn.isDying()) {
            this.all.rotationPointY = 10.0F;
        } else {
            this.all.rotationPointY = 0.0F;
        }
    }*/

}
