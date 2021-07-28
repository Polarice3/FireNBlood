package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.MirageModel;
import com.Polarice3.FireNBlood.entities.neutral.MirageEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class MirageRenderer extends MobRenderer<MirageEntity, MirageModel<MirageEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/mirage.png");
    protected static final ResourceLocation ANGRY = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/mirage_angry.png");


    public MirageRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new MirageModel<>(), 0.5F);
    }

    @Override
    public ResourceLocation getEntityTexture(MirageEntity entity) {
        return entity.isAggressive() ? ANGRY : TEXTURE;
    }
}
