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

public class BulletEntity extends FlyingTaillessEntity {
    protected static final DataParameter<Byte> BULLET_FLAGS = EntityDataManager.createKey(BulletEntity.class, DataSerializers.BYTE);

    private MobEntity owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;


    public BulletEntity(EntityType<? extends FlyingTaillessEntity> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 0;
        this.moveController = new BulletEntity.MoveHelperController(this);
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

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 5.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new BulletEntity.AttackGoal());
        this.goalSelector.addGoal(8, new BulletEntity.MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, new BulletEntity.CopyOwnerTargetGoal(this));
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
            if (BulletEntity.this.getAttackTarget() != null && !BulletEntity.this.getMoveHelper().isUpdating()) {
                return true/*BulletEntity.this.getDistance(BulletEntity.this.getAttackTarget()) > 4.0D*/;
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return BulletEntity.this.getMoveHelper().isUpdating() && BulletEntity.this.isCharging() && BulletEntity.this.getAttackTarget() != null && BulletEntity.this.getAttackTarget().isAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            LivingEntity livingentity = BulletEntity.this.getAttackTarget();
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            BulletEntity.this.moveController.setMoveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            BulletEntity.this.setCharging(true);
        }

        public void resetTask() {
            BulletEntity.this.setCharging(false);
        }


        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = BulletEntity.this.getAttackTarget();
            if (BulletEntity.this.getBoundingBox().grow(0.2D).intersects(livingentity.getBoundingBox())) {
                this.explode();
            }

        }

        public void explode() {
            if (!world.isRemote) {
                BulletEntity.this.dead = true;
                world.createExplosion(BulletEntity.this, BulletEntity.this.getPosX(), BulletEntity.this.getPosY(), BulletEntity.this.getPosZ(), 1.5F, false, Explosion.Mode.NONE);
                BulletEntity.this.remove();
            }
        }
    }

    class CopyOwnerTargetGoal extends TargetGoal {
        private final EntityPredicate field_220803_b = (new EntityPredicate()).setLineOfSiteRequired().setUseInvisibilityCheck();

        public CopyOwnerTargetGoal(CreatureEntity creature) {
            super(creature, false);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return BulletEntity.this.owner != null && BulletEntity.this.owner.getAttackTarget() != null && this.isSuitableTarget(BulletEntity.this.owner.getAttackTarget(), this.field_220803_b);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            BulletEntity.this.setAttackTarget(BulletEntity.this.owner.getAttackTarget());
            super.startExecuting();
        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(BulletEntity bullet) {
            super(bullet);
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.posX - BulletEntity.this.getPosX(), this.posY - BulletEntity.this.getPosY(), this.posZ - BulletEntity.this.getPosZ());
                double d0 = vector3d.length();
                if (d0 < BulletEntity.this.getBoundingBox().getAverageEdgeLength()) {
                    this.action = MovementController.Action.WAIT;
                    BulletEntity.this.setMotion(BulletEntity.this.getMotion().scale(0.5D));
                } else {
                    BulletEntity.this.setMotion(BulletEntity.this.getMotion().add(vector3d.scale(this.speed * 0.05D / d0)));
                    if (BulletEntity.this.getAttackTarget() == null) {
                        Vector3d vector3d1 = BulletEntity.this.getMotion();
                        BulletEntity.this.rotationYaw = -((float)MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                        BulletEntity.this.renderYawOffset = BulletEntity.this.rotationYaw;
                    } else {
                        double d2 = BulletEntity.this.getAttackTarget().getPosX() - BulletEntity.this.getPosX();
                        double d1 = BulletEntity.this.getAttackTarget().getPosZ() - BulletEntity.this.getPosZ();
                        BulletEntity.this.rotationYaw = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                        BulletEntity.this.renderYawOffset = BulletEntity.this.rotationYaw;
                    }
                }

            }
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return !BulletEntity.this.getMoveHelper().isUpdating() && BulletEntity.this.rand.nextInt(7) == 0;
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
            BlockPos blockpos = BulletEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = BulletEntity.this.getPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(BulletEntity.this.rand.nextInt(15) - 7, BulletEntity.this.rand.nextInt(11) - 5, BulletEntity.this.rand.nextInt(15) - 7);
                if (BulletEntity.this.world.isAirBlock(blockpos1)) {
                    BulletEntity.this.moveController.setMoveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (BulletEntity.this.getAttackTarget() == null) {
                        BulletEntity.this.getLookController().setLookPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }


}
