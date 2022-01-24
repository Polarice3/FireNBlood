package com.Polarice3.FireNBlood.entities.ally;

import com.Polarice3.FireNBlood.entities.neutral.MinionEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class FriendlyScorchEntity extends MinionEntity {
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(FriendlyScorchEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private LivingEntity owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;

    public FriendlyScorchEntity(EntityType<? extends FriendlyScorchEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.experienceValue = 0;
    }

    public void tick() {
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }
        Vector3d vector3d = this.getMotion();
        double d0 = this.getPosX() + vector3d.x;
        double d1 = this.getPosY() + vector3d.y;
        double d2 = this.getPosZ() + vector3d.z;
        if (this.isCharging()){
            this.world.addParticle(ParticleTypes.FLAME, d0 + world.rand.nextDouble()/2, d1 + 0.5D, d2 + world.rand.nextDouble()/2, 0.0D, 0.0D, 0.0D);
        } else{
            this.world.addParticle(ParticleTypes.SMOKE, d0 + world.rand.nextDouble()/2, d1 + 0.5D, d2 + world.rand.nextDouble()/2, 0.0D, 0.0D, 0.0D);
        }
        super.tick();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new ChargeAttackGoal());
        this.goalSelector.addGoal(8, new MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (entity) ->
                entity instanceof IMob
                        && entity != this.owner
                        && Objects.equals(entity.getAttackingEntity(), this.getOwnerfromUUID())));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 14.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && entityIn instanceof LivingEntity) {
            entityIn.setFire(30);
        }
        return flag;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.isExplosion()){
            return false;
        } else {
            return super.attackEntityFrom(source, amount);
        }
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.empty());
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
        UUID uuid;
        if (compound.hasUniqueId("Owner")) {
            uuid = compound.getUniqueId("Owner");
        } else {
            String s = compound.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable ignored) {
            }
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
        if (this.getOwnerId() != null) {
            compound.putUniqueId("Owner", this.getOwnerId());
        }

    }

    public boolean isWaterSensitive() {
        return true;
    }

    public LivingEntity getTrueOwner() {
        return this.owner;
    }

    public LivingEntity getOwnerfromUUID() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.world.getPlayerByUuid(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getOwnerId() {
        return this.dataManager.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOriginIn) {
        this.boundOrigin = boundOriginIn;
    }

    public void setOwner(LivingEntity ownerIn) {
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

    public float getBrightness() {
        return 1.0F;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
    }

    class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean shouldExecute() {
            if (FriendlyScorchEntity.this.getAttackTarget() != null && FriendlyScorchEntity.this.getAttackTarget() != FriendlyScorchEntity.this.owner && !FriendlyScorchEntity.this.getMoveHelper().isUpdating() && FriendlyScorchEntity.this.rand.nextInt(7) == 0) {
                return FriendlyScorchEntity.this.getDistanceSq(FriendlyScorchEntity.this.getAttackTarget()) > 4.0D;
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return FriendlyScorchEntity.this.getMoveHelper().isUpdating() && FriendlyScorchEntity.this.isCharging() && FriendlyScorchEntity.this.getAttackTarget() != null && FriendlyScorchEntity.this.getAttackTarget().isAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            LivingEntity livingentity = FriendlyScorchEntity.this.getAttackTarget();
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            FriendlyScorchEntity.this.moveController.setMoveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            FriendlyScorchEntity.this.setCharging(true);
            FriendlyScorchEntity.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            FriendlyScorchEntity.this.setCharging(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = FriendlyScorchEntity.this.getAttackTarget();
            if (FriendlyScorchEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                FriendlyScorchEntity.this.attackEntityAsMob(livingentity);
                FriendlyScorchEntity.this.setCharging(false);
            } else {
                double d0 = FriendlyScorchEntity.this.getDistanceSq(livingentity);
                if (d0 < 9.0D) {
                    Vector3d vector3d = livingentity.getEyePosition(1.0F);
                    FriendlyScorchEntity.this.moveController.setMoveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                }
            }

        }
    }

    class OwnerHurtTargetGoal extends TargetGoal {
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtTargetGoal(FriendlyScorchEntity friendlyVexEntity) {
            super(friendlyVexEntity, false);
            this.setMutexFlags(EnumSet.of(Flag.TARGET));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = FriendlyScorchEntity.this.getTrueOwner();
            this.attacker = livingentity.getLastAttackedEntity();
            int i = livingentity.getLastAttackedEntityTime();
            return i != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.goalOwner.setAttackTarget(this.attacker);
            LivingEntity livingentity = FriendlyScorchEntity.this.getTrueOwner();
            this.timestamp = livingentity.getLastAttackedEntityTime();

            super.startExecuting();
        }
    }

    class OwnerHurtByTargetGoal extends TargetGoal {
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtByTargetGoal(FriendlyScorchEntity friendlyVexEntity) {
            super(friendlyVexEntity, false);
            this.setMutexFlags(EnumSet.of(Flag.TARGET));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = FriendlyScorchEntity.this.getTrueOwner();
            this.attacker = livingentity.getRevengeTarget();
            int i = livingentity.getRevengeTimer();
            return i != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.goalOwner.setAttackTarget(this.attacker);
            LivingEntity livingentity = FriendlyScorchEntity.this.getTrueOwner();
            this.timestamp = livingentity.getRevengeTimer();

            super.startExecuting();
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
            return !FriendlyScorchEntity.this.getMoveHelper().isUpdating() && FriendlyScorchEntity.this.rand.nextInt(7) == 0;
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
            BlockPos blockpos = FriendlyScorchEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = FriendlyScorchEntity.this.getPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(FriendlyScorchEntity.this.rand.nextInt(15) - 7, FriendlyScorchEntity.this.rand.nextInt(11) - 5, FriendlyScorchEntity.this.rand.nextInt(15) - 7);
                if (FriendlyScorchEntity.this.world.isAirBlock(blockpos1)) {
                    FriendlyScorchEntity.this.moveController.setMoveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (FriendlyScorchEntity.this.getAttackTarget() == null) {
                        FriendlyScorchEntity.this.getLookController().setLookPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }
}
