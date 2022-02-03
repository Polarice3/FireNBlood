package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.BullmanModel;
import com.Polarice3.FireNBlood.client.render.layers.TaillessHeldItemLayer;
import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessWretchEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BullmanRenderer extends MobRenderer<TaillessWretchEntity, BullmanModel<TaillessWretchEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/taillesswretch.png");

    public BullmanRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BullmanModel<>(), 1.0F);
        this.addLayer(new TaillessHeldItemLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(TaillessWretchEntity entity) {
        return TEXTURE;
    }
}
