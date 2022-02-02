package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.AnathemaModel;
import com.Polarice3.FireNBlood.entities.hostile.tailless.masters.TaillessAnathemaEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class AnathemaRenderer extends MobRenderer<TaillessAnathemaEntity, AnathemaModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/anathema.png");

    public AnathemaRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new AnathemaModel(), 1.2F);
    }

    @Override
    public ResourceLocation getEntityTexture(TaillessAnathemaEntity entity) {
        return TEXTURE;
    }
}
