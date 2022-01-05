package com.Polarice3.FireNBlood.entities.ally;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FriendlyVexEntity extends CreatureEntity {
    protected static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.createKey(FriendlyVexEntity.class, DataSerializers.BYTE);
    private PlayerEntity owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;

    public FriendlyVexEntity(EntityType<? extends CreatureEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.moveController = new FriendlyVexEntity.MoveHelperController(this);
        this.experienceValue = 3;
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.doBlockCollisions();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }

    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new FriendlyVexEntity.ChargeAttackGoal());
        this.goalSelector.addGoal(8, new FriendlyVexEntity.MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_234199_0_) -> {
            return p_234199_0_ instanceof IMob && !(p_234199_0_ instanceof CreeperEntity) && !(p_234199_0_ instanceof FriendlyVexEntity);
        }));
        this.targetSelector.addGoal(1, new FriendlyVexEntity.OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new FriendlyVexEntity.OwnerHurtTargetGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 14.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    protected PathNavigator createNavigation(World worldIn) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanSwim(true);
        flyingpathnavigator.setCanEnterDoors(true);
        return flyingpathnavigator;
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(VEX_FLAGS, (byte)0);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
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

    public PlayerEntity getOwner() {
        return this.owner;
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOriginIn) {
        this.boundOrigin = boundOriginIn;
    }

    private boolean getVexFlag(int mask) {
        int i = this.dataManager.get(VEX_FLAGS);
        return (i & mask) != 0;
    }

    private void setVexFlag(int mask, boolean value) {
        int i = this.dataManager.get(VEX_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(VEX_FLAGS, (byte)(i & 255));
    }

    public boolean isCharging() {
        return this.getVexFlag(1);
    }

    public void setCharging(boolean charging) {
        this.setVexFlag(1, charging);
    }

    public void setOwner(PlayerEntity ownerIn) {
        this.owner = ownerIn;
    }

    public void setLimitedLife(int limitedLifeTicksIn) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VEX_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness() {
        return 1.0F;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
    }

    class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (FriendlyVexEntity.this.getAttackTarget() != null && !FriendlyVexEntity.this.getMoveHelper().isUpdating() && FriendlyVexEntity.this.rand.nextInt(7) == 0) {
                return FriendlyVexEntity.this.getDistanceSq(FriendlyVexEntity.this.getAttackTarget()) > 4.0D;
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return FriendlyVexEntity.this.getMoveHelper().isUpdating() && FriendlyVexEntity.this.isCharging() && FriendlyVexEntity.this.getAttackTarget() != null && FriendlyVexEntity.this.getAttackTarget().isAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            LivingEntity livingentity = FriendlyVexEntity.this.getAttackTarget();
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            FriendlyVexEntity.this.moveController.setMoveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            FriendlyVexEntity.this.setCharging(true);
            FriendlyVexEntity.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            FriendlyVexEntity.this.setCharging(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = FriendlyVexEntity.this.getAttackTarget();
            if (FriendlyVexEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                FriendlyVexEntity.this.attackEntityAsMob(livingentity);
                FriendlyVexEntity.this.setCharging(false);
            } else {
                double d0 = FriendlyVexEntity.this.getDistanceSq(livingentity);
                if (d0 < 9.0D) {
                    Vector3d vector3d = livingentity.getEyePosition(1.0F);
                    FriendlyVexEntity.this.moveController.setMoveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                }
            }

        }
    }

    class OwnerHurtTargetGoal extends TargetGoal {
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtTargetGoal(FriendlyVexEntity friendlyVexEntity) {
            super(friendlyVexEntity, false);
            this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = FriendlyVexEntity.this.getOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getLastAttackedEntity();
                int i = livingentity.getLastAttackedEntityTime();
                return i != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT);
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.goalOwner.setAttackTarget(this.attacker);
            LivingEntity livingentity = FriendlyVexEntity.this.getOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getLastAttackedEntityTime();
            }

            super.startExecuting();
        }
    }

    class OwnerHurtByTargetGoal extends TargetGoal {
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtByTargetGoal(FriendlyVexEntity friendlyVexEntity) {
            super(friendlyVexEntity, false);
            this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = FriendlyVexEntity.this.getOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getRevengeTarget();
                int i = livingentity.getRevengeTimer();
                return i != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT);
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.goalOwner.setAttackTarget(this.attacker);
            LivingEntity livingentity = FriendlyVexEntity.this.getOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getRevengeTimer();
            }

            super.startExecuting();
        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(FriendlyVexEntity vex) {
            super(vex);
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.posX - FriendlyVexEntity.this.getPosX(), this.posY - FriendlyVexEntity.this.getPosY(), this.posZ - FriendlyVexEntity.this.getPosZ());
                double d0 = vector3d.length();
                if (d0 < FriendlyVexEntity.this.getBoundingBox().getAverageEdgeLength()) {
                    this.action = MovementController.Action.WAIT;
                    FriendlyVexEntity.this.setMotion(FriendlyVexEntity.this.getMotion().scale(0.5D));
                } else {
                    FriendlyVexEntity.this.setMotion(FriendlyVexEntity.this.getMotion().add(vector3d.scale(this.speed * 0.05D / d0)));
                    if (FriendlyVexEntity.this.getAttackTarget() == null) {
                        Vector3d vector3d1 = FriendlyVexEntity.this.getMotion();
                        FriendlyVexEntity.this.rotationYaw = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                        FriendlyVexEntity.this.renderYawOffset = FriendlyVexEntity.this.rotationYaw;
                    } else {
                        double d2 = FriendlyVexEntity.this.getAttackTarget().getPosX() - FriendlyVexEntity.this.getPosX();
                        double d1 = FriendlyVexEntity.this.getAttackTarget().getPosZ() - FriendlyVexEntity.this.getPosZ();
                        FriendlyVexEntity.this.rotationYaw = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                        FriendlyVexEntity.this.renderYawOffset = FriendlyVexEntity.this.rotationYaw;
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
            return !FriendlyVexEntity.this.getMoveHelper().isUpdating() && FriendlyVexEntity.this.rand.nextInt(7) == 0;
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
            BlockPos blockpos = FriendlyVexEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = FriendlyVexEntity.this.getPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(FriendlyVexEntity.this.rand.nextInt(15) - 7, FriendlyVexEntity.this.rand.nextInt(11) - 5, FriendlyVexEntity.this.rand.nextInt(15) - 7);
                if (FriendlyVexEntity.this.world.isAirBlock(blockpos1)) {
                    FriendlyVexEntity.this.moveController.setMoveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (FriendlyVexEntity.this.getAttackTarget() == null) {
                        FriendlyVexEntity.this.getLookController().setLookPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }
}
