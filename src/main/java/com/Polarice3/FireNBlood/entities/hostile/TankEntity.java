package com.Polarice3.FireNBlood.entities.hostile;

import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class TankEntity extends AbstractTaillessEntity{
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof TankEntity);
    };

    public TankEntity(EntityType<? extends AbstractTaillessEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.maxUpStep = 1.0F;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 256.0D)
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 10.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new TankEntity.RangeAttackGoal());
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractIllagerEntity.class, true));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractPiglinEntity.class)).setAlertOthers());
    }

    @Override
    protected int getExperienceReward(PlayerEntity player){
        return 5 + this.level.random.nextInt(5);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.FIRE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ANVIL_LAND;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.25F, 1.0F);
    }

    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getDirectEntity();
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (entity instanceof AbstractArrowEntity) {
            return false;
        } else {
            return super.hurt(source, amount);
        }
    }

    public void aiStep() {
        if (this.level.isClientSide) {
            for(int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }

        super.aiStep();
    }

    class RangeAttackGoal extends Goal{
        public int attackTimer;
        public int attackTimes = 0;

        @Override
        public boolean canUse() {
            if (TankEntity.this.getTarget() == null) {
                return false;
            } else {
                return true;
            }
        }

        public void start() {
            this.attackTimer = 0;
        }

        public void tick() {
            LivingEntity livingentity = TankEntity.this.getTarget();
            assert livingentity != null;
            if (livingentity.distanceToSqr(TankEntity.this) < 4096.0D && TankEntity.this.canSee(livingentity)) {
                TankEntity.this.getLookControl().setLookAt(livingentity, 10.0F, 10.0F);
                ++this.attackTimer;
                World world = TankEntity.this.level;
                if (this.attackTimes < 3) {
                    if (this.attackTimer >= 20) {
                        this.attackTimes = this.attackTimes + 1;
                        double d1 = 4.0D;
                        Vector3d vector3d = TankEntity.this.getViewVector( 1.0F);
                        double d2 = livingentity.getX() - (TankEntity.this.getX() + vector3d.x * 2.0D);
                        double d3 = livingentity.getY(0.5D) - (0.5D + TankEntity.this.getY(0.5D));
                        double d4 = livingentity.getZ() - (TankEntity.this.getZ() + vector3d.z * 2.0D);
                        FireballEntity fireballentity = new FireballEntity(world, TankEntity.this, d2, d3, d4);
                        fireballentity.setPos(TankEntity.this.getX() + vector3d.x * 2.0D, TankEntity.this.getY(0.5D) + 0.25D, fireballentity.getZ() + vector3d.z * 2.0D);
                        level.addFreshEntity(fireballentity);
                        if (!TankEntity.this.isSilent()) {
                            TankEntity.this.level.levelEvent(null, 1016, TankEntity.this.blockPosition(), 0);
                        }
                        this.attackTimer = -40;
                    }
                } else {
                    if (this.attackTimer >= 20) {
                        Vector3d vector3d = TankEntity.this.getViewVector( 1.0F);
                        double d2 = livingentity.getX() - (TankEntity.this.getX() + vector3d.x * 2.0D);
                        double d3 = livingentity.getY(0.5D) - (0.5D + TankEntity.this.getY(0.5D));
                        double d4 = livingentity.getZ() - (TankEntity.this.getZ() + vector3d.z * 2.0D);
                        SmallFireballEntity smallfireballentity = new SmallFireballEntity(world, TankEntity.this, d2, d3, d4);
                        smallfireballentity.setPos(TankEntity.this.getX() + vector3d.x * 2.0D, TankEntity.this.getY(0.5D) + 0.25D, smallfireballentity.getZ() + vector3d.z * 2.0D);
                        level.addFreshEntity(smallfireballentity);
                        if (!TankEntity.this.isSilent()) {
                            TankEntity.this.level.levelEvent(null, 1016, TankEntity.this.blockPosition(), 0);
                        }
                        if (this.attackTimer >= 40) {
                            this.attackTimes = 0;
                            this.attackTimer = -80;
                        }
                    }
                }
                double d0 = TankEntity.this.distanceToSqr(livingentity);
                if (d0 > 16.0D) {
                    TankEntity.this.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
                }
                for (Entity entity : TankEntity.this.level.getEntitiesOfClass(LivingEntity.class, TankEntity.this.getBoundingBox().inflate(1.5D), field_213690_b)) {
                    if (!(entity instanceof AbstractTaillessEntity)) {
                        TankEntity.this.doHurtTarget(entity);
                        this.launch(entity);
                    }
                }
            }
        }
            private void launch(Entity p_213688_1_) {
                double d0 = p_213688_1_.getX() - TankEntity.this.getX();
                double d1 = p_213688_1_.getZ() - TankEntity.this.getZ();
                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                p_213688_1_.push(d0 / d2 * 2.0D, 0.2D, d1 / d2 * 2.0D);
            }

    }

}
