package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.CallerModel;
import com.Polarice3.FireNBlood.client.render.layers.CallerAuraLayer;
import com.Polarice3.FireNBlood.entities.hostile.tailless.CallerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class CallerRenderer extends MobRenderer<CallerEntity, CallerModel<CallerEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/caller.png");

    public CallerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new CallerModel<>(0.0F), 1.0F);
        this.addLayer(new CallerAuraLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(CallerEntity entity) {
        return TEXTURE;
    }
}
