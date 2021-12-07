package com.Polarice3.FireNBlood.entities.ally;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FriendlyTankEntity extends TameableEntity {
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof FriendlyTankEntity);
    };

    public int attackTimer;
    public int attackTimes = 0;
    public int attackStep = 0;
    public boolean FireCharging;

    public FriendlyTankEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.stepHeight = 1.0F;
        this.setTamed(false);
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
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(3, new FriendlyTankEntity.RangeAttackGoal());
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 20.0F, 4.0F, false));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_234199_0_) -> {
            return p_234199_0_ instanceof IMob && !(p_234199_0_ instanceof CreeperEntity) && !(p_234199_0_ instanceof FriendlyVexEntity);
        }));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.isSitting()) {
            return SoundEvents.BLOCK_FIRE_AMBIENT;
        } else {
            return null;
        }
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

    public boolean canBeSteered() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }


    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
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

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }

    @Override
    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    public void livingTick() {
            if (this.world.isRemote) {
                if (!this.isSitting()) {
                    for (int i = 0; i < 2; ++i) {
                        this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
                    }
                }
            }
            if (this.FireCharging){
                this.FireCharge();
            }

        super.livingTick();
    }

    public FriendlyTankEntity.Cracks func_226512_l_() {
        return FriendlyTankEntity.Cracks.func_226515_a_(this.getHealth() / this.getMaxHealth());
    }

    protected void mountTo(PlayerEntity player) {
        if (!this.world.isRemote) {
            player.rotationYaw = this.rotationYaw;
            player.rotationPitch = this.rotationPitch;
            player.startRiding(this);
        }
    }

    public void travel(Vector3d travelVector) {
        if (this.isAlive()) {
            if (this.isBeingRidden() && this.canBeSteered()) {
                if (this.attackTimer < 20) {
                    ++this.attackTimer;
                }
                LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();
                this.rotationYaw = livingentity.rotationYaw;
                this.prevRotationYaw = this.rotationYaw;
                this.rotationPitch = livingentity.rotationPitch * 0.5F;
                this.setRotation(this.rotationYaw, this.rotationPitch);
                this.renderYawOffset = this.rotationYaw;
                this.rotationYawHead = this.renderYawOffset;
                float f = livingentity.moveStrafing * 0.5F;
                float f1 = livingentity.moveForward;
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                }

                this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
                if (this.canPassengerSteer()) {
                    this.setAIMoveSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    super.travel(new Vector3d((double) f, travelVector.y, (double) f1));
                } else if (livingentity instanceof PlayerEntity) {
                    this.setMotion(Vector3d.ZERO);
                }

                if (livingentity.swingProgressInt == -1) {
                    if (this.attackTimes < 3) {
                        if (this.attackTimer >= 20) {
                            World world = FriendlyTankEntity.this.world;
                            Vector3d vector3d = FriendlyTankEntity.this.getLook(1.0F);
                            Random random = world.rand;
                            double d2 = random.nextGaussian() * 0.05D + (double) vector3d.x;
                            double d3 = random.nextGaussian() * 0.05D + (double) vector3d.y;
                            double d4 = random.nextGaussian() * 0.05D + (double) vector3d.z;
                            FireballEntity fireballentity = new FireballEntity(world, FriendlyTankEntity.this, d2, d3, d4);
                            fireballentity.setPosition(FriendlyTankEntity.this.getPosX() + vector3d.x * 2.0D, FriendlyTankEntity.this.getPosYHeight(0.5D) + 0.25D, fireballentity.getPosZ() + vector3d.z * 2.0D);
                            world.addEntity(fireballentity);
                            if (!FriendlyTankEntity.this.isSilent()) {
                                FriendlyTankEntity.this.world.playEvent(null, 1016, FriendlyTankEntity.this.getPosition(), 0);
                            }
                            this.attackTimer = 0;
                            this.attackTimes = this.attackTimes + 1;
                        }
                    } else {
                        this.FireCharging = true;
                    }
                }
                for (Entity entity : FriendlyTankEntity.this.world.getEntitiesWithinAABB(LivingEntity.class, FriendlyTankEntity.this.getBoundingBox().grow(1.5D), field_213690_b)) {
                    if (!(entity instanceof PlayerEntity)) {
                        FriendlyTankEntity.this.attackEntityAsMob(entity);
                        this.launch(entity);
                    }
                }
            }

            this.func_233629_a_(this, false);
            } else {
                this.jumpMovementFactor = 0.02F;
                super.travel(travelVector);
            }
    }

    public void FireCharge(){
        if (this.attackTimer >= 20) {
            ++this.attackStep;
            World world = FriendlyTankEntity.this.world;
            Vector3d vector3d = FriendlyTankEntity.this.getLook(1.0F);
            Random random = world.rand;
            double d2 = random.nextGaussian() * 0.05D + (double) vector3d.x;
            double d3 = random.nextGaussian() * 0.05D + (double) vector3d.y;
            double d4 = random.nextGaussian() * 0.05D + (double) vector3d.z;
            SmallFireballEntity fireballentity = new SmallFireballEntity(world, FriendlyTankEntity.this, d2, d3, d4);
            fireballentity.setPosition(FriendlyTankEntity.this.getPosX() + vector3d.x * 2.0D, FriendlyTankEntity.this.getPosYHeight(0.5D) + 0.25D, fireballentity.getPosZ() + vector3d.z * 2.0D);
            world.addEntity(fireballentity);
            if (!FriendlyTankEntity.this.isSilent()) {
                FriendlyTankEntity.this.world.playEvent(null, 1016, FriendlyTankEntity.this.getPosition(), 0);
            }
            if (this.attackStep >= 20) {
                this.attackTimes = 0;
                this.attackTimer = 0;
                this.attackStep = 0;
                this.FireCharging = false;
            }
        }
    }

    private void launch(Entity p_213688_1_) {
        double d0 = p_213688_1_.getPosX() - FriendlyTankEntity.this.getPosX();
        double d1 = p_213688_1_.getPosZ() - FriendlyTankEntity.this.getPosZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.addVelocity(d0 / d2 * 2.0D, 0.2D, d1 / d2 * 2.0D);
    }


    public int speed = 0;

    public ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getHeldItem(p_230254_2_);
        Item item = itemstack.getItem();
        if (this.isTamed() && !p_230254_1_.isCrouching() && item != Items.REDSTONE_BLOCK && item != Items.IRON_INGOT  && !this.isSitting()) {
            this.mountTo(p_230254_1_);
            return p_230254_1_.startRiding(this) ? ActionResultType.CONSUME : ActionResultType.PASS;
        }
        if (item != Items.IRON_INGOT) {
            if (item == Items.COMPARATOR){
                if (this.isTamed()) {return ActionResultType.PASS;}
                else {
                    if (!p_230254_1_.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                    }
                    this.setTamedBy(p_230254_1_);
                    this.navigator.clearPath();
                    this.setAttackTarget((LivingEntity)null);
                    this.func_233687_w_(true);
                    this.world.setEntityState(this, (byte)7);
                    return ActionResultType.SUCCESS;
                }
            } else {
                if (item == Items.REDSTONE_BLOCK && speed <= 8){
                    if (!p_230254_1_.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    speed = speed + 1;
                    for(int i = 0; i < 7; ++i) {
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                    }
                    this.playSound(SoundEvents.BLOCK_ANVIL_USE,1.0f,1.0f);
                    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.getBaseAttributeValue(Attributes.MOVEMENT_SPEED) + 0.025D);
                    return ActionResultType.SUCCESS;
                } else {
                    if (p_230254_1_.isCrouching() && this.isTamed()){
                        ActionResultType actionresulttype = super.func_230254_b_(p_230254_1_, p_230254_2_);
                        if (!actionresulttype.isSuccessOrConsume() || this.isOwner(p_230254_1_)) {
                            for(int i = 0; i < 7; ++i) {
                                double d0 = this.rand.nextGaussian() * 0.02D;
                                double d1 = this.rand.nextGaussian() * 0.02D;
                                double d2 = this.rand.nextGaussian() * 0.02D;
                                this.world.addParticle(ParticleTypes.POOF, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                            }
                            this.func_233687_w_(!this.isSitting());
                            this.isJumping = false;
                            this.navigator.clearPath();
                            this.setAttackTarget((LivingEntity)null);
                            return ActionResultType.SUCCESS;
                        }
                        return actionresulttype;
                    } else {
                        return ActionResultType.PASS;
                    }
                }
            }
        } else {
            float f = this.getHealth();
            this.heal(25.0F);
            if (this.getHealth() == f) {
                return ActionResultType.PASS;
            } else {
                float f1 = 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
                this.playSound(SoundEvents.ENTITY_IRON_GOLEM_REPAIR, 1.0F, f1);
                if (!p_230254_1_.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                }

                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
        }
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setRenderYawOffset(this.rotationYaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    @OnlyIn(Dist.CLIENT)
    public void applyOrientationToEntity(Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }

    class RangeAttackGoal extends Goal{
        public int attackTimer;
        public int attackTimes = 0;

        @Override
        public boolean shouldExecute() {
            if (FriendlyTankEntity.this.getAttackTarget() == null) {
                return false;
            } else if (FriendlyTankEntity.this.isSitting()){
                return false;
            } else if (FriendlyTankEntity.this.isBeingRidden()){
                return false;
            }
            else {
                return true;
            }
        }

        public void startExecuting() {
            this.attackTimer = 0;
        }

        public void tick() {
            LivingEntity livingentity = FriendlyTankEntity.this.getAttackTarget();
            assert livingentity != null;
            if (livingentity.getDistanceSq(FriendlyTankEntity.this) < 4096.0D && FriendlyTankEntity.this.canEntityBeSeen(livingentity)) {
                FriendlyTankEntity.this.getLookController().setLookPositionWithEntity(livingentity, 10.0F, 10.0F);
                ++this.attackTimer;
                World world = FriendlyTankEntity.this.world;
                if (this.attackTimes < 3) {
                    if (this.attackTimer >= 20) {
                        this.attackTimes = this.attackTimes + 1;
                        Vector3d vector3d = FriendlyTankEntity.this.getLook(1.0F);
                        double d2 = livingentity.getPosX() - (FriendlyTankEntity.this.getPosX() + vector3d.x * 2.0D);
                        double d3 = livingentity.getPosYHeight(0.5D) - (0.5D + FriendlyTankEntity.this.getPosYHeight(0.5D));
                        double d4 = livingentity.getPosZ() - (FriendlyTankEntity.this.getPosZ() + vector3d.z * 2.0D);
                        FireballEntity fireballentity = new FireballEntity(world, FriendlyTankEntity.this, d2, d3, d4);
                        fireballentity.setPosition(FriendlyTankEntity.this.getPosX() + vector3d.x * 2.0D, FriendlyTankEntity.this.getPosYHeight(0.5D) + 0.25D, fireballentity.getPosZ() + vector3d.z * 2.0D);
                        world.addEntity(fireballentity);
                        if (!FriendlyTankEntity.this.isSilent()) {
                            FriendlyTankEntity.this.world.playEvent(null, 1016, FriendlyTankEntity.this.getPosition(), 0);
                        }
                        this.attackTimer = -40;
                    }
                } else {
                    if (this.attackTimer >= 20) {
                        Vector3d vector3d = FriendlyTankEntity.this.getLook(1.0F);
                        double d2 = livingentity.getPosX() - (FriendlyTankEntity.this.getPosX() + vector3d.x * 2.0D);
                        double d3 = livingentity.getPosYHeight(0.5D) - (0.5D + FriendlyTankEntity.this.getPosYHeight(0.5D));
                        double d4 = livingentity.getPosZ() - (FriendlyTankEntity.this.getPosZ() + vector3d.z * 2.0D);
                        SmallFireballEntity smallfireballentity = new SmallFireballEntity(world, FriendlyTankEntity.this, d2, d3, d4);
                        smallfireballentity.setPosition(FriendlyTankEntity.this.getPosX() + vector3d.x * 2.0D, FriendlyTankEntity.this.getPosYHeight(0.5D) + 0.25D, smallfireballentity.getPosZ() + vector3d.z * 2.0D);
                        world.addEntity(smallfireballentity);
                        if (!FriendlyTankEntity.this.isSilent()) {
                            FriendlyTankEntity.this.world.playEvent(null, 1016, FriendlyTankEntity.this.getPosition(), 0);
                        }
                        if (this.attackTimer >= 40) {
                            this.attackTimes = 0;
                            this.attackTimer = -80;
                        }
                    }
                }
                double d0 = FriendlyTankEntity.this.getDistanceSq(livingentity);
                if (d0 > 16.0D) {
                    FriendlyTankEntity.this.getMoveHelper().setMoveTo(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), 1.0D);
                }
                for (Entity entity : FriendlyTankEntity.this.world.getEntitiesWithinAABB(LivingEntity.class, FriendlyTankEntity.this.getBoundingBox().grow(1.5D), field_213690_b)) {
                    if (!(entity instanceof PlayerEntity)) {
                        FriendlyTankEntity.this.attackEntityAsMob(entity);
                        this.launch(entity);
                    }
                }
            }
        }
            private void launch(Entity p_213688_1_) {
                double d0 = p_213688_1_.getPosX() - FriendlyTankEntity.this.getPosX();
                double d1 = p_213688_1_.getPosZ() - FriendlyTankEntity.this.getPosZ();
                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                p_213688_1_.addVelocity(d0 / d2 * 2.0D, 0.2D, d1 / d2 * 2.0D);
            }

    }

    public static enum Cracks {
        NONE(1.0F),
        LOW(0.75F),
        MEDIUM(0.5F),
        HIGH(0.25F);

        private static final List<FriendlyTankEntity.Cracks> field_226513_e_ = Stream.of(values()).sorted(Comparator.comparingDouble((p_226516_0_) -> {
            return (double)p_226516_0_.field_226514_f_;
        })).collect(ImmutableList.toImmutableList());
        private final float field_226514_f_;

        private Cracks(float p_i225732_3_) {
            this.field_226514_f_ = p_i225732_3_;
        }

        public static FriendlyTankEntity.Cracks func_226515_a_(float p_226515_0_) {
            for(FriendlyTankEntity.Cracks tankentity$cracks : field_226513_e_) {
                if (p_226515_0_ < tankentity$cracks.field_226514_f_) {
                    return tankentity$cracks;
                }
            }

            return NONE;
        }
    }

}
