package com.Polarice3.FireNBlood.entities.hostile;

import com.Polarice3.FireNBlood.entities.neutral.AcolyteEntity;
import com.Polarice3.FireNBlood.entities.neutral.protectors.BrewerEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.potions.ModPotions;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class NeophyteEntity extends CreatureEntity implements IRangedAttackMob {
    private static final UUID MODIFIER_UUID = UUID.fromString("ed267621-aa4a-4b46-8dfe-6a10d164ab30");
    private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, AttributeModifier.Operation.ADDITION);
    private static final DataParameter<Boolean> IS_DRINKING = EntityDataManager.createKey(NeophyteEntity.class, DataSerializers.BOOLEAN);
    private int potionUseTimer;
    private VillagerEntity TemptTarget;
    private int witchtime;

    public NeophyteEntity(EntityType<? extends NeophyteEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 16.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, -1.0F);
        ((GroundPathNavigator)this.getNavigator()).setBreakDoors(true);
        this.witchtime = 720000;
        this.getNavigator().setCanSwim(true);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, WitchEntity.class));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, NeophyteEntity.class));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    protected void registerData() {
        super.registerData();
        this.getDataManager().register(IS_DRINKING, false);
    }

    private final EntityPredicate villagers = (new EntityPredicate().setDistance(8.0D));

    private void setTemptTarget(@Nullable VillagerEntity TemptTargetIn) {
        this.TemptTarget = TemptTargetIn;
    }

    @Nullable
    private VillagerEntity getTemptTarget() {
        return this.TemptTarget;
    }

    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    public void setDrinkingPotion(boolean drinkingPotion) {
        this.getDataManager().set(IS_DRINKING, drinkingPotion);
    }

    public boolean isDrinkingPotion() {
        return this.getDataManager().get(IS_DRINKING);
    }

    public void livingTick() {
        if (!this.world.isRemote && this.isAlive()) {
            if (this.witchtime > 0){
                --this.witchtime;
            } else{
                WitchEntity witchentity = EntityType.WITCH.create(this.world);
                witchentity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
                witchentity.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(witchentity.getPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                witchentity.setNoAI(this.isAIDisabled());
                if (this.hasCustomName()) {
                    witchentity.setCustomName(this.getCustomName());
                    witchentity.setCustomNameVisible(this.isCustomNameVisible());
                }

                witchentity.enablePersistence();
                this.remove();
            }
            if (this.isDrinkingPotion()) {
                if (this.potionUseTimer-- <= 0) {
                    this.setDrinkingPotion(false);
                    ItemStack itemstack = this.getHeldItemMainhand();
                    this.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                    if (itemstack.getItem() == Items.POTION) {
                        List<EffectInstance> list = PotionUtils.getEffectsFromStack(itemstack);
                        if (list != null) {
                            for(EffectInstance effectinstance : list) {
                                this.addPotionEffect(new EffectInstance(effectinstance));
                            }
                        }
                    }

                    this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                }
            } else {
                Potion potion = null;
                if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
                    potion = Potions.REGENERATION;
                }

                if (potion != null) {
                    this.setItemStackToSlot(EquipmentSlotType.MAINHAND, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), potion));
                    this.potionUseTimer = this.getHeldItemMainhand().getUseDuration();
                    this.setDrinkingPotion(true);
                    if (!this.isSilent()) {
                        this.world.playSound((PlayerEntity)null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
                    }

                    ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                    modifiableattributeinstance.removeModifier(MODIFIER);
                    modifiableattributeinstance.applyNonPersistentModifier(MODIFIER);
                }
                List<WitchEntity> witches = this.world.getTargettableEntitiesWithinAABB(WitchEntity.class, this.villagers, this, this.getBoundingBox().grow(32.0D, 32.0D, 32.0D));
                List<BrewerEntity> brewers = this.world.getTargettableEntitiesWithinAABB(BrewerEntity.class, this.villagers, this, this.getBoundingBox().grow(32.0D, 32.0D, 32.0D));
                List<VillagerEntity> mobs = this.world.getTargettableEntitiesWithinAABB(VillagerEntity.class, this.villagers, this, this.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
                List<NeophyteEntity> neophytes = this.world.getTargettableEntitiesWithinAABB(NeophyteEntity.class, this.villagers, this, this.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
                if (witches.isEmpty() && !brewers.isEmpty()){
                    AcolyteEntity acolyteEntity = new AcolyteEntity(ModEntityType.ACOLYTE.get(), world);
                    acolyteEntity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
                    acolyteEntity.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(acolyteEntity.getPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
                    acolyteEntity.setNoAI(this.isAIDisabled());
                    if (this.hasCustomName()) {
                        acolyteEntity.setCustomName(this.getCustomName());
                        acolyteEntity.setCustomNameVisible(this.isCustomNameVisible());
                    }
                    acolyteEntity.enablePersistence();
                    this.remove();
                }
                if (!mobs.isEmpty() && mobs.size() < neophytes.size()){
                    this.setTemptTarget(mobs.get(this.rand.nextInt(mobs.size())));
                    VillagerEntity villager = this.getTemptTarget();
                    if (villager != null && villager.isAlive()){
                        NeophyteEntity witchEntity = new NeophyteEntity(ModEntityType.NEOPHYTE.get(), world);
                        witchEntity.setLocationAndAngles(villager.getPosX(), villager.getPosY(), villager.getPosZ(), villager.rotationYaw, villager.rotationPitch);
                        witchEntity.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(witchEntity.getPosition()), SpawnReason.CONVERSION, null, null);
                        witchEntity.setNoAI(villager.isAIDisabled());
                        witchEntity.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                        if (villager.hasCustomName()) {
                            witchEntity.setCustomName(villager.getCustomName());
                            witchEntity.setCustomNameVisible(villager.isCustomNameVisible());
                        }
                        witchEntity.enablePersistence();
                        world.addEntity(witchEntity);
                        for (int i = 0; i < 5; ++i) {
                            double d0 = rand.nextGaussian() * 0.02D;
                            double d1 = rand.nextGaussian() * 0.02D;
                            double d2 = rand.nextGaussian() * 0.02D;
                            world.addParticle(ParticleTypes.WITCH, witchEntity.getPosXRandom(1.0D), witchEntity.getPosYRandom() + 1.0D, witchEntity.getPosZRandom(1.0D), d0, d1, d2);
                        }
                        villager.resetMemoryPoint(MemoryModuleType.HOME);
                        villager.resetMemoryPoint(MemoryModuleType.JOB_SITE);
                        villager.resetMemoryPoint(MemoryModuleType.POTENTIAL_JOB_SITE);
                        villager.resetMemoryPoint(MemoryModuleType.MEETING_POINT);
                        villager.remove();
                    }
                }
            }

            if (this.rand.nextFloat() < 7.5E-4F) {
                this.world.setEntityState(this, (byte)15);
            }
        }

        super.livingTick();
    }

    public void causeLightningStrike(ServerWorld p_241841_1_, LightningBoltEntity p_241841_2_) {
        if (p_241841_1_.getDifficulty() != Difficulty.PEACEFUL) {
            WitchEntity witchentity = EntityType.WITCH.create(p_241841_1_);
            witchentity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
            witchentity.onInitialSpawn(p_241841_1_, p_241841_1_.getDifficultyForLocation(witchentity.getPosition()), SpawnReason.CONVERSION, (ILivingEntityData)null, (CompoundNBT)null);
            witchentity.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                witchentity.setCustomName(this.getCustomName());
                witchentity.setCustomNameVisible(this.isCustomNameVisible());
            }

            witchentity.enablePersistence();
            p_241841_1_.func_242417_l(witchentity);
            this.remove();
        } else {
            super.causeLightningStrike(p_241841_1_, p_241841_2_);
        }

    }

    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        if (!this.isDrinkingPotion()) {
            Vector3d vector3d = target.getMotion();
            double d0 = target.getPosX() + vector3d.x - this.getPosX();
            double d1 = target.getPosYEye() - (double)1.1F - this.getPosY();
            double d2 = target.getPosZ() + vector3d.z - this.getPosZ();
            float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
            Potion potion = ModPotions.MINOR_HARM.get();
            if (target.getHealth() >= 8.0F && !target.isPotionActive(Effects.POISON)) {
                potion = Potions.POISON;
            }

            PotionEntity potionentity = new PotionEntity(this.world, this);
            potionentity.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potion));
            potionentity.rotationPitch -= -20.0F;
            potionentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
            if (!this.isSilent()) {
                this.world.playSound((PlayerEntity)null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
            }

            this.world.addEntity(potionentity);
        }
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.62F;
    }

}
