package com.Polarice3.FireNBlood.entities.neutral.protectors;

import com.Polarice3.FireNBlood.entities.ally.SummonedEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
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
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
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
        this.maxUpStep = 1.0F;
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
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractProtectorEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractVillagerEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractTaillessEntity.class, true, !this.isSleeping()));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractRaiderEntity.class, true, !this.isSleeping()));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_234199_0_) -> {
            return p_234199_0_ instanceof IMob && !(p_234199_0_ instanceof CreeperEntity) && !(p_234199_0_ instanceof SummonedEntity) && !this.isSleeping();
        }));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 3.0D);
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn instanceof AbstractProtectorEntity){
            return this.getTeam() == null && entityIn.getTeam() == null;
        }
        return super.isAlliedTo(entityIn);
    }

    protected void updateControlFlags() {
        boolean flag = !(this.getControllingPassenger() instanceof MobEntity) || this.getControllingPassenger() instanceof AbstractProtectorEntity;
        boolean flag1 = !(this.getVehicle() instanceof BoatEntity);
        this.goalSelector.setControlFlag(Goal.Flag.MOVE, flag);
        this.goalSelector.setControlFlag(Goal.Flag.JUMP, flag && flag1);
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, flag);
        this.goalSelector.setControlFlag(Goal.Flag.TARGET, flag);
    }

    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (entity instanceof AbstractProtectorEntity && !(entity instanceof BrewerEntity)) {
            return false;
        } else if (entity == this.getVehicle()){
            return false;
        } else if (this.isDying()){
            return false;
        } else if (this.isHired() && entity == this.getOwner()){
            --this.loyaltyPoints;
            return super.hurt(source, amount);
        } else {
            return super.hurt(source, amount);
        }
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("hiredTimer", this.hiredTimer);
        compound.putInt("dyingTimer", this.dyingTimer);
        compound.putInt("loyaltyPoints", this.loyaltyPoints);
        compound.putInt("AttackTick", this.attackTick);
        compound.putInt("RoarTick", this.roarTick);
        compound.putInt("CooldownTick", this.cooldownTick);
        compound.putBoolean("isDying", this.dying);
        compound.putBoolean("isLoyal", this.loyal);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
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

    protected PathNavigator createNavigation(World worldIn) {
        return new SavagerEntity.Navigator(this, worldIn);
    }

    public int getMaxHeadYRot() {
        return 45;
    }

    public double getPassengersRidingOffset() {
        if (this.getVehicle() instanceof PlayerEntity){
            return 2.5D;
        } else {
            return 2.0D;
        }
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData ilivingentitydata = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return ilivingentitydata;
    }

    public boolean canBeSteered() {
        return !this.isNoAi() && this.getControllingPassenger() instanceof LivingEntity;
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

    private final EntityPredicate mobs = (new EntityPredicate().range(8.0D));

    public void aiStep() {
        if (this.isAlive()) {
            double d0 = this.getTarget() != null ? 0.35D : 0.3D;
            double d1 = this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(MathHelper.lerp(0.1D, d1, d0));

            if (this.horizontalCollision && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                boolean flag = false;
                AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(0.2D);

                for(BlockPos blockpos : BlockPos.betweenClosed(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ))) {
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    Block block = blockstate.getBlock();
                    if (block instanceof LeavesBlock) {
                        flag = this.level.destroyBlock(blockpos, true, this) || flag;
                    }
                }

                if (!flag && this.onGround) {
                    this.jumpFromGround();
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

            List<MonsterEntity> mobs = this.level.getNearbyEntities(MonsterEntity.class, this.mobs, this, this.getBoundingBox().inflate(4.0D, 4.0D, 4.0D));
            if (!mobs.isEmpty() && mobs.size() >= 4 && !this.isDying()) {
                if (this.cooldownTick <= 0) {
                    this.playSound(SoundEvents.RAVAGER_ROAR, 1.0F, 1.0F);
                    this.cooldownTick = 300;
                    this.roarTick = 20;
                }
            }
        }
        super.aiStep();
    }

    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);
        Item item = itemstack.getItem();
        if (this.isDying()){
            if (item == Revive()){
                if (!p_230254_1_.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                if (!this.isLoyal()){
                    ++this.loyaltyPoints;
                }
                this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                this.func_233687_w_(false);
                this.setDying(false);
                this.getAttribute(Attributes.MAX_HEALTH).removeModifier(MODIFIER);
                this.heal(HealAmount());
                this.addEffect(new EffectInstance(Effects.REGENERATION, 120));
                this.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 120));
                this.dyingTimer = DyingTimer();
                this.setInvulnerable(false);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        } else if (item == Payment() && !this.isLoyal() && !this.isVehicle()) {
            if (!this.isHired()) {
                if (!p_230254_1_.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                if (p_230254_1_.getItemBySlot(EquipmentSlotType.HEAD).isEmpty()){
                    p_230254_1_.setItemSlot(EquipmentSlotType.HEAD, AbstractProtectorEntity.createProtectorBanner());
                }
                if (this.isSummoned()){
                    this.setSummoned(false);
                }
                this.setHiredBy(p_230254_1_);
                this.hiredTimer = AbstractProtectorEntity.HiredTimer();
                this.dyingTimer = DyingTimer();
                this.navigation.stop();
                this.setTarget((LivingEntity) null);
                this.func_233687_w_(true);
                this.level.broadcastEntityEvent(this, (byte) 7);
                for (int i = 0; i < 7; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                }
                return ActionResultType.SUCCESS;
            } else {
                if (!p_230254_1_.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                this.hiredTimer = this.hiredTimer + 24000;
                ++this.loyaltyPoints;
                if (this.loyaltyPoints >= 10){
                    this.setLoyal(true);
                    this.playSound(SoundEvents.VINDICATOR_CELEBRATE, 1.0F, 1.0F);
                }
                for (int i = 0; i < 7; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                }
                return ActionResultType.SUCCESS;
            }
        } else {
            if (this.isHired() && !this.isDying()){
                if (item == Items.MILK_BUCKET){
                    if (!p_230254_1_.abilities.instabuild) {
                        itemstack.shrink(1);
                        p_230254_1_.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BUCKET));
                    }
                    this.playSound(SoundEvents.GENERIC_DRINK, 1.0F, 1.0F);
                    this.removeAllEffects();
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (item == Food() && this.getHealth() < this.getMaxHealth()){
                    if (!p_230254_1_.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                    this.heal(HealAmount());
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (item == Termination() && p_230254_1_ == this.getOwner()){
                    this.remove();
                    this.setHired(false);
                    if (!p_230254_1_.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayerEntity) {
                        this.getOwner().sendMessage(new StringTextComponent(this.getDisplayName().getString() + " has been release from duty!"), Util.NIL_UUID);
                    }
                }
                if (!p_230254_1_.isCrouching() && p_230254_1_.getMainHandItem().getItem() == Items.AIR){
                    this.mountTo(p_230254_1_);
                    this.func_233687_w_(false);
                } else {
                    this.func_233687_w_(!this.riding());
                }
                this.jumping = false;
                this.navigation.stop();
                this.setTarget((LivingEntity) null);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        }
    }

    protected boolean isImmobile() {
        return super.isImmobile() || this.attackTick > 0 || this.roarTick > 0 || this.isDying();
    }

    protected void mountTo(PlayerEntity player) {
        if (!this.level.isClientSide) {
            player.yRot = this.yRot;
            player.xRot = this.xRot;
            player.startRiding(this);
        }

    }

    public void travel(Vector3d travelVector) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeSteered() && this.getControllingPassenger() instanceof PlayerEntity) {
                LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();
                assert livingentity != null;
                this.yRot = livingentity.yRot;
                this.yRotO = this.yRot;
                this.xRot = livingentity.xRot * 0.5F;
                this.setRot(this.yRot, this.xRot);
                this.yBodyRot = this.yRot;
                this.yHeadRot = this.yBodyRot;
                float f = livingentity.xxa * 0.5F;
                float f1 = livingentity.zza;
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                }

                if (this.jumpPower > 0.0F && !this.isSavagerJumping() && this.onGround) {
                    double d0 = 0.7F * (double)this.jumpPower * (double)this.getJumpPower();
                    double d1;
                    if (this.hasEffect(Effects.JUMP)) {
                        d1 = d0 + (double)((float)(this.getEffect(Effects.JUMP).getAmplifier() + 1) * 0.1F);
                    } else {
                        d1 = d0;
                    }

                    Vector3d vector3d = this.getDeltaMovement();
                    this.setDeltaMovement(vector3d.x, d1, vector3d.z);
                    this.setSavagerJumping(true);
                    this.hasImpulse = true;
                    net.minecraftforge.common.ForgeHooks.onLivingJump(this);
                    if (f1 > 0.0F) {
                        float f2 = MathHelper.sin(this.yRot * ((float)Math.PI / 180F));
                        float f3 = MathHelper.cos(this.yRot * ((float)Math.PI / 180F));
                        this.setDeltaMovement(this.getDeltaMovement().add((double)(-0.4F * f2 * this.jumpPower), 0.0D, (double)(0.4F * f3 * this.jumpPower)));
                    }

                    this.jumpPower = 0.0F;
                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    super.travel(new Vector3d((double) f, travelVector.y, (double) f1));
                } else if (livingentity instanceof PlayerEntity) {
                    this.setDeltaMovement(Vector3d.ZERO);
                }

                if (this.onGround) {
                    this.jumpPower = 0.0F;
                    this.setSavagerJumping(false);
                }

                this.calculateEntityAnimation(this, false);
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(travelVector);
            }
        }
    }

    @Override
    public void onPlayerJump(int jumpPowerIn) {
        if (this.isVehicle()) {
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
            float f = SavagerEntity.this.getBbWidth() - 0.5F;
            return (double)(f * 4.0F + attackTarget.getBbWidth());
        }

    }

    public boolean canSee(Entity entityIn) {
        return this.roarTick <= 0 && super.canSee(entityIn);
    }

    protected void blockedByShield(LivingEntity entityIn) {
        if (this.roarTick == 0) {
            if (this.random.nextDouble() < 0.5D) {
                this.launch(entityIn);
            }

            entityIn.hurtMarked = true;
        }

    }

    private void roar() {
        if (this.isAlive()) {
            for(Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D), field_213690_b)) {
                if (!(entity == this.getOwner())) {
                    entity.hurt(DamageSource.mobAttack(this), 6.0F);
                    this.launch(entity);
                }

            }

            Vector3d vector3d = this.getBoundingBox().getCenter();

            for(int i = 0; i < 40; ++i) {
                double d0 = this.random.nextGaussian() * 0.2D;
                double d1 = this.random.nextGaussian() * 0.2D;
                double d2 = this.random.nextGaussian() * 0.2D;
                this.level.addParticle(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
            }
        }

    }

    private void launch(Entity p_213688_1_) {
        double d0 = p_213688_1_.getX() - this.getX();
        double d1 = p_213688_1_.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.push(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attackTick = 10;
            this.playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
        }
        super.handleEntityEvent(id);
    }

    @OnlyIn(Dist.CLIENT)
    public int func_213683_l() {
        return this.attackTick;
    }

    @OnlyIn(Dist.CLIENT)
    public int func_213687_eg() {
        return this.roarTick;
    }

    public boolean doHurtTarget(Entity entityIn) {
        this.attackTick = 10;
        this.level.broadcastEntityEvent(this, (byte)4);
        this.playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
        return super.doHurtTarget(entityIn);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.RAVAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.RAVAGER_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.RAVAGER_STEP, 0.15F, 1.0F);
    }

    public boolean checkSpawnObstruction(IWorldReader levelIn) {
        return !levelIn.containsAnyLiquid(this.getBoundingBox());
    }

    static class Navigator extends GroundPathNavigator {
        public Navigator(MobEntity p_i50754_1_, World p_i50754_2_) {
            super(p_i50754_1_, p_i50754_2_);
        }

        protected PathFinder createPathFinder(int p_179679_1_) {
            this.nodeEvaluator = new SavagerEntity.Processor();
            return new PathFinder(this.nodeEvaluator, p_179679_1_);
        }
    }

    static class Processor extends WalkNodeProcessor {
        private Processor() {
        }

        protected PathNodeType evaluateBlockPathType(IBlockReader worldIn, boolean canOpenDoors, boolean canEnterDoors, BlockPos pos, PathNodeType nodeType) {
            return nodeType == PathNodeType.LEAVES ? PathNodeType.OPEN : super.evaluateBlockPathType(worldIn, canOpenDoors, canEnterDoors, pos, nodeType);
        }
    }

}
