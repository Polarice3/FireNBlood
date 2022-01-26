package com.Polarice3.FireNBlood.spells;

import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.SoundEvent;

import java.util.Objects;

public abstract class Spells {

    public Spells(){

    }

    public abstract int SoulCost();

    public abstract int CastDuration();

    public abstract SoundEvent CastingSound();

    public boolean SoulDiscount(LivingEntity entityLiving){
        return entityLiving.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == RegistryHandler.DARKROBE.get()
                || entityLiving.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.NECROROBE.get();
    }

    public boolean SoulCostUp(LivingEntity entityLiving){
        return entityLiving.isPotionActive(RegistryHandler.SUMMONDOWN.get());
    }

    public boolean ReduceCastTime(LivingEntity entityLiving){
        return entityLiving.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.DARKHELM.get()
                || entityLiving.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.NECROHELM.get();
    }

    public int SoulUse(LivingEntity entityLiving){
        if (SoulCostUp(entityLiving)){
            int amp = Objects.requireNonNull(entityLiving.getActivePotionEffect(RegistryHandler.SUMMONDOWN.get())).getAmplifier() + 2;
            return SoulCost() * amp;
        } else if (SoulDiscount(entityLiving)){
            return SoulCost()/2;
        } else {
            return SoulCost();
        }
    }

    public int CastTime(LivingEntity entityLiving){
        if (ReduceCastTime(entityLiving)){
            return CastDuration()/2;
        } else {
            return CastDuration();
        }
    }
}
