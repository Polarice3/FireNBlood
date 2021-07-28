package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.SavagerModel;
import com.Polarice3.FireNBlood.client.render.layers.ProtectorLoyalLayer;
import com.Polarice3.FireNBlood.client.render.layers.SavagerLoyalLayer;
import com.Polarice3.FireNBlood.entities.neutral.SavagerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class SavagerRenderer extends MobRenderer<SavagerEntity, SavagerModel>  {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/savager.png");

    public SavagerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SavagerModel(0.0F, 0.0F), 1.0F);
        this.addLayer(new SavagerLoyalLayer(this));
    }

    @Override
    public ResourceLocation getEntityTexture(SavagerEntity entity) {
        return TEXTURE;
    }

}
