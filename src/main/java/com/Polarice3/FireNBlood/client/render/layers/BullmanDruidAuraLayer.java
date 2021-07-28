package com.Polarice3.FireNBlood.client.render.layers;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.model.BullmanDruidModel;
import com.Polarice3.FireNBlood.entities.hostile.TaillessDruidEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class BullmanDruidAuraLayer extends EnergyLayer<TaillessDruidEntity, BullmanDruidModel<TaillessDruidEntity>> {
    private static final ResourceLocation DRUID_ARMOR = new ResourceLocation(FireNBlood.MOD_ID, "textures/entity/druidarmor.png");
    private final BullmanDruidModel<TaillessDruidEntity> druidModel = new BullmanDruidModel<>(0.5F);

    public BullmanDruidAuraLayer(IEntityRenderer<TaillessDruidEntity, BullmanDruidModel<TaillessDruidEntity>> p_i50915_1_) {
        super(p_i50915_1_);
    }

    protected float func_225634_a_(float p_225634_1_) {
        return MathHelper.cos(p_225634_1_ * 0.02F) * 3.0F;
    }

    protected ResourceLocation func_225633_a_() {
        return DRUID_ARMOR;
    }

    protected EntityModel<TaillessDruidEntity> func_225635_b_() {
        return this.druidModel;
    }
}
