package com.Polarice3.FireNBlood.entities.neutral;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
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
import net.minecraft.world.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class AbstractProtectorEntity extends CreatureEntity {
    protected static final DataParameter<Byte> HIRED = EntityDataManager.createKey(AbstractProtectorEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> STATUS = EntityDataManager.createKey(AbstractProtectorEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(AbstractProtectorEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    public static final UUID MODIFIER_UUID = UUID.fromString("d90769a5-e164-4565-bba2-aa65f9ebd878");
    public static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "Dying", -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final Predicate<Entity> field_213690_b = Entity::isAlive;
    private static final Predicate<ItemEntity> bannerPredicate = (banner) -> {
        return !banner.cannotPickup() && banner.isAlive() && ItemStack.areItemStacksEqual(banner.getItem(), AbstractProtectorEntity.createProtectorBanner());
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

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    public static int HiredTimer(){
        return 24000;
    }

    public static int DyingTimer(){
        return 12000;
    }

    public static int SummonedTimer(){
        return 12000;
    }

    public static int ShieldDurability(){
        return 336;
    }

    public static float HealAmount(){
        return 10.0F;
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

    protected void registerData() {
        super.registerData();
        this.dataManager.register(HIRED, (byte)0);
        this.dataManager.register(STATUS, (byte)0);
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.empty());
    }

    private boolean getStatusFlag(int mask) {
        int i = this.dataManager.get(STATUS);
        return (i & mask) != 0;
    }

    private void setStatusFlag(int mask, boolean value) {
        int i = this.dataManager.get(STATUS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(STATUS, (byte)(i & 255));
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

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.getOwnerId() != null) {
            compound.putUniqueId("Owner", this.getOwnerId());
        }
        compound.putBoolean("Sitting", this.field_233683_bw_);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        UUID uuid;
        if (compound.hasUniqueId("Owner")) {
            uuid = compound.getUniqueId("Owner");
        } else {
            String s = compound.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s);
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
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.addParticle(iparticledata, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 7) {
            this.playHiredEffect(true);
        } else if (id == 6) {
            this.playHiredEffect(false);
        } else {
            super.handleStatusUpdate(id);
        }

    }

    public boolean isHired() {
        return (this.dataManager.get(HIRED) & 4) != 0;
    }

    public void setHired(boolean tamed) {
        byte b0 = this.dataManager.get(HIRED);
        if (tamed) {
            this.dataManager.set(HIRED, (byte)(b0 | 4));
        } else {
            this.dataManager.set(HIRED, (byte)(b0 & -5));
        }

        this.setupHiredAI();
    }

    protected void setupHiredAI() {
    }

    public boolean isEntitySleeping() {
        return (this.dataManager.get(HIRED) & 1) != 0;
    }

    public void setSleeping(boolean p_233686_1_) {
        byte b0 = this.dataManager.get(HIRED);
        if (p_233686_1_) {
            this.dataManager.set(HIRED, (byte)(b0 | 1));
        } else {
            this.dataManager.set(HIRED, (byte)(b0 & -2));
        }

    }

    @Nullable
    public UUID getOwnerId() {
        return this.dataManager.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public void setHiredBy(PlayerEntity player) {
        this.setHired(true);
        this.setOwnerId(player.getUniqueID());

    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.world.getPlayerByUuid(uuid);
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

    public boolean isOnSameTeam(Entity entityIn) {
        if (this.isHired()) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }

            if (livingentity != null) {
                return livingentity.isOnSameTeam(entityIn);
            }
        }
        if (this.isDying()){
            return entityIn.getTeam() == this.getTeam() && this.getTeam() == entityIn.getTeam();
        }
        return super.isOnSameTeam(entityIn);
    }

    public void onDeath(DamageSource cause) {
        if (this.isHired()) {
            this.clearActivePotions();
            this.setHealth(1.0F);
            this.getAttribute(Attributes.MAX_HEALTH).applyNonPersistentModifier(MODIFIER);
            this.func_233687_w_(true);
            this.setDying(true);
            this.setInvulnerable(true);
            if (this.getRidingEntity() != null){
                Entity entity = this.getRidingEntity();
                entity.dismount();
            }
        } else {
            super.onDeath(cause);
        }
    }

    public boolean isSitting() {
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

    public void livingTick() {
        if (this.isSummoned()){
            --this.summonedTimer;
            if (this.summonedTimer == 0){
                this.remove();
            }
        }
        if (this.isHired()){
            if (this.isDying()){
                for(int i = 0; i < 7; ++i) {
                    double d0 = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.world.addParticle(ParticleTypes.CRIT, this.getPosXRandom(1.0D), this.getPosYRandom() - 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                }
                --this.dyingTimer;
                if (this.dyingTimer == 0){
                    this.remove();
                    this.playSound(Objects.requireNonNull(this.getDeathSound()), 1.0F, 1.0F);
                    if (!this.world.isRemote && this.world.getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES) && this.getOwner() instanceof ServerPlayerEntity) {
                        this.getOwner().sendMessage(new StringTextComponent(this.getDisplayName().getString() + " couldn't stay alive any longer..."), Util.DUMMY_UUID);
                    }
                }
            } else {
                if (!this.isLoyal()){
                    --this.hiredTimer;
                }
            }
            if (this.hiredTimer == 0){
                this.remove();
                this.setHired(false);
                IParticleData iparticledata = ParticleTypes.POOF;
                for(int i = 0; i < 7; ++i) {
                    double d0 = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.world.addParticle(iparticledata, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                }
                if (!this.world.isRemote && this.world.getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES) && this.getOwner() instanceof ServerPlayerEntity) {
                    this.getOwner().sendMessage(new StringTextComponent(this.getDisplayName().getString() + "'s time with you is up!"), Util.DUMMY_UUID);
                }
            }
        }
        if (this.cooldown > 0){
            --this.cooldown;
        }
        super.livingTick();
    }

    public static ItemStack createProtectorBanner() {
        ItemStack itemstack = new ItemStack(Items.WHITE_BANNER);
        CompoundNBT compoundnbt = itemstack.getOrCreateChildTag("BlockEntityTag");
        ListNBT listnbt = (new BannerPattern.Builder()).setPatternWithColor(BannerPattern.HALF_HORIZONTAL, DyeColor.WHITE).setPatternWithColor(BannerPattern.HALF_HORIZONTAL_MIRROR, DyeColor.CYAN).setPatternWithColor(BannerPattern.TRIANGLE_TOP, DyeColor.CYAN).setPatternWithColor(BannerPattern.RHOMBUS_MIDDLE, DyeColor.WHITE).setPatternWithColor(BannerPattern.STRIPE_MIDDLE, DyeColor.WHITE).setPatternWithColor(BannerPattern.FLOWER, DyeColor.YELLOW).setPatternWithColor(BannerPattern.CIRCLE_MIDDLE, DyeColor.ORANGE).buildNBT();
        compoundnbt.put("Patterns", listnbt);
        itemstack.func_242395_a(ItemStack.TooltipDisplayFlags.ADDITIONAL);
        itemstack.setDisplayName((new StringTextComponent("Captain Banner")).mergeStyle(TextFormatting.GOLD));
        return itemstack;
    }

    protected void updateEquipmentIfNeeded(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        if (ItemStack.areItemStacksEqual(itemstack, AbstractProtectorEntity.createProtectorBanner())) {
            EquipmentSlotType equipmentslottype = EquipmentSlotType.HEAD;
            ItemStack itemstack1 = this.getItemStackFromSlot(equipmentslottype);
            double d0 = (double)this.getDropChance(equipmentslottype);
            if (!itemstack1.isEmpty() && (double)Math.max(this.rand.nextFloat() - 0.1F, 0.0F) < d0) {
                this.entityDropItem(itemstack1);
            }

            this.triggerItemPickupTrigger(itemEntity);
            this.setItemStackToSlot(equipmentslottype, itemstack);
            this.onItemPickup(itemEntity, itemstack.getCount());
            itemEntity.remove();
        } else {
            super.updateEquipmentIfNeeded(itemEntity);
        }

    }
//Classes
    public class SitGoal extends Goal {
        private final AbstractProtectorEntity hireable;

        public SitGoal(AbstractProtectorEntity entityIn) {
            this.hireable = entityIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return this.hireable.isSitting();
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (!this.hireable.isHired()) {
                return false;
            } else if (this.hireable.isInWaterOrBubbleColumn()) {
                return false;
            } else if (!this.hireable.isOnGround()) {
                return false;
            } else {
                LivingEntity livingentity = this.hireable.getOwner();
                if (livingentity == null) {
                    return true;
                } else {
                    return (!(this.hireable.getDistanceSq(livingentity) < 144.0D) || livingentity.getRevengeTarget() == null) && this.hireable.isSitting();
                }
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.hireable.getNavigator().clearPath();
            this.hireable.setSleeping(true);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            this.hireable.setSleeping(false);
        }
    }

    public class FollowOwnerGoal extends Goal {
        private final AbstractProtectorEntity hireable;
        private LivingEntity owner;
        private final IWorldReader world;
        private final double followSpeed;
        private final PathNavigator navigator;
        private int timeToRecalcPath;
        private final float maxDist;
        private final float minDist;
        private float oldWaterCost;
        private final boolean teleportToLeaves;

        public FollowOwnerGoal(AbstractProtectorEntity hireable, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
            this.hireable = hireable;
            this.world = hireable.world;
            this.followSpeed = speed;
            this.navigator = hireable.getNavigator();
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.teleportToLeaves = teleportToLeaves;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            if (!(hireable.getNavigator() instanceof GroundPathNavigator) && !(hireable.getNavigator() instanceof FlyingPathNavigator)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = this.hireable.getOwner();
            if (livingentity == null) {
                return false;
            } else if (livingentity.isSpectator()) {
                return false;
            } else if (this.hireable.isSitting()) {
                return false;
            } else if (this.hireable.getDistanceSq(livingentity) < (double)(this.minDist * this.minDist)) {
                return false;
            } else {
                this.owner = livingentity;
                return true;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            if (this.navigator.noPath()) {
                return false;
            } else if (this.hireable.isSitting()) {
                return false;
            } else {
                return !(this.hireable.getDistanceSq(this.owner) <= (double)(this.maxDist * this.maxDist));
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.hireable.getPathPriority(PathNodeType.WATER);
            this.hireable.setPathPriority(PathNodeType.WATER, 0.0F);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            this.owner = null;
            this.navigator.clearPath();
            this.hireable.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            this.hireable.getLookController().setLookPositionWithEntity(this.owner, 10.0F, (float)this.hireable.getVerticalFaceSpeed());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.hireable.getLeashed() && !this.hireable.isPassenger()) {
                    if (this.hireable.getDistanceSq(this.owner) >= 144.0D) {
                        this.tryToTeleportNearEntity();
                    } else {
                        this.navigator.tryMoveToEntityLiving(this.owner, this.followSpeed);
                    }

                }
            }
        }

        private void tryToTeleportNearEntity() {
            BlockPos blockpos = this.owner.getPosition();

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
            if (Math.abs((double)x - this.owner.getPosX()) < 2.0D && Math.abs((double)z - this.owner.getPosZ()) < 2.0D) {
                return false;
            } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
                return false;
            } else {
                this.hireable.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, this.hireable.rotationYaw, this.hireable.rotationPitch);
                this.navigator.clearPath();
                return true;
            }
        }

        private boolean isTeleportFriendlyBlock(BlockPos pos) {
            PathNodeType pathnodetype = WalkNodeProcessor.func_237231_a_(this.world, pos.toMutable());
            if (pathnodetype != PathNodeType.WALKABLE) {
                return false;
            } else {
                BlockState blockstate = this.world.getBlockState(pos.down());
                if (!this.teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos blockpos = pos.subtract(this.hireable.getPosition());
                    return this.world.hasNoCollisions(this.hireable, this.hireable.getBoundingBox().offset(blockpos));
                }
            }
        }

        private int getRandomNumber(int min, int max) {
            return this.hireable.getRNG().nextInt(max - min + 1) + min;
        }
    }

    public class OwnerHurtByTargetGoal extends TargetGoal {
        private final AbstractProtectorEntity hireable;
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtByTargetGoal(AbstractProtectorEntity theDefendingTameableIn) {
            super(theDefendingTameableIn, false);
            this.hireable = theDefendingTameableIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (this.hireable.isHired() && !this.hireable.isSitting()) {
                LivingEntity livingentity = this.hireable.getOwner();
                if (livingentity == null) {
                    return false;
                } else {
                    this.attacker = livingentity.getRevengeTarget();
                    int i = livingentity.getRevengeTimer();
                    if (this.attacker != null) {
                        return i != this.timestamp && !(this.attacker.getEntity() instanceof AbstractProtectorEntity)&& this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT) && this.hireable.shouldAttackEntity(this.attacker, livingentity);
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
        public void startExecuting() {
            this.goalOwner.setAttackTarget(this.attacker);
            LivingEntity livingentity = this.hireable.getOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getRevengeTimer();
            }

            super.startExecuting();
        }
    }

    public class OwnerHurtTargetGoal extends TargetGoal {
        private final AbstractProtectorEntity hireable;
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtTargetGoal(AbstractProtectorEntity theEntityTameableIn) {
            super(theEntityTameableIn, false);
            this.hireable = theEntityTameableIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        public boolean shouldExecute() {
            if (this.hireable.isHired() && !this.hireable.isSitting()) {
                LivingEntity livingentity = this.hireable.getOwner();
                if (livingentity == null) {
                    return false;
                } else {
                    this.attacker = livingentity.getLastAttackedEntity();
                    int i = livingentity.getLastAttackedEntityTime();
                    if (this.attacker != null) {
                        return i != this.timestamp && !(this.attacker.getEntity() instanceof AbstractProtectorEntity) && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT) && this.hireable.shouldAttackEntity(this.attacker, livingentity);
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }

        public void startExecuting() {
            this.goalOwner.setAttackTarget(this.attacker);
            LivingEntity livingentity = this.hireable.getOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getLastAttackedEntityTime();
            }

            super.startExecuting();
        }
    }

    public static class RallyGoal extends Goal {
        private final AbstractProtectorEntity protector;
        private final EntityPredicate ally = (new EntityPredicate().setDistance(8.0D));
        private int rally;

        public RallyGoal(AbstractProtectorEntity entityIn) {
            this.protector = entityIn;
            this.rally = 0;
        }

        public boolean shouldExecute() {
            ItemStack itemstack = this.protector.getItemStackFromSlot(EquipmentSlotType.HEAD);
            if (ItemStack.areItemStacksEqual(itemstack, AbstractProtectorEntity.createProtectorBanner())) {
                return this.protector.getAttackTarget() != null && this.protector.cooldown == 0;
            } else {
                return false;
            }
        }

        public void tick(){
            ++this.rally;
            if (this.rally == 60){
                this.protector.playSound(SoundEvents.EVENT_RAID_HORN, 1.0F, 1.0F);
                for (AbstractProtectorEntity allies: this.protector.world.getEntitiesWithinAABB(AbstractProtectorEntity.class, this.protector.getBoundingBox().grow(8.0D), field_213690_b)){
                    allies.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1200));
                    allies.addPotionEffect(new EffectInstance(Effects.SPEED, 1200));
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
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            if (this.protector.getAttackTarget() == null &&
                    !(this.protector.getEntity() instanceof BrewerEntity) &&
                    !ItemStack.areItemStacksEqual(this.protector.getItemStackFromSlot(EquipmentSlotType.HEAD), AbstractProtectorEntity.createProtectorBanner())) {
                List<ItemEntity> list = this.protector.world.getEntitiesWithinAABB(ItemEntity.class, this.protector.getBoundingBox().grow(16.0D, 8.0D, 16.0D), AbstractProtectorEntity.bannerPredicate);
                if (!list.isEmpty()) {
                    return this.protector.getNavigator().tryMoveToEntityLiving(list.get(0), (double)1.15F);
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
            if (this.protector.getNavigator().getTargetPos().withinDistance(this.protector.getPositionVec(), 1.414D)) {
                List<ItemEntity> list = this.protector.world.getEntitiesWithinAABB(ItemEntity.class, this.protector.getBoundingBox().grow(4.0D, 4.0D, 4.0D), AbstractProtectorEntity.bannerPredicate);
                if (!list.isEmpty()) {
                    this.protector.updateEquipmentIfNeeded(list.get(0));
                }
            }

        }
    }


}
