package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.TankModel;
import com.Polarice3.FireNBlood.client.render.layers.TankCracksLayer;
import com.Polarice3.FireNBlood.entities.hostile.TankEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.IronGolemCracksLayer;
import net.minecraft.util.ResourceLocation;

public class TankRenderer extends MobRenderer<TankEntity, TankModel<TankEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/tank.png");

    public TankRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new TankModel<>(), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(TankEntity entity) {
        return TEXTURE;
    }
}
