package com.Polarice3.FireNBlood.entities.hostile;

import com.Polarice3.FireNBlood.entities.masters.TaillessAnathemaEntity;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
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
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
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
    protected static final DataParameter<Byte> HORROR_FLAGS = EntityDataManager.createKey(TaillessHorrorEntity.class, DataSerializers.BYTE);
    private Vector3d orbitOffset = Vector3d.ZERO;
    private BlockPos orbitPosition = BlockPos.ZERO;
    private TaillessHorrorEntity.AttackPhase attackPhase = TaillessHorrorEntity.AttackPhase.CIRCLE;
    public int attackTimer;

    public TaillessHorrorEntity(EntityType<? extends FlyingTaillessEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new TaillessHorrorEntity.MoveHelperController(this);
        this.lookController = new TaillessHorrorEntity.LookHelperController(this);
    }

    protected BodyController createBodyController() {
        return new TaillessHorrorEntity.BodyHelperController(this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TaillessHorrorEntity.PickAttackGoal());
        this.goalSelector.addGoal(2, new TaillessHorrorEntity.AttackGoal());
        this.goalSelector.addGoal(3, new TaillessHorrorEntity.OrbitPointGoal());
        this.targetSelector.addGoal(1, new TaillessHorrorEntity.AttackPlayerGoal());
        this.targetSelector.addGoal(1, new TaillessHorrorEntity.AttackIllagerGoal());
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractPiglinEntity.class)).setCallsForHelp());
    }

    public AbstractTaillessEntity.ArmPose getArmPose() {
        if(TaillessHorrorEntity.this.attackPhase == TaillessHorrorEntity.AttackPhase.ATTACK) {
            return AbstractTaillessEntity.ArmPose.ATTACKING;
        } else {
            return AbstractTaillessEntity.ArmPose.NEUTRAL;
        }
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 30.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 8.0D);
    }

    protected void updateAITasks() {
        super.updateAITasks();
    }

    protected boolean isDespawnPeaceful() {
        return true;
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player){
        return 10 + this.world.rand.nextInt(10);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_POLAR_BEAR_HURT;
    }

    public void tick() {
        super.tick();
        if (this.world.isRemote) {
            float f = MathHelper.cos((float)(this.getEntityId() * 3 + this.ticksExisted) * 0.13F + (float)Math.PI);
            float f1 = MathHelper.cos((float)(this.getEntityId() * 3 + this.ticksExisted + 1) * 0.13F + (float)Math.PI);
            if (f > 0.0F && f1 <= 0.0F) {
                this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_PHANTOM_FLAP, this.getSoundCategory(), 0.95F + this.rand.nextFloat() * 0.05F, 0.95F + this.rand.nextFloat() * 0.05F, false);
            }
        }

    }

    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.orbitPosition = this.getPosition().up(5);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("AX")) {
            this.orbitPosition = new BlockPos(compound.getInt("AX"), compound.getInt("AY"), compound.getInt("AZ"));
        }
        this.attackTimer = compound.getInt("Attacking");
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(HORROR_FLAGS, (byte)0);
    }

    private boolean getHorrorFlag(int mask) {
        int i = this.dataManager.get(HORROR_FLAGS);
        return (i & mask) != 0;
    }

    private void setHorrorFlag(int mask, boolean value) {
        int i = this.dataManager.get(HORROR_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(HORROR_FLAGS, (byte)(i & 255));
    }

    public boolean isRoaring(){
        return this.getHorrorFlag(1);
    }

    public void setRoaring(boolean roaring){
        this.setHorrorFlag(1,roaring);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
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

    public boolean isOnSameTeam(Entity entityIn) {
        if (super.isOnSameTeam(entityIn)) {
            return true;
        } else if (entityIn instanceof AbstractTaillessEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof AbstractPiglinEntity){
            return this.isOnSameTeam(entityIn);
        }  else {
            return false;
        }
    }

    public void onDeath(DamageSource cause) {
        Entity entity = cause.getTrueSource();
        PlayerEntity playerentity;
        if (entity instanceof PlayerEntity) {
            playerentity = (PlayerEntity)entity;
            int random = this.world.rand.nextInt(10);
            if (random == 0) {
                EffectInstance effectinstance1 = playerentity.getActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                if (effectinstance1 == null) {
                    EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                    playerentity.addPotionEffect(effectinstance);
                } else {
                    int amp = effectinstance1.getAmplifier();
                    int i = amp + 1;
                    i = MathHelper.clamp(i, 0, 5);
                    playerentity.removeActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                    EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                    playerentity.addPotionEffect(effectinstance);
                }
            }
        } else if (entity instanceof WolfEntity) {
            WolfEntity wolfentity = (WolfEntity)entity;
            LivingEntity livingentity = wolfentity.getOwner();
            if (wolfentity.isTamed() && livingentity instanceof PlayerEntity) {
                playerentity = (PlayerEntity)livingentity;
                int random = this.world.rand.nextInt(10);
                if (random == 0) {
                    EffectInstance effectinstance1 = playerentity.getActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                    if (effectinstance1 == null) {
                        EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                        playerentity.addPotionEffect(effectinstance);
                    } else {
                        int amp = effectinstance1.getAmplifier();
                        int i = amp + 1;
                        i = MathHelper.clamp(i, 0, 5);
                        playerentity.removeActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                        EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                        playerentity.addPotionEffect(effectinstance);
                    }
                }
            }
        }
    }

    class AttackPlayerGoal extends Goal {
        private final EntityPredicate field_220842_b = (new EntityPredicate()).setDistance(64.0D);
        private int tickDelay = 20;

        private AttackPlayerGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (this.tickDelay > 0) {
                --this.tickDelay;
                return false;
            } else {
                this.tickDelay = 60;
                List<PlayerEntity> list = TaillessHorrorEntity.this.world.getTargettablePlayersWithinAABB(this.field_220842_b, TaillessHorrorEntity.this, TaillessHorrorEntity.this.getBoundingBox().grow(16.0D, 64.0D, 16.0D));
                if (!list.isEmpty()) {
                    list.sort(Comparator.<Entity, Double>comparing(Entity::getPosY).reversed());

                    for(PlayerEntity playerentity : list) {
                        if (TaillessHorrorEntity.this.canAttack(playerentity, EntityPredicate.DEFAULT)) {
                            TaillessHorrorEntity.this.setAttackTarget(playerentity);
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
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = TaillessHorrorEntity.this.getAttackTarget();
            return livingentity != null && TaillessHorrorEntity.this.canAttack(livingentity, EntityPredicate.DEFAULT);
        }
    }

    class AttackIllagerGoal extends Goal {
        private final EntityPredicate field_220842_b = (new EntityPredicate()).setDistance(64.0D);
        private int tickDelay = 20;

        private AttackIllagerGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (this.tickDelay > 0) {
                --this.tickDelay;
                return false;
            } else {
                this.tickDelay = 60;
                List<AbstractIllagerEntity> list = TaillessHorrorEntity.this.world.getTargettableEntitiesWithinAABB(AbstractIllagerEntity.class, field_220842_b, TaillessHorrorEntity.this, TaillessHorrorEntity.this.getBoundingBox().grow(16.0D, 64.0D, 16.0D));
                if (!list.isEmpty()) {
                    list.sort(Comparator.<Entity, Double>comparing(Entity::getPosY).reversed());

                    for(AbstractIllagerEntity abstractIllagerEntity : list) {
                        if (TaillessHorrorEntity.this.canAttack(abstractIllagerEntity, EntityPredicate.DEFAULT)) {
                            TaillessHorrorEntity.this.setAttackTarget(abstractIllagerEntity);
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
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = TaillessHorrorEntity.this.getAttackTarget();
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
            TaillessHorrorEntity.this.rotationYawHead = TaillessHorrorEntity.this.renderYawOffset;
            TaillessHorrorEntity.this.renderYawOffset = TaillessHorrorEntity.this.rotationYaw;
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
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        protected boolean func_203146_f() {
            return TaillessHorrorEntity.this.orbitOffset.squareDistanceTo(TaillessHorrorEntity.this.getPosX(), TaillessHorrorEntity.this.getPosY(), TaillessHorrorEntity.this.getPosZ()) < 4.0D;
        }
    }

    class MoveHelperController extends MovementController {
        private float speedFactor = 0.1F;

        public MoveHelperController(MobEntity entityIn) {
            super(entityIn);
        }

        public void tick() {
            if (TaillessHorrorEntity.this.collidedHorizontally) {
                TaillessHorrorEntity.this.rotationYaw += 180.0F;
                this.speedFactor = 0.1F;
            }

            float f = (float)(TaillessHorrorEntity.this.orbitOffset.x - TaillessHorrorEntity.this.getPosX());
            float f1 = (float)(TaillessHorrorEntity.this.orbitOffset.y - TaillessHorrorEntity.this.getPosY());
            float f2 = (float)(TaillessHorrorEntity.this.orbitOffset.z - TaillessHorrorEntity.this.getPosZ());
            double d0 = (double)MathHelper.sqrt(f * f + f2 * f2);
            double d1 = 1.0D - (double)MathHelper.abs(f1 * 0.7F) / d0;
            f = (float)((double)f * d1);
            f2 = (float)((double)f2 * d1);
            d0 = (double)MathHelper.sqrt(f * f + f2 * f2);
            double d2 = (double)MathHelper.sqrt(f * f + f2 * f2 + f1 * f1);
            float f3 = TaillessHorrorEntity.this.rotationYaw;
            float f4 = (float)MathHelper.atan2((double)f2, (double)f);
            float f5 = MathHelper.wrapDegrees(TaillessHorrorEntity.this.rotationYaw + 90.0F);
            float f6 = MathHelper.wrapDegrees(f4 * (180F / (float)Math.PI));
            TaillessHorrorEntity.this.rotationYaw = MathHelper.approachDegrees(f5, f6, 4.0F) - 90.0F;
            TaillessHorrorEntity.this.renderYawOffset = TaillessHorrorEntity.this.rotationYaw;
            if (MathHelper.degreesDifferenceAbs(f3, TaillessHorrorEntity.this.rotationYaw) < 3.0F) {
                this.speedFactor = MathHelper.approach(this.speedFactor, 1.8F, 0.005F * (1.8F / this.speedFactor));
            } else {
                this.speedFactor = MathHelper.approach(this.speedFactor, 0.2F, 0.025F);
            }

            float f7 = (float)(-(MathHelper.atan2((double)(-f1), d0) * (double)(180F / (float)Math.PI)));
            TaillessHorrorEntity.this.rotationPitch = f7;
            float f8 = TaillessHorrorEntity.this.rotationYaw + 90.0F;
            double d3 = (double)(this.speedFactor * MathHelper.cos(f8 * ((float)Math.PI / 180F))) * Math.abs((double)f / d2);
            double d4 = (double)(this.speedFactor * MathHelper.sin(f8 * ((float)Math.PI / 180F))) * Math.abs((double)f2 / d2);
            double d5 = (double)(this.speedFactor * MathHelper.sin(f7 * ((float)Math.PI / 180F))) * Math.abs((double)f1 / d2);
            Vector3d vector3d = TaillessHorrorEntity.this.getMotion();
            TaillessHorrorEntity.this.setMotion(vector3d.add((new Vector3d(d3, d5, d4)).subtract(vector3d).scale(0.2D)));
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
        public boolean shouldExecute() {
            return TaillessHorrorEntity.this.getAttackTarget() == null || TaillessHorrorEntity.this.attackPhase == TaillessHorrorEntity.AttackPhase.CIRCLE;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.field_203151_d = 5.0F + TaillessHorrorEntity.this.rand.nextFloat() * 10.0F;
            this.field_203152_e = -4.0F + TaillessHorrorEntity.this.rand.nextFloat() * 9.0F;
            this.field_203153_f = TaillessHorrorEntity.this.rand.nextBoolean() ? 1.0F : -1.0F;
            this.func_203148_i();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (TaillessHorrorEntity.this.rand.nextInt(350) == 0) {
                this.field_203152_e = -4.0F + TaillessHorrorEntity.this.rand.nextFloat() * 9.0F;
            }

            if (TaillessHorrorEntity.this.rand.nextInt(250) == 0) {
                ++this.field_203151_d;
                if (this.field_203151_d > 15.0F) {
                    this.field_203151_d = 5.0F;
                    this.field_203153_f = -this.field_203153_f;
                }
            }

            if (TaillessHorrorEntity.this.rand.nextInt(450) == 0) {
                this.field_203150_c = TaillessHorrorEntity.this.rand.nextFloat() * 2.0F * (float)Math.PI;
                this.func_203148_i();
            }

            if (this.func_203146_f()) {
                this.func_203148_i();
            }

            if (TaillessHorrorEntity.this.orbitOffset.y < TaillessHorrorEntity.this.getPosY() && !TaillessHorrorEntity.this.world.isAirBlock(TaillessHorrorEntity.this.getPosition().down(1))) {
                this.field_203152_e = Math.max(1.0F, this.field_203152_e);
                this.func_203148_i();
            }

            if (TaillessHorrorEntity.this.orbitOffset.y > TaillessHorrorEntity.this.getPosY() && !TaillessHorrorEntity.this.world.isAirBlock(TaillessHorrorEntity.this.getPosition().up(1))) {
                this.field_203152_e = Math.min(-1.0F, this.field_203152_e);
                this.func_203148_i();
            }

        }

        private void func_203148_i() {
            if (BlockPos.ZERO.equals(TaillessHorrorEntity.this.orbitPosition)) {
                TaillessHorrorEntity.this.orbitPosition = TaillessHorrorEntity.this.getPosition();
            }

            this.field_203150_c += this.field_203153_f * 15.0F * ((float)Math.PI / 180F);
            TaillessHorrorEntity.this.orbitOffset = Vector3d.copy(TaillessHorrorEntity.this.orbitPosition).add((double)(this.field_203151_d * MathHelper.cos(this.field_203150_c)), (double)(-4.0F + this.field_203152_e), (double)(this.field_203151_d * MathHelper.sin(this.field_203150_c)));
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
        public boolean shouldExecute() {
            LivingEntity livingentity = TaillessHorrorEntity.this.getAttackTarget();
            return livingentity != null && TaillessHorrorEntity.this.canAttack(TaillessHorrorEntity.this.getAttackTarget(), EntityPredicate.DEFAULT);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.tickDelay = 10;
            TaillessHorrorEntity.this.attackPhase = TaillessHorrorEntity.AttackPhase.CIRCLE;
            this.func_203143_f();
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            TaillessHorrorEntity.this.orbitPosition = TaillessHorrorEntity.this.world.getHeight(Heightmap.Type.MOTION_BLOCKING, TaillessHorrorEntity.this.orbitPosition).up(10 + TaillessHorrorEntity.this.rand.nextInt(20));
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
                    this.tickDelay = (8 + TaillessHorrorEntity.this.rand.nextInt(4)) * 20;
                    TaillessHorrorEntity.this.playSound(SoundEvents.ENTITY_POLAR_BEAR_WARNING, 10.0F, 0.95F + TaillessHorrorEntity.this.rand.nextFloat() * 0.1F);
                }
            }

        }

        private void func_203143_f() {
            TaillessHorrorEntity.this.orbitPosition = TaillessHorrorEntity.this.getAttackTarget().getPosition().up(20 + TaillessHorrorEntity.this.rand.nextInt(20));
            if (TaillessHorrorEntity.this.orbitPosition.getY() < TaillessHorrorEntity.this.world.getSeaLevel()) {
                TaillessHorrorEntity.this.orbitPosition = new BlockPos(TaillessHorrorEntity.this.orbitPosition.getX(), TaillessHorrorEntity.this.world.getSeaLevel() + 1, TaillessHorrorEntity.this.orbitPosition.getZ());
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
        public boolean shouldExecute() {
            return TaillessHorrorEntity.this.getAttackTarget() != null && TaillessHorrorEntity.this.attackPhase == TaillessHorrorEntity.AttackPhase.ATTACK;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = TaillessHorrorEntity.this.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            }  else if (!(livingentity instanceof PlayerEntity) || !((PlayerEntity)livingentity).isSpectator() && !((PlayerEntity)livingentity).isCreative()) {
                if (!this.shouldExecute()) {
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
        public void startExecuting() {
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            TaillessHorrorEntity.this.setAttackTarget((LivingEntity)null);
            TaillessHorrorEntity.this.attackPhase = TaillessHorrorEntity.AttackPhase.CIRCLE;
            TaillessHorrorEntity.this.setRoaring(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            TaillessHorrorEntity.this.setRoaring(true);
            LivingEntity livingentity = TaillessHorrorEntity.this.getAttackTarget();
            assert livingentity != null;
            TaillessHorrorEntity.this.orbitOffset = new Vector3d(livingentity.getPosX(), livingentity.getPosYHeight(2.5D), livingentity.getPosZ());
            ++TaillessHorrorEntity.this.attackTimer;
            if (TaillessHorrorEntity.this.attackTimer >= 40){
                this.RangedAttack();
                TaillessHorrorEntity.this.attackTimer = -10;
            }
            if (TaillessHorrorEntity.this.hurtTime > 0 && this.hurt < 3){
                this.hurt = this.hurt + 1;
                TaillessHorrorEntity.this.hurtTime = 0;
            }
            if (TaillessHorrorEntity.this.collidedHorizontally || this.hurt >= 3) {
                TaillessHorrorEntity.this.attackPhase = AttackPhase.CIRCLE;
                this.hurt = 0;
                TaillessHorrorEntity.this.attackTimer = 0;
                TaillessHorrorEntity.this.setRoaring(false);
            }

        }

        public void RangedAttack() {
            LivingEntity livingentity = TaillessHorrorEntity.this.getAttackTarget();
/*            Vector3d vector3d = TaillessHorrorEntity.this.getLook(1.0F);
            ArrowEntity arrowentity = new ArrowEntity(world, TaillessHorrorEntity.this.getPosX() + vector3d.x, TaillessHorrorEntity.this.getPosYHeight(0.55D), TaillessHorrorEntity.this.getPosZ() + vector3d.z);*/
            ArrowEntity arrowentity = new ArrowEntity(world, TaillessHorrorEntity.this);
            assert livingentity != null;
            double d0 = livingentity.getPosX() - TaillessHorrorEntity.this.getPosX();
            double d1 = livingentity.getPosYHeight(0.3333333333333333D) - arrowentity.getPosY();
            double d2 = livingentity.getPosZ() - TaillessHorrorEntity.this.getPosZ();
            double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
            arrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 2.4F, (float)(14 - world.getDifficulty().getId() * 4));
            playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
            world.addEntity(arrowentity);
        }
    }

}
