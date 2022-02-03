package com.Polarice3.FireNBlood.entities.hostile.tailless;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class RoyalBulletEntity extends FlyingTaillessEntity {
    protected static final DataParameter<Byte> BULLET_FLAGS = EntityDataManager.defineId(RoyalBulletEntity.class, DataSerializers.BYTE);

    private MobEntity owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;


    public RoyalBulletEntity(EntityType<? extends FlyingTaillessEntity> type, World worldIn) {
        super(type, worldIn);
        this.xpReward = 0;
        this.moveControl = new RoyalBulletEntity.MoveHelperController(this);
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.checkInsideBlocks();
    }

    public void tick() {
        super.tick();
        this.setNoGravity(true);
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(DamageSource.STARVE, 1.0F);
        }

    }

    public boolean ignoreExplosion(){return true;}

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new RoyalBulletEntity.AttackGoal());
        this.goalSelector.addGoal(8, new RoyalBulletEntity.MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new RoyalBulletEntity.CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllagerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.POLAR_BEAR_AMBIENT_BABY;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.FOX_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PHANTOM_DEATH;
    }

    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BULLET_FLAGS, (byte)0);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
        }

        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }

    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.boundOrigin != null) {
            compound.putInt("BoundX", this.boundOrigin.getX());
            compound.putInt("BoundY", this.boundOrigin.getY());
            compound.putInt("BoundZ", this.boundOrigin.getZ());
        }

        if (this.limitedLifespan) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }

    }

    public void setOwner(MobEntity ownerIn) {
        this.owner = ownerIn;
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOriginIn) {
        this.boundOrigin = boundOriginIn;
    }

    public void setLimitedLife(int limitedLifeTicksIn) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private boolean getBulletFlag(int mask) {
        int i = this.entityData.get(BULLET_FLAGS);
        return (i & mask) != 0;
    }

    private void setBulletFlag(int mask, boolean value) {
        int i = this.entityData.get(BULLET_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(BULLET_FLAGS, (byte)(i & 255));
    }

    public boolean isCharging() {
        return this.getBulletFlag(1);
    }

    public void setChargingCrossbow(boolean charging) {
        this.setBulletFlag(1, charging);
    }

    class AttackGoal extends Goal {
        private AttackGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (RoyalBulletEntity.this.getTarget() != null && !RoyalBulletEntity.this.getMoveControl().hasWanted()) {
                return true/*RoyalBulletEntity.this.distanceTo(RoyalBulletEntity.this.getTarget()) > 4.0D*/;
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return RoyalBulletEntity.this.getMoveControl().hasWanted() && RoyalBulletEntity.this.isCharging() && RoyalBulletEntity.this.getTarget() != null && RoyalBulletEntity.this.getTarget().isAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            LivingEntity livingentity = RoyalBulletEntity.this.getTarget();
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            RoyalBulletEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            RoyalBulletEntity.this.setChargingCrossbow(true);
        }

        public void stop() {
            RoyalBulletEntity.this.setChargingCrossbow(false);
        }


        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = RoyalBulletEntity.this.getTarget();
            if (RoyalBulletEntity.this.getBoundingBox().inflate(1.0D).intersects(livingentity.getBoundingBox())) {
                this.explode();
            }

        }

        public void explode() {
            if (!level.isClientSide) {
                RoyalBulletEntity.this.dead = true;
                level.explode(RoyalBulletEntity.this, RoyalBulletEntity.this.getX(), RoyalBulletEntity.this.getY(), RoyalBulletEntity.this.getZ(), 1.5F, false, Explosion.Mode.NONE);
                RoyalBulletEntity.this.remove();
            }
        }
    }

    class CopyOwnerTargetGoal extends TargetGoal {
        private final EntityPredicate field_220803_b = (new EntityPredicate()).allowUnseeable().ignoreInvisibilityTesting();

        public CopyOwnerTargetGoal(CreatureEntity creature) {
            super(creature, false);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return RoyalBulletEntity.this.owner != null && RoyalBulletEntity.this.owner.getTarget() != null && this.canAttack(RoyalBulletEntity.this.owner.getTarget(), this.field_220803_b);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            RoyalBulletEntity.this.setTarget(RoyalBulletEntity.this.owner.getTarget());
            super.start();
        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(RoyalBulletEntity bullet) {
            super(bullet);
        }

        public void tick() {
            if (this.operation == Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.wantedX - RoyalBulletEntity.this.getX(), this.wantedY - RoyalBulletEntity.this.getY(), this.wantedZ - RoyalBulletEntity.this.getZ());
                double d0 = vector3d.length();
                if (d0 < RoyalBulletEntity.this.getBoundingBox().getSize()) {
                    this.operation = Action.WAIT;
                    RoyalBulletEntity.this.setDeltaMovement(RoyalBulletEntity.this.getDeltaMovement().scale(0.5D));
                } else {
                    RoyalBulletEntity.this.setDeltaMovement(RoyalBulletEntity.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                    if (RoyalBulletEntity.this.getTarget() == null) {
                        Vector3d vector3d1 = RoyalBulletEntity.this.getDeltaMovement();
                        RoyalBulletEntity.this.yRot = -((float)MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                        RoyalBulletEntity.this.yBodyRot = RoyalBulletEntity.this.yRot;
                    } else {
                        double d2 = RoyalBulletEntity.this.getTarget().getX() - RoyalBulletEntity.this.getX();
                        double d1 = RoyalBulletEntity.this.getTarget().getZ() - RoyalBulletEntity.this.getZ();
                        RoyalBulletEntity.this.yRot = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                        RoyalBulletEntity.this.yBodyRot = RoyalBulletEntity.this.yRot;
                    }
                }

            }
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return !RoyalBulletEntity.this.getMoveControl().hasWanted() && RoyalBulletEntity.this.random.nextInt(7) == 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return false;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            BlockPos blockpos = RoyalBulletEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = RoyalBulletEntity.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(RoyalBulletEntity.this.random.nextInt(15) - 7, RoyalBulletEntity.this.random.nextInt(11) - 5, RoyalBulletEntity.this.random.nextInt(15) - 7);
                if (RoyalBulletEntity.this.level.isEmptyBlock(blockpos1)) {
                    RoyalBulletEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (RoyalBulletEntity.this.getTarget() == null) {
                        RoyalBulletEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }


}
