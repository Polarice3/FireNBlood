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
        super(new Item.Properties().group(FireNBlood.TAB)
                .maxStackSize(1)
                .food(new Food.Builder()
                        .hunger(5)
                        .saturation(0.6F)
                        .setAlwaysEdible()
                        .build())
        );
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        if (entityLiving instanceof PlayerEntity) {
            ((PlayerEntity)entityLiving).getCooldownTracker().setCooldown(this, 120);
        }
        return new ItemStack(this);
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    public SoundEvent getEatSound() {
        return SoundEvents.ENTITY_GENERIC_EAT;
    }
}
