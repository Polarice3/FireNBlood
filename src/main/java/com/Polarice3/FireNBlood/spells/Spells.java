package com.Polarice3.FireNBlood.spells;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public abstract class Spells {

    public Spells(){
    }

    public abstract int SoulCost();

    public abstract int CastDuration();

    public abstract SoundEvent CastingSound();

    public abstract ItemStack WandResult(World worldIn, LivingEntity entityLiving);

    public abstract void StaffResult(World worldIn, LivingEntity entityLiving);

}
