package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.MinotaurModel;
import com.Polarice3.FireNBlood.client.render.layers.TaillessHeldItemLayer;
import com.Polarice3.FireNBlood.entities.masters.MinotaurEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class MinotaurRenderer extends MobRenderer<MinotaurEntity, MinotaurModel<MinotaurEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/minotaur.png");

    public MinotaurRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new MinotaurModel<>(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(MinotaurEntity entity) {
        return TEXTURE;
    }
}
