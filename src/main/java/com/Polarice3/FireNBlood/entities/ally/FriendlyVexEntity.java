package com.Polarice3.FireNBlood.entities.ally;

import com.Polarice3.FireNBlood.entities.neutral.MinionEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
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
import net.minecraft.pathfinding.*;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class FriendlyVexEntity extends MinionEntity {
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(FriendlyVexEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private LivingEntity owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;

    public FriendlyVexEntity(EntityType<? extends FriendlyVexEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.experienceValue = 0;
    }

    public void tick() {
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }
        super.tick();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 0.5D, 6.0f, 3.0f, true));
        this.goalSelector.addGoal(4, new ChargeAttackGoal());
        this.goalSelector.addGoal(8, new MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (entity) ->
                entity instanceof IMob
                        && !(entity instanceof CreeperEntity)));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 14.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    public Team getTeam() {
        if (this.getOwnerId() != null) {
            LivingEntity livingentity = this.getTrueOwner();
            if (livingentity != null) {
                return livingentity.getTeam();
            }
        }

        return super.getTeam();
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (this.getOwnerId() != null) {
            LivingEntity livingentity = this.getTrueOwner();
            if (entityIn == livingentity) {
                return true;
            }

            if (livingentity != null) {
                return livingentity.isOnSameTeam(entityIn);
            }
        }
        if (entityIn instanceof FriendlyVexEntity && ((FriendlyVexEntity) entityIn).getTrueOwner() == this.getTrueOwner()){
            return true;
        }
        if (entityIn instanceof SummonedEntity && ((SummonedEntity) entityIn).getTrueOwner() == this.getTrueOwner()){
            return true;
        }
        if (entityIn instanceof FriendlyTankEntity && ((FriendlyTankEntity) entityIn).getOwner() == this.getTrueOwner()){
            return true;
        }
        return super.isOnSameTeam(entityIn);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.empty());
    }

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

    public LivingEntity getTrueOwner() {
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
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
    }

    class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean shouldExecute() {
            if (FriendlyVexEntity.this.getAttackTarget() != null
                    && !FriendlyVexEntity.this.getAttackTarget().isOnSameTeam(FriendlyVexEntity.this)
                    && !FriendlyVexEntity.this.getMoveHelper().isUpdating()
                    && FriendlyVexEntity.this.rand.nextInt(7) == 0) {
                return FriendlyVexEntity.this.getDistanceSq(FriendlyVexEntity.this.getAttackTarget()) > 4.0D;
            } else {
                return false;
            }
        }

        public boolean shouldContinueExecuting() {
            return FriendlyVexEntity.this.getMoveHelper().isUpdating()
                    && FriendlyVexEntity.this.isCharging()
                    && FriendlyVexEntity.this.getAttackTarget() != null
                    && FriendlyVexEntity.this.getAttackTarget().isAlive();
        }

        public void startExecuting() {
            LivingEntity livingentity = FriendlyVexEntity.this.getAttackTarget();
            assert livingentity != null;
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
            assert livingentity != null;
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
            this.setMutexFlags(EnumSet.of(Flag.TARGET));
        }

        public boolean shouldExecute() {
            LivingEntity livingentity = FriendlyVexEntity.this.getTrueOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getLastAttackedEntity();
                int i = livingentity.getLastAttackedEntityTime();
                return i != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT);
            }
        }

        public void startExecuting() {
            this.goalOwner.setAttackTarget(this.attacker);
            LivingEntity livingentity = FriendlyVexEntity.this.getTrueOwner();
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
            this.setMutexFlags(EnumSet.of(Flag.TARGET));
        }

        public boolean shouldExecute() {
            LivingEntity livingentity = FriendlyVexEntity.this.getTrueOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getRevengeTarget();
                int i = livingentity.getRevengeTimer();
                return i != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT);
            }
        }

        public void startExecuting() {
            this.goalOwner.setAttackTarget(this.attacker);
            LivingEntity livingentity = FriendlyVexEntity.this.getTrueOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getRevengeTimer();
            }

            super.startExecuting();
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean shouldExecute() {
            return !FriendlyVexEntity.this.getMoveHelper().isUpdating()
                    && FriendlyVexEntity.this.rand.nextInt(7) == 0
                    && !FriendlyVexEntity.this.isCharging();
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = FriendlyVexEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = FriendlyVexEntity.this.getPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(FriendlyVexEntity.this.rand.nextInt(8) - 4, FriendlyVexEntity.this.rand.nextInt(6) - 2, FriendlyVexEntity.this.rand.nextInt(8) - 4);
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
    public static class FollowOwnerGoal extends Goal {
        private final FriendlyVexEntity summonedEntity;
        private LivingEntity owner;
        private final IWorldReader world;
        private final double followSpeed;
        private final PathNavigator navigator;
        private int timeToRecalcPath;
        private final float maxDist;
        private final float minDist;
        private float oldWaterCost;
        private final boolean teleportToLeaves;

        public FollowOwnerGoal(FriendlyVexEntity summonedEntity, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
            this.summonedEntity = summonedEntity;
            this.world = summonedEntity.world;
            this.followSpeed = speed;
            this.navigator = summonedEntity.getNavigator();
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.teleportToLeaves = teleportToLeaves;
            this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            if (!(summonedEntity.getNavigator() instanceof GroundPathNavigator) && !(summonedEntity.getNavigator() instanceof FlyingPathNavigator)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        public boolean shouldExecute() {
            LivingEntity livingentity = this.summonedEntity.getTrueOwner();
            if (livingentity == null) {
                return false;
            } else if (livingentity.isSpectator()) {
                return false;
            } else if (this.summonedEntity.getDistanceSq(livingentity) < (double)(this.minDist * this.minDist)) {
                return false;
            } else if (this.summonedEntity.getAttackTarget() != null) {
                return false;
            } else if (this.summonedEntity.isCharging()) {
                return false;
            } else {
                this.owner = livingentity;
                return true;
            }
        }

        public boolean shouldContinueExecuting() {
            if (this.summonedEntity.getAttackTarget() != null) {
                return false;
            } else if (this.summonedEntity.isCharging()) {
                return false;
            } else if (this.navigator.noPath()){
                return false;
            } else {
                return !(this.summonedEntity.getDistanceSq(this.owner) <= (double)(this.maxDist * this.maxDist));
            }
        }

        public void startExecuting() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.summonedEntity.getPathPriority(PathNodeType.WATER);
            this.summonedEntity.setPathPriority(PathNodeType.WATER, 0.0F);
        }

        public void resetTask() {
            this.navigator.clearPath();
            this.summonedEntity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
        }

        public void tick() {
            this.summonedEntity.getLookController().setLookPositionWithEntity(this.owner, 10.0F, (float)this.summonedEntity.getVerticalFaceSpeed());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (this.summonedEntity.getDistance(this.owner) > 8.0D) {
                    double x = MathHelper.floor(this.owner.getPosX()) - 2;
                    double y = MathHelper.floor(this.owner.getBoundingBox().minY);
                    double z = MathHelper.floor(this.owner.getPosZ()) - 2;
                    for(int l = 0; l <= 4; ++l) {
                        for(int i1 = 0; i1 <= 4; ++i1) {
                            if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.ValidPosition(new BlockPos(x + l, y + 2, z + i1))){
                                float a = (float) ((x + l) + 0.5F);
                                float b = (float) ((z + i1) + 0.5F);
                                this.summonedEntity.getMoveHelper().setMoveTo(a, y, b, this.followSpeed);
                                this.navigator.clearPath();
                            }
                        }
                    }
                }
                if (this.summonedEntity.getDistanceSq(this.owner) > 144.0){
                    this.tryToTeleportNearEntity();
                }
            }
        }

        private void tryToTeleportNearEntity() {
            BlockPos blockpos = this.owner.getPosition();

            for(int i = 0; i < 10; ++i) {
                int j = this.getRandomNumber(-3, 3);
                int k = this.getRandomNumber(-1, 1);
                int l = this.getRandomNumber(-3, 3);
                boolean flag = this.tryToTeleportToLocation(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
                if (flag) {
                    return;
                }
            }

        }

        private boolean tryToTeleportToLocation(int x, int y, int z) {
            if (Math.abs((double)x - this.owner.getPosX()) < 2.0D && Math.abs((double)z - this.owner.getPosZ()) < 2.0D) {
                return false;
            } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
                return false;
            } else {
                this.summonedEntity.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, this.summonedEntity.rotationYaw, this.summonedEntity.rotationPitch);
                this.navigator.clearPath();
                return true;
            }
        }

        private boolean isTeleportFriendlyBlock(BlockPos pos) {
            PathNodeType pathnodetype = WalkNodeProcessor.getFloorNodeType(this.world, pos.toMutable());
            if (pathnodetype != PathNodeType.WALKABLE) {
                return false;
            } else {
                BlockState blockstate = this.world.getBlockState(pos.down());
                if (!this.teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos blockpos = pos.subtract(this.summonedEntity.getPosition());
                    return this.world.hasNoCollisions(this.summonedEntity, this.summonedEntity.getBoundingBox().offset(blockpos));
                }
            }
        }

        protected boolean ValidPosition(BlockPos pos) {
            BlockState blockstate = this.world.getBlockState(pos);
            return (blockstate.isValidPosition(this.world, pos) && this.world.isAirBlock(pos.up()) && this.world.isAirBlock(pos.up(2)));
        }

        private int getRandomNumber(int min, int max) {
            return this.summonedEntity.getRNG().nextInt(max - min + 1) + min;
        }
    }
}
