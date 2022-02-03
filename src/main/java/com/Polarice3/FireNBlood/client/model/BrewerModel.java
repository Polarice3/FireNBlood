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
        this.all.setPos(0.0F, 0.0F, 0.0F);
        this.head = (new ModelRenderer(this)).setTexSize(64, 128);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.head.addChild(this.head);
        this.head.texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, scale);
/*        this.all.addChild(this.head);
        this.all.addChild(this.villagerBody);
        this.all.addChild(this.villagerArms);
        this.all.addChild(this.leg0);
        this.all.addChild(this.leg1);*/
    }

    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (this.riding || entityIn.isEntitySleeping() || entityIn.isDying()) {
            this.leg0.xRot = -1.4137167F;
            this.leg0.yRot = ((float)Math.PI / 10F);
            this.leg0.zRot = 0.07853982F;
            this.leg1.xRot = -1.4137167F;
            this.leg1.yRot = (-(float)Math.PI / 10F);
            this.leg1.zRot = -0.07853982F;
//            this.all.y = 10.0F;
        } else {
            this.leg0.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
            this.leg0.yRot = 0.0F;
            this.leg0.zRot = 0.0F;
            this.leg1.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
            this.leg1.yRot = 0.0F;
            this.leg1.zRot = 0.0F;
//            this.all.y = 0.0F;
        }

    }
}
