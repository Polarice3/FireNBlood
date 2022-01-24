package com.Polarice3.FireNBlood.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class SoulWand extends SoulUsingItem{
    public SoulWand(Properties properties) {
        super(properties);
    }

    @Override
    public int SoulCost() {
        return 0;
    }

    @Override
    public int CastDuration() {
        return 0;
    }

    @Override
    public SoundEvent CastingSound() {
        return null;
    }

    @Override
    public ItemStack MagicResults(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        return null;
    }
}
