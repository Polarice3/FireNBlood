package com.Polarice3.FireNBlood.entities.hostile;

import com.Polarice3.FireNBlood.entities.projectiles.ScorchBallEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerBossInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class CallerEntity extends AbstractTaillessEntity implements IChargeableMob {
    public boolean regen;
    private static final Predicate<Entity> field_213690_b = Entity::isAlive;
    private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS).setDarkenSky(true));

    public CallerEntity(EntityType<? extends AbstractTaillessEntity> type, World worldIn) {
        super(type, worldIn);
    }
    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 200.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 0D)
                .createMutableAttribute(Attributes.ATTACK_SPEED, 0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .createMutableAttribute(Attributes.ARMOR, 10.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MobSpawnGoal());
        this.goalSelector.addGoal(1, new MeteorGoal());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setCallsForHelp());
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player) {
        return 25 + this.world.rand.nextInt(5);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ITEM_BREAK;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.BLOCK_WOOD_BREAK;
    }

    public void onDeath(DamageSource cause) {
        Entity entity = cause.getTrueSource();
        PlayerEntity playerentity;
        if (entity instanceof PlayerEntity) {
            playerentity = (PlayerEntity)entity;
            EffectInstance effectinstance1 = playerentity.getActivePotionEffect(RegistryHandler.EVIL_EYE.get());
            if (effectinstance1 == null) {
                EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                playerentity.addPotionEffect(effectinstance);
            } else {
                int amp = effectinstance1.getAmplifier();
                int i = amp + 1;
                i = MathHelper.clamp(i, 0, 5);
                playerentity.removeActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                playerentity.addPotionEffect(effectinstance);
            }
        } else if (entity instanceof WolfEntity) {
            WolfEntity wolfentity = (WolfEntity)entity;
            LivingEntity livingentity = wolfentity.getOwner();
            if (wolfentity.isTamed() && livingentity instanceof PlayerEntity) {
                playerentity = (PlayerEntity)livingentity;
                EffectInstance effectinstance1 = playerentity.getActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                if (effectinstance1 == null) {
                    EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                    playerentity.addPotionEffect(effectinstance);
                } else {
                    int amp = effectinstance1.getAmplifier();
                    int i = amp + 1;
                    i = MathHelper.clamp(i, 0, 5);
                    playerentity.removeActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                    EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                    playerentity.addPotionEffect(effectinstance);
                }
            }
        }
        super.onDeath(cause);
    }

    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return false;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public void applyEntityCollision(Entity entityIn) {
    }

    private final EntityPredicate ally = (new EntityPredicate().setDistance(10.0D));

    public boolean attackEntityFrom(DamageSource source, float amount){
        List<TaillessDruidEntity> list = CallerEntity.this.world.getTargettableEntitiesWithinAABB(TaillessDruidEntity.class, this.ally, CallerEntity.this, CallerEntity.this.getBoundingBox().grow(10.0D, 8.0D, 10.0D));
        if (list.isEmpty()) {
            return super.attackEntityFrom(source, amount);
        } else {
            return false;
        }
    }

    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    protected void updateAITasks(){
        if (this.regen) {
            if (this.ticksExisted % 20 == 0) {
                this.heal(1.0F);
            }
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        super.updateAITasks();
    }

    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
        ItemEntity itementity = this.entityDropItem(RegistryHandler.RIFTSHARD.get());
        if (itementity != null) {
            itementity.setNoDespawn();
        }

    }

    private final EntityPredicate mobs = (new EntityPredicate().setDistance(64.0D));

    public void tick() {
        super.tick();
        this.bossInfo.setVisible(!this.isCharged());
        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.FLAME, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void livingTick() {
        this.setMotion(Vector3d.ZERO);
        if (!this.isAIDisabled()) {
            this.prevRenderYawOffset = 0.0F;
            this.renderYawOffset = 0.0F;
        }

        super.livingTick();
    }

    @Override
    public boolean isCharged() {
        List<TaillessDruidEntity> list = CallerEntity.this.world.getTargettableEntitiesWithinAABB(TaillessDruidEntity.class, this.ally, CallerEntity.this, CallerEntity.this.getBoundingBox().grow(10.0D, 8.0D, 10.0D));
        return !list.isEmpty();
    }

    class MobSpawnGoal extends Goal {
        public int wavetimer;
        public int wave = 0;

        public boolean shouldExecute() {
            return CallerEntity.this.isAlive();
        }

        public void tick() {
            LivingEntity target = CallerEntity.this.getAttackTarget();
            int random = world.rand.nextInt(10);
            int random2 = world.rand.nextInt(16);
            int waveSpawn = CallerEntity.this.isCharged() ? world.rand.nextInt(30) + 60 : world.rand.nextInt(30) + 30;
            int a = random % 2;
            double d = (world.rand.nextBoolean() ? 1: -1);
            double e = (world.rand.nextBoolean() ? 1: -1);
            double f = (world.rand.nextBoolean() ? 32: 16);
            double d0 = CallerEntity.this.getPosX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)16 + (f * d);
            double d2 = CallerEntity.this.getPosZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)16 + (f * e);
            double d1 = world.getHeight(Heightmap.Type.WORLD_SURFACE, (int) d0, (int) d2);
            double d7 = CallerEntity.this.getPosX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)16 + (0.5D * d);
            double d9 = CallerEntity.this.getPosZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)16 + (0.5D * e);
            double d8 = world.getHeight(Heightmap.Type.WORLD_SURFACE, (int) d7, (int) d9);
            for (Entity entity : CallerEntity.this.world.getEntitiesWithinAABB(LivingEntity.class, CallerEntity.this.getBoundingBox().grow(32.0D, 8.0D, 32.0D), field_213690_b)) {
                if (entity instanceof ZombifiedPiglinEntity && target != null) {
                    ((ZombifiedPiglinEntity) entity).setAttackTarget(target);
                }
            }
            ++wavetimer;
            if (CallerEntity.this.isCharged()) {
                CallerEntity.this.regen = true;
                if (wavetimer >= waveSpawn) {
                    ++this.wave;
                    this.wavetimer = 0;
                    if (this.wave > 30) {
                        if (target != null) {
                            target.addPotionEffect(new EffectInstance(Effects.WITHER, 90, 2));
                            target.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 90));
                        }
                    }
                    if (random == a) {
                        if (random2 == 4){
                            for(int i1 = 0; i1 < 3; ++i1) {
                                ZombifiedPiglinEntity zombifiedPiglin = new ZombifiedPiglinEntity(EntityType.ZOMBIFIED_PIGLIN, world);
                                zombifiedPiglin.setPosition(d7, d8, d9);
                                zombifiedPiglin.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                                assert target != null;
                                zombifiedPiglin.enablePersistence();
                                zombifiedPiglin.spawnExplosionParticle();
                                world.addEntity(zombifiedPiglin);
                            }
                        } else {
                            ZombifiedPiglinEntity zombifiedPiglin = new ZombifiedPiglinEntity(EntityType.ZOMBIFIED_PIGLIN, world);
                            zombifiedPiglin.setPosition(d0, d1, d2);
                            zombifiedPiglin.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                            assert target != null;
                            zombifiedPiglin.enablePersistence();
                            zombifiedPiglin.spawnExplosionParticle();
                            world.addEntity(zombifiedPiglin);
                        }
                    }
                    if (random == 3 || random == 5) {
                        if (random2 == 4){
                            ChannellerEntity witchEntity = new ChannellerEntity(ModEntityType.CHANNELLER.get(), world);
                            witchEntity.setPosition(d0, d1, d2);
                            witchEntity.enablePersistence();
                            witchEntity.spawnExplosionParticle();
                            world.addEntity(witchEntity);
                        } else {
                            WitchEntity witchEntity = new WitchEntity(EntityType.WITCH, world);
                            witchEntity.setPosition(d0, d1, d2);
                            witchEntity.enablePersistence();
                            witchEntity.spawnExplosionParticle();
                            world.addEntity(witchEntity);
                        }
                    }
                    if (random == 7 || random == 9) {
                        if (random2 == 4){
                            for(int i1 = 0; i1 < 2; ++i1) {
                                BlackBullEntity blackBullEntity = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                                blackBullEntity.setPosition(d0, d1, d2);
                                blackBullEntity.enablePersistence();
                                blackBullEntity.spawnExplosionParticle();
                                world.addEntity(blackBullEntity);
                            }
                        } else {
                            BlackBullEntity blackBullEntity = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                            blackBullEntity.setPosition(d0, d1, d2);
                            blackBullEntity.enablePersistence();
                            blackBullEntity.spawnExplosionParticle();
                            world.addEntity(blackBullEntity);
                        }
                    }

                }
            } else {
                CallerEntity.this.regen = false;
                if (wavetimer >= waveSpawn) {
                    ++this.wave;
                    this.wavetimer = 0;
                    if (this.wave > 30) {
                        CallerEntity.this.damageEntity(DamageSource.GENERIC, 8.0F);
                    }
                    if (random == a) {
                        if (CallerEntity.this.getHealth() < CallerEntity.this.getMaxHealth()/2){
                            for(int i1 = 0; i1 < 3; ++i1) {
                                ZombifiedPiglinEntity zombifiedPiglin = new ZombifiedPiglinEntity(EntityType.ZOMBIFIED_PIGLIN, world);
                                zombifiedPiglin.setPosition(d7, d8, d9);
                                zombifiedPiglin.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                                assert target != null;
                                zombifiedPiglin.enablePersistence();
                                zombifiedPiglin.spawnExplosionParticle();
                                world.addEntity(zombifiedPiglin);
                            }
                        } else {
                            ZombifiedPiglinEntity zombifiedPiglin = new ZombifiedPiglinEntity(EntityType.ZOMBIFIED_PIGLIN, world);
                            zombifiedPiglin.setPosition(d7, d8, d9);
                            zombifiedPiglin.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                            assert target != null;
                            zombifiedPiglin.enablePersistence();
                            zombifiedPiglin.spawnExplosionParticle();
                            world.addEntity(zombifiedPiglin);
                        }
                    }
                    if (random == 3 || random == 5) {
                        if (random2 == 4){
                            ChannellerEntity witchEntity = new ChannellerEntity(ModEntityType.CHANNELLER.get(), world);
                            witchEntity.setPosition(d0, d1, d2);
                            witchEntity.enablePersistence();
                            witchEntity.spawnExplosionParticle();
                            world.addEntity(witchEntity);
                        } else {
                            WitchEntity witchEntity = new WitchEntity(EntityType.WITCH, world);
                            witchEntity.setPosition(d0, d1, d2);
                            witchEntity.enablePersistence();
                            witchEntity.spawnExplosionParticle();
                            world.addEntity(witchEntity);
                        }
                    }
                    if (random == 7 || random == 9) {
                        if (random2 == 4){
                            TaillessWretchEntity blackBullEntity = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                            blackBullEntity.setPosition(d7, d8, d9);
                            blackBullEntity.enablePersistence();
                            blackBullEntity.spawnExplosionParticle();
                            world.addEntity(blackBullEntity);
                        } else {
                            BlackBullEntity blackBullEntity = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                            blackBullEntity.setPosition(d7, d8, d9);
                            blackBullEntity.enablePersistence();
                            blackBullEntity.spawnExplosionParticle();
                            world.addEntity(blackBullEntity);
                        }
                    }
                }
            }
        }

    }

    class MeteorGoal extends Goal{
        public int meteorTimer;
        public int meteorTimerEnd;

        @Override
        public boolean shouldExecute() {
            return CallerEntity.this.isAlive();
        }

        public void tick() {
            ++this.meteorTimer;
            if (!CallerEntity.this.isCharged()){
                this.meteorTimerEnd = 200 + world.rand.nextInt(200);
            } else {
                this.meteorTimerEnd = 400 + world.rand.nextInt(400);
            }
            if (this.meteorTimer >= meteorTimerEnd){
                this.meteorTimer = 0;
                Random random = CallerEntity.this.world.rand;
                double d = (world.rand.nextBoolean() ? 1: -1);
                double e = (world.rand.nextBoolean() ? 1: -1);
                double d2 = random.nextInt(900) * d;
                double d3 = -900.0D;
                double d4 = random.nextInt(900) * e;
                ScorchBallEntity fireball = new ScorchBallEntity(CallerEntity.this.world, CallerEntity.this, d2, d3, d4);
                fireball.setPosition(CallerEntity.this.getPosX(), CallerEntity.this.getPosY() + 32.0D, CallerEntity.this.getPosZ());
                CallerEntity.this.world.addEntity(fireball);
                if (!CallerEntity.this.isSilent()) {
                    CallerEntity.this.world.playEvent(null, 1016, fireball.getPosition(), 0);
                }
            }
        }
    }
}