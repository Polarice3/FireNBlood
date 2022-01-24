package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;

public abstract class SummonSoulUsingItem extends SoulUsingItem{
    public SummonSoulUsingItem(Properties properties) {
        super(properties);
    }

    public abstract int SummonDownDuration();

    public boolean NecroPower(LivingEntity entityLiving){
        return entityLiving.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == RegistryHandler.NECROROBE.get();
    }

    public boolean NecroMastery(LivingEntity entityLiving){
        return entityLiving.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.NECROHELM.get();
    }

    public void SummonDown(LivingEntity entityLiving){
        EffectInstance effectinstance1 = entityLiving.getActivePotionEffect(RegistryHandler.SUMMONDOWN.get());
        int i = 1;
        if (effectinstance1 != null) {
            i += effectinstance1.getAmplifier();
            entityLiving.removeActivePotionEffect(RegistryHandler.SUMMONDOWN.get());
        } else {
            --i;
        }

        i = MathHelper.clamp(i, 0, 4);
        EffectInstance effectinstance = new EffectInstance(RegistryHandler.SUMMONDOWN.get(), SummonDownDuration(), i, false, false, true);
        entityLiving.addPotionEffect(effectinstance);
    }

}
