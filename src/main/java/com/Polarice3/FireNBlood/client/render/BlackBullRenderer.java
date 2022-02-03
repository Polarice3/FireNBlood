package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.BlackBullModel;
import com.Polarice3.FireNBlood.entities.hostile.tailless.BlackBullEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BlackBullRenderer extends MobRenderer<BlackBullEntity, BlackBullModel<BlackBullEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/blackbull.png");

    public BlackBullRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new BlackBullModel<>(), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(BlackBullEntity entity) {
        return TEXTURE;
    }

        }
