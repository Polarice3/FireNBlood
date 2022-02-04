package com.Polarice3.FireNBlood.events;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.items.GoldTotemItem;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FireNBlood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GoldTotemEvent {

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        Entity killer = event.getSource().getEntity();
        Entity killed = event.getEntity();

        if (killer instanceof PlayerEntity && killed instanceof MobEntity){
            PlayerEntity slayer = (PlayerEntity) killer;
            LivingEntity victim = (LivingEntity) killed;
            if (!(slayer instanceof FakePlayer)){
                GoldTotemItem.handleKill(slayer, victim);
            }
        }

        if (event.getEntityLiving() instanceof PlayerEntity && FNBConfig.TotemUndying.get()){
            if (event.getEntityLiving().hasEffect(RegistryHandler.DEATHPROTECT.get())){
                event.getEntityLiving().setHealth(1.0F);
                event.getEntityLiving().removeAllEffects();
                event.getEntityLiving().addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                event.getEntityLiving().addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                event.getEntityLiving().addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                event.getEntityLiving().level.broadcastEntityEvent(event.getEntityLiving(), (byte)35);
                GoldTotemItem.EmptySoulTotem((PlayerEntity) event.getEntityLiving());
                event.setCanceled(true);
            }
        }

    }

    private static ItemStack getTotemItem(PlayerEntity player) {
        for(Hand hand : Hand.values()) {
            ItemStack itemstack = player.getItemInHand(hand);
            if (itemstack.getItem() == RegistryHandler.GOLDTOTEM.get()) {
                return itemstack;
            }
        }

        return new ItemStack(RegistryHandler.GOLDTOTEM.get());
    }

}
