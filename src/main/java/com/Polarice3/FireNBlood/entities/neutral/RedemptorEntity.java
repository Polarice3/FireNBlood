package com.Polarice3.FireNBlood.entities.neutral;

import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class RedemptorEntity extends AbstractProtectorEntity {
    protected static final DataParameter<Byte> REDEMPTOR_UPGRADES = EntityDataManager.createKey(RedemptorEntity.class, DataSerializers.BYTE);
    private boolean shielded;
    private boolean dying;
    private boolean loyal;
    private int shield;

    public RedemptorEntity(EntityType<? extends AbstractProtectorEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new AbstractProtectorEntity.SitGoal(this));
        this.goalSelector.addGoal(2, new AttackGoal(this));
        this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(4, new AbstractProtectorEntity.FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(5, new ReturnToVillageGoal(this, 0.6D, false));
        this.goalSelector.addGoal(6, new PatrolVillageGoal(this, 0.6D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
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
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 12.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 24.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (entityIn instanceof AbstractProtectorEntity){
            return this.getTeam() == null && entityIn.getTeam() == null;
        }
        return super.isOnSameTeam(entityIn);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getImmediateSource();
        Entity entity2 = source.getTrueSource();
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.isShielded()) {
            if (entity instanceof ProjectileEntity || entity2 instanceof SmallFireballEntity){
                this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 1.0F);
                --this.shield;
                if (this.shield == 0){
                    ItemStack itemstack = this.getHeldItemOffhand();
                    this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 1.0F, 1.0F);
                    itemstack.shrink(1);
                    this.setShield(false);
                }
                return false;
            } else {
                return super.attackEntityFrom(source, amount);
            }
        } else if (entity instanceof AbstractProtectorEntity && !(entity instanceof BrewerEntity)) {
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
        compound.putInt("shield", this.shield);
        compound.putBoolean("isShielded", this.shielded);
        compound.putBoolean("isDying", this.dying);
        compound.putBoolean("isLoyal", this.loyal);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.hiredTimer = compound.getInt("hiredTimer");
        this.dyingTimer = compound.getInt("dyingTimer");
        this.loyaltyPoints = compound.getInt("loyaltyPoints");
        this.shield = compound.getInt("shield");
        this.shielded = compound.getBoolean("isShielded");
        this.dying = compound.getBoolean("isDying");
        this.loyal = compound.getBoolean("isLoyal");
        this.setShield(this.shielded);
        this.setDying(this.dying);
        this.setLoyal(this.loyal);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(REDEMPTOR_UPGRADES, (byte)0);
    }

    private boolean getRedemptorUpgradesFlag(int mask) {
        int i = this.dataManager.get(REDEMPTOR_UPGRADES);
        return (i & mask) != 0;
    }

    private void setRedemptorUpgradesFlag(int mask, boolean value) {
        int i = this.dataManager.get(REDEMPTOR_UPGRADES);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(REDEMPTOR_UPGRADES, (byte)(i & 255));
    }

    public boolean isShielded(){
        return this.getRedemptorUpgradesFlag(1);
    }

    public void setShield(boolean shield){
        this.setRedemptorUpgradesFlag(1,shield);
    }

    protected boolean isMovementBlocked() {
        return this.isDying();
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractProtectorEntity.ArmPose getArmPose() {
        if (this.isAggressive()) {
            return AbstractProtectorEntity.ArmPose.ATTACKING;
        } else {
            return AbstractProtectorEntity.ArmPose.CROSSED;
        }
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData ilivingentitydata = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return ilivingentitydata;
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
    }

    public ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getHeldItem(p_230254_2_);
        Item item = itemstack.getItem();
        ItemStack itemstack2 = this.getHeldItemMainhand();
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
        } else if (item == Payment() && !this.isLoyal()) {
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
                for (int i = 0; i < 7; ++i) {
                    double d0 = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                }
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
                if (item == Items.SHIELD && !this.isShielded()){
                    if (!p_230254_1_.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.shield = ShieldDurability();
                    this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                    this.setShield(true);
                    this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.SHIELD));
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (item == Items.DIAMOND_AXE && this.getHeldItemMainhand().getItem() != Items.DIAMOND_AXE){
                    if (!p_230254_1_.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.BLOCK_ANVIL_USE, 1.0F, 1.0F);
                    this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_AXE));

                    this.entityDropItem(itemstack2);
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (item == Items.NETHERITE_AXE && this.getHeldItemMainhand().getItem() != Items.NETHERITE_AXE){
                    if (!p_230254_1_.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.BLOCK_ANVIL_USE, 1.0F, 1.0F);
                    this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.NETHERITE_AXE));
                    this.entityDropItem(itemstack2);
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (item instanceof ArmorItem){
                    ItemStack helmet = this.getItemStackFromSlot(EquipmentSlotType.HEAD);
                    ItemStack chestplate = this.getItemStackFromSlot(EquipmentSlotType.CHEST);
                    ItemStack legging = this.getItemStackFromSlot(EquipmentSlotType.LEGS);
                    ItemStack boots = this.getItemStackFromSlot(EquipmentSlotType.FEET);
                    if (!p_230254_1_.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                    if (((ArmorItem) item).getEquipmentSlot() == EquipmentSlotType.HEAD){
                        this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(item));
                        this.entityDropItem(helmet);
                    }
                    if (((ArmorItem) item).getEquipmentSlot() == EquipmentSlotType.CHEST){
                        this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(item));
                        this.entityDropItem(chestplate);
                    }
                    if (((ArmorItem) item).getEquipmentSlot() == EquipmentSlotType.LEGS){
                        this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(item));
                        this.entityDropItem(legging);
                    }
                    if (((ArmorItem) item).getEquipmentSlot() == EquipmentSlotType.FEET){
                        this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(item));
                        this.entityDropItem(boots);
                    }
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
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
                if (item == Termination() && p_230254_1_ == this.getOwner()){
                    this.remove();
                    this.setHired(false);
                    if (!p_230254_1_.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    if (!this.world.isRemote && this.world.getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES) && this.getOwner() instanceof ServerPlayerEntity) {
                        this.getOwner().sendMessage(new StringTextComponent(this.getDisplayName().getString() + " has been release from duty!"), Util.DUMMY_UUID);
                    }
                }
                this.func_233687_w_(!this.isSitting());
                this.isJumping = false;
                this.navigator.clearPath();
                this.setAttackTarget((LivingEntity)null);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VINDICATOR_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VINDICATOR_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VINDICATOR_HURT;
    }

    class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(RedemptorEntity p_i50577_2_) {
            super(p_i50577_2_, 1.0D, false);
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            if (this.attacker.getRidingEntity() instanceof SavagerEntity) {
                float f = this.attacker.getRidingEntity().getWidth() - 0.1F;
                return (double)(f * 4.0F + attackTarget.getWidth());
            } else {
                return super.getAttackReachSqr(attackTarget);
            }
        }
    }

}
