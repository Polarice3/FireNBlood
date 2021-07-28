package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DarkSteak extends Item {
    public DarkSteak() {
        super(new Item.Properties()
                .group(FireNBlood.TAB)
                .food(new Food.Builder()
                        .hunger(4)
                        .saturation(0.8F)
                        .setAlwaysEdible()
                        .build())
        );
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        int random = worldIn.rand.nextInt(15);
        if (random == 0) {
            EffectInstance effectinstance1 = entityLiving.getActivePotionEffect(RegistryHandler.EVIL_EYE.get());
            if (effectinstance1 == null) {
                EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                entityLiving.addPotionEffect(effectinstance);
            } else {
                int amp = effectinstance1.getAmplifier();
                int i = amp + 1;
                i = MathHelper.clamp(i, 0, 5);
                entityLiving.removeActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                entityLiving.addPotionEffect(effectinstance);
            }
        }

        return stack;
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

}
