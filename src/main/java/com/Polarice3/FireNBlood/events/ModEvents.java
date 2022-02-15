package com.Polarice3.FireNBlood.events;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.client.gui.overlay.SoulEnergyGui;
import com.Polarice3.FireNBlood.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.neutral.*;
import com.Polarice3.FireNBlood.entities.neutral.protectors.AbstractProtectorEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.items.FocusBagItem;
import com.Polarice3.FireNBlood.items.SoulWand;
import com.Polarice3.FireNBlood.utils.FocusBagFinder;
import com.Polarice3.FireNBlood.utils.KeyPressed;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = FireNBlood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void openBagandWand(InputEvent.KeyInputEvent event){
        KeyPressed.setWand(RegistryHandler.keyBindings[0].isDown());
        KeyPressed.setWandandbag(RegistryHandler.keyBindings[1].isDown());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void spawnEntities(BiomeLoadingEvent event){
        if (event.getName() != null) {
            Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());
            if (biome != null) {
                if (biome.getBiomeCategory() == Biome.Category.NETHER) {

                } else if (biome.getBiomeCategory() == Biome.Category.THEEND) {

                } else {
                    if (biome.getBiomeCategory() == Biome.Category.OCEAN) {
                        event.getSpawns().getSpawner(EntityClassification.MISC).add(new MobSpawnInfo.Spawners(ModEntityType.SACRED_FISH.get(), 1, 1, 1));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderSoulEnergyHUD(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        final PlayerEntity player = Minecraft.getInstance().player;

        if (player != null) {
            ItemStack foundStack = ItemStack.EMPTY;

            for (int i = 0; i <= 9; i++) {
                ItemStack itemStack = player.inventory.getItem(i);
                if (!itemStack.isEmpty() && itemStack.getItem() == RegistryHandler.GOLDTOTEM.get()) {
                    foundStack = itemStack;
                    break;
                }
            }

            if (!foundStack.isEmpty()) {
                new SoulEnergyGui(Minecraft.getInstance(), player).drawHUD(event.getMatrixStack(), event.getPartialTicks());
            }
        }
    }

    private static final Map<ServerWorld, HexerSpawner> HEXER_SPAWNER_MAP = new HashMap<>();
    private static final Map<ServerWorld, CultistsSpawner> CULTISTS_SPAWNER_MAP = new HashMap<>();

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load evt) {
        if (!evt.getWorld().isClientSide() && evt.getWorld() instanceof ServerWorld) {
            HEXER_SPAWNER_MAP.put((ServerWorld) evt.getWorld(), new HexerSpawner((ServerWorld) evt.getWorld()));
            CULTISTS_SPAWNER_MAP.put((ServerWorld) evt.getWorld(), new CultistsSpawner());
        }
    }

    @SubscribeEvent
    public static void worldUnload(WorldEvent.Unload evt) {
        if (!evt.getWorld().isClientSide() && evt.getWorld() instanceof ServerWorld) {
            HEXER_SPAWNER_MAP.remove(evt.getWorld());
            CULTISTS_SPAWNER_MAP.remove(evt.getWorld());
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.WorldTickEvent tick){
        if(!tick.world.isClientSide && tick.world instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)tick.world;
            HexerSpawner spawner = HEXER_SPAWNER_MAP.get(serverWorld);
            CultistsSpawner spawner2 = CULTISTS_SPAWNER_MAP.get(serverWorld);
            if (spawner != null) {
                spawner.tick();
            }
            if (spawner2 != null){
                spawner2.tick(serverWorld);
            }
        }

    }

    @SubscribeEvent
    public static void onPlayerFirstEntersWorld(PlayerEvent.PlayerLoggedInEvent event){
        if (FNBConfig.StarterTotem.get() && !event.getPlayer().level.isClientSide) {
            CompoundNBT playerData = event.getPlayer().getPersistentData();
            CompoundNBT data;

            if (!playerData.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
                data = new CompoundNBT();
            } else {
                data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            }

            if (!data.getBoolean("firenblood:gotTotem")) {
                event.getPlayer().addItem(new ItemStack(RegistryHandler.GOLDTOTEM.get()));
                data.putBoolean("firenblood:gotTotem", true);
                playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
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
        if (KeyPressed.openWandandBag() && player.getMainHandItem().getItem() instanceof SoulWand){
            SoulWand.BagonKeyPressed(player.getMainHandItem(), player);
        }
        if (KeyPressed.openWand()){
            SoulWand.onKeyPressed(player.getMainHandItem(), player);
        }
        if (player.getItemBySlot(EquipmentSlotType.HEAD).getItem() == RegistryHandler.FURRED_HELMET.get()
                && player.getItemBySlot(EquipmentSlotType.CHEST).getItem() == RegistryHandler.FURRED_CHESTPLATE.get()
                && player.getItemBySlot(EquipmentSlotType.LEGS).getItem() == RegistryHandler.FURRED_LEGGINGS.get()
                && player.getItemBySlot(EquipmentSlotType.FEET).getItem() == RegistryHandler.FURRED_BOOTS.get()
        ){
            player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 100));
            player.addEffect(new EffectInstance(Effects.DIG_SPEED, 100));
        }
        if (player.getItemBySlot(EquipmentSlotType.FEET).getItem() == RegistryHandler.DARKBOOTSOFWANDER.get()
                || player.getItemBySlot(EquipmentSlotType.FEET).getItem() == RegistryHandler.NECROBOOTSOFWANDER.get()){
            FluidState fluidstate = player.level.getFluidState(player.blockPosition());
            if (player.isInWater() && player.isAffectedByFluids() && !player.canStandOnFluid(fluidstate.getType())){
                player.setDeltaMovement(player.getDeltaMovement().x * 1.015, player.getDeltaMovement().y, player.getDeltaMovement().z * 1.015);
            }
        }
    }

    @SubscribeEvent
    public static void OnLivingFall(LivingFallEvent event){
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (player.getItemBySlot(EquipmentSlotType.FEET).getItem() == RegistryHandler.DARKBOOTSOFWANDER.get()
                    || player.getItemBySlot(EquipmentSlotType.FEET).getItem() == RegistryHandler.NECROBOOTSOFWANDER.get()){
                event.setDistance(event.getDistance()/2);
            }
        }

    }

    @SubscribeEvent
    public static void OnLivingJump(LivingEvent.LivingJumpEvent event){
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (player.getItemBySlot(EquipmentSlotType.FEET).getItem() == RegistryHandler.DARKBOOTSOFWANDER.get()
                    || player.getItemBySlot(EquipmentSlotType.FEET).getItem() == RegistryHandler.NECROBOOTSOFWANDER.get()){
               player.setDeltaMovement(player.getDeltaMovement().x, 0.75, player.getDeltaMovement().z);
            }
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
                attacker.setTarget(null);
            }
        }
    }

    @SubscribeEvent
    public static void GoldTouchDeath(LivingDeathEvent event){
        Entity killed = event.getEntity();
        if (killed instanceof CreatureEntity){
            if (((CreatureEntity) killed).hasEffect(RegistryHandler.GOLDTOUCHED.get())){
                int amp = Objects.requireNonNull(((CreatureEntity) killed).getEffect(RegistryHandler.GOLDTOUCHED.get())).getAmplifier();
                for(int i = 0; i < 8 * amp + 1; ++i) {
                    killed.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void Mutation(PotionEvent.PotionAddedEvent event){
        if (event.getPotionEffect().getEffect() == RegistryHandler.COSMIC.get()){
            World world = event.getEntityLiving().level;
            LivingEntity entity = event.getEntityLiving();
            for(int i = 0; i < world.random.nextInt(35) + 10; ++i) {
                world.addParticle(ParticleTypes.DRAGON_BREATH, entity.getX(), entity.getEyeY(), entity.getZ(), 0.0F, 0.0F, 0.0F);
            }
            if (entity instanceof CowEntity){
                MutatedCowEntity mutatedCowEntity = new MutatedCowEntity(ModEntityType.MUTATED_COW.get(), world);
                mutatedCowEntity.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);
                mutatedCowEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedCowEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (entity.hasCustomName()) {
                    mutatedCowEntity.setCustomName(entity.getCustomName());
                    mutatedCowEntity.setCustomNameVisible(entity.isCustomNameVisible());
                }
                mutatedCowEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedCowEntity);
                entity.remove();
            } else if (entity instanceof ChickenEntity){
                MutatedChickenEntity mutatedChickenEntity = new MutatedChickenEntity(ModEntityType.MUTATED_CHICKEN.get(), world);
                mutatedChickenEntity.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);
                mutatedChickenEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedChickenEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (entity.hasCustomName()) {
                    mutatedChickenEntity.setCustomName(entity.getCustomName());
                    mutatedChickenEntity.setCustomNameVisible(entity.isCustomNameVisible());
                }
                mutatedChickenEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedChickenEntity);
                entity.remove();
            } else if (entity instanceof SheepEntity){
                MutatedSheepEntity mutatedSheepEntity = new MutatedSheepEntity(ModEntityType.MUTATED_SHEEP.get(), world);
                mutatedSheepEntity.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);
                mutatedSheepEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedSheepEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (entity.hasCustomName()) {
                    mutatedSheepEntity.setCustomName(entity.getCustomName());
                    mutatedSheepEntity.setCustomNameVisible(entity.isCustomNameVisible());
                }
                mutatedSheepEntity.setColor(((SheepEntity) entity).getColor());
                mutatedSheepEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedSheepEntity);
                entity.remove();
            } else if (entity instanceof PigEntity){
                MutatedPigEntity mutatedPigEntity = new MutatedPigEntity(ModEntityType.MUTATED_PIG.get(), world);
                mutatedPigEntity.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);
                mutatedPigEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedPigEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (entity.hasCustomName()) {
                    mutatedPigEntity.setCustomName(entity.getCustomName());
                    mutatedPigEntity.setCustomNameVisible(entity.isCustomNameVisible());
                }
                mutatedPigEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedPigEntity);
                entity.remove();
            } else if (entity instanceof RabbitEntity){
                RabbitEntity rabbit = (RabbitEntity) entity;
                MutatedRabbitEntity mutatedRabbitEntity = new MutatedRabbitEntity(ModEntityType.MUTATED_RABBIT.get(), world);
                mutatedRabbitEntity.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.yRot, entity.xRot);
                mutatedRabbitEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(mutatedRabbitEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                if (entity.hasCustomName()) {
                    mutatedRabbitEntity.setCustomName(entity.getCustomName());
                    mutatedRabbitEntity.setCustomNameVisible(entity.isCustomNameVisible());
                }
                if (rabbit.getRabbitType() != 99) {
                    mutatedRabbitEntity.setRabbitType(rabbit.getRabbitType());
                } else {
                    mutatedRabbitEntity.setRabbitType(1);
                }
                mutatedRabbitEntity.setPersistenceRequired();
                world.addFreshEntity(mutatedRabbitEntity);
                entity.remove();
            }
        }
    }

}
