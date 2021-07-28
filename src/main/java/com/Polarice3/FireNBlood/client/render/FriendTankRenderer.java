package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.FriendTankModel;
import com.Polarice3.FireNBlood.client.render.layers.TankCracksLayer;
import com.Polarice3.FireNBlood.entities.ally.FriendlyTankEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class FriendTankRenderer extends MobRenderer<FriendlyTankEntity, FriendTankModel<FriendlyTankEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/tank.png");

    public FriendTankRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new FriendTankModel<>(0.0F), 1.0F);
        this.addLayer(new TankCracksLayer(this));
    }

    @Override
    public ResourceLocation getEntityTexture(FriendlyTankEntity entity) {
        return TEXTURE;
    }
}
