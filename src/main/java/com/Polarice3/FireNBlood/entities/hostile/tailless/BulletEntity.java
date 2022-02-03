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

public class BulletEntity extends FlyingTaillessEntity {
    protected static final DataParameter<Byte> BULLET_FLAGS = EntityDataManager.defineId(BulletEntity.class, DataSerializers.BYTE);

    private MobEntity owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;


    public BulletEntity(EntityType<? extends FlyingTaillessEntity> type, World worldIn) {
        super(type, worldIn);
        this.xpReward = 0;
        this.moveControl = new BulletEntity.MoveHelperController(this);
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

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new BulletEntity.AttackGoal());
        this.goalSelector.addGoal(8, new BulletEntity.MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new BulletEntity.CopyOwnerTargetGoal(this));
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
            if (BulletEntity.this.getTarget() != null && !BulletEntity.this.getMoveControl().hasWanted()) {
                return true/*BulletEntity.this.distanceTo(BulletEntity.this.getTarget()) > 4.0D*/;
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return BulletEntity.this.getMoveControl().hasWanted() && BulletEntity.this.isCharging() && BulletEntity.this.getTarget() != null && BulletEntity.this.getTarget().isAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            LivingEntity livingentity = BulletEntity.this.getTarget();
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            BulletEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            BulletEntity.this.setChargingCrossbow(true);
        }

        public void stop() {
            BulletEntity.this.setChargingCrossbow(false);
        }


        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = BulletEntity.this.getTarget();
            if (BulletEntity.this.getBoundingBox().inflate(0.2D).intersects(livingentity.getBoundingBox())) {
                this.explode();
            }

        }

        public void explode() {
            if (!level.isClientSide) {
                BulletEntity.this.dead = true;
                level.explode(BulletEntity.this, BulletEntity.this.getX(), BulletEntity.this.getY(), BulletEntity.this.getZ(), 1.5F, false, Explosion.Mode.NONE);
                BulletEntity.this.remove();
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
            return BulletEntity.this.owner != null && BulletEntity.this.owner.getTarget() != null && this.canAttack(BulletEntity.this.owner.getTarget(), this.field_220803_b);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            BulletEntity.this.setTarget(BulletEntity.this.owner.getTarget());
            super.start();
        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(BulletEntity bullet) {
            super(bullet);
        }

        public void tick() {
            if (this.operation == MovementController.Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.wantedX - BulletEntity.this.getX(), this.wantedY - BulletEntity.this.getY(), this.wantedZ - BulletEntity.this.getZ());
                double d0 = vector3d.length();
                if (d0 < BulletEntity.this.getBoundingBox().getSize()) {
                    this.operation = MovementController.Action.WAIT;
                    BulletEntity.this.setDeltaMovement(BulletEntity.this.getDeltaMovement().scale(0.5D));
                } else {
                    BulletEntity.this.setDeltaMovement(BulletEntity.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                    if (BulletEntity.this.getTarget() == null) {
                        Vector3d vector3d1 = BulletEntity.this.getDeltaMovement();
                        BulletEntity.this.yRot = -((float)MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                        BulletEntity.this.yBodyRot = BulletEntity.this.yRot;
                    } else {
                        double d2 = BulletEntity.this.getTarget().getX() - BulletEntity.this.getX();
                        double d1 = BulletEntity.this.getTarget().getZ() - BulletEntity.this.getZ();
                        BulletEntity.this.yRot = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                        BulletEntity.this.yBodyRot = BulletEntity.this.yRot;
                    }
                }

            }
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return !BulletEntity.this.getMoveControl().hasWanted() && BulletEntity.this.random.nextInt(7) == 0;
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
            BlockPos blockpos = BulletEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = BulletEntity.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(BulletEntity.this.random.nextInt(15) - 7, BulletEntity.this.random.nextInt(11) - 5, BulletEntity.this.random.nextInt(15) - 7);
                if (BulletEntity.this.level.isEmptyBlock(blockpos1)) {
                    BulletEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (BulletEntity.this.getTarget() == null) {
                        BulletEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }


}
