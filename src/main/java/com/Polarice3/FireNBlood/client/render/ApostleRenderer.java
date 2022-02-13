package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.ACArmorModel;
import com.Polarice3.FireNBlood.client.model.AbstractCultistModel;
import com.Polarice3.FireNBlood.entities.hostile.cultists.ApostleEntity;
import com.Polarice3.FireNBlood.entities.hostile.cultists.DiscipleEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class ApostleRenderer extends AbstractCultistRenderer<ApostleEntity>{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/apostle.png");

    public ApostleRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new AbstractCultistModel<>(0.0F, 0.0F), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ApostleEntity entity) {
        return TEXTURE;
    }
}
