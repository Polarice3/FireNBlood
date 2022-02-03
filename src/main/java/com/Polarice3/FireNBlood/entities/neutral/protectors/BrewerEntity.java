package com.Polarice3.FireNBlood.entities.neutral.protectors;

import com.Polarice3.FireNBlood.entities.ally.SummonedEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.projectiles.WitchBombEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.*;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class BrewerEntity extends AbstractProtectorEntity implements IRangedAttackMob {
    private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, AttributeModifier.Operation.ADDITION);
    private static final DataParameter<Boolean> IS_DRINKING = EntityDataManager.defineId(BrewerEntity.class, DataSerializers.BOOLEAN);
    private int potionUseTimer;
    private NearestAttackableTargetHealingGoal<PlayerEntity> field_213694_A;
    private NearestAttackableTargetHealingGoal<AbstractProtectorEntity> field_213694_B;
    private NearestAttackableTargetHealingGoal<AbstractVillagerEntity> field_213694_C;
    private ToggleableAttackableTargetGoal<AbstractTaillessEntity> field_213695_D0;
    private ToggleableAttackableTargetGoal<AbstractRaiderEntity> field_213695_D1;
    private ToggleableAttackableTargetGoal<MobEntity> field_213695_D2;
    private boolean dying;
    private boolean loyal;

    public BrewerEntity(EntityType<? extends AbstractProtectorEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.field_213694_A = new NearestAttackableTargetHealingGoal<>(this, PlayerEntity.class, true, (p_213693_1_) -> {
            if (this.isHired()){
                LivingEntity employer = this.getOwner();
                return p_213693_1_ != null && employer == p_213693_1_ && employer.getHealth() < employer.getMaxHealth();
            } else {
                return false;
            }
        });
        this.field_213694_B = new NearestAttackableTargetHealingGoal<>(this, AbstractProtectorEntity.class, true, (p_213693_1_) -> p_213693_1_ != null && p_213693_1_.getType() != ModEntityType.BREWER.get() && p_213693_1_.getHealth() < p_213693_1_.getMaxHealth());
        this.field_213694_C = new NearestAttackableTargetHealingGoal<>(this, AbstractVillagerEntity.class, true, (p_213693_1_) -> p_213693_1_ != null && p_213693_1_.getHealth() < p_213693_1_.getMaxHealth());
        this.field_213695_D0 = new ToggleableAttackableTargetGoal<>(this, AbstractTaillessEntity.class, 10, true, false, (Predicate<LivingEntity>)null);
        this.field_213695_D1 = new ToggleableAttackableTargetGoal<>(this, AbstractRaiderEntity.class, 10, true, false, (Predicate<LivingEntity>)null);
        this.field_213695_D2 = new ToggleableAttackableTargetGoal<>(this, MobEntity.class, 5, true, false, (p_234199_0_) -> p_234199_0_ instanceof IMob && !(p_234199_0_ instanceof CreeperEntity)  && !(p_234199_0_ instanceof SummonedEntity));
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new AbstractProtectorEntity.SitGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0F, 60, 10.0F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(4, new AbstractProtectorEntity.FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(5, new ReturnToVillageGoal(this, 0.6D, false));
        this.goalSelector.addGoal(6, new PatrolVillageGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new AbstractProtectorEntity.OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new AbstractProtectorEntity.OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractProtectorEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractVillagerEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(4, this.field_213694_A);
        this.targetSelector.addGoal(5, this.field_213694_B);
        this.targetSelector.addGoal(5, this.field_213694_C);
        this.targetSelector.addGoal(6, this.field_213695_D0);
        this.targetSelector.addGoal(6, this.field_213695_D1);
        this.targetSelector.addGoal(6, this.field_213695_D2);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(IS_DRINKING, false);
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("hiredTimer", this.hiredTimer);
        compound.putInt("dyingTimer", this.dyingTimer);
        compound.putInt("loyaltyPoints", this.loyaltyPoints);
        compound.putBoolean("isDying", this.dying);
        compound.putBoolean("isLoyal", this.loyal);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.hiredTimer = compound.getInt("hiredTimer");
        this.dyingTimer = compound.getInt("dyingTimer");
        this.loyaltyPoints = compound.getInt("loyaltyPoints");
        this.dying = compound.getBoolean("isDying");
        this.loyal = compound.getBoolean("isLoyal");
        this.setDying(this.dying);
        this.setLoyal(this.loyal);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITCH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.WITCH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WITCH_DEATH;
    }

    public void setDrinkingPotion(boolean drinkingPotion) {
        this.getEntityData().set(IS_DRINKING, drinkingPotion);
    }

    public boolean isDrinkingPotion() {
        return this.getEntityData().get(IS_DRINKING);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 26.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (entity instanceof AbstractProtectorEntity) {
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

    protected boolean isImmobile() {
        return this.isDying();
    }

    public void aiStep() {
        if (!this.level.isClientSide && this.isAlive() && !this.isDying()) {
            this.field_213694_A.tickCooldown();
            this.field_213694_B.tickCooldown();
            this.field_213694_C.tickCooldown();
            if (this.field_213694_A.getCooldown() <=0 || this.field_213694_B.getCooldown() <= 0 || this.field_213694_C.getCooldown() <= 0) {
                this.field_213695_D0.func_220783_a(true);
                this.field_213695_D1.func_220783_a(true);
                this.field_213695_D2.func_220783_a(true);
            } else {
                this.field_213695_D0.func_220783_a(false);
                this.field_213695_D1.func_220783_a(false);
                this.field_213695_D2.func_220783_a(false);
            }
            if (this.isDrinkingPotion()) {
                if (this.potionUseTimer-- <= 0) {
                    this.setDrinkingPotion(false);
                    ItemStack itemstack = this.getMainHandItem();
                    this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                    if (itemstack.getItem() == Items.POTION) {
                        List<EffectInstance> list = PotionUtils.getMobEffects(itemstack);
                        if (list != null) {
                            for(EffectInstance effectinstance : list) {
                                this.addEffect(new EffectInstance(effectinstance));
                            }
                        }
                    }

                    this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                }
            } else {
                Potion potion = null;
                if (this.random.nextFloat() < 0.15F && this.isEyeInFluid(FluidTags.WATER) && !this.hasEffect(Effects.WATER_BREATHING)) {
                    potion = Potions.WATER_BREATHING;
                } else if (this.random.nextFloat() < 0.15F && (this.isOnFire() || this.getLastDamageSource() != null && this.getLastDamageSource().isFire()) && !this.hasEffect(Effects.FIRE_RESISTANCE)) {
                    potion = Potions.FIRE_RESISTANCE;
                } else if (this.random.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
                    potion = Potions.HEALING;
                } else if (this.random.nextFloat() < 0.5F && this.getTarget() != null && !this.hasEffect(Effects.MOVEMENT_SPEED) && this.getTarget().distanceToSqr(this) > 121.0D) {
                    potion = Potions.SWIFTNESS;
                }

                if (potion != null) {
                    this.setItemSlot(EquipmentSlotType.MAINHAND, PotionUtils.setPotion(new ItemStack(Items.POTION), potion));
                    this.potionUseTimer = this.getMainHandItem().getUseDuration();
                    this.setDrinkingPotion(true);
                    if (!this.isSilent()) {
                        this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_DRINK, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
                    }

                    ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                    modifiableattributeinstance.removeModifier(MODIFIER);
                    modifiableattributeinstance.addTransientModifier(MODIFIER);
                }
            }

            if (this.random.nextFloat() < 7.5E-4F) {
                this.level.broadcastEntityEvent(this, (byte)15);
            }
        }

        super.aiStep();
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 15) {
            for(int i = 0; i < this.random.nextInt(35) + 10; ++i) {
                this.level.addParticle(ParticleTypes.WITCH, this.getX() + this.random.nextGaussian() * (double)0.13F, this.getBoundingBox().maxY + 0.5D + this.random.nextGaussian() * (double)0.13F, this.getZ() + this.random.nextGaussian() * (double)0.13F, 0.0D, 0.0D, 0.0D);
            }
        } else {
            super.handleEntityEvent(id);
        }

    }

    protected float getDamageAfterMagicAbsorb(DamageSource source, float damage) {
        damage = super.getDamageAfterMagicAbsorb(source, damage);
        if (source.getEntity() == this) {
            damage = 0.0F;
        }

        if (source.isMagic()) {
            damage = (float)((double)damage * 0.15D);
        }

        return damage;
    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (!this.isDrinkingPotion() && !this.isDying() && !(target instanceof WitchEntity)) {
            Vector3d vector3d = target.getDeltaMovement();
            double d0 = target.getX() + vector3d.x - this.getX();
            double d1 = target.getEyeY() - (double)1.1F - this.getY();
            double d2 = target.getZ() + vector3d.z - this.getZ();
            float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
            Potion potion = Potions.HARMING;
            if (target instanceof AbstractVillagerEntity || target instanceof AbstractProtectorEntity || target instanceof PlayerEntity) {
                if (target.isOnFire()){
                    potion = Potions.FIRE_RESISTANCE;
                } else {
                    potion = Potions.HEALING;
                }

                this.setTarget((LivingEntity)null);
            } else if (target instanceof RavagerEntity && !target.hasEffect(Effects.WEAKNESS)) {
                potion = Potions.WEAKNESS;
            } else if (f >= 8.0F && !target.hasEffect(Effects.MOVEMENT_SLOWDOWN)) {
                potion = Potions.SLOWNESS;
            } else if (target.getHealth() >= 8.0F && !target.hasEffect(Effects.POISON) && !(target.getEntity() instanceof CaveSpiderEntity)) {
                potion = Potions.POISON;
            } else if (f <= 3.0F && !target.hasEffect(Effects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
                potion = Potions.WEAKNESS;
            } else if (target.getMobType() == CreatureAttribute.UNDEAD){
                potion = Potions.HEALING;
            }

            PotionEntity potionentity = new PotionEntity(this.level, this);
            potionentity.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion));
            potionentity.xRot -= -20.0F;
            potionentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
            if (!this.isSilent()) {
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
            }

            this.level.addFreshEntity(potionentity);
        }
        if (!this.isDrinkingPotion() && !this.isDying() && target instanceof WitchEntity){
            WitchBombEntity snowballentity = new WitchBombEntity(this.level, this);
            Vector3d vector3d = target.getDeltaMovement();
            double d0 = target.getX() + vector3d.x - this.getX();
            double d1 = target.getEyeY() - (double)1.1F - this.getY();
            double d2 = target.getZ() + vector3d.z - this.getZ();
            float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
            snowballentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 2.0F);
            this.playSound(SoundEvents.WITCH_THROW, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(snowballentity);
        }
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.62F;
    }

    static class NearestAttackableTargetHealingGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        private int cooldown = 0;

        public NearestAttackableTargetHealingGoal(MobEntity ally, Class<T> targetClass, boolean checkSight, @Nullable Predicate<LivingEntity> p_i50311_4_) {
            super(ally, targetClass, 500, checkSight, false, p_i50311_4_);
        }

        public int getCooldown() {
            return this.cooldown;
        }

        public void tickCooldown() {
            --this.cooldown;
        }

        public boolean canUse() {
            if (this.cooldown <= 0 && this.mob.getRandom().nextBoolean()) {
                this.findTarget();
                return this.target != null && this.target.getHealth() < this.target.getMaxHealth();
            } else {
                return false;
            }
        }

        public void start() {
            this.cooldown = 200;
            super.start();
        }
    }

    public static class ToggleableAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        private boolean field_220784_i = true;

        public ToggleableAttackableTargetGoal(MobEntity p_i50312_1_, Class<T> p_i50312_2_, int p_i50312_3_, boolean p_i50312_4_, boolean p_i50312_5_, @Nullable Predicate<LivingEntity> p_i50312_6_) {
            super(p_i50312_1_, p_i50312_2_, p_i50312_3_, p_i50312_4_, p_i50312_5_, p_i50312_6_);
        }

        public void func_220783_a(boolean p_220783_1_) {
            this.field_220784_i = p_220783_1_;
        }

        public boolean canUse() {
            return this.field_220784_i && super.canUse();
        }
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
                    this.playSound(SoundEvents.WITCH_CELEBRATE, 1.0F, 1.0F);
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
}
