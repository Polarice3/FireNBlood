package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.ACArmorModel;
import com.Polarice3.FireNBlood.client.model.AbstractCultistModel;
import com.Polarice3.FireNBlood.client.model.SkeletonVillagerModel;
import com.Polarice3.FireNBlood.entities.hostile.cultists.SkeletonVillagerMinionEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class SkeletonVillagerMinionRenderer extends AbstractCultistRenderer<SkeletonVillagerMinionEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/skeletonvillagerminion.png");

    public SkeletonVillagerMinionRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new SkeletonVillagerModel<>(0.0F, 0.5F),0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
    }

    public ResourceLocation getTextureLocation(SkeletonVillagerMinionEntity entity) {
        return TEXTURE;
    }
}
