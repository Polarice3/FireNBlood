package com.Polarice3.FireNBlood.events;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.ally.FriendlyVexEntity;
import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.ParasiteEntity;
import com.Polarice3.FireNBlood.entities.hostile.TankEntity;
import com.Polarice3.FireNBlood.entities.neutral.AbstractProtectorEntity;
import com.Polarice3.FireNBlood.entities.neutral.MinionEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.injection.selectors.TargetSelector;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = FireNBlood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

/*    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void spawnEntities(BiomeLoadingEvent event){
        if (event.getName() != null) {
            Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());
            if (biome != null) {
                if (biome.getCategory() == Biome.Category.NETHER) {

                } else if (biome.getCategory() == Biome.Category.THEEND) {

                } else {
                    if (biome.getCategory() != Biome.Category.OCEAN) {
                            event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(ModEntityType.TAILLESS_WRETCH.get(), 1, 1, 2));
                    }
                }
            }
        }
    }*/

    private static final Map<ServerWorld, HexerSpawner> HEXER_SPAWNER_MAP = new HashMap<>();

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load evt) {
        if (!evt.getWorld().isRemote() && evt.getWorld() instanceof ServerWorld) {
            HEXER_SPAWNER_MAP.put((ServerWorld) evt.getWorld(), new HexerSpawner((ServerWorld) evt.getWorld()));
        }
    }

    @SubscribeEvent
    public static void worldUnload(WorldEvent.Unload evt) {
        if (!evt.getWorld().isRemote() && evt.getWorld() instanceof ServerWorld) {
            HEXER_SPAWNER_MAP.remove(evt.getWorld());
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.WorldTickEvent tick){
        if(!tick.world.isRemote && tick.world instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)tick.world;
            HexerSpawner spawner = HEXER_SPAWNER_MAP.get(serverWorld);
            if (spawner != null) {
                spawner.tick();
            }
        }

    }

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PillagerEntity) {
                Entity entity = event.getEntity();
                PillagerEntity illager = (PillagerEntity) entity;
                illager.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(illager, AbstractTaillessEntity.class, false));
                illager.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(illager, AbstractProtectorEntity.class, false));
        }
        if (event.getEntity() instanceof VindicatorEntity) {
                Entity entity = event.getEntity();
                VindicatorEntity illager = (VindicatorEntity) entity;
                illager.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(illager, AbstractTaillessEntity.class, false));
                illager.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(illager, AbstractProtectorEntity.class, false));
        }
        if (event.getEntity() instanceof EvokerEntity) {
                Entity entity = event.getEntity();
                EvokerEntity illager = (EvokerEntity) entity;
                illager.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(illager, AbstractTaillessEntity.class, false));
                illager.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(illager, AbstractProtectorEntity.class, false));
        }
        if (event.getEntity() instanceof IllusionerEntity) {
                Entity entity = event.getEntity();
                IllusionerEntity illager = (IllusionerEntity) entity;
                illager.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(illager, AbstractTaillessEntity.class, false));
                illager.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(illager, AbstractProtectorEntity.class, false));
        }
        if (event.getEntity() instanceof WitchEntity) {
            Entity entity = event.getEntity();
            WitchEntity illager = (WitchEntity) entity;
            illager.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(illager, AbstractProtectorEntity.class, false));
        }
    }

    @SubscribeEvent
    public static void onPlayerEquipment(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        if (player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.FURRED_HELMET.get()
                && player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == RegistryHandler.FURRED_CHESTPLATE.get()
                && player.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == RegistryHandler.FURRED_LEGGINGS.get()
                && player.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == RegistryHandler.FURRED_BOOTS.get()
        ){
            player.addPotionEffect(new EffectInstance(Effects.SPEED, 100));
            player.addPotionEffect(new EffectInstance(Effects.HASTE, 100));
        }
    }

    @SubscribeEvent
    public static void DyingTarget(LivingSetAttackTargetEvent event){
        if (event.getTarget() instanceof AbstractProtectorEntity) {
            Entity target = event.getTarget();
            Entity entity = event.getEntity();
            AbstractProtectorEntity protector = (AbstractProtectorEntity) target;
            MobEntity attacker = (MobEntity) entity;
            if (protector.isDying()){
                attacker.setAttackTarget(null);
            }
        }
    }

    @SubscribeEvent
    public static void GoldTouchDeath(LivingDeathEvent event){
        Entity killed = event.getEntity();
        if (killed instanceof CreatureEntity){
            if (((CreatureEntity) killed).isPotionActive(RegistryHandler.GOLDTOUCHED.get())){
                int amp = Objects.requireNonNull(((CreatureEntity) killed).getActivePotionEffect(RegistryHandler.GOLDTOUCHED.get())).getAmplifier();
                for(int i = 0; i < 8 * amp + 1; ++i) {
                    killed.entityDropItem(new ItemStack(Items.GOLD_NUGGET));
                }
            }
        }
    }

}
