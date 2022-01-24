package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.ZombieMinionModel;
import com.Polarice3.FireNBlood.client.render.layers.SavagerLoyalLayer;
import com.Polarice3.FireNBlood.client.render.layers.ZombieMinionClothingLayer;
import com.Polarice3.FireNBlood.entities.ally.ZombieMinionEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

public class ZombieMinionRenderer extends BipedRenderer<ZombieMinionEntity, ZombieMinionModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/zombieminion.png");

    public ZombieMinionRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new ZombieMinionModel(0.0F, false),0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ZombieMinionModel(0.5F, false), new ZombieMinionModel(1.0F, false)));
        this.addLayer(new ZombieMinionClothingLayer(this));
    }

    public ResourceLocation getEntityTexture(ZombieMinionEntity entity) {
        return TEXTURE;
    }
}
