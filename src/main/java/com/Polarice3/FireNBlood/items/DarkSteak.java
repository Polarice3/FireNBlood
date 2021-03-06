package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DarkSteak extends Item {
    public DarkSteak() {
        super(new Item.Properties()
                .tab(FireNBlood.TAB)
                .food(new Food.Builder()
                        .nutrition(8)
                        .saturationMod(0.8F)
                        .alwaysEat()
                        .meat()
                        .build())
        );
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        int random = worldIn.random.nextInt(8);
        entityLiving.addEffect(new EffectInstance(Effects.REGENERATION, 100));
        if (random == 0 && FNBConfig.EvilEyeEvent.get()) {
            EffectInstance effectinstance1 = entityLiving.getEffect(RegistryHandler.EVIL_EYE.get());
            if (effectinstance1 == null) {
                EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                entityLiving.addEffect(effectinstance);
            } else {
                int amp = effectinstance1.getAmplifier();
                int i = amp + 1;
                i = MathHelper.clamp(i, 0, 5);
                entityLiving.removeEffectNoUpdate(RegistryHandler.EVIL_EYE.get());
                EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                entityLiving.addEffect(effectinstance);
                }
        } else {
            if (random == 0) {
                EffectInstance effectinstance1 = entityLiving.getEffect(RegistryHandler.CURSED.get());
                if (effectinstance1 == null) {
                    EffectInstance effectinstance = new EffectInstance(RegistryHandler.CURSED.get(), 1200, 0);
                    entityLiving.addEffect(effectinstance);
                } else {
                    int amp = effectinstance1.getAmplifier();
                    int i = amp + 1;
                    i = MathHelper.clamp(i, 0, 5);
                    entityLiving.removeEffectNoUpdate(RegistryHandler.CURSED.get());
                    EffectInstance effectinstance = new EffectInstance(RegistryHandler.CURSED.get(), 1200, i);
                    entityLiving.addEffect(effectinstance);
                }
            }
        }

        return stack;
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.EAT;
    }

}
