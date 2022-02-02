package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.BullmanDruidModel;
import com.Polarice3.FireNBlood.client.render.layers.BullmanDruidAuraLayer;
import com.Polarice3.FireNBlood.entities.hostile.tailless.TaillessDruidEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BullmanDruidRenderer extends MobRenderer<TaillessDruidEntity, BullmanDruidModel<TaillessDruidEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/taillessdruid.png");


    public BullmanDruidRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BullmanDruidModel<>(0.0F), 1.0F);
        this.addLayer(new BullmanDruidAuraLayer(this));
    }

    @Override
    public ResourceLocation getEntityTexture(TaillessDruidEntity entity) {
        return TEXTURE;
    }
}
