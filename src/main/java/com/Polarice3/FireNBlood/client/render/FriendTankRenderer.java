package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.render.layers.TankCracksLayer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class FriendTankRenderer extends TankRenderer {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/tank.png");

    public FriendTankRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn);
        this.addLayer(new TankCracksLayer(this));
    }
}
