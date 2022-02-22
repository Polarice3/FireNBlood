package com.Polarice3.FireNBlood.entities.neutral.protectors;

import com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal;
import com.Polarice3.FireNBlood.entities.ally.SummonedEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Predicate;

public class ProtectorEntity extends AbstractProtectorEntity implements ICrossbowUser {
    private static final DataParameter<Boolean> DATA_CHARGING_STATE = EntityDataManager.defineId(ProtectorEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Byte> PROTECTOR_UPGRADES = EntityDataManager.defineId(ProtectorEntity.class, DataSerializers.BYTE);
    private final Inventory inventory = new Inventory(5);
    private boolean shielded;
    private boolean weakness;
    private boolean dying;
    private boolean loyal;
    private int shield;

    public ProtectorEntity(EntityType<? extends AbstractProtectorEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new AbstractProtectorEntity.SitGoal(this));
        this.goalSelector.addGoal(2, new ProtectorCrossbowGoal<>(this, 1.0F, 8.0F));
        this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(4, new AbstractProtectorEntity.FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(5, new ReturnToVillageGoal(this, 0.6D, false));
        this.goalSelector.addGoal(6, new PatrolVillageGoal(this, 0.6D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 15.0F));
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
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn instanceof AbstractProtectorEntity){
            return this.getTeam() == null && entityIn.getTeam() == null;
        }
        return super.isAlliedTo(entityIn);
    }

    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getDirectEntity();
        Entity entity2 = source.getEntity();
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.isShielded()) {
            if (entity instanceof ProjectileEntity || entity2 instanceof SmallFireballEntity){
                this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 1.0F);
                --this.shield;
                if (this.shield == 0){
                    this.playSound(SoundEvents.SHIELD_BREAK, 1.0F, 1.0F);
                    this.setShield(false);
                }
                return false;
            } else {
                return super.hurt(source, amount);
            }
        } else if (entity instanceof AbstractProtectorEntity && !(entity instanceof BrewerEntity)) {
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

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CHARGING_STATE, false);
        this.entityData.define(PROTECTOR_UPGRADES, (byte)0);
    }

    private boolean getProtectorUpgradesFlag(int mask) {
        int i = this.entityData.get(PROTECTOR_UPGRADES);
        return (i & mask) != 0;
    }

    private void setProtectorUpgradesFlag(int mask, boolean value) {
        int i = this.entityData.get(PROTECTOR_UPGRADES);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(PROTECTOR_UPGRADES, (byte)(i & 255));
    }

    public boolean isShielded(){
        return this.getProtectorUpgradesFlag(1);
    }

    public void setShield(boolean shield){
        this.setProtectorUpgradesFlag(1,shield);
    }

    public boolean isWeakness(){
        return this.getProtectorUpgradesFlag(5);
    }

    public void setWeakness(boolean weakness){
        this.setProtectorUpgradesFlag(5,weakness);
    }

    public boolean canFireProjectileWeapon(ShootableItem p_230280_1_) {
        return p_230280_1_ == Items.CROSSBOW;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isCharging() {
        return this.entityData.get(DATA_CHARGING_STATE);
    }

    public void setChargingCrossbow(boolean isCharging) {
        this.entityData.set(DATA_CHARGING_STATE, isCharging);
    }

    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        ListNBT listnbt = new ListNBT();

        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                listnbt.add(itemstack.save(new CompoundNBT()));
            }
        }
        compound.putInt("hiredTimer", this.hiredTimer);
        compound.putInt("dyingTimer", this.dyingTimer);
        compound.putInt("loyaltyPoints", this.loyaltyPoints);
        compound.putInt("shield", this.shield);
        compound.putBoolean("isShielded", this.shielded);
        compound.putBoolean("isWeakness", this.weakness);
        compound.putBoolean("isDying", this.dying);
        compound.putBoolean("isLoyal", this.loyal);
        compound.put("Inventory", listnbt);
    }

    protected boolean isImmobile() {
        return this.isDying();
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractProtectorEntity.ArmPose getArmPose() {
        if (this.isCharging()) {
            return AbstractProtectorEntity.ArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(Items.CROSSBOW)) {
            return AbstractProtectorEntity.ArmPose.CROSSBOW_HOLD;
        } else {
            return this.isAggressive() ? AbstractProtectorEntity.ArmPose.ATTACKING : AbstractProtectorEntity.ArmPose.NEUTRAL;
        }
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        ListNBT listnbt = compound.getList("Inventory", 10);

        for(int i = 0; i < listnbt.size(); ++i) {
            ItemStack itemstack = ItemStack.of(listnbt.getCompound(i));
            if (!itemstack.isEmpty()) {
                this.inventory.addItem(itemstack);
            }
        }
        this.hiredTimer = compound.getInt("hiredTimer");
        this.dyingTimer = compound.getInt("dyingTimer");
        this.loyaltyPoints = compound.getInt("loyaltyPoints");
        this.shield = compound.getInt("shield");
        this.shielded = compound.getBoolean("isShielded");
        this.weakness = compound.getBoolean("isWeakness");
        this.dying = compound.getBoolean("isDying");
        this.loyal = compound.getBoolean("isLoyal");
        this.setShield(this.shielded);
        this.setDying(this.dying);
        this.setLoyal(this.loyal);
        this.setCanPickUpLoot(true);
    }

    public float getWalkTargetValue(BlockPos pos, IWorldReader levelIn) {
        BlockState blockstate = levelIn.getBlockState(pos.below());
        return !blockstate.is(Blocks.GRASS_BLOCK) && !blockstate.is(Blocks.SAND) ? 0.5F - levelIn.getBrightness(pos) : 10.0F;
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
    }

    protected void enchantSpawnedWeapon(float p_241844_1_) {
        super.enchantSpawnedWeapon(p_241844_1_);
        if (this.random.nextInt(300) == 0) {
            ItemStack itemstack = this.getMainHandItem();
            if (itemstack.getItem() == Items.CROSSBOW) {
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack);
                map.putIfAbsent(Enchantments.PIERCING, 1);
                EnchantmentHelper.setEnchantments(map, itemstack);
                this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
            }
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.PILLAGER_HURT;
    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        this.performCrossbowAttack(this, 1.6F);
    }

    public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) {
        this.shootCrossbowProjectile(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
    }

    public boolean setSlot(int inventorySlot, ItemStack itemStackIn) {
        if (super.setSlot(inventorySlot, itemStackIn)) {
            return true;
        } else {
            int i = inventorySlot - 300;
            if (i >= 0 && i < this.inventory.getContainerSize()) {
                this.inventory.setItem(i, itemStackIn);
                return true;
            } else {
                return false;
            }
        }
    }

    public ItemStack getProjectile(ItemStack shootable) {
        if (shootable.getItem() instanceof ShootableItem) {
            Predicate<ItemStack> predicate = ((ShootableItem)shootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ShootableItem.getHeldProjectile(this, predicate);
            if (this.isWeakness()){
                ItemStack itemstack1 = new ItemStack(Items.TIPPED_ARROW, 1);
                PotionUtils.setPotion(itemstack1, Potions.WEAKNESS);
                return itemstack.isEmpty() ? itemstack1 : itemstack;
            } else {
                return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);
        Item item = itemstack.getItem();
        Item paying;
        if (p_230254_1_.hasEffect(Effects.HERO_OF_THE_VILLAGE)){
            paying = DiscountPayment();
        } else {
            paying = Payment();
        }
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
        } else if (item == paying && !this.isLoyal()) {
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
                ++this.loyaltyPoints;
                if (this.loyaltyPoints >= 10){
                    this.setLoyal(true);
                    this.playSound(SoundEvents.PILLAGER_CELEBRATE, 1.0F, 1.0F);
                }
                this.hiredTimer = this.hiredTimer + 24000;
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
                if (item == Items.SHIELD && !this.isShielded()){
                    if (!p_230254_1_.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    this.shield = item.getDamage(itemstack);
                    this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                    this.setShield(true);
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (item instanceof ArmorItem){
                    ItemStack helmet = this.getItemBySlot(EquipmentSlotType.HEAD);
                    ItemStack chestplate = this.getItemBySlot(EquipmentSlotType.CHEST);
                    ItemStack legging = this.getItemBySlot(EquipmentSlotType.LEGS);
                    ItemStack boots = this.getItemBySlot(EquipmentSlotType.FEET);
                    if (!p_230254_1_.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                    if (((ArmorItem) item).getSlot() == EquipmentSlotType.HEAD){
                        this.setItemSlot(EquipmentSlotType.HEAD, itemstack.copy());
                        this.spawnAtLocation(helmet);
                    }
                    if (((ArmorItem) item).getSlot() == EquipmentSlotType.CHEST){
                        this.setItemSlot(EquipmentSlotType.CHEST, itemstack.copy());
                        this.spawnAtLocation(chestplate);
                    }
                    if (((ArmorItem) item).getSlot() == EquipmentSlotType.LEGS){
                        this.setItemSlot(EquipmentSlotType.LEGS, itemstack.copy());
                        this.spawnAtLocation(legging);
                    }
                    if (((ArmorItem) item).getSlot() == EquipmentSlotType.FEET){
                        this.setItemSlot(EquipmentSlotType.FEET, itemstack.copy());
                        this.spawnAtLocation(boots);
                    }
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (item == Items.FERMENTED_SPIDER_EYE && !this.isWeakness()){
                    if (!p_230254_1_.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.BREWING_STAND_BREW, 1.0F, 1.0F);
                    this.setWeakness(true);
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
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
                this.func_233687_w_(!this.isSitting());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget((LivingEntity)null);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        }
    }



}
