package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.ACArmorModel;
import com.Polarice3.FireNBlood.client.model.AbstractCultistModel;
import com.Polarice3.FireNBlood.entities.hostile.cultists.ZombieVillagerMinionEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

public class ZombieVillagerMinionRenderer extends BipedRenderer<ZombieVillagerMinionEntity, AbstractCultistModel<ZombieVillagerMinionEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/zombievillagerminion.png");

    public ZombieVillagerMinionRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new AbstractCultistModel<>(0.0F, 0.5F),0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
    }

    public ResourceLocation getEntityTexture(ZombieVillagerMinionEntity entity) {
        return TEXTURE;
    }
}
