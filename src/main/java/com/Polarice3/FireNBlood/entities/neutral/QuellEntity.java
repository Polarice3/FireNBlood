package com.Polarice3.FireNBlood.entities.neutral;

import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.projectiles.EtherealPunchEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class QuellEntity extends AbstractProtectorEntity {
    protected static final DataParameter<Byte> QUELL_FLAGS = EntityDataManager.createKey(QuellEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> DATA_CHARGING_STATE = EntityDataManager.createKey(QuellEntity.class, DataSerializers.BOOLEAN);
    private MobEntity owner;
    private boolean limitedLifespan;
    private int limitedLifeTicks;
    public int attackTick;

    public QuellEntity(EntityType<? extends AbstractProtectorEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new QuellEntity.MoveHelperController(this);
        this.attackTick = 0;
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.doBlockCollisions();
    }

    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }
        if (this.attackTick > 0){
            --this.attackTick;
        }

    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(5, new QuellEntity.AttackGoal());
        this.goalSelector.addGoal(6, new QuellEntity.FollowOwnerGoal());
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new QuellEntity.CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractProtectorEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractVillagerEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractTaillessEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractRaiderEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_234199_0_) -> {
            return p_234199_0_ instanceof IMob && !(p_234199_0_ instanceof CreeperEntity);
        }));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .createMutableAttribute(Attributes.MAX_HEALTH, 8.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(QUELL_FLAGS, (byte)0);
        this.dataManager.register(DATA_CHARGING_STATE, false);
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source != DamageSource.STARVE){
            return false;
        } else {
            return super.attackEntityFrom(source, amount);
        }
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }

    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.limitedLifespan) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }

    }

    public void setLimitedLife(int limitedLifeTicksIn) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    public MobEntity getOwner() {
        return this.owner;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ELDER_GUARDIAN_HURT;
    }

    public void setOwner(MobEntity ownerIn) {
        this.owner = ownerIn;
    }

    private boolean getQuellFlag(int mask) {
        int i = this.dataManager.get(QUELL_FLAGS);
        return (i & mask) != 0;
    }

    private void setQuellFlag(int mask, boolean value) {
        int i = this.dataManager.get(QUELL_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(QUELL_FLAGS, (byte)(i & 255));
    }

    public boolean isCharging() {
        return this.dataManager.get(DATA_CHARGING_STATE);
    }

    public void setCharging(boolean isCharging) {
        this.dataManager.set(DATA_CHARGING_STATE, isCharging);
    }

    public float getBrightness() {
        return 1.0F;
    }

    class FollowOwnerGoal extends Goal{

        public FollowOwnerGoal(){
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            MobEntity summoner = QuellEntity.this.getOwner();
            return summoner !=null && !QuellEntity.this.isCharging();
        }

        public void tick() {
            LivingEntity owner = QuellEntity.this.getOwner();
            assert owner != null;
            Vector3d vector3d = owner.getLook(1.0F);
            QuellEntity.this.moveController.setMoveTo(vector3d.x - 1, owner.getPosY() + 2, vector3d.z - 1, 1.0D);
        }
    }

    class AttackGoal extends Goal{
        int attacking;
        int attackTime;

        public boolean shouldExecute() {
            return QuellEntity.this.getAttackTarget() != null && QuellEntity.this.attackTick <= 0;
        }

        @Override
        public void startExecuting() {
            QuellEntity.this.setCharging(true);
        }

        public void tick(){
            LivingEntity livingentity = QuellEntity.this.getAttackTarget();
            assert livingentity != null;
            Vector3d vector3d0 = livingentity.getEyePosition(1.0F);
            QuellEntity.this.moveController.setMoveTo(vector3d0.x, livingentity.getPosY() + 1, vector3d0.z - 5, 1.0D);
            QuellEntity.this.lookController.setLookPosition(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
            ++this.attackTime;
            ++this.attacking;
            if (this.attacking >= 5){
                this.attacking = 0;
                Vector3d vector3d = QuellEntity.this.getLook(1.0F);
                double d2 = livingentity.getPosX() - (QuellEntity.this.getPosX() + vector3d.x * 2.0D);
                double d3 = livingentity.getPosYHeight(0.5D) - (0.5D + QuellEntity.this.getPosYHeight(0.5D));
                double d4 = livingentity.getPosZ() - (QuellEntity.this.getPosZ() + vector3d.z * 2.0D);
                EtherealPunchEntity punch = new EtherealPunchEntity(world, QuellEntity.this, d2, d3, d4);
                punch.setPosition(QuellEntity.this.getPosX(), QuellEntity.this.getPosYHeight(0.5D) + QuellEntity.this.world.rand.nextDouble(), punch.getPosZ() - QuellEntity.this.world.rand.nextDouble());
                QuellEntity.this.world.addEntity(punch);
                if (!QuellEntity.this.isSilent()) {
                    QuellEntity.this.world.playEvent(null, 1016, QuellEntity.this.getPosition(), 0);
                }
            }
            if (this.attackTime >= 60){
                QuellEntity.this.attackTick = 60;
                this.attacking = 0;
                this.attackTime = 0;
                QuellEntity.this.setCharging(false);
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
            return QuellEntity.this.owner != null && QuellEntity.this.owner.getAttackTarget() != null && this.isSuitableTarget(QuellEntity.this.owner.getAttackTarget(), this.field_220803_b);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            QuellEntity.this.setAttackTarget(QuellEntity.this.owner.getAttackTarget());
            super.startExecuting();
        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(QuellEntity quell) {
            super(quell);
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.posX - QuellEntity.this.getPosX(), this.posY - QuellEntity.this.getPosY(), this.posZ - QuellEntity.this.getPosZ());
                double d0 = vector3d.length();
                if (d0 < QuellEntity.this.getBoundingBox().getAverageEdgeLength()) {
                    this.action = MovementController.Action.WAIT;
                    QuellEntity.this.setMotion(QuellEntity.this.getMotion().scale(0.5D));
                } else {
                    QuellEntity.this.setMotion(QuellEntity.this.getMotion().add(vector3d.scale(this.speed * 0.05D / d0)));
                    if (QuellEntity.this.getAttackTarget() == null) {
                        Vector3d vector3d1 = QuellEntity.this.getMotion();
                        QuellEntity.this.rotationYaw = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                    } else {
                        double d2 = QuellEntity.this.getAttackTarget().getPosX() - QuellEntity.this.getPosX();
                        double d1 = QuellEntity.this.getAttackTarget().getPosZ() - QuellEntity.this.getPosZ();
                        QuellEntity.this.rotationYaw = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                    }
                    QuellEntity.this.renderYawOffset = QuellEntity.this.rotationYaw;
                }

            }
        }
    }


}
