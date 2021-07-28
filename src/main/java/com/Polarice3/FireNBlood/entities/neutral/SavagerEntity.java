package com.Polarice3.FireNBlood.entities.neutral;

import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class SavagerEntity extends AbstractProtectorEntity implements IJumpingMount{
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof AbstractProtectorEntity);
    };
    private int attackTick;
    private int roarTick;
    private int cooldownTick;
    private boolean dying;
    private boolean loyal;
    protected boolean Jumping;
    protected float jumpPower;

    public SavagerEntity(EntityType<? extends AbstractProtectorEntity> type, World worldIn) {
        super(type, worldIn);
        this.stepHeight = 1.0F;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new AbstractProtectorEntity.SitGoal(this));
        this.goalSelector.addGoal(2, new AttackGoal());
        this.goalSelector.addGoal(4, new AbstractProtectorEntity.FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(5, new ReturnToVillageGoal(this, 0.6D, false));
        this.goalSelector.addGoal(6, new PatrolVillageGoal(this, 0.6D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new AbstractProtectorEntity.OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new AbstractProtectorEntity.OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractProtectorEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractVillagerEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractTaillessEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractRaiderEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_234199_0_) -> {
            return p_234199_0_ instanceof IMob && !(p_234199_0_ instanceof CreeperEntity);
        }));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 100.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 12.0D)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 3.0D);
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (entityIn instanceof AbstractProtectorEntity){
            return this.getTeam() == null && entityIn.getTeam() == null;
        }
        return super.isOnSameTeam(entityIn);
    }

    protected void updateMovementGoalFlags() {
        boolean flag = !(this.getControllingPassenger() instanceof MobEntity) || this.getControllingPassenger() instanceof AbstractProtectorEntity;
        boolean flag1 = !(this.getRidingEntity() instanceof BoatEntity);
        this.goalSelector.setFlag(Goal.Flag.MOVE, flag);
        this.goalSelector.setFlag(Goal.Flag.JUMP, flag && flag1);
        this.goalSelector.setFlag(Goal.Flag.LOOK, flag);
        this.goalSelector.setFlag(Goal.Flag.TARGET, flag);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getTrueSource();
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (entity instanceof AbstractProtectorEntity && !(entity instanceof BrewerEntity)) {
            return false;
        } else if (entity == this.getRidingEntity()){
            return false;
        } else if (this.isDying()){
            return false;
        } else if (this.isHired() && entity == this.getOwner()){
            --this.loyaltyPoints;
            return super.attackEntityFrom(source, amount);
        } else {
            return super.attackEntityFrom(source, amount);
        }
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("hiredTimer", this.hiredTimer);
        compound.putInt("dyingTimer", this.dyingTimer);
        compound.putInt("loyaltyPoints", this.loyaltyPoints);
        compound.putInt("AttackTick", this.attackTick);
        compound.putInt("RoarTick", this.roarTick);
        compound.putInt("CooldownTick", this.cooldownTick);
        compound.putBoolean("isDying", this.dying);
        compound.putBoolean("isLoyal", this.loyal);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.hiredTimer = compound.getInt("hiredTimer");
        this.dyingTimer = compound.getInt("dyingTimer");
        this.loyaltyPoints = compound.getInt("loyaltyPoints");
        this.attackTick = compound.getInt("AttackTick");
        this.roarTick = compound.getInt("RoarTick");
        this.cooldownTick = compound.getInt("CooldownTick");
        this.dying = compound.getBoolean("isDying");
        this.loyal = compound.getBoolean("isLoyal");
        this.setDying(this.dying);
        this.setLoyal(this.loyal);
    }

    protected PathNavigator createNavigator(World worldIn) {
        return new SavagerEntity.Navigator(this, worldIn);
    }

    public int getHorizontalFaceSpeed() {
        return 45;
    }

    public double getMountedYOffset() {
        if (this.getRidingEntity() instanceof PlayerEntity){
            return 2.5D;
        } else {
            return 2.0D;
        }
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData ilivingentitydata = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return ilivingentitydata;
    }

    public boolean canBeSteered() {
        return !this.isAIDisabled() && this.getControllingPassenger() instanceof LivingEntity;
    }

    public boolean isSavagerJumping() {
        return this.Jumping;
    }

    public void setSavagerJumping(boolean jumping) {
        this.Jumping = jumping;
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    private final EntityPredicate mobs = (new EntityPredicate().setDistance(8.0D));

    public void livingTick() {
        if (this.isAlive()) {
            double d0 = this.getAttackTarget() != null ? 0.35D : 0.3D;
            double d1 = this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(MathHelper.lerp(0.1D, d1, d0));

            if (this.collidedHorizontally && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
                boolean flag = false;
                AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(0.2D);

                for(BlockPos blockpos : BlockPos.getAllInBoxMutable(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ))) {
                    BlockState blockstate = this.world.getBlockState(blockpos);
                    Block block = blockstate.getBlock();
                    if (block instanceof LeavesBlock) {
                        flag = this.world.destroyBlock(blockpos, true, this) || flag;
                    }
                }

                if (!flag && this.onGround) {
                    this.jump();
                }
            }

            if (this.roarTick > 0) {
                --this.roarTick;
                if (this.roarTick == 10) {
                    this.roar();
                }
            }

            if (this.attackTick > 0) {
                --this.attackTick;
            }

            if (this.cooldownTick > 0){
                --this.cooldownTick;
            }

            List<MonsterEntity> mobs = this.world.getTargettableEntitiesWithinAABB(MonsterEntity.class, this.mobs, this, this.getBoundingBox().grow(4.0D, 4.0D, 4.0D));
            if (!mobs.isEmpty() && mobs.size() >= 4 && !this.isDying()) {
                if (this.cooldownTick <= 0) {
                    this.playSound(SoundEvents.ENTITY_RAVAGER_ROAR, 1.0F, 1.0F);
                    this.cooldownTick = 300;
                    this.roarTick = 20;
                }
            }
        }
        super.livingTick();
    }

    public ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getHeldItem(p_230254_2_);
        Item item = itemstack.getItem();
        if (this.isDying()){
            if (item == Revive()){
                if (!p_230254_1_.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
                if (!this.isLoyal()){
                    ++this.loyaltyPoints;
                }
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
                this.func_233687_w_(false);
                this.setDying(false);
                this.getAttribute(Attributes.MAX_HEALTH).removeModifier(MODIFIER);
                this.heal(HealAmount());
                this.addPotionEffect(new EffectInstance(Effects.REGENERATION, 120));
                this.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 120));
                this.dyingTimer = DyingTimer();
                this.setInvulnerable(false);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        } else if (item == Payment() && !this.isLoyal() && !this.isBeingRidden()) {
            if (!this.isHired()) {
                if (!p_230254_1_.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
                if (p_230254_1_.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty()){
                    p_230254_1_.setItemStackToSlot(EquipmentSlotType.HEAD, AbstractProtectorEntity.createProtectorBanner());
                }
                if (this.isSummoned()){
                    this.setSummoned(false);
                }
                this.setHiredBy(p_230254_1_);
                this.hiredTimer = AbstractProtectorEntity.HiredTimer();
                this.dyingTimer = DyingTimer();
                this.navigator.clearPath();
                this.setAttackTarget((LivingEntity) null);
                this.func_233687_w_(true);
                this.world.setEntityState(this, (byte) 7);
                return ActionResultType.SUCCESS;
            } else {
                if (!p_230254_1_.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
                this.hiredTimer = this.hiredTimer + 24000;
                ++this.loyaltyPoints;
                if (this.loyaltyPoints >= 10){
                    this.setLoyal(true);
                    this.playSound(SoundEvents.ENTITY_VINDICATOR_CELEBRATE, 1.0F, 1.0F);
                }
                for (int i = 0; i < 7; ++i) {
                    double d0 = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                }
                return ActionResultType.SUCCESS;
            }
        } else {
            if (this.isHired() && !this.isDying()){
                if (item == Items.MILK_BUCKET){
                    if (!p_230254_1_.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                        p_230254_1_.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BUCKET));
                    }
                    this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);
                    this.clearActivePotions();
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        this.world.addParticle(ParticleTypes.POOF, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (item == Food() && this.getHealth() < this.getMaxHealth()){
                    if (!p_230254_1_.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
                    this.heal(HealAmount());
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        this.world.addParticle(ParticleTypes.HEART, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (!p_230254_1_.isCrouching() && p_230254_1_.getHeldItemMainhand().getItem() == Items.AIR){
                    this.mountTo(p_230254_1_);
                    this.func_233687_w_(false);
                } else {
                    this.func_233687_w_(!this.isSitting());
                }
                this.isJumping = false;
                this.navigator.clearPath();
                this.setAttackTarget((LivingEntity) null);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        }
    }

    protected boolean isMovementBlocked() {
        return super.isMovementBlocked() || this.attackTick > 0 || this.roarTick > 0 || this.isDying();
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
            if (this.isBeingRidden() && this.canBeSteered() && this.getControllingPassenger() instanceof PlayerEntity) {
                LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();
                assert livingentity != null;
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

                if (this.jumpPower > 0.0F && !this.isSavagerJumping() && this.onGround) {
                    double d0 = 0.7F * (double)this.jumpPower * (double)this.getJumpFactor();
                    double d1;
                    if (this.isPotionActive(Effects.JUMP_BOOST)) {
                        d1 = d0 + (double)((float)(this.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
                    } else {
                        d1 = d0;
                    }

                    Vector3d vector3d = this.getMotion();
                    this.setMotion(vector3d.x, d1, vector3d.z);
                    this.setSavagerJumping(true);
                    this.isAirBorne = true;
                    net.minecraftforge.common.ForgeHooks.onLivingJump(this);
                    if (f1 > 0.0F) {
                        float f2 = MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F));
                        float f3 = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F));
                        this.setMotion(this.getMotion().add((double)(-0.4F * f2 * this.jumpPower), 0.0D, (double)(0.4F * f3 * this.jumpPower)));
                    }

                    this.jumpPower = 0.0F;
                }

                this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
                if (this.canPassengerSteer()) {
                    this.setAIMoveSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    super.travel(new Vector3d((double) f, travelVector.y, (double) f1));
                } else if (livingentity instanceof PlayerEntity) {
                    this.setMotion(Vector3d.ZERO);
                }

                if (this.onGround) {
                    this.jumpPower = 0.0F;
                    this.setSavagerJumping(false);
                }

                this.func_233629_a_(this, false);
            } else {
                this.jumpMovementFactor = 0.02F;
                super.travel(travelVector);
            }
        }
    }

    @Override
    public void setJumpPower(int jumpPowerIn) {
        if (this.isBeingRidden()) {
            if (jumpPowerIn < 0) {
                jumpPowerIn = 0;
            }
            if (jumpPowerIn >= 90) {
                this.jumpPower = 1.0F;
            } else {
                this.jumpPower = 0.4F + 0.4F * (float)jumpPowerIn / 90.0F;
            }

        }
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int jumpPower) {

    }

    @Override
    public void handleStopJump() {

    }

    class AttackGoal extends MeleeAttackGoal {

        public AttackGoal() {
            super(SavagerEntity.this, 1.0F, true);
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            float f = SavagerEntity.this.getWidth() - 0.5F;
            return (double)(f * 4.0F + attackTarget.getWidth());
        }

    }

    public boolean canEntityBeSeen(Entity entityIn) {
        return this.roarTick <= 0 && super.canEntityBeSeen(entityIn);
    }

    protected void constructKnockBackVector(LivingEntity entityIn) {
        if (this.roarTick == 0) {
            if (this.rand.nextDouble() < 0.5D) {
                this.launch(entityIn);
            }

            entityIn.velocityChanged = true;
        }

    }

    private void roar() {
        if (this.isAlive()) {
            for(Entity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(4.0D), field_213690_b)) {
                if (!(entity == this.getOwner())) {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this), 6.0F);
                    this.launch(entity);
                }

            }

            Vector3d vector3d = this.getBoundingBox().getCenter();

            for(int i = 0; i < 40; ++i) {
                double d0 = this.rand.nextGaussian() * 0.2D;
                double d1 = this.rand.nextGaussian() * 0.2D;
                double d2 = this.rand.nextGaussian() * 0.2D;
                this.world.addParticle(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
            }
        }

    }

    private void launch(Entity p_213688_1_) {
        double d0 = p_213688_1_.getPosX() - this.getPosX();
        double d1 = p_213688_1_.getPosZ() - this.getPosZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.addVelocity(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 4) {
            this.attackTick = 10;
            this.playSound(SoundEvents.ENTITY_RAVAGER_ATTACK, 1.0F, 1.0F);
        }
        super.handleStatusUpdate(id);
    }

    @OnlyIn(Dist.CLIENT)
    public int func_213683_l() {
        return this.attackTick;
    }

    @OnlyIn(Dist.CLIENT)
    public int func_213687_eg() {
        return this.roarTick;
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        this.attackTick = 10;
        this.world.setEntityState(this, (byte)4);
        this.playSound(SoundEvents.ENTITY_RAVAGER_ATTACK, 1.0F, 1.0F);
        return super.attackEntityAsMob(entityIn);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_RAVAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_RAVAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_RAVAGER_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_RAVAGER_STEP, 0.15F, 1.0F);
    }

    public boolean isNotColliding(IWorldReader worldIn) {
        return !worldIn.containsAnyLiquid(this.getBoundingBox());
    }

    static class Navigator extends GroundPathNavigator {
        public Navigator(MobEntity p_i50754_1_, World p_i50754_2_) {
            super(p_i50754_1_, p_i50754_2_);
        }

        protected PathFinder getPathFinder(int p_179679_1_) {
            this.nodeProcessor = new SavagerEntity.Processor();
            return new PathFinder(this.nodeProcessor, p_179679_1_);
        }
    }

    static class Processor extends WalkNodeProcessor {
        private Processor() {
        }

        protected PathNodeType func_215744_a(IBlockReader p_215744_1_, boolean p_215744_2_, boolean p_215744_3_, BlockPos p_215744_4_, PathNodeType p_215744_5_) {
            return p_215744_5_ == PathNodeType.LEAVES ? PathNodeType.OPEN : super.func_215744_a(p_215744_1_, p_215744_2_, p_215744_3_, p_215744_4_, p_215744_5_);
        }
    }

}
