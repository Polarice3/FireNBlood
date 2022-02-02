package com.Polarice3.FireNBlood.entities.hostile;

import com.Polarice3.FireNBlood.entities.neutral.MinionEntity;
import com.Polarice3.FireNBlood.entities.neutral.protectors.MirageEntity;
import com.Polarice3.FireNBlood.entities.neutral.MutatedEntity;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Predicate;

public class ParasiteEntity extends MonsterEntity {
    private final NearestAttackableTargetGoal<LivingEntity> aiAttackAllGoal =  new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, true, TARGETS);
    private int lifetime;
    private boolean attackAll;
    private int field_234358_by_ = 0;
    private static final Predicate<LivingEntity> TARGETS = (enemy) -> !(enemy instanceof ParasiteEntity)
            && !(enemy instanceof AbstractSkeletonEntity)
            && !(enemy instanceof VexEntity) && !(enemy instanceof MinionEntity) && !(enemy instanceof MirageEntity)
            && !(enemy instanceof GhastEntity) && !(enemy instanceof BlazeEntity) && !(enemy instanceof SlimeEntity)
            && !(enemy instanceof MutatedEntity)
            && !(enemy instanceof GuardianEntity) && !(enemy instanceof IronGolemEntity) && !(enemy instanceof TankEntity)
            && !(enemy instanceof SilverfishEntity) && !(enemy instanceof EndermiteEntity)
            && !(enemy instanceof WitherEntity) && !(enemy instanceof EnderDragonEntity);
    private static final Predicate<LivingEntity> HOSTED = (p_213797_0_) ->
            p_213797_0_.isPotionActive(RegistryHandler.HOSTED.get()) && !(p_213797_0_ instanceof ParasiteEntity) && p_213797_0_.attackable();

    public ParasiteEntity(EntityType<? extends ParasiteEntity> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 3;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, true, HOSTED));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 8.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.lifetime = compound.getInt("Lifetime");
        this.attackAll = compound.getBoolean("AttackAll");
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Lifetime", this.lifetime);
        compound.putBoolean("AttackAll", this.attackAll);
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 0.13F;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDERMITE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ENDERMITE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMITE_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_ENDERMITE_STEP, 0.15F, 1.0F);
    }

    public boolean isAttackAll() {
        return this.attackAll;
    }

    public void setAttackAll(boolean attackAll) {
        this.attackAll = attackAll;
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && entityIn instanceof LivingEntity) {
            int random = this.world.rand.nextInt(16);
            if (random == 0) {
                float f = this.world.getDifficultyForLocation(this.getPosition()).getAdditionalDifficulty();
                ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(RegistryHandler.HOSTED.get(), 140 * (int) f));
                this.lifetime = 0;
            }
        }

        return flag;
    }

    public void updateAITasks(){
        if (this.isAttackAll()){
            this.goalSelector.addGoal(2, aiAttackAllGoal);
        }
        if (this.func_234364_eK_()) {
            ++this.field_234358_by_;
            if (this.field_234358_by_ > 300 && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(this, EntityType.ENDERMITE, (timer) -> this.field_234358_by_ = timer)) {
                this.playSound(SoundEvents.ENTITY_ENDERMITE_DEATH, 1.0F, 1.0F);
                this.func_234360_a_((ServerWorld)this.world);
            }
        } else {
            this.field_234358_by_ = 0;
        }
        super.updateAITasks();
    }

    private void func_234360_a_(ServerWorld p_234360_1_) {
        EndermiteEntity endermiteEntity = this.func_233656_b_(EntityType.ENDERMITE, true);
        if (endermiteEntity != null) {
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, endermiteEntity);
        }

    }

    public void setRenderYawOffset(float offset) {
        this.rotationYaw = offset;
        super.setRenderYawOffset(offset);
    }

    public double getYOffset() {
        return 0.1D;
    }

    public void livingTick() {
        super.livingTick();
        if (!this.isNoDespawnRequired()) {
            ++this.lifetime;
        }

        if (this.lifetime >= 1200 && !this.world.getDimensionType().doesHasDragonFight()) {
            this.damageEntity(DamageSource.STARVE, this.getMaxHealth());
        }
        this.renderYawOffset = this.rotationYaw;
    }

    public boolean func_234364_eK_() {
        return this.world.getDimensionType().doesHasDragonFight() && !this.isAIDisabled();
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

}
