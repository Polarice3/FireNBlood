package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.BlackBullModel;
import com.Polarice3.FireNBlood.client.model.SeatModel;
import com.Polarice3.FireNBlood.entities.hostile.BlackBullEntity;
import com.Polarice3.FireNBlood.entities.utilities.FakeSeatEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

public class FakeSeatRenderer extends EntityRenderer<FakeSeatEntity> {

    public FakeSeatRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(FakeSeatEntity entity) {
        return null;
    }
}
