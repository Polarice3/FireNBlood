package com.Polarice3.FireNBlood.entities.hostile.tailless;

import com.Polarice3.FireNBlood.entities.hostile.cultists.AbstractCultistEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
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
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class TaillessHorrorEntity extends FlyingTaillessEntity {
    protected static final DataParameter<Byte> HORROR_FLAGS = EntityDataManager.defineId(TaillessHorrorEntity.class, DataSerializers.BYTE);
    private Vector3d orbitOffset = Vector3d.ZERO;
    private BlockPos orbitPosition = BlockPos.ZERO;
    private TaillessHorrorEntity.AttackPhase attackPhase = TaillessHorrorEntity.AttackPhase.CIRCLE;
    public int attackTimer;

    public TaillessHorrorEntity(EntityType<? extends FlyingTaillessEntity> type, World worldIn) {
        super(type, worldIn);
        this.setrandom(10);
        this.moveControl = new TaillessHorrorEntity.MoveHelperController(this);
        this.lookControl = new TaillessHorrorEntity.LookHelperController(this);
    }

    protected BodyController createBodyControl() {
        return new TaillessHorrorEntity.BodyHelperController(this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TaillessHorrorEntity.PickAttackGoal());
        this.goalSelector.addGoal(2, new TaillessHorrorEntity.AttackGoal());
        this.goalSelector.addGoal(3, new TaillessHorrorEntity.OrbitPointGoal());
        this.targetSelector.addGoal(1, new TaillessHorrorEntity.AttackPlayerGoal());
        this.targetSelector.addGoal(1, new TaillessHorrorEntity.AttackIllagerGoal());
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractPiglinEntity.class)).setAlertOthers());
    }

    public AbstractTaillessEntity.ArmPose getArmPose() {
        if(TaillessHorrorEntity.this.attackPhase == TaillessHorrorEntity.AttackPhase.ATTACK) {
            return AbstractTaillessEntity.ArmPose.ATTACKING;
        } else {
            return AbstractTaillessEntity.ArmPose.NEUTRAL;
        }
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D);
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected int getExperienceReward(PlayerEntity player){
        return 10 + this.level.random.nextInt(10);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.POLAR_BEAR_HURT;
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            float f = MathHelper.cos((float)(this.getId() * 3 + this.tickCount) * 0.13F + (float)Math.PI);
            float f1 = MathHelper.cos((float)(this.getId() * 3 + this.tickCount + 1) * 0.13F + (float)Math.PI);
            if (f > 0.0F && f1 <= 0.0F) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_FLAP, this.getSoundSource(), 0.95F + this.random.nextFloat() * 0.05F, 0.95F + this.random.nextFloat() * 0.05F, false);
            }
        }

    }

    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.orbitPosition = this.blockPosition().above(5);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("AX")) {
            this.orbitPosition = new BlockPos(compound.getInt("AX"), compound.getInt("AY"), compound.getInt("AZ"));
        }
        this.attackTimer = compound.getInt("Attacking");
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HORROR_FLAGS, (byte)0);
    }

    private boolean getHorrorFlag(int mask) {
        int i = this.entityData.get(HORROR_FLAGS);
        return (i & mask) != 0;
    }

    private void setHorrorFlag(int mask, boolean value) {
        int i = this.entityData.get(HORROR_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(HORROR_FLAGS, (byte)(i & 255));
    }

    public boolean isRoaring(){
        return this.getHorrorFlag(1);
    }

    public void setRoaring(boolean roaring){
        this.setHorrorFlag(1,roaring);
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AX", this.orbitPosition.getX());
        compound.putInt("AY", this.orbitPosition.getY());
        compound.putInt("AZ", this.orbitPosition.getZ());
        compound.putInt("Attacking", this.attackTimer);
    }

    public boolean canAttack(EntityType<?> typeIn) {
        return true;
    }

    public int Attacking(){
        return this.attackTimer;
    }

    static enum AttackPhase {
        CIRCLE,
        ATTACK;
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof AbstractTaillessEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof AbstractCultistEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof AbstractPiglinEntity){
            return this.isAlliedTo(entityIn);
        }  else {
            return false;
        }
    }

    class AttackPlayerGoal extends Goal {
        private final EntityPredicate field_220842_b = (new EntityPredicate()).range(64.0D);
        private int tickDelay = 20;

        private AttackPlayerGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (this.tickDelay > 0) {
                --this.tickDelay;
                return false;
            } else {
                this.tickDelay = 60;
                List<PlayerEntity> list = TaillessHorrorEntity.this.level.getNearbyPlayers(this.field_220842_b, TaillessHorrorEntity.this, TaillessHorrorEntity.this.getBoundingBox().inflate(16.0D, 64.0D, 16.0D));
                if (!list.isEmpty()) {
                    list.sort(Comparator.<Entity, Double>comparing(Entity::getY).reversed());

                    for(PlayerEntity playerentity : list) {
                        if (TaillessHorrorEntity.this.canAttack(playerentity, EntityPredicate.DEFAULT)) {
                            TaillessHorrorEntity.this.setTarget(playerentity);
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            LivingEntity livingentity = TaillessHorrorEntity.this.getTarget();
            return livingentity != null && TaillessHorrorEntity.this.canAttack(livingentity, EntityPredicate.DEFAULT);
        }
    }

    class AttackIllagerGoal extends Goal {
        private final EntityPredicate field_220842_b = (new EntityPredicate()).range(64.0D);
        private int tickDelay = 20;

        private AttackIllagerGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (this.tickDelay > 0) {
                --this.tickDelay;
                return false;
            } else {
                this.tickDelay = 60;
                List<AbstractIllagerEntity> list = TaillessHorrorEntity.this.level.getNearbyEntities(AbstractIllagerEntity.class, field_220842_b, TaillessHorrorEntity.this, TaillessHorrorEntity.this.getBoundingBox().inflate(16.0D, 64.0D, 16.0D));
                if (!list.isEmpty()) {
                    list.sort(Comparator.<Entity, Double>comparing(Entity::getY).reversed());

                    for(AbstractIllagerEntity abstractIllagerEntity : list) {
                        if (TaillessHorrorEntity.this.canAttack(abstractIllagerEntity, EntityPredicate.DEFAULT)) {
                            TaillessHorrorEntity.this.setTarget(abstractIllagerEntity);
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            LivingEntity livingentity = TaillessHorrorEntity.this.getTarget();
            return livingentity != null && TaillessHorrorEntity.this.canAttack(livingentity, EntityPredicate.DEFAULT);
        }
    }

    class BodyHelperController extends BodyController {
        public BodyHelperController(MobEntity mob) {
            super(mob);
        }

        /**
         * Update the Head and Body rendenring angles
         */
        public void updateRenderAngles() {
            TaillessHorrorEntity.this.yHeadRot = TaillessHorrorEntity.this.yBodyRot;
            TaillessHorrorEntity.this.yBodyRot = TaillessHorrorEntity.this.yRot;
        }
    }

    class LookHelperController extends LookController {
        public LookHelperController(MobEntity entityIn) {
            super(entityIn);
        }

        /**
         * Updates look
         */
        public void tick() {
        }
    }

    abstract class MoveGoal extends Goal {
        public MoveGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        protected boolean func_203146_f() {
            return TaillessHorrorEntity.this.orbitOffset.distanceToSqr(TaillessHorrorEntity.this.getX(), TaillessHorrorEntity.this.getY(), TaillessHorrorEntity.this.getZ()) < 4.0D;
        }
    }

    class MoveHelperController extends MovementController {
        private float speedFactor = 0.1F;

        public MoveHelperController(MobEntity entityIn) {
            super(entityIn);
        }

        public void tick() {
            if (TaillessHorrorEntity.this.horizontalCollision) {
                TaillessHorrorEntity.this.yRot += 180.0F;
                this.speedFactor = 0.1F;
            }

            float f = (float)(TaillessHorrorEntity.this.orbitOffset.x - TaillessHorrorEntity.this.getX());
            float f1 = (float)(TaillessHorrorEntity.this.orbitOffset.y - TaillessHorrorEntity.this.getY());
            float f2 = (float)(TaillessHorrorEntity.this.orbitOffset.z - TaillessHorrorEntity.this.getZ());
            double d0 = (double)MathHelper.sqrt(f * f + f2 * f2);
            double d1 = 1.0D - (double)MathHelper.abs(f1 * 0.7F) / d0;
            f = (float)((double)f * d1);
            f2 = (float)((double)f2 * d1);
            d0 = (double)MathHelper.sqrt(f * f + f2 * f2);
            double d2 = (double)MathHelper.sqrt(f * f + f2 * f2 + f1 * f1);
            float f3 = TaillessHorrorEntity.this.yRot;
            float f4 = (float)MathHelper.atan2((double)f2, (double)f);
            float f5 = MathHelper.wrapDegrees(TaillessHorrorEntity.this.yRot + 90.0F);
            float f6 = MathHelper.wrapDegrees(f4 * (180F / (float)Math.PI));
            TaillessHorrorEntity.this.yRot = MathHelper.approachDegrees(f5, f6, 4.0F) - 90.0F;
            TaillessHorrorEntity.this.yBodyRot = TaillessHorrorEntity.this.yRot;
            if (MathHelper.degreesDifferenceAbs(f3, TaillessHorrorEntity.this.yRot) < 3.0F) {
                this.speedFactor = MathHelper.approach(this.speedFactor, 1.8F, 0.005F * (1.8F / this.speedFactor));
            } else {
                this.speedFactor = MathHelper.approach(this.speedFactor, 0.2F, 0.025F);
            }

            float f7 = (float)(-(MathHelper.atan2((double)(-f1), d0) * (double)(180F / (float)Math.PI)));
            TaillessHorrorEntity.this.xRot = f7;
            float f8 = TaillessHorrorEntity.this.yRot + 90.0F;
            double d3 = (double)(this.speedFactor * MathHelper.cos(f8 * ((float)Math.PI / 180F))) * Math.abs((double)f / d2);
            double d4 = (double)(this.speedFactor * MathHelper.sin(f8 * ((float)Math.PI / 180F))) * Math.abs((double)f2 / d2);
            double d5 = (double)(this.speedFactor * MathHelper.sin(f7 * ((float)Math.PI / 180F))) * Math.abs((double)f1 / d2);
            Vector3d vector3d = TaillessHorrorEntity.this.getDeltaMovement();
            TaillessHorrorEntity.this.setDeltaMovement(vector3d.add((new Vector3d(d3, d5, d4)).subtract(vector3d).scale(0.2D)));
        }
    }

    class OrbitPointGoal extends TaillessHorrorEntity.MoveGoal {
        private float field_203150_c;
        private float field_203151_d;
        private float field_203152_e;
        private float field_203153_f;

        private OrbitPointGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return TaillessHorrorEntity.this.getTarget() == null || TaillessHorrorEntity.this.attackPhase == TaillessHorrorEntity.AttackPhase.CIRCLE;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.field_203151_d = 5.0F + TaillessHorrorEntity.this.random.nextFloat() * 10.0F;
            this.field_203152_e = -4.0F + TaillessHorrorEntity.this.random.nextFloat() * 9.0F;
            this.field_203153_f = TaillessHorrorEntity.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.func_203148_i();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (TaillessHorrorEntity.this.random.nextInt(350) == 0) {
                this.field_203152_e = -4.0F + TaillessHorrorEntity.this.random.nextFloat() * 9.0F;
            }

            if (TaillessHorrorEntity.this.random.nextInt(250) == 0) {
                ++this.field_203151_d;
                if (this.field_203151_d > 15.0F) {
                    this.field_203151_d = 5.0F;
                    this.field_203153_f = -this.field_203153_f;
                }
            }

            if (TaillessHorrorEntity.this.random.nextInt(450) == 0) {
                this.field_203150_c = TaillessHorrorEntity.this.random.nextFloat() * 2.0F * (float)Math.PI;
                this.func_203148_i();
            }

            if (this.func_203146_f()) {
                this.func_203148_i();
            }

            if (TaillessHorrorEntity.this.orbitOffset.y < TaillessHorrorEntity.this.getY() && !TaillessHorrorEntity.this.level.isEmptyBlock(TaillessHorrorEntity.this.blockPosition().below(1))) {
                this.field_203152_e = Math.max(1.0F, this.field_203152_e);
                this.func_203148_i();
            }

            if (TaillessHorrorEntity.this.orbitOffset.y > TaillessHorrorEntity.this.getY() && !TaillessHorrorEntity.this.level.isEmptyBlock(TaillessHorrorEntity.this.blockPosition().above(1))) {
                this.field_203152_e = Math.min(-1.0F, this.field_203152_e);
                this.func_203148_i();
            }

        }

        private void func_203148_i() {
            if (BlockPos.ZERO.equals(TaillessHorrorEntity.this.orbitPosition)) {
                TaillessHorrorEntity.this.orbitPosition = TaillessHorrorEntity.this.blockPosition();
            }

            this.field_203150_c += this.field_203153_f * 15.0F * ((float)Math.PI / 180F);
            TaillessHorrorEntity.this.orbitOffset = Vector3d.atLowerCornerOf(TaillessHorrorEntity.this.orbitPosition).add((double)(this.field_203151_d * MathHelper.cos(this.field_203150_c)), (double)(-4.0F + this.field_203152_e), (double)(this.field_203151_d * MathHelper.sin(this.field_203150_c)));
        }
    }
    
    class PickAttackGoal extends Goal {
        private int tickDelay;

        private PickAttackGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = TaillessHorrorEntity.this.getTarget();
            return livingentity != null && TaillessHorrorEntity.this.canAttack(TaillessHorrorEntity.this.getTarget(), EntityPredicate.DEFAULT);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.tickDelay = 10;
            TaillessHorrorEntity.this.attackPhase = TaillessHorrorEntity.AttackPhase.CIRCLE;
            this.func_203143_f();
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            TaillessHorrorEntity.this.orbitPosition = TaillessHorrorEntity.this.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, TaillessHorrorEntity.this.orbitPosition).above(10 + TaillessHorrorEntity.this.random.nextInt(20));
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (TaillessHorrorEntity.this.attackPhase == TaillessHorrorEntity.AttackPhase.CIRCLE) {
                --this.tickDelay;
                if (this.tickDelay <= 0) {
                    TaillessHorrorEntity.this.attackPhase = TaillessHorrorEntity.AttackPhase.ATTACK;
                    this.func_203143_f();
                    this.tickDelay = (8 + TaillessHorrorEntity.this.random.nextInt(4)) * 20;
                    TaillessHorrorEntity.this.playSound(SoundEvents.POLAR_BEAR_WARNING, 10.0F, 0.95F + TaillessHorrorEntity.this.random.nextFloat() * 0.1F);
                }
            }

        }

        private void func_203143_f() {
            TaillessHorrorEntity.this.orbitPosition = TaillessHorrorEntity.this.getTarget().blockPosition().above(20 + TaillessHorrorEntity.this.random.nextInt(20));
            if (TaillessHorrorEntity.this.orbitPosition.getY() < TaillessHorrorEntity.this.level.getSeaLevel()) {
                TaillessHorrorEntity.this.orbitPosition = new BlockPos(TaillessHorrorEntity.this.orbitPosition.getX(), TaillessHorrorEntity.this.level.getSeaLevel() + 1, TaillessHorrorEntity.this.orbitPosition.getZ());
            }

        }
    }

    class AttackGoal extends TaillessHorrorEntity.MoveGoal {
        public int hurt = 0;

        private AttackGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return TaillessHorrorEntity.this.getTarget() != null && TaillessHorrorEntity.this.attackPhase == TaillessHorrorEntity.AttackPhase.ATTACK;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            LivingEntity livingentity = TaillessHorrorEntity.this.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            }  else if (!(livingentity instanceof PlayerEntity) || !((PlayerEntity)livingentity).isSpectator() && !((PlayerEntity)livingentity).isCreative()) {
                if (!this.canUse()) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            TaillessHorrorEntity.this.setTarget((LivingEntity)null);
            TaillessHorrorEntity.this.attackPhase = TaillessHorrorEntity.AttackPhase.CIRCLE;
            TaillessHorrorEntity.this.setRoaring(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            TaillessHorrorEntity.this.setRoaring(true);
            LivingEntity livingentity = TaillessHorrorEntity.this.getTarget();
            assert livingentity != null;
            TaillessHorrorEntity.this.orbitOffset = new Vector3d(livingentity.getX(), livingentity.getY(2.5D), livingentity.getZ());
            ++TaillessHorrorEntity.this.attackTimer;
            if (TaillessHorrorEntity.this.attackTimer >= 40){
                this.RangedAttack();
                TaillessHorrorEntity.this.attackTimer = -10;
            }
            if (TaillessHorrorEntity.this.hurtTime > 0 && this.hurt < 3){
                this.hurt = this.hurt + 1;
                TaillessHorrorEntity.this.hurtTime = 0;
            }
            if (TaillessHorrorEntity.this.horizontalCollision || this.hurt >= 3) {
                TaillessHorrorEntity.this.attackPhase = AttackPhase.CIRCLE;
                this.hurt = 0;
                TaillessHorrorEntity.this.attackTimer = 0;
                TaillessHorrorEntity.this.setRoaring(false);
            }

        }

        public void RangedAttack() {
            LivingEntity livingentity = TaillessHorrorEntity.this.getTarget();
/*            Vector3d vector3d = TaillessHorrorEntity.this.getViewVector( 1.0F);
            ArrowEntity arrowentity = new ArrowEntity(world, TaillessHorrorEntity.this.getX() + vector3d.x, TaillessHorrorEntity.this.getY(0.55D), TaillessHorrorEntity.this.getZ() + vector3d.z);*/
            ArrowEntity arrowentity = new ArrowEntity(level, TaillessHorrorEntity.this);
            assert livingentity != null;
            double d0 = livingentity.getX() - TaillessHorrorEntity.this.getX();
            double d1 = livingentity.getY(0.3333333333333333D) - arrowentity.getY();
            double d2 = livingentity.getZ() - TaillessHorrorEntity.this.getZ();
            double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
            arrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 2.4F, (float)(14 - level.getDifficulty().getId() * 4));
            playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
            level.addFreshEntity(arrowentity);
        }
    }

}
