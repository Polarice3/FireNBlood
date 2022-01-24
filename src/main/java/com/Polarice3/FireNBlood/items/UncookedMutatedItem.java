package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class UncookedMutatedItem extends Item {
    public UncookedMutatedItem() {
        super(new Properties()
                .group(FireNBlood.TAB)
                .maxDamage(16)
                .food(new Food.Builder()
                        .hunger(5)
                        .saturation(0.5F)
                        .setAlwaysEdible()
                        .build())
        );
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        int random = worldIn.rand.nextInt(4);
        if (random == 0) {
            EffectInstance effectinstance1 = entityLiving.getActivePotionEffect(Effects.HUNGER);
            if (effectinstance1 == null) {
                EffectInstance effectinstance = new EffectInstance(Effects.HUNGER, 600, 0);
                entityLiving.addPotionEffect(effectinstance);
            } else {
                int amp = effectinstance1.getAmplifier();
                int i = amp + 1;
                i = MathHelper.clamp(i, 0, 5);
                entityLiving.removeActivePotionEffect(Effects.HUNGER);
                EffectInstance effectinstance = new EffectInstance(Effects.HUNGER, 600, i);
                entityLiving.addPotionEffect(effectinstance);
            }
        }
        ItemStack container = stack.copy();
        container.setDamage(stack.getDamage() + 1);
        return container;
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

}
