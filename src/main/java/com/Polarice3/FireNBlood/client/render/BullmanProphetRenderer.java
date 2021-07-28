package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.BullmanProphetModel;
import com.Polarice3.FireNBlood.client.render.layers.BullmanProphetAuraLayer;
import com.Polarice3.FireNBlood.entities.masters.TaillessProphetEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BullmanProphetRenderer extends MobRenderer<TaillessProphetEntity, BullmanProphetModel<TaillessProphetEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/prophet.png");


    public BullmanProphetRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BullmanProphetModel<>(0.0F), 1.0F);
        this.addLayer(new BullmanProphetAuraLayer(this));
    }

    @Override
    public ResourceLocation getEntityTexture(TaillessProphetEntity entity) {
        return TEXTURE;
    }
}
