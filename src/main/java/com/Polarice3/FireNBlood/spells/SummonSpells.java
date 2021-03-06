package com.Polarice3.FireNBlood.spells;

import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;

public abstract class SummonSpells extends Spells{
    public abstract int SummonDownDuration();

    public boolean NecroPower(LivingEntity entityLiving){
        return entityLiving.getItemBySlot(EquipmentSlotType.CHEST).getItem() == RegistryHandler.NECROROBE.get() || entityLiving.getItemBySlot(EquipmentSlotType.CHEST).getItem() == RegistryHandler.NECROARMOREDROBE.get();
    }

    public boolean NecroMastery(LivingEntity entityLiving){
        return entityLiving.getItemBySlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.NECROHELM.get() || entityLiving.getItemBySlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.NECROARMOREDHELM.get();
    }

    public boolean SummonMastery(LivingEntity entityLiving){
        return entityLiving.getItemBySlot(EquipmentSlotType.LEGS).getItem() == RegistryHandler.DARKLEGGINGS.get() || entityLiving.getItemBySlot(EquipmentSlotType.LEGS).getItem() == RegistryHandler.DARKARMOREDLEGGINGS.get();
    }

    public void SummonDown(LivingEntity entityLiving){
        EffectInstance effectinstance1 = entityLiving.getEffect(RegistryHandler.SUMMONDOWN.get());
        int i = 1;
        if (effectinstance1 != null) {
            i += effectinstance1.getAmplifier();
            entityLiving.removeEffectNoUpdate(RegistryHandler.SUMMONDOWN.get());
        } else {
            --i;
        }

        i = MathHelper.clamp(i, 0, 4);
        int s;
        if (SummonMastery(entityLiving)){
            int random = entityLiving.level.random.nextInt(2);
            if (random == 0){
                s = 0;
            } else {
                s = SummonDownDuration();
            }
        } else {
            s = SummonDownDuration();
        }
        EffectInstance effectinstance = new EffectInstance(RegistryHandler.SUMMONDOWN.get(), s, i, false, false, true);
        entityLiving.addEffect(effectinstance);
    }
}
