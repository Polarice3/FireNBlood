package com.Polarice3.FireNBlood.entities.hostile;

import com.Polarice3.FireNBlood.entities.ally.SummonedEntity;
import com.Polarice3.FireNBlood.entities.neutral.AbstractProtectorEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class ChannellerEntity extends AbstractRaiderEntity {
    private static final DataParameter<Boolean> IS_PRAYING = EntityDataManager.createKey(ChannellerEntity.class, DataSerializers.BOOLEAN);
    private static final Predicate<LivingEntity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof ChannellerEntity);
    };
    private static final DataParameter<Integer> TARGET_ALLY = EntityDataManager.createKey(ChannellerEntity.class, DataSerializers.VARINT);
    private MonsterEntity AllyTarget;
    private int prayingTick;
    private int prayingCooldown;
    private int healTick;

    public ChannellerEntity(EntityType<? extends ChannellerEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 16.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, -1.0F);
        ((GroundPathNavigator)this.getNavigator()).setBreakDoors(true);
        this.getNavigator().setCanSwim(true);
        this.prayingCooldown = 0;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractProtectorEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 26.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(IS_PRAYING, false);
        this.dataManager.register(TARGET_ALLY, 0);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.prayingTick = compound.getInt("prayingTick");
        this.prayingCooldown = compound.getInt("prayingCooldown");
        this.healTick = compound.getInt("healTick");
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("prayingTick", this.prayingTick);
        compound.putInt("prayingCooldown", this.prayingCooldown);
        compound.putInt("healTick", this.healTick);
    }

    private void setAllyTarget(int AllyTargetIn) {
        this.dataManager.set(TARGET_ALLY, AllyTargetIn);
    }

    public boolean hasAllyTarget() {
        return this.dataManager.get(TARGET_ALLY) != 0;
    }

    public MonsterEntity getAllyTarget() {
        Entity entity = this.world.getEntityByID(this.dataManager.get(TARGET_ALLY));
        if (entity instanceof MonsterEntity
                && !(entity instanceof CreeperEntity)
                && !(entity instanceof TaillessDruidEntity)
                && !(entity instanceof SummonedEntity)) {
            return (MonsterEntity) entity;
        } else {
            return null;
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount){
        if (!this.isPraying()) {
            return super.attackEntityFrom(source, amount);
        } else {
            return super.attackEntityFrom(source, amount/2);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public ChannellerEntity.ArmPose getArmPose() {
        if (this.prayingTick > 0) {
            return ArmPose.SPELLCASTING;
        } else {
            return ChannellerEntity.ArmPose.CROSSED;
        }
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData ilivingentitydata = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return ilivingentitydata;
    }

    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    private final EntityPredicate ally = (new EntityPredicate().setDistance(32.0D).setCustomPredicate(field_213690_b));

    public void setIsPraying(boolean praying) {
        this.dataManager.set(IS_PRAYING, praying);
    }

    public boolean isPraying() {
        return this.dataManager.get(IS_PRAYING);
    }

    public void livingTick() {
        super.livingTick();
        if (this.prayingCooldown == 0) {
            List<MonsterEntity> list = this.world.getTargettableEntitiesWithinAABB(MonsterEntity.class, this.ally, this, this.getBoundingBox().grow(64.0D, 8.0D, 64.0D));
            if (!list.isEmpty() && !this.hasAllyTarget()) {
                MonsterEntity ally = list.get(this.rand.nextInt(list.size()));
                this.setAllyTarget(ally.getEntityId());
            }
            if (this.hasAllyTarget()) {
                if (this.prayingTick < 20) {
                    ++this.prayingTick;
                } else {
                    if (this.getDistance(this.getAllyTarget()) > 8.0D && this.getAllyTarget() != null) {
                        Vector3d vector3d = getAllyTarget().getPositionVec();
                        this.getNavigator().tryMoveToXYZ(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                    } else {
                        this.navigator.clearPath();
                        this.idleTime = 0;
                        this.setIsPraying(true);
                        this.getLookController().setLookPositionWithEntity(this.getAllyTarget(), (float) this.getHorizontalFaceSpeed(), (float) this.getVerticalFaceSpeed());
                        this.getAllyTarget().setAttackTarget(this.getAttackTarget());
                        this.getAllyTarget().addPotionEffect(new EffectInstance(Effects.SPEED, 60, 1));
                        this.getAllyTarget().addPotionEffect(new EffectInstance(Effects.RESISTANCE, 60, 1));
                        this.getAllyTarget().enablePersistence();
                        if (this.getHealth() < this.getMaxHealth()) {
                            ++this.healTick;
                            if (this.healTick >= 10) {
                                this.getAllyTarget().attackEntityFrom(DamageSource.STARVE, 1.0F);
                                this.heal(1.0F);
                                this.healTick = 0;
                            }
                        }
                    }
                    if (this.getAllyTarget().getShouldBeDead()) {
                        this.setAllyTarget(0);
                        this.setIsPraying(false);
                        this.prayingCooldown = 300;
                    }
                }
            } else {
                this.prayingTick = 0;
                this.RunAway();
            }
        } else {
            if (this.prayingCooldown > 0){
                --this.prayingCooldown;
            }
            this.prayingTick = 0;
            this.RunAway();
        }
    }

    public void RunAway(){
        LivingEntity enemy = this.getAttackTarget();
        this.setIsPraying(false);
        if (enemy != null) {
            this.world.setEntityState(this, (byte) 42);
            PathNavigator navigation = this.getNavigator();
            Vector3d vector3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this, 16, 7, enemy.getPositionVec());
            if (vector3d != null && enemy.getDistanceSq(vector3d.x, vector3d.y, vector3d.z) < enemy.getDistanceSq(this)) {
                Path path = navigation.pathfind(vector3d.x, vector3d.y, vector3d.z, 0);
                navigation.setPath(path, 0.6D);
                if (this.getDistanceSq(enemy) < 49.0D) {
                    this.getNavigator().setSpeed(1.0D);
                } else {
                    this.getNavigator().setSpeed(0.6D);
                }
            }
        }
    }

    protected float applyPotionDamageCalculations(DamageSource source, float damage) {
        damage = super.applyPotionDamageCalculations(source, damage);
        if (source.getTrueSource() == this) {
            damage = 0.0F;
        }

        if (source.isMagicDamage()) {
            damage = (float)((double)damage * 0.15D);
        }

        return damage;
    }

    @Override
    public void applyWaveBonus(int wave, boolean p_213660_2_) {
    }

    @Override
    public SoundEvent getRaidLossSound() {
        return SoundEvents.ENTITY_VILLAGER_CELEBRATE;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.62F;
    }

    public boolean canBeLeader() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static enum ArmPose {
        CROSSED,
        SPELLCASTING,
        NEUTRAL;
    }
}
