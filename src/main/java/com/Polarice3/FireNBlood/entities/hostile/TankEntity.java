package com.Polarice3.FireNBlood.entities.hostile;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
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

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TankEntity extends ServantTaillessEntity{
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof TankEntity);
    };

    public TankEntity(EntityType<? extends ServantTaillessEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.stepHeight = 1.0F;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 256.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 100.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 6.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .createMutableAttribute(Attributes.ARMOR, 10.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new TankEntity.RangeAttackGoal());
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractIllagerEntity.class, true));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractPiglinEntity.class)).setCallsForHelp());
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player){
        return 5 + this.world.rand.nextInt(5);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLOCK_FIRE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.BLOCK_ANVIL_LAND;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 0.25F, 1.0F);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getImmediateSource();
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (entity instanceof AbstractArrowEntity) {
            return false;
        } else {
            return super.attackEntityFrom(source, amount);
        }
    }

    public void livingTick() {
        if (this.world.isRemote) {
            for(int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }

        super.livingTick();
    }

    class RangeAttackGoal extends Goal{
        public int attackTimer;
        public int attackTimes = 0;

        @Override
        public boolean shouldExecute() {
            if (TankEntity.this.getAttackTarget() == null) {
                return false;
            } else {
                return true;
            }
        }

        public void startExecuting() {
            this.attackTimer = 0;
        }

        public void tick() {
            LivingEntity livingentity = TankEntity.this.getAttackTarget();
            assert livingentity != null;
            if (livingentity.getDistanceSq(TankEntity.this) < 4096.0D && TankEntity.this.canEntityBeSeen(livingentity)) {
                TankEntity.this.getLookController().setLookPositionWithEntity(livingentity, 10.0F, 10.0F);
                ++this.attackTimer;
                World world = TankEntity.this.world;
                if (this.attackTimes < 3) {
                    if (this.attackTimer >= 20) {
                        this.attackTimes = this.attackTimes + 1;
                        double d1 = 4.0D;
                        Vector3d vector3d = TankEntity.this.getLook(1.0F);
                        double d2 = livingentity.getPosX() - (TankEntity.this.getPosX() + vector3d.x * 2.0D);
                        double d3 = livingentity.getPosYHeight(0.5D) - (0.5D + TankEntity.this.getPosYHeight(0.5D));
                        double d4 = livingentity.getPosZ() - (TankEntity.this.getPosZ() + vector3d.z * 2.0D);
                        FireballEntity fireballentity = new FireballEntity(world, TankEntity.this, d2, d3, d4);
                        fireballentity.setPosition(TankEntity.this.getPosX() + vector3d.x * 2.0D, TankEntity.this.getPosYHeight(0.5D) + 0.25D, fireballentity.getPosZ() + vector3d.z * 2.0D);
                        world.addEntity(fireballentity);
                        if (!TankEntity.this.isSilent()) {
                            TankEntity.this.world.playEvent(null, 1016, TankEntity.this.getPosition(), 0);
                        }
                        this.attackTimer = -40;
                    }
                } else {
                    if (this.attackTimer >= 20) {
                        Vector3d vector3d = TankEntity.this.getLook(1.0F);
                        double d2 = livingentity.getPosX() - (TankEntity.this.getPosX() + vector3d.x * 2.0D);
                        double d3 = livingentity.getPosYHeight(0.5D) - (0.5D + TankEntity.this.getPosYHeight(0.5D));
                        double d4 = livingentity.getPosZ() - (TankEntity.this.getPosZ() + vector3d.z * 2.0D);
                        SmallFireballEntity smallfireballentity = new SmallFireballEntity(world, TankEntity.this, d2, d3, d4);
                        smallfireballentity.setPosition(TankEntity.this.getPosX() + vector3d.x * 2.0D, TankEntity.this.getPosYHeight(0.5D) + 0.25D, smallfireballentity.getPosZ() + vector3d.z * 2.0D);
                        world.addEntity(smallfireballentity);
                        if (!TankEntity.this.isSilent()) {
                            TankEntity.this.world.playEvent(null, 1016, TankEntity.this.getPosition(), 0);
                        }
                        if (this.attackTimer >= 40) {
                            this.attackTimes = 0;
                            this.attackTimer = -80;
                        }
                    }
                }
                double d0 = TankEntity.this.getDistanceSq(livingentity);
                if (d0 > 16.0D) {
                    TankEntity.this.getMoveHelper().setMoveTo(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), 1.0D);
                }
                for (Entity entity : TankEntity.this.world.getEntitiesWithinAABB(LivingEntity.class, TankEntity.this.getBoundingBox().grow(1.5D), field_213690_b)) {
                    if (!(entity instanceof AbstractTaillessEntity)) {
                        TankEntity.this.attackEntityAsMob(entity);
                        this.launch(entity);
                    }
                }
            }
        }
            private void launch(Entity p_213688_1_) {
                double d0 = p_213688_1_.getPosX() - TankEntity.this.getPosX();
                double d1 = p_213688_1_.getPosZ() - TankEntity.this.getPosZ();
                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                p_213688_1_.addVelocity(d0 / d2 * 2.0D, 0.2D, d1 / d2 * 2.0D);
            }

    }

}
