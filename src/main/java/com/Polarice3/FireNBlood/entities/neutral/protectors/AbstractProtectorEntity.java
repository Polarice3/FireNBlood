package com.Polarice3.FireNBlood.entities.neutral.protectors;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.entities.ally.FriendlyTankEntity;
import com.Polarice3.FireNBlood.entities.ally.FriendlyVexEntity;
import com.Polarice3.FireNBlood.entities.ally.SummonedEntity;
import com.Polarice3.FireNBlood.entities.utilities.FakeSeatEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class AbstractProtectorEntity extends CreatureEntity {
    protected static final DataParameter<Byte> HIRED = EntityDataManager.defineId(AbstractProtectorEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> STATUS = EntityDataManager.defineId(AbstractProtectorEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.defineId(AbstractProtectorEntity.class, DataSerializers.OPTIONAL_UUID);
    public static final UUID MODIFIER_UUID = UUID.fromString("d90769a5-e164-4565-bba2-aa65f9ebd878");
    public static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "Dying", -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final Predicate<Entity> field_213690_b = Entity::isAlive;
    private static final Predicate<ItemEntity> bannerPredicate = (banner) -> {
        return !banner.hasPickUpDelay() && banner.isAlive() && ItemStack.matches(banner.getItem(), AbstractProtectorEntity.createProtectorBanner());
    };
    private boolean field_233683_bw_;
    public int cooldown;
    public int hiredTimer;
    public int dyingTimer;
    public int summonedTimer;
    public int loyaltyPoints;

    protected AbstractProtectorEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.cooldown = 0;
        this.setupHiredAI();
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public static int HiredTimer(){
        return FNBConfig.HiredTimer.get();
    }

    public static int DyingTimer(){
        return FNBConfig.DyingTimer.get();
    }

    public static int SummonedTimer(){
        return 12000;
    }

    public static int ShieldDurability(){
        return 336;
    }

    public static float HealAmount(){
        return FNBConfig.HealAmount.get();
    }

    public static Item Payment(){
        return Items.EMERALD_BLOCK;
    }

    public static Item Food(){
        return Items.BREAD;
    }

    public static Item Revive(){
        return Items.GOLDEN_APPLE;
    }

    public static Item Termination(){
        return RegistryHandler.LEAVEFORM.get();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HIRED, (byte)0);
        this.entityData.define(STATUS, (byte)0);
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
    }

    private boolean getStatusFlag(int mask) {
        int i = this.entityData.get(STATUS);
        return (i & mask) != 0;
    }

    private void setStatusFlag(int mask, boolean value) {
        int i = this.entityData.get(STATUS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(STATUS, (byte)(i & 255));
    }

    public boolean isDying(){
        return this.getStatusFlag(1);
    }

    public void setDying(boolean dying){
        this.setStatusFlag(1,dying);
    }

    public boolean isLoyal(){
        return this.getStatusFlag(2);
    }

    public void setLoyal(boolean loyal){
        this.setStatusFlag(2,loyal);
    }

    public boolean isSummoned(){
        return this.getStatusFlag(3);
    }

    public void setSummoned(boolean summoned){
        this.setStatusFlag(3, summoned);
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerId() != null) {
            compound.putUUID("Owner", this.getOwnerId());
        }
        compound.putBoolean("Sitting", this.field_233683_bw_);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else {
            String s = compound.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
                this.setHired(true);
            } catch (Throwable throwable) {
                this.setHired(false);
            }
        }

        this.field_233683_bw_ = compound.getBoolean("Sitting");
        this.setSleeping(this.field_233683_bw_);
    }

    @OnlyIn(Dist.CLIENT)
    protected void playHiredEffect(boolean play) {
        IParticleData iparticledata = ParticleTypes.HAPPY_VILLAGER;
        if (!play) {
            iparticledata = ParticleTypes.SMOKE;
        }

        for(int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(iparticledata, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            this.playHiredEffect(true);
        } else if (id == 6) {
            this.playHiredEffect(false);
        } else {
            super.handleEntityEvent(id);
        }

    }

    public boolean isHired() {
        return (this.entityData.get(HIRED) & 4) != 0;
    }

    public void setHired(boolean tamed) {
        byte b0 = this.entityData.get(HIRED);
        if (tamed) {
            this.entityData.set(HIRED, (byte)(b0 | 4));
        } else {
            this.entityData.set(HIRED, (byte)(b0 & -5));
        }

        this.setupHiredAI();
    }

    protected void setupHiredAI() {
    }

    public boolean isEntitySleeping() {
        return (this.entityData.get(HIRED) & 1) != 0;
    }

    public void setSleeping(boolean p_233686_1_) {
        byte b0 = this.entityData.get(HIRED);
        if (p_233686_1_) {
            this.entityData.set(HIRED, (byte)(b0 | 1));
        } else {
            this.entityData.set(HIRED, (byte)(b0 & -2));
        }

    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public void setHiredBy(PlayerEntity player) {
        this.setHired(true);
        this.setOwnerId(player.getUUID());

    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public boolean canAttack(LivingEntity target) {
        return !this.isOwner(target) && super.canAttack(target);
    }

    public boolean isOwner(LivingEntity entityIn) {
        return entityIn == this.getOwner();
    }

    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        return true;
    }

    public Team getTeam() {
        if (this.isHired()) {
            LivingEntity livingentity = this.getOwner();
            if (livingentity != null) {
                return livingentity.getTeam();
            }
        }

        return super.getTeam();
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (this.isHired()) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }

            if (livingentity != null) {
                return livingentity.isAlliedTo(entityIn);
            }
        }
        if (this.isDying()){
            return entityIn.getTeam() == this.getTeam() && this.getTeam() == entityIn.getTeam();
        }
        if (entityIn instanceof FriendlyVexEntity && ((FriendlyVexEntity) entityIn).getTrueOwner() == this.getOwner()){
            return true;
        }
        if (entityIn instanceof SummonedEntity && ((SummonedEntity) entityIn).getTrueOwner() == this.getOwner()){
            return true;
        }
        if (entityIn instanceof FriendlyTankEntity && ((FriendlyTankEntity) entityIn).getOwner() == this.getOwner()){
            return true;
        }
        return super.isAlliedTo(entityIn);
    }

    public void die(DamageSource cause) {
        if (this.isHired()) {
            this.removeAllEffects();
            this.setHealth(1.0F);
            this.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(MODIFIER);
            this.func_233687_w_(true);
            this.setDying(true);
            this.setInvulnerable(true);
            if (this.getVehicle() != null){
                Entity entity = this.getVehicle();
                entity.stopRiding();
            }
        } else {
            super.die(cause);
        }
    }

    public boolean riding() {
        return this.field_233683_bw_;
    }

    public void func_233687_w_(boolean p_233687_1_) {
        this.field_233683_bw_ = p_233687_1_;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new PromoteLeaderGoal<>(this));
        this.goalSelector.addGoal(2, new RallyGoal(this));
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractProtectorEntity.ArmPose getArmPose() {
        return AbstractProtectorEntity.ArmPose.CROSSED;
    }

    @OnlyIn(Dist.CLIENT)
    public static enum ArmPose {
        CROSSED,
        ATTACKING,
        SPELLCASTING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        NEUTRAL;
    }

    public void aiStep() {
        if (this.isSummoned()){
            --this.summonedTimer;
            if (this.summonedTimer == 0){
                this.remove();
            }
        }
        if (this.isHired()){
            if (this.isDying()){
                for(int i = 0; i < 7; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(ParticleTypes.CRIT, this.getRandomX(1.0D), this.getRandomY() - 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                }
                --this.dyingTimer;
                if (this.dyingTimer == 0){
                    this.remove();
                    this.playSound(Objects.requireNonNull(this.getDeathSound()), 1.0F, 1.0F);
                    if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayerEntity) {
                        this.getOwner().sendMessage(new StringTextComponent(this.getDisplayName().getString() + " couldn't stay alive any longer..."), Util.NIL_UUID);
                    }
                }
            } else {
                if (!this.isLoyal()){
                    --this.hiredTimer;
                }
            }
            if (this.hiredTimer == 5){
                IParticleData iparticledata = ParticleTypes.POOF;
                for(int i = 0; i < 7; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(iparticledata, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                }
            }
            if (this.hiredTimer == 0){
                this.remove();
                this.setHired(false);
                if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayerEntity) {
                    this.getOwner().sendMessage(new StringTextComponent(this.getDisplayName().getString() + "'s time with you is up!"), Util.NIL_UUID);
                }
            }
        }
        if (this.cooldown > 0){
            --this.cooldown;
        }
        super.aiStep();
    }

    public static ItemStack createProtectorBanner() {
        ItemStack itemstack = new ItemStack(Items.WHITE_BANNER);
        CompoundNBT compoundnbt = itemstack.getOrCreateTagElement("BlockEntityTag");
        ListNBT listnbt = (new BannerPattern.Builder()).addPattern(BannerPattern.HALF_HORIZONTAL, DyeColor.WHITE).addPattern(BannerPattern.HALF_HORIZONTAL_MIRROR, DyeColor.CYAN).addPattern(BannerPattern.TRIANGLE_TOP, DyeColor.CYAN).addPattern(BannerPattern.RHOMBUS_MIDDLE, DyeColor.WHITE).addPattern(BannerPattern.STRIPE_MIDDLE, DyeColor.WHITE).addPattern(BannerPattern.FLOWER, DyeColor.YELLOW).addPattern(BannerPattern.CIRCLE_MIDDLE, DyeColor.ORANGE).toListTag();
        compoundnbt.put("Patterns", listnbt);
        itemstack.hideTooltipPart(ItemStack.TooltipDisplayFlags.ADDITIONAL);
        itemstack.setHoverName((new StringTextComponent("Captain Banner")).withStyle(TextFormatting.GOLD));
        return itemstack;
    }

    protected void pickUpItem(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        if (ItemStack.matches(itemstack, AbstractProtectorEntity.createProtectorBanner())) {
            EquipmentSlotType equipmentslottype = EquipmentSlotType.HEAD;
            ItemStack itemstack1 = this.getItemBySlot(equipmentslottype);
            double d0 = (double)this.getEquipmentDropChance(equipmentslottype);
            if (!itemstack1.isEmpty() && (double)Math.max(this.random.nextFloat() - 0.1F, 0.0F) < d0) {
                this.spawnAtLocation(itemstack1);
            }

            this.onItemPickup(itemEntity);
            this.setItemSlot(equipmentslottype, itemstack);
            this.take(itemEntity, itemstack.getCount());
            itemEntity.remove();
        } else {
            super.pickUpItem(itemEntity);
        }

    }
//Classes
    public class SitGoal extends Goal {
        private final AbstractProtectorEntity hireable;

        public SitGoal(AbstractProtectorEntity entityIn) {
            this.hireable = entityIn;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return this.hireable.riding();
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (!this.hireable.isHired()) {
                return false;
            } else if (this.hireable.isInWaterOrBubble()) {
                return false;
            } else if (!this.hireable.isOnGround()) {
                return false;
            } else {
                LivingEntity livingentity = this.hireable.getOwner();
                if (livingentity == null) {
                    return true;
                } else {
                    return (!(this.hireable.distanceToSqr(livingentity) < 144.0D) || livingentity.getLastHurtByMob() == null) && this.hireable.riding();
                }
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            if (this.hireable instanceof SavagerEntity) {
                this.hireable.getNavigation().stop();
                this.hireable.setSleeping(true);
            } else {
                FakeSeatEntity fakeseat = new FakeSeatEntity(ModEntityType.FAKESEAT.get(), level);
                fakeseat.moveTo(this.hireable.getX(), this.hireable.getY(), this.hireable.getZ(), this.hireable.yRot, this.hireable.xRot);
                level.addFreshEntity(fakeseat);
                this.hireable.startRiding(fakeseat);
            }
            this.hireable.getNavigation().stop();
            this.hireable.setSleeping(true);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            if (this.hireable instanceof SavagerEntity) {
                this.hireable.setSleeping(false);
            } else {
                this.hireable.stopRiding();
            }
            this.hireable.setSleeping(false);
        }
    }

    public class FollowOwnerGoal extends Goal {
        private final AbstractProtectorEntity hireable;
        private LivingEntity owner;
        private final IWorldReader level;
        private final double followSpeed;
        private final PathNavigator navigation;
        private int timeToRecalcPath;
        private final float maxDist;
        private final float minDist;
        private float oldWaterCost;
        private final boolean teleportToLeaves;

        public FollowOwnerGoal(AbstractProtectorEntity hireable, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
            this.hireable = hireable;
            this.level = hireable.level;
            this.followSpeed = speed;
            this.navigation = hireable.getNavigation();
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.teleportToLeaves = teleportToLeaves;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            if (!(hireable.getNavigation() instanceof GroundPathNavigator) && !(hireable.getNavigation() instanceof FlyingPathNavigator)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = this.hireable.getOwner();
            if (livingentity == null) {
                return false;
            } else if (livingentity.isSpectator()) {
                return false;
            } else if (this.hireable.riding()) {
                return false;
            } else if (this.hireable.distanceToSqr(livingentity) < (double)(this.minDist * this.minDist)) {
                return false;
            } else {
                this.owner = livingentity;
                return true;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            if (this.navigation.isDone()) {
                return false;
            } else if (this.hireable.riding()) {
                return false;
            } else {
                return !(this.hireable.distanceToSqr(this.owner) <= (double)(this.maxDist * this.maxDist));
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.hireable.getPathfindingMalus(PathNodeType.WATER);
            this.hireable.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.hireable.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            this.hireable.getLookControl().setLookAt(this.owner, 10.0F, (float)this.hireable.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.hireable.isLeashed() && !this.hireable.isPassenger()) {
                    if (this.hireable.distanceToSqr(this.owner) >= 144.0D) {
                        this.tryToTeleportNearEntity();
                    } else {
                        this.navigation.moveTo(this.owner, this.followSpeed);
                    }

                }
            }
        }

        private void tryToTeleportNearEntity() {
            BlockPos blockpos = this.owner.blockPosition();

            for(int i = 0; i < 10; ++i) {
                int j = this.getRandomNumber(-3, 3);
                int k = this.getRandomNumber(-1, 1);
                int l = this.getRandomNumber(-3, 3);
                boolean flag = this.tryToTeleportToLocation(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
                if (flag) {
                    return;
                }
            }

        }

        private boolean tryToTeleportToLocation(int x, int y, int z) {
            if (Math.abs((double)x - this.owner.getX()) < 2.0D && Math.abs((double)z - this.owner.getZ()) < 2.0D) {
                return false;
            } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
                return false;
            } else {
                this.hireable.moveTo((double)x + 0.5D, (double)y, (double)z + 0.5D, this.hireable.yRot, this.hireable.xRot);
                this.navigation.stop();
                return true;
            }
        }

        private boolean isTeleportFriendlyBlock(BlockPos pos) {
            PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(this.level, pos.mutable());
            if (pathnodetype != PathNodeType.WALKABLE) {
                return false;
            } else {
                BlockState blockstate = this.level.getBlockState(pos.below());
                if (!this.teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos blockpos = pos.subtract(this.hireable.blockPosition());
                    return this.level.noCollision(this.hireable, this.hireable.getBoundingBox().move(blockpos));
                }
            }
        }

        private int getRandomNumber(int min, int max) {
            return this.hireable.getRandom().nextInt(max - min + 1) + min;
        }
    }

    public class OwnerHurtByTargetGoal extends TargetGoal {
        private final AbstractProtectorEntity hireable;
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtByTargetGoal(AbstractProtectorEntity theDefendingTameableIn) {
            super(theDefendingTameableIn, false);
            this.hireable = theDefendingTameableIn;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (this.hireable.isHired() && !this.hireable.riding()) {
                LivingEntity livingentity = this.hireable.getOwner();
                if (livingentity == null) {
                    return false;
                } else {
                    this.attacker = livingentity.getLastHurtByMob();
                    int i = livingentity.getLastHurtByMobTimestamp();
                    if (this.attacker != null) {
                        return i != this.timestamp && !(this.attacker.getEntity() instanceof AbstractProtectorEntity)&& this.canAttack(this.attacker, EntityPredicate.DEFAULT) && this.hireable.shouldAttackEntity(this.attacker, livingentity);
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.mob.setTarget(this.attacker);
            LivingEntity livingentity = this.hireable.getOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getLastHurtByMobTimestamp();
            }

            super.start();
        }
    }

    public class OwnerHurtTargetGoal extends TargetGoal {
        private final AbstractProtectorEntity hireable;
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtTargetGoal(AbstractProtectorEntity theEntityTameableIn) {
            super(theEntityTameableIn, false);
            this.hireable = theEntityTameableIn;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        public boolean canUse() {
            if (this.hireable.isHired() && !this.hireable.riding()) {
                LivingEntity livingentity = this.hireable.getOwner();
                if (livingentity == null) {
                    return false;
                } else {
                    this.attacker = livingentity.getLastHurtMob();
                    int i = livingentity.getLastHurtMobTimestamp();
                    if (this.attacker != null) {
                        return i != this.timestamp && !(this.attacker.getEntity() instanceof AbstractProtectorEntity) && this.canAttack(this.attacker, EntityPredicate.DEFAULT) && this.hireable.shouldAttackEntity(this.attacker, livingentity);
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }

        public void start() {
            this.mob.setTarget(this.attacker);
            LivingEntity livingentity = this.hireable.getOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getLastHurtMobTimestamp();
            }

            super.start();
        }
    }

    public static class RallyGoal extends Goal {
        private final AbstractProtectorEntity protector;
        private final EntityPredicate ally = (new EntityPredicate().range(8.0D));
        private int rally;

        public RallyGoal(AbstractProtectorEntity entityIn) {
            this.protector = entityIn;
            this.rally = 0;
        }

        public boolean canUse() {
            ItemStack itemstack = this.protector.getItemBySlot(EquipmentSlotType.HEAD);
            if (ItemStack.matches(itemstack, AbstractProtectorEntity.createProtectorBanner())) {
                return this.protector.getTarget() != null && this.protector.cooldown == 0;
            } else {
                return false;
            }
        }

        public void tick(){
            ++this.rally;
            if (this.rally == 60){
                this.protector.playSound(SoundEvents.RAID_HORN, 1.0F, 1.0F);
                for (AbstractProtectorEntity allies: this.protector.level.getEntitiesOfClass(AbstractProtectorEntity.class, this.protector.getBoundingBox().inflate(8.0D), field_213690_b)){
                    allies.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 1200));
                    allies.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 1200));
                }
                this.rally = 0;
                this.protector.cooldown = 12000;
            }
        }


    }

    public static class PromoteLeaderGoal<T extends AbstractProtectorEntity> extends Goal {
        private final T protector;

        public PromoteLeaderGoal(T protector) {
            this.protector = protector;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (this.protector.getTarget() == null &&
                    !(this.protector.getEntity() instanceof BrewerEntity) &&
                    !ItemStack.matches(this.protector.getItemBySlot(EquipmentSlotType.HEAD), AbstractProtectorEntity.createProtectorBanner())) {
                List<ItemEntity> list = this.protector.level.getEntitiesOfClass(ItemEntity.class, this.protector.getBoundingBox().inflate(16.0D, 8.0D, 16.0D), AbstractProtectorEntity.bannerPredicate);
                if (!list.isEmpty()) {
                    return this.protector.getNavigation().moveTo(list.get(0), (double)1.15F);
                }
                return false;
            } else {
                return false;
            }
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (this.protector.getNavigation().getTargetPos().closerThan(this.protector.position(), 1.414D)) {
                List<ItemEntity> list = this.protector.level.getEntitiesOfClass(ItemEntity.class, this.protector.getBoundingBox().inflate(4.0D, 4.0D, 4.0D), AbstractProtectorEntity.bannerPredicate);
                if (!list.isEmpty()) {
                    this.protector.pickUpItem(list.get(0));
                }
            }

        }
    }


}
