package com.Polarice3.FireNBlood.entities.neutral.protectors;

import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.EnumSet;

public class MirageEntity extends AbstractProtectorEntity {
    private MobEntity owner;
    private boolean limitedLifespan;
    private int limitedLifeTicks;

    public MirageEntity(EntityType<? extends AbstractProtectorEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new MirageEntity.CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractProtectorEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractVillagerEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractTaillessEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractRaiderEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_234199_0_) -> {
            return p_234199_0_ instanceof IMob && !(p_234199_0_ instanceof CreeperEntity);
        }));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    public boolean hurt(DamageSource source, float amount) {
        if (source != DamageSource.STARVE){
            return false;
        } else {
            return super.hurt(source, amount);
        }
    }


    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }

    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.limitedLifespan) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }

    }

    public void tick() {
        super.tick();
        --this.limitedLifeTicks;
        if (this.limitedLifespan && this.limitedLifeTicks <= 0) {
            this.hurt(DamageSource.STARVE, 200.0F);
        }
    }

    public void setLimitedLife(int limitedLifeTicksIn) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_GROWL;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.WOLF_GROWL;
    }

    public MobEntity getOwner() {
        return this.owner;
    }

    public void setOwner(MobEntity ownerIn) {
        this.owner = ownerIn;
    }

    public float getBrightness() {
        return 1.0F;
    }

    public static class FollowOwnerGoal extends Goal {
        private final MirageEntity mirage;
        private MobEntity owner;
        private final IWorldReader level;
        private final double followSpeed;
        private final PathNavigator navigation;
        private int timeToRecalcPath;
        private final float maxDist;
        private final float minDist;
        private float oldWaterCost;
        private final boolean teleportToLeaves;

        public FollowOwnerGoal(MirageEntity mirage, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
            this.mirage = mirage;
            this.level = mirage.level;
            this.followSpeed = speed;
            this.navigation = mirage.getNavigation();
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.teleportToLeaves = teleportToLeaves;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            if (!(mirage.getNavigation() instanceof GroundPathNavigator) && !(mirage.getNavigation() instanceof FlyingPathNavigator)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            MobEntity summoner = this.mirage.getOwner();
            if (summoner == null) {
                return false;
            } else if (summoner.isSpectator()) {
                return false;
            } else if (this.mirage.distanceToSqr(summoner) < (double)(this.minDist * this.minDist)) {
                return false;
            } else if (!summoner.isAlive()){
                return false;
            } else {
                this.owner = summoner;
                return true;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            if (this.navigation.isDone()) {
                return false;
            } else {
                return !(this.mirage.distanceToSqr(this.owner) <= (double)(this.maxDist * this.maxDist));
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.mirage.getPathfindingMalus(PathNodeType.WATER);
            this.mirage.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.mirage.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            this.mirage.getLookControl().setLookAt(this.owner, 10.0F, (float)this.mirage.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.mirage.isLeashed() && !this.mirage.isPassenger()) {
                    if (this.mirage.distanceToSqr(this.owner) >= 144.0D) {
                        this.tryToTeleportNearEntity();
                    } else {
                        this.navigation.moveTo(this.owner, this.followSpeed);
                    }

                }
            }
        }

        private void tryToTeleportNearEntity() {
            BlockPos blockpos = this.owner.blockPosition();

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
            if (Math.abs((double)x - this.owner.getX()) < 2.0D && Math.abs((double)z - this.owner.getZ()) < 2.0D) {
                return false;
            } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
                return false;
            } else {
                this.mirage.moveTo((double)x + 0.5D, (double)y, (double)z + 0.5D, this.mirage.yRot, this.mirage.xRot);
                this.navigation.stop();
                return true;
            }
        }

        private boolean isTeleportFriendlyBlock(BlockPos pos) {
            PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(this.level, pos.mutable());
            if (pathnodetype != PathNodeType.WALKABLE) {
                return false;
            } else {
                BlockState blockstate = this.level.getBlockState(pos.below());
                if (!this.teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos blockpos = pos.subtract(this.mirage.blockPosition());
                    return this.level.noCollision(this.mirage, this.mirage.getBoundingBox().move(blockpos));
                }
            }
        }

        private int getRandomNumber(int min, int max) {
            return this.mirage.getRandom().nextInt(max - min + 1) + min;
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
            return MirageEntity.this.owner != null && MirageEntity.this.owner.getTarget() != null && this.canAttack(MirageEntity.this.owner.getTarget(), this.field_220803_b);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            MirageEntity.this.setTarget(MirageEntity.this.owner.getTarget());
            super.start();
        }
    }
}
