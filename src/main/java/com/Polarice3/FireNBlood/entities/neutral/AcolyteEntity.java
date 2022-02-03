package com.Polarice3.FireNBlood.entities.neutral;

import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.neutral.protectors.BrewerEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.potions.ModPotions;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.UUID;

public class AcolyteEntity extends CreatureEntity implements IRangedAttackMob {
    private static final UUID MODIFIER_UUID = UUID.fromString("ed267621-aa4a-4b46-8dfe-6a10d164ab30");
    private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, AttributeModifier.Operation.ADDITION);
    private static final DataParameter<Boolean> IS_DRINKING = EntityDataManager.defineId(AcolyteEntity.class, DataSerializers.BOOLEAN);
    private int potionUseTimer;
    private int witchtime;

    public AcolyteEntity(EntityType<? extends AcolyteEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, -1.0F);
        ((GroundPathNavigator)this.getNavigation()).setCanOpenDoors(true);
        this.witchtime = 720000;
        this.getNavigation().setCanFloat(true);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(1, new FollowBrewerGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, BrewerEntity.class));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, AcolyteEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractTaillessEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractRaiderEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_234199_0_) -> {
            return p_234199_0_ instanceof IMob && !(p_234199_0_ instanceof CreeperEntity);
        }));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(IS_DRINKING, false);
    }

    public boolean canBeLeashed(PlayerEntity player) {
        return false;
    }

    public void setDrinkingPotion(boolean drinkingPotion) {
        this.getEntityData().set(IS_DRINKING, drinkingPotion);
    }

    public boolean isDrinkingPotion() {
        return this.getEntityData().get(IS_DRINKING);
    }

    public void aiStep() {
        if (!this.level.isClientSide && this.isAlive()) {
            if (this.witchtime > 0) {
                --this.witchtime;
            } else {
                BrewerEntity witchentity = ModEntityType.BREWER.get().create(this.level);
                witchentity.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
                witchentity.finalizeSpawn((IServerWorld) this.level, this.level.getCurrentDifficultyAt(witchentity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData) null, (CompoundNBT) null);
                witchentity.setNoAi(this.isNoAi());
                if (this.hasCustomName()) {
                    witchentity.setCustomName(this.getCustomName());
                    witchentity.setCustomNameVisible(this.isCustomNameVisible());
                }
                witchentity.setPersistenceRequired();
                this.remove();
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
                if (this.random.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
                    potion = Potions.REGENERATION;
                }

                if (potion != null) {
                    this.setItemSlot(EquipmentSlotType.MAINHAND, PotionUtils.setPotion(new ItemStack(Items.POTION), potion));
                    this.potionUseTimer = this.getMainHandItem().getUseDuration();
                    this.setDrinkingPotion(true);
                    if (!this.isSilent()) {
                        this.level.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_DRINK, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
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

    public void thunderHit(ServerWorld p_241841_1_, LightningBoltEntity p_241841_2_) {
        if (p_241841_1_.getDifficulty() != Difficulty.PEACEFUL) {
            BrewerEntity witchentity = ModEntityType.BREWER.get().create(p_241841_1_);
            witchentity.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
            witchentity.finalizeSpawn(p_241841_1_, p_241841_1_.getCurrentDifficultyAt(witchentity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
            witchentity.setNoAi(this.isNoAi());
            if (this.hasCustomName()) {
                witchentity.setCustomName(this.getCustomName());
                witchentity.setCustomNameVisible(this.isCustomNameVisible());
            }
            witchentity.setPersistenceRequired();
            p_241841_1_.addFreshEntityWithPassengers(witchentity);
            this.remove();
        } else {
            super.thunderHit(p_241841_1_, p_241841_2_);
        }

    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (!this.isDrinkingPotion()) {
            Vector3d vector3d = target.getDeltaMovement();
            double d0 = target.getX() + vector3d.x - this.getX();
            double d1 = target.getEyeY() - (double)1.1F - this.getY();
            double d2 = target.getZ() + vector3d.z - this.getZ();
            float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
            Potion potion = ModPotions.MINOR_HARM.get();
            if (target.getHealth() >= 8.0F && !target.hasEffect(Effects.POISON)) {
                potion = Potions.POISON;
            }

            PotionEntity potionentity = new PotionEntity(this.level, this);
            potionentity.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion));
            potionentity.xRot -= -20.0F;
            potionentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
            if (!this.isSilent()) {
                this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
            }

            this.level.addFreshEntity(potionentity);
        }
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.62F;
    }

    static class FollowBrewerGoal extends Goal {
        private final AcolyteEntity acolyteEntity;
        private BrewerEntity brewerEntity;
        private final double moveSpeed;
        private int delayCounter;
        private final EntityPredicate brewer = (new EntityPredicate().range(32.0D));

        public FollowBrewerGoal(AcolyteEntity acolyte, double speed) {
            this.acolyteEntity = acolyte;
            this.moveSpeed = speed;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            List<BrewerEntity> list = this.acolyteEntity.level.getNearbyEntities(BrewerEntity.class, this.brewer, this.acolyteEntity, this.acolyteEntity.getBoundingBox().inflate(32.0D, 4.0D, 32.0D));
            BrewerEntity brewerEntity = null;
            double d0 = Double.MAX_VALUE;

            for(BrewerEntity brewerEntity1 : list) {
                double d1 = this.acolyteEntity.distanceToSqr(brewerEntity1);
                if (!(d1 > d0)) {
                    d0 = d1;
                    brewerEntity = brewerEntity1;
                }
            }

            if (brewerEntity == null) {
                return false;
            } else if (d0 < 18.0D) {
                return false;
            } else {
                this.brewerEntity = brewerEntity;
                return true;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            if (!this.brewerEntity.isAlive()) {
                return false;
            } else {
                double d0 = this.acolyteEntity.distanceToSqr(this.brewerEntity);
                return !(d0 < 18.0D) && !(d0 > 256.0D);
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.delayCounter = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.brewerEntity = null;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (--this.delayCounter <= 0) {
                this.delayCounter = 10;
                this.acolyteEntity.getNavigation().moveTo(this.brewerEntity, this.moveSpeed);
            }
        }
    }


}
