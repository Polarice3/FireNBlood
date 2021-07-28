package com.Polarice3.FireNBlood.client.render;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.QuellModel;
import com.Polarice3.FireNBlood.entities.neutral.QuellEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class QuellRenderer extends MobRenderer<QuellEntity, QuellModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/quell.png");

    protected int getBlockLight(QuellEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    public QuellRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new QuellModel(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(QuellEntity entity) {
        return TEXTURE;
    }
}
