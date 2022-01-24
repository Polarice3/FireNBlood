package com.Polarice3.FireNBlood.events;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.hostile.ParasiteEntity;
import com.Polarice3.FireNBlood.entities.hostile.TankEntity;
import com.Polarice3.FireNBlood.entities.neutral.MinionEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = FireNBlood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HostedEvent {

    @SubscribeEvent
    public static void HostedEffect(LivingEvent.LivingUpdateEvent event){
        LivingEntity host = event.getEntityLiving();
        World world = event.getEntity().getEntityWorld();
        if (host.isPotionActive(RegistryHandler.HOSTED.get())) {
            if (host instanceof AbstractSkeletonEntity || host instanceof ParasiteEntity
                    || host instanceof VexEntity || host instanceof MinionEntity
                    || host instanceof BlazeEntity || host instanceof MagmaCubeEntity
                    || host instanceof GuardianEntity || host instanceof IronGolemEntity || host instanceof TankEntity
                    || host instanceof SilverfishEntity || host instanceof EndermiteEntity || host instanceof WitherEntity){
                host.removePotionEffect(RegistryHandler.HOSTED.get());
            } else if (host instanceof EndermanEntity){
                int amp = Objects.requireNonNull(host.getActivePotionEffect(RegistryHandler.HOSTED.get())).getAmplifier() * 10;
                int random = world.rand.nextInt(120 - amp);
                if (random == 0) {
                    EndermiteEntity parasiteEntity = new EndermiteEntity(EntityType.ENDERMITE, world);
                    parasiteEntity.setPosition(host.getPosX(), host.getPosY(), host.getPosZ());
                    parasiteEntity.setAttackTarget(host);
                    world.addEntity(parasiteEntity);
                }
            } else {
                int amp = Objects.requireNonNull(host.getActivePotionEffect(RegistryHandler.HOSTED.get())).getAmplifier() * 10;
                int random = world.rand.nextInt(120 - amp);
                if (random == 0) {
                    ParasiteEntity parasiteEntity = new ParasiteEntity(ModEntityType.PARASITE.get(), world);
                    parasiteEntity.setPosition(host.getPosX(), host.getPosY(), host.getPosZ());
                    parasiteEntity.setAttackTarget(host);
                    world.addEntity(parasiteEntity);
                }
            }
        }
    }
}
