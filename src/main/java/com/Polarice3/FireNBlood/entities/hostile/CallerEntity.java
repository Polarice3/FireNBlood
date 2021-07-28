package com.Polarice3.FireNBlood.entities.hostile;

import com.Polarice3.FireNBlood.entities.masters.MinotaurEntity;
import com.Polarice3.FireNBlood.entities.masters.TaillessProphetEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class CallerEntity extends AbstractTaillessEntity {
    public CallerEntity(EntityType<? extends AbstractTaillessEntity> type, World worldIn) {
        super(type, worldIn);
    }
    public boolean regen = true;


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
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractIllagerEntity.class, true));
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

    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return false;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public void applyEntityCollision(Entity entityIn) {
    }

    protected void updateAITasks(){
        if (this.regen = true) {
            if (this.ticksExisted % 20 == 0) {
                this.heal(1.0F);
            }
        }
        super.updateAITasks();
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    private final EntityPredicate mobs = (new EntityPredicate().setDistance(32.0D));

    public boolean Activation(){
        List<AbstractIllagerEntity> illagers = this.world.getTargettableEntitiesWithinAABB(AbstractIllagerEntity.class, this.mobs, this, this.getBoundingBox().grow(32.0D, 32.0D, 32.0D));
        List<IronGolemEntity> golems = this.world.getTargettableEntitiesWithinAABB(IronGolemEntity.class, this.mobs, this, this.getBoundingBox().grow(32.0D, 32.0D, 32.0D));
        List<PlayerEntity> players = this.world.getTargettableEntitiesWithinAABB(PlayerEntity.class, this.mobs, this, this.getBoundingBox().grow(32.0D, 32.0D, 32.0D));
        if (!illagers.isEmpty() || !golems.isEmpty() || !players.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void tick() {
        super.tick();
        if (this.world.isRemote && this.Activation()) {
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

    class MobSpawnGoal extends Goal {
        public int wavetimer;
        public int wave = 0;
        public int Nextwave = 0;

        public boolean shouldExecute() {
            if (CallerEntity.this.isAlive()) {
                if (CallerEntity.this.getAttackTarget() != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        public void tick() {
            double rp = CallerEntity.this.world.rand.nextInt(5);
            ++wavetimer;
            if (this.wave == 0) {
                if (wavetimer == 120) {
                    CallerEntity.this.damageEntity(DamageSource.GENERIC, 8.0F);
                    this.wavetimer = 0;
                    this.Nextwave = this.Nextwave + 1;
                    BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                    blackbull.setPosition(CallerEntity.this.getPosX() + rp, CallerEntity.this.getPosY(), CallerEntity.this.getPosZ() + rp);
                    for (int i = 0; i < 4; ++i) {
                        CallerEntity.this.world.addParticle(ParticleTypes.FLAME, blackbull.getPosXRandom(0.5D), blackbull.getPosYRandom(), blackbull.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                    }
                    world.addEntity(blackbull);
                    if (this.Nextwave >= 6) {
                        this.wave = 1;
                        this.Nextwave = 0;
                    }
                }
            }
            if (this.wave == 1) {
                if (wavetimer == 120) {
                    CallerEntity.this.damageEntity(DamageSource.GENERIC, 8.0F);
                    this.wavetimer = 0;
                    this.Nextwave = this.Nextwave + 1;
                    TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                    wretch.setPosition(CallerEntity.this.getPosX() + rp, CallerEntity.this.getPosY(), CallerEntity.this.getPosZ() + rp);
                    for (int i = 0; i < 4; ++i) {
                        CallerEntity.this.world.addParticle(ParticleTypes.FLAME, wretch.getPosXRandom(0.5D), wretch.getPosYRandom(), wretch.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                    }
                    world.addEntity(wretch);
                    if (this.Nextwave >= 4) {
                        this.wave = 2;
                        this.Nextwave = 0;
                    }
                }
            }
            if (this.wave == 2) {
                if (wavetimer == 120) {
                    CallerEntity.this.damageEntity(DamageSource.GENERIC, 8.0F);
                    this.wavetimer = 0;
                    this.Nextwave = this.Nextwave + 1;
                    TaillessHorrorEntity horror = new TaillessHorrorEntity(ModEntityType.TAILLESS_HORROR.get(), world);
                    horror.setPosition(CallerEntity.this.getPosX() + rp, CallerEntity.this.getPosY(), CallerEntity.this.getPosZ() + rp);
                    for (int i = 0; i < 4; ++i) {
                        CallerEntity.this.world.addParticle(ParticleTypes.FLAME, horror.getPosXRandom(0.5D), horror.getPosYRandom(), horror.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                    }
                    world.addEntity(horror);
                    if (this.Nextwave >= 2) {
                        this.wave = 3;
                        this.Nextwave = 0;
                    }
                }
            }
            if (this.wave == 3) {
                if (wavetimer == 120) {
                    CallerEntity.this.damageEntity(DamageSource.GENERIC, 8.0F);
                    this.wavetimer = 0;
                    this.Nextwave = this.Nextwave + 1;
                    TaillessDruidEntity druid = new TaillessDruidEntity(ModEntityType.TAILLESS_DRUID.get(), world);
                    druid.setPosition(CallerEntity.this.getPosX() + rp, CallerEntity.this.getPosY(), CallerEntity.this.getPosZ() + rp);
                    for (int i = 0; i < 4; ++i) {
                        CallerEntity.this.world.addParticle(ParticleTypes.FLAME, druid.getPosXRandom(0.5D), druid.getPosYRandom(), druid.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                    }
                    world.addEntity(druid);
                    if (this.Nextwave >= 2) {
                        this.wave = 4;
                        this.Nextwave = 0;
                    }
                }
            }
            if (this.wave == 4) {
                if (wavetimer == 240) {
                    CallerEntity.this.damageEntity(DamageSource.GENERIC, 8.0F);
                    this.wavetimer = 0;
                    this.Nextwave = this.Nextwave + 1;
                    int random = CallerEntity.this.world.rand.nextInt(2);
                    if (random == 1) {
                        MinotaurEntity minotaur = new MinotaurEntity(ModEntityType.MINOTAUR.get(), world);
                        minotaur.setPosition(CallerEntity.this.getPosX(), CallerEntity.this.getPosY(), CallerEntity.this.getPosZ());
                        for (int i = 0; i < 4; ++i) {
                            CallerEntity.this.world.addParticle(ParticleTypes.FLAME, minotaur.getPosXRandom(0.5D), minotaur.getPosYRandom(), minotaur.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                        }
                        world.addEntity(minotaur);
                        if (this.Nextwave >= 1) {
                            this.wave = 5;
                            this.Nextwave = 0;
                        }
                    } else {
                        TaillessProphetEntity prophet = new TaillessProphetEntity(ModEntityType.TAILLESS_PROPHET.get(), world);
                        prophet.setPosition(CallerEntity.this.getPosX(), CallerEntity.this.getPosY(), CallerEntity.this.getPosZ());
                        for (int i = 0; i < 4; ++i) {
                            CallerEntity.this.world.addParticle(ParticleTypes.FLAME, prophet.getPosXRandom(0.5D), prophet.getPosYRandom(), prophet.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                        }
                        world.addEntity(prophet);
                        if (this.Nextwave >= 1) {
                            this.wave = 5;
                            this.Nextwave = 0;
                        }
                    }
                }
            }
            if (this.wave == 5) {
                CallerEntity.this.regen = false;
                if (wavetimer == 120) {
                    CallerEntity.this.damageEntity(DamageSource.GENERIC, 8.0F);
                    this.wavetimer = 0;
                    int random = CallerEntity.this.world.rand.nextInt(12);
                    if (random == 1 || random == 2 || random == 3 || random == 4 || random == 5 || random == 6 || random == 7) {
                        BlackBullEntity blackbull = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
                        blackbull.setPosition(CallerEntity.this.getPosX() + rp, CallerEntity.this.getPosY(), CallerEntity.this.getPosZ() + rp);
                        for (int i = 0; i < 4; ++i) {
                            CallerEntity.this.world.addParticle(ParticleTypes.FLAME, blackbull.getPosXRandom(0.5D), blackbull.getPosYRandom(), blackbull.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                        }
                        world.addEntity(blackbull);
                    } else if (random == 8 || random == 9) {
                        TaillessWretchEntity wretch = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), world);
                        wretch.setPosition(CallerEntity.this.getPosX() + rp, CallerEntity.this.getPosY(), CallerEntity.this.getPosZ() + rp);
                        for (int i = 0; i < 4; ++i) {
                            CallerEntity.this.world.addParticle(ParticleTypes.FLAME, wretch.getPosXRandom(0.5D), wretch.getPosYRandom(), wretch.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                        }
                        world.addEntity(wretch);
                    } else if (random == 10 || random == 11) {
                        TaillessHorrorEntity horror = new TaillessHorrorEntity(ModEntityType.TAILLESS_HORROR.get(), world);
                        horror.setPosition(CallerEntity.this.getPosX() + rp, CallerEntity.this.getPosY(), CallerEntity.this.getPosZ() + rp);
                        for (int i = 0; i < 4; ++i) {
                            CallerEntity.this.world.addParticle(ParticleTypes.FLAME, horror.getPosXRandom(0.5D), horror.getPosYRandom(), horror.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                        }
                        world.addEntity(horror);
                    } else {
                        TaillessDruidEntity druid = new TaillessDruidEntity(ModEntityType.TAILLESS_DRUID.get(), world);
                        druid.setPosition(CallerEntity.this.getPosX() + rp, CallerEntity.this.getPosY(), CallerEntity.this.getPosZ() + rp);
                        for (int i = 0; i < 4; ++i) {
                            CallerEntity.this.world.addParticle(ParticleTypes.FLAME, druid.getPosXRandom(0.5D), druid.getPosYRandom(), druid.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                        }
                        world.addEntity(druid);
                    }
                }
            }
        }

    }
}