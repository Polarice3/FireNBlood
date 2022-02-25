package com.Polarice3.FireNBlood.potions;

import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;

import java.util.ArrayList;
import java.util.List;

public class IllagueEffect extends ModEffects{
    private int tickCount;

    public IllagueEffect() {
        super(EffectType.HARMFUL, 9804699);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof PlayerEntity){
            ++ this.tickCount;
            int r = pLivingEntity.level.random.nextInt(10);
            int a = pLivingEntity.level.random.nextInt(5);
            if (this.tickCount >= (pLivingEntity.level.random.nextInt(400)/pAmplifier + 1)) {
                switch (r) {
                    case 0:
                        pLivingEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 400, a));
                        break;
                    case 1:
                        pLivingEntity.addEffect(new EffectInstance(Effects.HUNGER, 400, a));
                        break;
                    case 2:
                        pLivingEntity.addEffect(new EffectInstance(Effects.CONFUSION, 400));
                        break;
                    case 3:
                        pLivingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400, a));
                        break;
                    case 4:
                        pLivingEntity.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 400, a));
                        break;
                    case 5:
                        pLivingEntity.addEffect(new EffectInstance(Effects.POISON, 400, a));
                        break;
                    case 6:
                        pLivingEntity.addEffect(new EffectInstance(RegistryHandler.HOSTED.get(), 400, a));
                        break;
                }
                this.tickCount = 0;
            }
        }
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
