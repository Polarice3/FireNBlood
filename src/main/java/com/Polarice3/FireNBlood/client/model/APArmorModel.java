package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.neutral.protectors.AbstractProtectorEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class APArmorModel <T extends AbstractProtectorEntity> extends BipedModel<T> {
    public final ModelRenderer all;

    public APArmorModel(float modelSizeIn) {
        super(modelSizeIn);
        this.all = new ModelRenderer(this);
        this.all.setPos(0.0F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSizeIn);
        this.head.setPos(0.0F, 0.0F, 0.0F);
    }

}
