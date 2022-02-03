package com.Polarice3.FireNBlood.entities.hostile.cultists;

import com.Polarice3.FireNBlood.entities.hostile.tailless.BlackBullEntity;
import com.Polarice3.FireNBlood.entities.neutral.protectors.AbstractProtectorEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ZealotEntity extends AbstractCultistEntity implements ICrossbowUser, IRangedAttackMob {
    private static final DataParameter<Boolean> DATA_CHARGING_STATE = EntityDataManager.defineId(ZealotEntity.class, DataSerializers.BOOLEAN);

    public ZealotEntity(EntityType<? extends ZealotEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, -1.0F);
        ((GroundPathNavigator)this.getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F));
        this.goalSelector.addGoal(2, new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F));
        this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractCultistEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, WitchEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractProtectorEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.EVOKER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        if ((double)worldIn.getRandom().nextFloat() < 0.05D) {
            BlackBullEntity blackBullEntity = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), level);
            blackBullEntity.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
            blackBullEntity.finalizeSpawn(worldIn, difficultyIn, SpawnReason.JOCKEY, (ILivingEntityData)null, (CompoundNBT)null);
            this.startRiding(blackBullEntity);
            worldIn.addFreshEntity(blackBullEntity);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        int random = this.level.random.nextInt(2);
        int random2 = this.level.random.nextInt(4);
        if (random == 1) {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        } else {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
        }
        switch (random2){
            case 0:
            case 1:
                break;
            case 2:
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                break;
            case 3:
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        }
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(RegistryHandler.APOSTLEHELM.get()));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CHARGING_STATE, false);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isCharging() {
        return this.entityData.get(DATA_CHARGING_STATE);
    }

    public void setChargingCrossbow(boolean isCharging) {
        this.entityData.set(DATA_CHARGING_STATE, isCharging);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) {
        this.shootCrossbowProjectile(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
    }

    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    protected AbstractArrowEntity getMobArrow(ItemStack arrowStack, float distanceFactor) {
        return ProjectileHelper.getMobArrow(this, arrowStack, distanceFactor);
    }

    public boolean canFireProjectileWeapon(ShootableItem p_230280_1_) {
        return p_230280_1_ == Items.BOW || p_230280_1_ == Items.CROSSBOW;
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractCultistEntity.ArmPose getArmPose() {
        if(this.getMainHandItem().getItem() == Items.CROSSBOW){
            if (this.isCharging()) {
                return AbstractCultistEntity.ArmPose.CROSSBOW_CHARGE;
            } else {
                return AbstractCultistEntity.ArmPose.CROSSBOW_HOLD;
            }
        } else if (this.getMainHandItem().getItem() == Items.BOW) {
            if (this.isAggressive()){
                return ArmPose.BOW_AND_ARROW;
            } else {
                return ArmPose.NEUTRAL;
            }
        } else {
            return ArmPose.NEUTRAL;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.item.BowItem)));
        AbstractArrowEntity abstractarrowentity = this.getMobArrow(itemstack, distanceFactor);
        if (this.getMainHandItem().getItem() instanceof net.minecraft.item.BowItem)
            abstractarrowentity = ((net.minecraft.item.BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrowentity);
    }

    public ItemStack getProjectile(ItemStack shootable) {
        if (shootable.getItem() instanceof ShootableItem) {
            Predicate<ItemStack> predicate = ((ShootableItem)shootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ShootableItem.getHeldProjectile(this, predicate);
            return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }
}
