package com.Polarice3.FireNBlood.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;

import java.util.Objects;

public class CursedEffect extends ModEffects{
    public CursedEffect() {
        super(EffectType.HARMFUL, 10044730);
        this.addAttributesModifier(Attributes.ARMOR, "9126b017-4bcb-43cb-9c48-9bdbf579f899", -0.15D, AttributeModifier.Operation.MULTIPLY_BASE);
    }
}
