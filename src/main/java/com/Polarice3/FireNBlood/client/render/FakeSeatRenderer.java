package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.entities.utilities.FakeSeatEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class FakeSeatRenderer extends EntityRenderer<FakeSeatEntity> {

    public FakeSeatRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getTextureLocation(FakeSeatEntity entity) {
        return null;
    }
}
