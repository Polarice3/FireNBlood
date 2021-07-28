package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.CallerModel;
import com.Polarice3.FireNBlood.entities.hostile.CallerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class CallerRenderer extends MobRenderer<CallerEntity, CallerModel<CallerEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/caller.png");

    public CallerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new CallerModel<>(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(CallerEntity entity) {
        return TEXTURE;
    }
}
