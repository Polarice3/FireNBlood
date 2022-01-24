package com.Polarice3.FireNBlood.entities.hostile;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.VexEntity;
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
    protected static final DataParameter<Byte> BULLET_FLAGS = EntityDataManager.createKey(RoyalBulletEntity.class, DataSerializers.BYTE);

    private MobEntity owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;


    public RoyalBulletEntity(EntityType<? extends FlyingTaillessEntity> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 0;
        this.moveController = new RoyalBulletEntity.MoveHelperController(this);
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.doBlockCollisions();
    }

    public void tick() {
        super.tick();
        this.setNoGravity(true);
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }

    }

    public boolean isImmuneToExplosions(){return true;}

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 5.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.4D);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new RoyalBulletEntity.AttackGoal());
        this.goalSelector.addGoal(8, new RoyalBulletEntity.MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, new RoyalBulletEntity.CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllagerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    protected void updateAITasks() {
        super.updateAITasks();
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_POLAR_BEAR_AMBIENT_BABY;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_FOX_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PHANTOM_DEATH;
    }

    protected boolean isDespawnPeaceful() {
        return true;
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(BULLET_FLAGS, (byte)0);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
        }

        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }

    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
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

    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private boolean getBulletFlag(int mask) {
        int i = this.dataManager.get(BULLET_FLAGS);
        return (i & mask) != 0;
    }

    private void setBulletFlag(int mask, boolean value) {
        int i = this.dataManager.get(BULLET_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(BULLET_FLAGS, (byte)(i & 255));
    }

    public boolean isCharging() {
        return this.getBulletFlag(1);
    }

    public void setCharging(boolean charging) {
        this.setBulletFlag(1, charging);
    }

    class AttackGoal extends Goal {
        private AttackGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (RoyalBulletEntity.this.getAttackTarget() != null && !RoyalBulletEntity.this.getMoveHelper().isUpdating()) {
                return true/*RoyalBulletEntity.this.getDistance(RoyalBulletEntity.this.getAttackTarget()) > 4.0D*/;
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return RoyalBulletEntity.this.getMoveHelper().isUpdating() && RoyalBulletEntity.this.isCharging() && RoyalBulletEntity.this.getAttackTarget() != null && RoyalBulletEntity.this.getAttackTarget().isAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            LivingEntity livingentity = RoyalBulletEntity.this.getAttackTarget();
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            RoyalBulletEntity.this.moveController.setMoveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            RoyalBulletEntity.this.setCharging(true);
        }

        public void resetTask() {
            RoyalBulletEntity.this.setCharging(false);
        }


        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = RoyalBulletEntity.this.getAttackTarget();
            if (RoyalBulletEntity.this.getBoundingBox().grow(1.0D).intersects(livingentity.getBoundingBox())) {
                this.explode();
            }

        }

        public void explode() {
            if (!world.isRemote) {
                RoyalBulletEntity.this.dead = true;
                world.createExplosion(RoyalBulletEntity.this, RoyalBulletEntity.this.getPosX(), RoyalBulletEntity.this.getPosY(), RoyalBulletEntity.this.getPosZ(), 1.5F, false, Explosion.Mode.NONE);
                RoyalBulletEntity.this.remove();
            }
        }
    }

    class CopyOwnerTargetGoal extends TargetGoal {
        private final EntityPredicate field_220803_b = (new EntityPredicate()).setIgnoresLineOfSight().setUseInvisibilityCheck();

        public CopyOwnerTargetGoal(CreatureEntity creature) {
            super(creature, false);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return RoyalBulletEntity.this.owner != null && RoyalBulletEntity.this.owner.getAttackTarget() != null && this.isSuitableTarget(RoyalBulletEntity.this.owner.getAttackTarget(), this.field_220803_b);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            RoyalBulletEntity.this.setAttackTarget(RoyalBulletEntity.this.owner.getAttackTarget());
            super.startExecuting();
        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(RoyalBulletEntity bullet) {
            super(bullet);
        }

        public void tick() {
            if (this.action == Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.posX - RoyalBulletEntity.this.getPosX(), this.posY - RoyalBulletEntity.this.getPosY(), this.posZ - RoyalBulletEntity.this.getPosZ());
                double d0 = vector3d.length();
                if (d0 < RoyalBulletEntity.this.getBoundingBox().getAverageEdgeLength()) {
                    this.action = Action.WAIT;
                    RoyalBulletEntity.this.setMotion(RoyalBulletEntity.this.getMotion().scale(0.5D));
                } else {
                    RoyalBulletEntity.this.setMotion(RoyalBulletEntity.this.getMotion().add(vector3d.scale(this.speed * 0.05D / d0)));
                    if (RoyalBulletEntity.this.getAttackTarget() == null) {
                        Vector3d vector3d1 = RoyalBulletEntity.this.getMotion();
                        RoyalBulletEntity.this.rotationYaw = -((float)MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                        RoyalBulletEntity.this.renderYawOffset = RoyalBulletEntity.this.rotationYaw;
                    } else {
                        double d2 = RoyalBulletEntity.this.getAttackTarget().getPosX() - RoyalBulletEntity.this.getPosX();
                        double d1 = RoyalBulletEntity.this.getAttackTarget().getPosZ() - RoyalBulletEntity.this.getPosZ();
                        RoyalBulletEntity.this.rotationYaw = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                        RoyalBulletEntity.this.renderYawOffset = RoyalBulletEntity.this.rotationYaw;
                    }
                }

            }
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return !RoyalBulletEntity.this.getMoveHelper().isUpdating() && RoyalBulletEntity.this.rand.nextInt(7) == 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return false;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            BlockPos blockpos = RoyalBulletEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = RoyalBulletEntity.this.getPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(RoyalBulletEntity.this.rand.nextInt(15) - 7, RoyalBulletEntity.this.rand.nextInt(11) - 5, RoyalBulletEntity.this.rand.nextInt(15) - 7);
                if (RoyalBulletEntity.this.world.isAirBlock(blockpos1)) {
                    RoyalBulletEntity.this.moveController.setMoveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (RoyalBulletEntity.this.getAttackTarget() == null) {
                        RoyalBulletEntity.this.getLookController().setLookPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }


}
