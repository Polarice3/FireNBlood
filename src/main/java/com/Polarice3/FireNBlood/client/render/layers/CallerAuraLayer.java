package com.Polarice3.FireNBlood.client.render.layers;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.CallerModel;
import com.Polarice3.FireNBlood.entities.hostile.tailless.CallerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class CallerAuraLayer extends EnergyLayer<CallerEntity, CallerModel<CallerEntity>> {
    private static final ResourceLocation CALLER_ARMOR = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/callerarmor.png");
    private final CallerModel<CallerEntity> callerModel = new CallerModel<>(0.5F);

    public CallerAuraLayer(IEntityRenderer<CallerEntity, CallerModel<CallerEntity>> p_i50915_1_) {
        super(p_i50915_1_);
    }

    protected float func_225634_a_(float p_225634_1_) {
        return MathHelper.cos(p_225634_1_ * 0.02F) * 3.0F;
    }

    protected ResourceLocation func_225633_a_() {
        return CALLER_ARMOR;
    }

    protected EntityModel<CallerEntity> func_225635_b_() {
        return this.callerModel;
    }
}
