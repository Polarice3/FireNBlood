package com.Polarice3.FireNBlood.entities.hostile.tailless;

import com.Polarice3.FireNBlood.entities.hostile.cultists.ChannellerEntity;
import com.Polarice3.FireNBlood.entities.projectiles.NetherBallEntity;
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
    private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS).setDarkenScreen(true));

    public CallerEntity(EntityType<? extends AbstractTaillessEntity> type, World worldIn) {
        super(type, worldIn);
        this.setrandom(0);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0D)
                .add(Attributes.ATTACK_DAMAGE, 0D)
                .add(Attributes.ATTACK_SPEED, 0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 10.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MobSpawnGoal());
        this.goalSelector.addGoal(1, new MeteorGoal());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setAlertOthers());
    }

    @Override
    protected int getExperienceReward(PlayerEntity player) {
        return 25 + this.level.random.nextInt(5);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ITEM_BREAK;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.WOOD_BREAK;
    }

    public boolean canBeAffected(EffectInstance potioneffectIn) {
        return false;
    }

    protected boolean isMovementNoisy() {
        return false;
    }

    public void push(Entity entityIn) {
    }

    private final EntityPredicate ally = (new EntityPredicate().range(10.0D));

    public boolean hurt(DamageSource source, float amount){
        List<TaillessDruidEntity> list = CallerEntity.this.level.getNearbyEntities(TaillessDruidEntity.class, this.ally, CallerEntity.this, CallerEntity.this.getBoundingBox().inflate(10.0D, 8.0D, 10.0D));
        if (list.isEmpty()) {
            return super.hurt(source, amount);
        } else {
            return false;
        }
    }

    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    public void die(DamageSource cause) {
        for (PlayerEntity entity : this.level.getEntitiesOfClass(PlayerEntity.class, this.getBoundingBox().inflate(64.0D, 64.0D, 64.0D), field_213690_b)) {
            EffectInstance effectinstance1 = entity.getEffect(RegistryHandler.EVIL_EYE.get());
            if (effectinstance1 == null) {
                EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 2);
                entity.addEffect(effectinstance);
            } else {
                int amp = effectinstance1.getAmplifier();
                int i = amp + 2;
                i = MathHelper.clamp(i, 0, 5);
                entity.removeEffectNoUpdate(RegistryHandler.EVIL_EYE.get());
                EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                entity.addEffect(effectinstance);
            }
        }
    }

    protected void customServerAiStep(){
        if (this.regen) {
            if (this.tickCount % 20 == 0) {
                this.heal(1.0F);
            }
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        super.customServerAiStep();
    }

    public void startSeenByPlayer(ServerPlayerEntity player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void stopSeenByPlayer(ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        this.spawnAtLocation(RegistryHandler.RIFTSHARD.get());
    }

    private final EntityPredicate mobs = (new EntityPredicate().range(64.0D));

    public void tick() {
        super.tick();
        this.bossInfo.setVisible(!this.isPowered());
        if (this.level.isClientSide) {
            for (int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleTypes.FLAME, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void aiStep() {
        this.setDeltaMovement(Vector3d.ZERO);
        if (!this.isNoAi()) {
            this.yBodyRotO = 0.0F;
            this.yBodyRot = 0.0F;
        }

        super.aiStep();
    }

    @Override
    public boolean isPowered() {
        List<TaillessDruidEntity> list = CallerEntity.this.level.getNearbyEntities(TaillessDruidEntity.class, this.ally, CallerEntity.this, CallerEntity.this.getBoundingBox().inflate(10.0D, 8.0D, 10.0D));
        return !list.isEmpty();
    }

    class MobSpawnGoal extends Goal {
        public int wavetimer;
        public int wave = 0;

        public boolean canUse() {
            return CallerEntity.this.isAlive();
        }

        public void tick() {
            LivingEntity target = CallerEntity.this.getTarget();
            int random = level.random.nextInt(10);
            int random2 = level.random.nextInt(4);
            int waveSpawn = CallerEntity.this.isPowered() ? level.random.nextInt(30) + 60 : level.random.nextInt(30) + 30;
            int a = random % 2;
            double d = (level.random.nextBoolean() ? 1: -1);
            double e = (level.random.nextBoolean() ? 1: -1);
            double f = (level.random.nextBoolean() ? 32: 16);
            double d0 = CallerEntity.this.getX() + (level.random.nextDouble() - level.random.nextDouble()) * (double)16 + (f * d);
            double d2 = CallerEntity.this.getZ() + (level.random.nextDouble() - level.random.nextDouble()) * (double)16 + (f * e);
            double d1 = level.getHeight(Heightmap.Type.WORLD_SURFACE, (int) d0, (int) d2);
            double d7 = CallerEntity.this.getX() + (level.random.nextDouble() - level.random.nextDouble()) * (double)16 + (0.5D * d);
            double d9 = CallerEntity.this.getZ() + (level.random.nextDouble() - level.random.nextDouble()) * (double)16 + (0.5D * e);
            double d8 = level.getHeight(Heightmap.Type.WORLD_SURFACE, (int) d7, (int) d9);
            for (Entity entity : CallerEntity.this.level.getEntitiesOfClass(LivingEntity.class, CallerEntity.this.getBoundingBox().inflate(64.0D, 64.0D, 64.0D), field_213690_b)) {
                if (entity instanceof ZombifiedPiglinEntity && target != null) {
                    ((ZombifiedPiglinEntity) entity).setTarget(target);
                }
                if (entity instanceof ZombifiedPiglinEntity && ((ZombifiedPiglinEntity) entity).getTarget() == CallerEntity.this){
                    ((ZombifiedPiglinEntity) entity).setTarget(null);
                    ((ZombifiedPiglinEntity) entity).setPersistentAngerTarget(null);
                    ((ZombifiedPiglinEntity) entity).setRemainingPersistentAngerTime(0);
                }
            }
            ++wavetimer;
            if (CallerEntity.this.isPowered()) {
                CallerEntity.this.regen = true;
                if (wavetimer >= waveSpawn) {
                    ++this.wave;
                    this.wavetimer = 0;
                    if (this.wave > 120) {
                        if (target != null) {
                            target.addEffect(new EffectInstance(Effects.WITHER, 90, 2));
                        }
                    }
                    if (random == a) {
                        if (random2 == 1){
                            for(int i1 = 0; i1 < 3; ++i1) {
                                ZombifiedPiglinEntity zombifiedPiglin = new ZombifiedPiglinEntity(EntityType.ZOMBIFIED_PIGLIN, level);
                                zombifiedPiglin.setPos(d7, d8, d9);
                                zombifiedPiglin.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                                assert target != null;
                                zombifiedPiglin.setPersistenceRequired();
                                zombifiedPiglin.spawnAnim();
                                level.addFreshEntity(zombifiedPiglin);
                            }
                        } else {
                            ZombifiedPiglinEntity zombifiedPiglin = new ZombifiedPiglinEntity(EntityType.ZOMBIFIED_PIGLIN, level);
                            zombifiedPiglin.setPos(d0, d1, d2);
                            zombifiedPiglin.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                            assert target != null;
                            zombifiedPiglin.setPersistenceRequired();
                            zombifiedPiglin.spawnAnim();
                            level.addFreshEntity(zombifiedPiglin);
                        }
                    }
                    if (random == 3) {
                        if (random2 == 1){
                            ChannellerEntity witchEntity = new ChannellerEntity(ModEntityType.CHANNELLER.get(), level);
                            witchEntity.setPos(d0, d1, d2);
                            witchEntity.setPersistenceRequired();
                            witchEntity.spawnAnim();
                            level.addFreshEntity(witchEntity);
                        } else {
                            WitchEntity witchEntity = new WitchEntity(EntityType.WITCH, level);
                            witchEntity.setPos(d0, d1, d2);
                            witchEntity.setPersistenceRequired();
                            witchEntity.spawnAnim();
                            level.addFreshEntity(witchEntity);
                        }
                    }
                    if (random == 7 || random == 9) {
                        if (random2 == 1){
                            for(int i1 = 0; i1 < 2; ++i1) {
                                BlackBullEntity blackBullEntity = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), level);
                                blackBullEntity.setPos(d0, d1, d2);
                                blackBullEntity.setPersistenceRequired();
                                blackBullEntity.spawnAnim();
                                level.addFreshEntity(blackBullEntity);
                            }
                        } else {
                            BlackBullEntity blackBullEntity = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), level);
                            blackBullEntity.setPos(d0, d1, d2);
                            blackBullEntity.setPersistenceRequired();
                            blackBullEntity.spawnAnim();
                            level.addFreshEntity(blackBullEntity);
                        }
                    }

                }
            } else {
                CallerEntity.this.regen = false;
                if (wavetimer >= waveSpawn) {
                    ++this.wave;
                    this.wavetimer = 0;
                    if (this.wave > 120) {
                        CallerEntity.this.actuallyHurt(DamageSource.GENERIC, 8.0F);
                    }
                    if (random == a) {
                        if (CallerEntity.this.getHealth() < CallerEntity.this.getMaxHealth()/2){
                            for(int i1 = 0; i1 < 3; ++i1) {
                                ZombifiedPiglinEntity zombifiedPiglin = new ZombifiedPiglinEntity(EntityType.ZOMBIFIED_PIGLIN, level);
                                zombifiedPiglin.setPos(d7, d8, d9);
                                zombifiedPiglin.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                                assert target != null;
                                zombifiedPiglin.setPersistenceRequired();
                                zombifiedPiglin.spawnAnim();
                                level.addFreshEntity(zombifiedPiglin);
                            }
                        } else {
                            ZombifiedPiglinEntity zombifiedPiglin = new ZombifiedPiglinEntity(EntityType.ZOMBIFIED_PIGLIN, level);
                            zombifiedPiglin.setPos(d7, d8, d9);
                            zombifiedPiglin.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                            assert target != null;
                            zombifiedPiglin.setPersistenceRequired();
                            zombifiedPiglin.spawnAnim();
                            level.addFreshEntity(zombifiedPiglin);
                        }
                    }
                    if (random == 3) {
                        if (random2 == 1){
                            ChannellerEntity witchEntity = new ChannellerEntity(ModEntityType.CHANNELLER.get(), level);
                            witchEntity.setPos(d0, d1, d2);
                            witchEntity.setPersistenceRequired();
                            witchEntity.spawnAnim();
                            level.addFreshEntity(witchEntity);
                        } else {
                            WitchEntity witchEntity = new WitchEntity(EntityType.WITCH, level);
                            witchEntity.setPos(d0, d1, d2);
                            witchEntity.setPersistenceRequired();
                            witchEntity.spawnAnim();
                            level.addFreshEntity(witchEntity);
                        }
                    }
                    if (random == 7 || random == 9) {
                        if (random2 == 1){
                            TaillessWretchEntity blackBullEntity = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), level);
                            blackBullEntity.setPos(d7, d8, d9);
                            blackBullEntity.setPersistenceRequired();
                            blackBullEntity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
                            blackBullEntity.spawnAnim();
                            level.addFreshEntity(blackBullEntity);
                        } else {
                            BlackBullEntity blackBullEntity = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), level);
                            blackBullEntity.setPos(d7, d8, d9);
                            blackBullEntity.setPersistenceRequired();
                            blackBullEntity.spawnAnim();
                            level.addFreshEntity(blackBullEntity);
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
        public boolean canUse() {
            return CallerEntity.this.isAlive();
        }

        public void tick() {
            ++this.meteorTimer;
            if (!CallerEntity.this.isPowered()){
                this.meteorTimerEnd = 3600 + level.random.nextInt(3600);
            } else {
                this.meteorTimerEnd = 3600 + level.random.nextInt(1200);
            }
            if (this.meteorTimer >= meteorTimerEnd){
                this.meteorTimer = 0;
                Random random = CallerEntity.this.level.random;
                double d = (level.random.nextBoolean() ? 1: -1);
                double e = (level.random.nextBoolean() ? 1: -1);
                double d2 = random.nextInt(900) * d;
                double d3 = -900.0D;
                double d4 = random.nextInt(900) * e;
                NetherBallEntity fireball = new NetherBallEntity(CallerEntity.this.level, CallerEntity.this, d2, d3, d4);
                fireball.setPos(CallerEntity.this.getX(), CallerEntity.this.getY() + 32.0D, CallerEntity.this.getZ());
                CallerEntity.this.level.addFreshEntity(fireball);
                if (!CallerEntity.this.isSilent()) {
                    CallerEntity.this.level.levelEvent(null, 1016, fireball.blockPosition(), 0);
                }
            }
        }
    }
}