package com.Polarice3.FireNBlood.entities.neutral.protectors;

import com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal;
import com.Polarice3.FireNBlood.entities.ally.SummonedEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.block.BlockState;
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
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class HexerEntity extends SpellcastingProtectorEntity implements ICrossbowUser {
    private static final DataParameter<Boolean> DATA_CHARGING_STATE = EntityDataManager.defineId(HexerEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Byte> HEXER_FLAGS = EntityDataManager.defineId(HexerEntity.class, DataSerializers.BYTE);
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof HexerEntity);
    };
    private final Inventory inventory = new Inventory(5);
    private SheepEntity wololoTarget;
    private boolean shielded;
    private boolean weakness;
    private boolean dying;
    private boolean loyal;
    private int shield;
    private int despawnDelay;
    private boolean wandering;
    private RavagerEntity RavagerTarget;

    public HexerEntity(EntityType<? extends SpellcastingProtectorEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new HexerEntity.CastingSpellGoal());
        this.goalSelector.addGoal(1, new AbstractProtectorEntity.SitGoal(this));
        this.goalSelector.addGoal(2, new ProtectorCrossbowGoal<>(this, 1.0F, 8.0F));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(4, new HexerEntity.DeRavageGoal());
        this.goalSelector.addGoal(5, new HexerEntity.SummonSpellGoal());
        this.goalSelector.addGoal(6, new HexerEntity.AttackSpellGoal());
        this.goalSelector.addGoal(7, new HexerEntity.WololoSpellGoal());
        this.goalSelector.addGoal(8, new AbstractProtectorEntity.FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(9, new ReturnToVillageGoal(this, 0.6D, false));
        this.goalSelector.addGoal(10, new PatrolVillageGoal(this, 0.6D));
        this.goalSelector.addGoal(11, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
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

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CHARGING_STATE, false);
        this.entityData.define(HEXER_FLAGS, (byte)0);
    }

    @Nullable
    private RavagerEntity getRavagerTarget() {
        return this.RavagerTarget;
    }

    private void setRavagerTarget(@Nullable RavagerEntity RavagerTargetIn) {
        this.RavagerTarget = RavagerTargetIn;
    }

    private boolean getHexerFlag(int mask){
        int i = this.entityData.get(HEXER_FLAGS);
        return (i & mask) != 0;
    }

    private void setHexerFlag(int mask, boolean value) {
        int i = this.entityData.get(HEXER_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(HEXER_FLAGS, (byte)(i & 255));
    }
    public boolean isShielded(){
        return this.getHexerFlag(1);
    }

    public void setShield(boolean shield){
        this.setHexerFlag(1,shield);
    }

    public boolean isWeakness(){
        return this.getHexerFlag(5);
    }

    public void setWeakness(boolean weakness){
        this.setHexerFlag(5,weakness);
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
        compound.putInt("DespawnDelay", this.despawnDelay);
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
        compound.putBoolean("isWanderer", this.wandering);
        compound.putBoolean("isShielded", this.shielded);
        compound.putBoolean("isWeakness", this.weakness);
        compound.putBoolean("isDying", this.dying);
        compound.putBoolean("isLoyal", this.loyal);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        ListNBT listnbt = compound.getList("Inventory", 10);
        if (compound.contains("DespawnDelay", 99)) {
            this.despawnDelay = compound.getInt("DespawnDelay");
        }

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
        this.wandering = compound.getBoolean("isWanderer");
        this.setShield(this.shielded);
        this.setDying(this.dying);
        this.setLoyal(this.loyal);
    }

    public void setDespawnDelay(int delay) {
        this.despawnDelay = delay;
    }

    public boolean isWanderer(){
        return this.getHexerFlag(9);
    }

    public void setWanderer(boolean wanderer){
        this.setHexerFlag(9, wanderer);
    }

    public int getDespawnDelay() {
        return this.despawnDelay;
    }

    private void handleDespawn() {
        if (this.despawnDelay > 0 && --this.despawnDelay == 0) {
            this.remove();
        }

    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide && !this.isHired() && this.isWanderer()) {
            this.handleDespawn();
        }

    }

    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getDirectEntity();
        Entity entity2 = source.getEntity();
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.isShielded() && !this.isSpellcasting()) {
            if (entity instanceof ProjectileEntity || entity2 instanceof SmallFireballEntity){
                this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 1.0F);
                --this.shield;
                if (this.shield == 0){
                    ItemStack itemstack = this.getOffhandItem();
                    this.playSound(SoundEvents.SHIELD_BREAK, 1.0F, 1.0F);
                    itemstack.shrink(1);
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

    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.EVOKER_HURT;
    }

    protected SoundEvent getCastingSoundEvent () {
        return SoundEvents.EVOKER_CAST_SPELL;
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

    private void setWololoTarget(@Nullable SheepEntity wololoTargetIn) {
        this.wololoTarget = wololoTargetIn;
    }

    @Nullable
    private SheepEntity getWololoTarget() {
        return this.wololoTarget;
    }

    protected boolean isImmobile() {
        return this.isDying();
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData ilivingentitydata = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return ilivingentitydata;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        int random = this.level.random.nextInt(2);
        if (random == 1) {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        } else {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
        }
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

    @OnlyIn(Dist.CLIENT)
    public AbstractProtectorEntity.ArmPose getArmPose() {
        if (this.isCharging()) {
            return AbstractProtectorEntity.ArmPose.CROSSBOW_CHARGE;
        } else if (this.isAggressive() && this.getMainHandItem().getItem() instanceof TieredItem) {
            return AbstractProtectorEntity.ArmPose.ATTACKING;
        } else if (this.isSpellcasting()){
            return ArmPose.SPELLCASTING;
        } else {
            return this.getMainHandItem().getItem() == Items.CROSSBOW ? ArmPose.CROSSBOW_HOLD : ArmPose.CROSSED;
        }
    }

    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);
        Item item = itemstack.getItem();
        ItemStack itemstack2 = this.getMainHandItem();
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
        } else if (item == Payment() && !this.isLoyal()) {
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
                    this.playSound(SoundEvents.EVOKER_CELEBRATE, 1.0F, 1.0F);
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
                    this.setItemSlot(EquipmentSlotType.OFFHAND, itemstack.copy());
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (item instanceof SwordItem){
                    if (!p_230254_1_.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.ANVIL_USE, 1.0F, 1.0F);
                    this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack.copy());
                    this.spawnAtLocation(itemstack2);
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (item == Items.CROSSBOW && this.getMainHandItem().getItem() != Items.CROSSBOW){
                    if (!p_230254_1_.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.ANVIL_USE, 1.0F, 1.0F);
                    this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack.copy());
                    this.spawnAtLocation(itemstack2);
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
                this.func_233687_w_(!this.riding());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget((LivingEntity)null);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        }
    }

    class SummonSpellGoal extends SpellcastingProtectorEntity.UseSpellGoal {
        private final EntityPredicate field_220843_e = (new EntityPredicate()).range(16.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

        private SummonSpellGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else if (HexerEntity.this.isDying()){
                return false;
            } else {
                int i = HexerEntity.this.level.getNearbyEntities(MirageEntity.class, this.field_220843_e, HexerEntity.this, HexerEntity.this.getBoundingBox().inflate(16.0D)).size();
                return 2 > i;
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 340;
        }

        protected void castSpell() {

            for(int i = 0; i < 2; ++i) {
                BlockPos blockpos = HexerEntity.this.blockPosition().offset(-2 + HexerEntity.this.random.nextInt(5), 0, -2 + HexerEntity.this.random.nextInt(5));
                MirageEntity mirageEntity = new MirageEntity(ModEntityType.MIRAGE.get(), HexerEntity.this.level);
                mirageEntity.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                mirageEntity.setOwner(HexerEntity.this);
                mirageEntity.setLimitedLife(10 * (15 + HexerEntity.this.random.nextInt(45)));
                ((ServerWorld) HexerEntity.this.level).addFreshEntityWithPassengers(mirageEntity);
            }

        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected SpellcastingProtectorEntity.SpellType getSpellType() {
            return SpellType.SUMMON_HELP;
        }
    }

    class AttackSpellGoal extends SpellcastingProtectorEntity.UseSpellGoal {
        private AttackSpellGoal() {
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 200;
        }

        protected void castSpell() {
            LivingEntity livingentity = HexerEntity.this.getTarget();
            double d0 = Math.min(livingentity.getY(), HexerEntity.this.getY());
            double d1 = Math.max(livingentity.getY(), HexerEntity.this.getY()) + 1.0D;
            float f = (float) MathHelper.atan2(livingentity.getZ() - HexerEntity.this.getZ(), livingentity.getX() - HexerEntity.this.getX());
            if (HexerEntity.this.distanceToSqr(livingentity) < 9.0D) {
                for(int i = 0; i < 5; ++i) {
                    float f1 = f + (float)i * (float)Math.PI * 0.4F;
                    this.spawnFangs(HexerEntity.this.getX() + (double)MathHelper.cos(f1) * 1.5D, HexerEntity.this.getZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
                }

                for(int k = 0; k < 8; ++k) {
                    float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
                    this.spawnFangs(HexerEntity.this.getX() + (double)MathHelper.cos(f2) * 2.5D, HexerEntity.this.getZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
                }
            } else {
                for(int l = 0; l < 16; ++l) {
                    double d2 = 1.25D * (double)(l + 1);
                    int j = 1 * l;
                    this.spawnFangs(HexerEntity.this.getX() + (double)MathHelper.cos(f) * d2, HexerEntity.this.getZ() + (double)MathHelper.sin(f) * d2, d0, d1, f, j);
                }
            }

        }

        private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = HexerEntity.this.level.getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(HexerEntity.this.level, blockpos1, Direction.UP)) {
                    if (!HexerEntity.this.level.isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = HexerEntity.this.level.getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(HexerEntity.this.level, blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.max(Direction.Axis.Y);
                        }
                    }

                    flag = true;
                    break;
                }

                blockpos = blockpos.below();
            } while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

            if (flag) {
                HexerEntity.this.level.addFreshEntity(new EvokerFangsEntity(HexerEntity.this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, HexerEntity.this));
            }

        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected SpellcastingProtectorEntity.SpellType getSpellType() {
            return SpellType.SPIKES;
        }
    }

    class CastingSpellGoal extends SpellcastingProtectorEntity.CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (HexerEntity.this.getTarget() != null) {
                HexerEntity.this.getLookControl().setLookAt(HexerEntity.this.getTarget(), (float)HexerEntity.this.getMaxHeadYRot(), (float)HexerEntity.this.getMaxHeadXRot());
            }

        }
    }

    class DeRavageGoal extends SpellcastingProtectorEntity.UseSpellGoal{
        private final EntityPredicate ravager = (new EntityPredicate().range(32.0D).allowInvulnerable());

        public boolean canUse() {
            if (HexerEntity.this.isDying()) {
                return false;
            } else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(HexerEntity.this.level, HexerEntity.this)) {
                return false;
            } else {
                List<RavagerEntity> list = HexerEntity.this.level.getNearbyEntities(RavagerEntity.class, this.ravager, HexerEntity.this, HexerEntity.this.getBoundingBox().inflate(32.0D, 4.0D, 32.0D));
                if (list.isEmpty()) {
                    return false;
                }
                else {
                    HexerEntity.this.setRavagerTarget(list.get(HexerEntity.this.random.nextInt(list.size())));
                    return true;
                }
            }
        }

        public boolean canContinueToUse() {
            return HexerEntity.this.getRavagerTarget() != null &&
                    HexerEntity.this.getRavagerTarget().hasEffect(Effects.WEAKNESS) &&
                    this.spellWarmup > 0;
        }

        public void stop() {
            super.stop();
            HexerEntity.this.setRavagerTarget(null);
        }

        protected void castSpell() {
            RavagerEntity ravager = HexerEntity.this.getRavagerTarget();
            if (ravager != null && ravager.isAlive()) {
                SavagerEntity savager = new SavagerEntity(ModEntityType.SAVAGER.get(), level);
                savager.moveTo(ravager.getX(), ravager.getY(), ravager.getZ(), ravager.yRot, ravager.xRot);
                savager.finalizeSpawn((IServerWorld) level, level.getCurrentDifficultyAt(ravager.blockPosition()), SpawnReason.CONVERSION, null, null);
                savager.setNoAi(ravager.isNoAi());
                savager.setPersistenceRequired();
                savager.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
                level.addFreshEntity(savager);
                ravager.remove();
            }
        }

        protected int getCastingTime() {
            return 200;
        }

        protected int getCastingInterval() {
            return 1000;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_CAST_SPELL;
        }

        protected SpellcastingProtectorEntity.SpellType getSpellType() {
            return SpellcastingProtectorEntity.SpellType.WOLOLO;
        }
    }

    public class WololoSpellGoal extends SpellcastingProtectorEntity.UseSpellGoal {
        private final EntityPredicate wololoTargetFlags = (new EntityPredicate()).range(16.0D).allowInvulnerable().selector((p_220844_0_) -> {
            return ((SheepEntity)p_220844_0_).getColor() == DyeColor.RED;
        });

        public boolean canUse() {
            if (HexerEntity.this.getTarget() != null) {
                return false;
            } else if (HexerEntity.this.isSpellcasting()) {
                return false;
            } else if (HexerEntity.this.tickCount < this.spellCooldown) {
                return false;
            } else if (HexerEntity.this.isDying()) {
                return false;
            } else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(HexerEntity.this.level, HexerEntity.this)) {
                return false;
            } else {
                List<SheepEntity> list = HexerEntity.this.level.getNearbyEntities(SheepEntity.class, this.wololoTargetFlags, HexerEntity.this, HexerEntity.this.getBoundingBox().inflate(16.0D, 4.0D, 16.0D));
                if (list.isEmpty()) {
                    return false;
                } else {
                    HexerEntity.this.setWololoTarget(list.get(HexerEntity.this.random.nextInt(list.size())));
                    return true;
                }
            }
        }

        public boolean canContinueToUse() {
            return HexerEntity.this.getWololoTarget() != null && this.spellWarmup > 0;
        }

        public void stop() {
            super.stop();
            HexerEntity.this.setWololoTarget((SheepEntity)null);
        }

        protected void castSpell() {
            SheepEntity sheepentity = HexerEntity.this.getWololoTarget();
            if (sheepentity != null && sheepentity.isAlive()) {
                sheepentity.setColor(DyeColor.BLUE);
            }

        }

        protected int getCastWarmupTime() {
            return 40;
        }

        protected int getCastingTime() {
            return 60;
        }

        protected int getCastingInterval() {
            return 140;
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_WOLOLO;
        }

        protected SpellcastingProtectorEntity.SpellType getSpellType() {
            return SpellcastingProtectorEntity.SpellType.WOLOLO;
        }
    }



}
