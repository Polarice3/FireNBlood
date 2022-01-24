package com.Polarice3.FireNBlood.entities.hostile;

import com.Polarice3.FireNBlood.entities.ai.TaillessMeleeGoal;
import com.Polarice3.FireNBlood.potions.ModPotions;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

public class TaillessWretchEntity extends ServantTaillessEntity {
    private static final UUID MODIFIER_UUID = UUID.fromString("73557538-b8f7-11eb-8529-0242ac130003");
    private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "Throwing Penalty", -0.35D, AttributeModifier.Operation.ADDITION);
    public int Executed = 0;

    public TaillessWretchEntity(EntityType<? extends ServantTaillessEntity> type, World worldIn) {
        super(type, worldIn);
        this.stepHeight = 1.0F;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 48.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.5D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PotionGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractIllagerEntity.class, true));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractPiglinEntity.class)).setCallsForHelp());
    }

    public AbstractTaillessEntity.ArmPose getArmPose() {
        if (this.isAggressive()) {
            return AbstractTaillessEntity.ArmPose.ATTACKING;
        } else {
            return AbstractTaillessEntity.ArmPose.NEUTRAL;
        }
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player){
        return 5 + this.world.rand.nextInt(5);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_POLAR_BEAR_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.25F, 1.0F);
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (super.isOnSameTeam(entityIn)) {
            return true;
        } else if (entityIn instanceof AbstractTaillessEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof AbstractPiglinEntity){
            return this.isOnSameTeam(entityIn);
        }  else {
            return false;
        }
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
    }

    public void onDeath(DamageSource cause) {
        Entity entity = cause.getTrueSource();
        PlayerEntity playerentity;
        if (entity instanceof PlayerEntity) {
            playerentity = (PlayerEntity)entity;
            int random = this.world.rand.nextInt(10);
            if (random == 0) {
                EffectInstance effectinstance1 = playerentity.getActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                if (effectinstance1 == null) {
                    EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                    playerentity.addPotionEffect(effectinstance);
                } else {
                    int amp = effectinstance1.getAmplifier();
                    int i = amp + 1;
                    i = MathHelper.clamp(i, 0, 5);
                    playerentity.removeActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                    EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                    playerentity.addPotionEffect(effectinstance);
                }
            }
        } else if (entity instanceof WolfEntity) {
            WolfEntity wolfentity = (WolfEntity)entity;
            LivingEntity livingentity = wolfentity.getOwner();
            if (wolfentity.isTamed() && livingentity instanceof PlayerEntity) {
                playerentity = (PlayerEntity)livingentity;
                int random = this.world.rand.nextInt(10);
                if (random == 0) {
                    EffectInstance effectinstance1 = playerentity.getActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                    if (effectinstance1 == null) {
                        EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                        playerentity.addPotionEffect(effectinstance);
                    } else {
                        int amp = effectinstance1.getAmplifier();
                        int i = amp + 1;
                        i = MathHelper.clamp(i, 0, 5);
                        playerentity.removeActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                        EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                        playerentity.addPotionEffect(effectinstance);
                    }
                }
            }
        }
        super.onDeath(cause);
    }

    static class PotionGoal extends Goal{
        private final TaillessWretchEntity wretch;
        public int seeTime = 0;
        public Potion potiontype;
        public int runTime = 0;

        public PotionGoal(TaillessWretchEntity wretch){
            this.wretch = wretch;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean shouldExecute() {
            LivingEntity livingentity = this.wretch.getAttackTarget();
            if (this.wretch.Executed == 0) {
                if (this.wretch.getHealth() <= this.wretch.getMaxHealth() / 1.2F && livingentity != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        public void tick() {
            LivingEntity livingentity = this.wretch.getAttackTarget();
            if (livingentity != null) {
                double avoid = this.wretch.getDistance(livingentity);
                if (avoid < 8.0D) {
                    ++this.runTime;
                    this.wretch.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                    Vector3d vector3d = livingentity.getLook(1.0F);
                    if (vector3d.x > 0){
                        this.wretch.moveController.setMoveTo(vector3d.x + 128.0D, vector3d.y, vector3d.z + 128.0D, 1.5F);
                    } else {
                        this.wretch.moveController.setMoveTo(vector3d.x - 128.0D, vector3d.y, vector3d.z - 128.0D, 1.5F);
                    }
                    if (this.runTime >= 30){
                        SlowBombEntity slowbomb = new SlowBombEntity(this.wretch.world, this.wretch.getPosX(),this.wretch.getPosY(),this.wretch.getPosZ(),this.wretch);
                        if (!this.wretch.isSilent()) {
                            this.wretch.world.playSound((PlayerEntity) null, this.wretch.getPosX(), this.wretch.getPosY(), this.wretch.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, this.wretch.getSoundCategory(), 1.0F, 0.8F + this.wretch.rand.nextFloat() * 0.4F);
                        }
                        this.wretch.world.addEntity(slowbomb);
                        this.runTime = 0;
                    }
                } else {
                    ++this.seeTime;
                    if (seeTime >= 30) {
                        Vector3d vector3d = livingentity.getMotion();
                        double d0 = livingentity.getPosX() + vector3d.x - this.wretch.getPosX();
                        double d1 = livingentity.getPosYEye() - (double) 1.1F - this.wretch.getPosY();
                        double d2 = livingentity.getPosZ() + vector3d.z - this.wretch.getPosZ();
                        float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
                        int random = this.wretch.world.rand.nextInt(2);
                        if (random == 1) {
                            this.potiontype = Potions.HARMING;
                        } else {
                            this.potiontype = ModPotions.MINING_FATIGUE.get();
                        }
                        Potion potion = this.potiontype;
                        PotionEntity potionentity = new PotionEntity(this.wretch.world, this.wretch);
                        potionentity.setItem(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potion));
                        potionentity.rotationPitch -= -20.0F;
                        potionentity.shoot(d0, d1 + (double) (f * 0.2F), d2, 0.75F, 8.0F);
                        if (!this.wretch.isSilent()) {
                            this.wretch.world.playSound((PlayerEntity) null, this.wretch.getPosX(), this.wretch.getPosY(), this.wretch.getPosZ(), SoundEvents.ENTITY_WITCH_THROW, this.wretch.getSoundCategory(), 1.0F, 0.8F + this.wretch.rand.nextFloat() * 0.4F);
                        }
                        this.wretch.world.addEntity(potionentity);
                        this.seeTime = 0;
                        this.wretch.Executed = 1;
                        this.wretch.getLookController().setLookPosition(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), this.wretch.getHorizontalFaceSpeed(), this.wretch.getVerticalFaceSpeed());
                        this.wretch.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                    } else {
                        ModifiableAttributeInstance modifiableattributeinstance = this.wretch.getAttribute(Attributes.MOVEMENT_SPEED);
                        modifiableattributeinstance.removeModifier(MODIFIER);
                        modifiableattributeinstance.applyNonPersistentModifier(MODIFIER);
                        this.wretch.getLookController().setLookPosition(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), this.wretch.getHorizontalFaceSpeed(), this.wretch.getVerticalFaceSpeed());
                    }
                }
            }
        }
    }
}
