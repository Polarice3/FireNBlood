package com.Polarice3.FireNBlood.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;

public class MinorHarmEffect extends ModEffects{
    public MinorHarmEffect() {
        super(EffectType.HARMFUL, 4393481);
    }

    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        if (!entityLivingBaseIn.isEntityUndead()){
            entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, (float)(2 << amplifier));
        } else {
            entityLivingBaseIn.heal((float)Math.max(2 << amplifier, 0));
        }
    }

    public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {
        if (!entityLivingBaseIn.isEntityUndead()) {
            int j = (int) (health * (double) (2 << amplifier) + 0.5D);
            if (source == null) {
                entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, (float) j);
            } else {
                entityLivingBaseIn.attackEntityFrom(DamageSource.causeIndirectMagicDamage(source, indirectSource), (float) j);
            }
            this.performEffect(entityLivingBaseIn, amplifier);
        } else {
            int i = (int)(health * (double)(2 << amplifier) + 0.5D);
            entityLivingBaseIn.heal((float)i);
        }
    }

}
