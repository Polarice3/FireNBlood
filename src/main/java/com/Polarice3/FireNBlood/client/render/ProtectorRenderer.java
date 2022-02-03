package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.APArmorModel;
import com.Polarice3.FireNBlood.client.model.AbstractProtectorModel;
import com.Polarice3.FireNBlood.client.render.layers.ProtectorLoyalLayer;
import com.Polarice3.FireNBlood.entities.neutral.protectors.ProtectorEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class ProtectorRenderer extends AbstractProtectorRenderer<ProtectorEntity>{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/protector.png");

    public ProtectorRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new AbstractProtectorModel<>(0.0F, 0.0F), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new APArmorModel<>(0.5F), new APArmorModel<>(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new ProtectorLoyalLayer<>(this));
    }

    public ResourceLocation getTextureLocation(ProtectorEntity entity) {
        return TEXTURE;
    }

}
