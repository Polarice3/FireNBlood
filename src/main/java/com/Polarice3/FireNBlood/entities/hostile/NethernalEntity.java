package com.Polarice3.FireNBlood.entities.hostile;

import com.Polarice3.FireNBlood.entities.hostile.cultists.ChannellerEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class NethernalEntity extends MonsterEntity {
    private int attackTimer;
    private static final Predicate<Entity> field_213690_b = Entity::isAlive;
    private static final Predicate<LivingEntity> TARGETS = (enemy) -> !(enemy instanceof WitchEntity)
            && !(enemy instanceof ChannellerEntity) && !(enemy instanceof AbstractTaillessEntity)
            && !(enemy instanceof AbstractPiglinEntity) && !(enemy instanceof HoglinEntity)
            && !(enemy.isImmuneToFire());
    private boolean limitedLifespan;
    private int limitedLifeTicks;

    public NethernalEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
        this.stepHeight = 1.0F;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, false, false, TARGETS));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 150.0D)
                .createMutableAttribute(Attributes.ARMOR, 6.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 17.0D);
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    protected void collideWithEntity(Entity entityIn) {
        if (entityIn instanceof IMob && this.getRNG().nextInt(20) == 0) {
            this.setAttackTarget((LivingEntity)entityIn);
        }

        super.collideWithEntity(entityIn);
    }

    protected void registerData() {
        super.registerData();
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

    public void livingTick() {
        super.livingTick();
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.attackEntityFrom(DamageSource.STARVE, this.getMaxHealth());
        }
        if (this.ticksExisted % 20 == 0) {
            this.heal(1.0F);
        }
        if (this.attackTimer > 0) {
            --this.attackTimer;
        }

        if (this.world.isRemote) {
            for(int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }

        if (this.collidedHorizontally && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
            boolean flag = false;
            AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(0.2D);

            for(BlockPos blockpos : BlockPos.getAllInBoxMutable(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ))) {
                BlockState blockstate = this.world.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (block instanceof LeavesBlock || (block instanceof RotatedPillarBlock && block != Blocks.BASALT && block != Blocks.POLISHED_BASALT && block != Blocks.QUARTZ_PILLAR)) {
                    flag = this.world.destroyBlock(blockpos, true, this) || flag;
                }
            }

            if (!flag && this.onGround) {
                this.jump();
            }
        }

        for(Entity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(4.0D), field_213690_b)) {
            if (!(entity instanceof WitchEntity)
                    && !(entity instanceof ChannellerEntity) && !(entity instanceof AbstractTaillessEntity)
                    && !(entity instanceof AbstractPiglinEntity) && !(entity instanceof HoglinEntity)
                    && !(entity.isImmuneToFire())){
                if (entity instanceof PlayerEntity){
                    if (!((PlayerEntity) entity).isCreative()){
                        entity.setFire(30);
                    }
                } else {
                    entity.setFire(30);
                }
            }
        }
    }

    private float func_226511_et_() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    public boolean isWaterSensitive() {
        return true;
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        this.attackTimer = 10;
        this.world.setEntityState(this, (byte)4);
        float f = this.func_226511_et_();
        float f1 = (int)f > 0 ? f / 2.0F + (float)this.rand.nextInt((int)f) : f;
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f1);
        if (flag) {
            entityIn.setMotion(entityIn.getMotion().add(0.0D, (double)0.4F, 0.0D));
            this.applyEnchantments(this, entityIn);
        }

        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    public float getBrightness() {
        return 1.0F;
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (source == DamageSource.MAGIC){
            return false;
        } else {
            int r = this.world.getDifficulty() == Difficulty.HARD ? 64: 128;
            int random = this.world.rand.nextInt(r);
            if (random == 1) {
                BlockPos blockpos = this.getPosition().add(0, 1, 0);
                ScorchEntity scorchEntity = new ScorchEntity(ModEntityType.SCORCH.get(), this.world);
                scorchEntity.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
                scorchEntity.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
                scorchEntity.setLimitedLife(20 * (30 + this.rand.nextInt(90)));
                this.world.addEntity(scorchEntity);
            }
            return super.attackEntityFrom(source, amount);
        }
    }

    public int getAttackTimer() {
        return this.attackTimer;
    }

    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
    }


}
