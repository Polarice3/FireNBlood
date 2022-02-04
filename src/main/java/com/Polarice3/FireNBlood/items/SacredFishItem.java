package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SacredFishItem extends Item {
    public SacredFishItem() {
        super(new Item.Properties().tab(FireNBlood.TAB)
                .stacksTo(1)
                .food(new Food.Builder()
                        .nutrition(5)
                        .saturationMod(0.6F)
                        .alwaysEat()
                        .build())
        );
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        if (entityLiving instanceof PlayerEntity) {
            ((PlayerEntity)entityLiving).getCooldowns().addCooldown(this, 120);
        }
        return new ItemStack(this);
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.EAT;
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_EAT;
    }
}
