package com.Polarice3.FireNBlood.events;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.items.GoldTotemItem;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FireNBlood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GoldTotemEvent {

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        Entity killer = event.getSource().getTrueSource();
        Entity killed = event.getEntity();

        if (killer instanceof PlayerEntity && killed instanceof MobEntity){
            PlayerEntity slayer = (PlayerEntity) killer;
            LivingEntity victim = (LivingEntity) killed;
            if (!(slayer instanceof FakePlayer)){
                GoldTotemItem.handleKill(slayer, victim);
            }
        }

        if (event.getEntityLiving() instanceof PlayerEntity){
            if (event.getEntityLiving().isPotionActive(RegistryHandler.DEATHPROTECT.get())){
                event.getEntityLiving().setHealth(1.0F);
                event.getEntityLiving().clearActivePotions();
                event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                event.getEntityLiving().addPotionEffect(new EffectInstance(RegistryHandler.SOULDRAIN.get(), 100));
                event.getEntityLiving().world.setEntityState(event.getEntityLiving(), (byte)35);
                event.setCanceled(true);
            }
        }

    }

}
