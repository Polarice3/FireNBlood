package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.render.layers.SkeletonMinionClothingLayer;
import com.Polarice3.FireNBlood.entities.ally.SkeletonMinionEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;

public class SkeletonMinionRenderer extends BipedRenderer<SkeletonMinionEntity, SkeletonModel<SkeletonMinionEntity>> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/skeletonminion.png");

    public SkeletonMinionRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SkeletonModel<>(), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new SkeletonModel<>(0.5F, true), new SkeletonModel<>(1.0F, true)));
        this.addLayer(new SkeletonMinionClothingLayer<>(this));
    }

    public ResourceLocation getTextureLocation(SkeletonMinionEntity entity) {
        return TEXTURES;
    }


}
