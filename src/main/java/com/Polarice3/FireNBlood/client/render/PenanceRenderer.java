package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.PenanceModel;
import com.Polarice3.FireNBlood.entities.bosses.PenanceEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class PenanceRenderer extends MobRenderer<PenanceEntity, PenanceModel<PenanceEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/penance.png");

    public PenanceRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new PenanceModel<>(), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(PenanceEntity entity) {
        return TEXTURE;
    }
}
