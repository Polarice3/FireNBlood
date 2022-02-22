package com.Polarice3.FireNBlood.entities.hostile.tailless;

import com.Polarice3.FireNBlood.entities.hostile.cultists.AbstractCultistEntity;
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
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
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
        this.maxUpStep = 1.0F;
        this.setrandom(10);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 48.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 5.5D);
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
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractPiglinEntity.class)).setAlertOthers());
    }

    public AbstractTaillessEntity.ArmPose getArmPose() {
        if (this.isAggressive()) {
            return AbstractTaillessEntity.ArmPose.ATTACKING;
        } else {
            return AbstractTaillessEntity.ArmPose.NEUTRAL;
        }
    }

    @Override
    protected int getExperienceReward(PlayerEntity player){
        return 5 + this.level.random.nextInt(5);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.POLAR_BEAR_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.COW_STEP, 0.25F, 1.0F);
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(RegistryHandler.GOLDEN_MACE.get()));
    }

    static class PotionGoal extends Goal{
        private final TaillessWretchEntity wretch;
        public int seeTime = 0;
        public Potion potiontype;
        public int runTime = 0;

        public PotionGoal(TaillessWretchEntity wretch){
            this.wretch = wretch;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.wretch.getTarget();
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
            LivingEntity livingentity = this.wretch.getTarget();
            if (livingentity != null) {
                double avoid = this.wretch.distanceTo(livingentity);
                if (avoid < 8.0D) {
                    ++this.runTime;
                    this.wretch.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                    Vector3d vector3d = livingentity.getViewVector( 1.0F);
                    if (vector3d.x > 0){
                        this.wretch.moveControl.setWantedPosition(vector3d.x + 128.0D, vector3d.y, vector3d.z + 128.0D, 1.5F);
                    } else {
                        this.wretch.moveControl.setWantedPosition(vector3d.x - 128.0D, vector3d.y, vector3d.z - 128.0D, 1.5F);
                    }
                    if (this.runTime >= 30){
                        SlowBombEntity slowbomb = new SlowBombEntity(this.wretch.level, this.wretch.getX(),this.wretch.getY(),this.wretch.getZ(),this.wretch);
                        if (!this.wretch.isSilent()) {
                            this.wretch.level.playSound((PlayerEntity) null, this.wretch.getX(), this.wretch.getY(), this.wretch.getZ(), SoundEvents.TNT_PRIMED, this.wretch.getSoundSource(), 1.0F, 0.8F + this.wretch.random.nextFloat() * 0.4F);
                        }
                        this.wretch.level.addFreshEntity(slowbomb);
                        this.runTime = 0;
                    }
                } else {
                    ++this.seeTime;
                    if (seeTime >= 30) {
                        Vector3d vector3d = livingentity.getDeltaMovement();
                        double d0 = livingentity.getX() + vector3d.x - this.wretch.getX();
                        double d1 = livingentity.getEyeY() - (double) 1.1F - this.wretch.getY();
                        double d2 = livingentity.getZ() + vector3d.z - this.wretch.getZ();
                        float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
                        int random = this.wretch.level.random.nextInt(2);
                        if (random == 1) {
                            this.potiontype = Potions.HARMING;
                        } else {
                            this.potiontype = ModPotions.MINING_FATIGUE.get();
                        }
                        Potion potion = this.potiontype;
                        PotionEntity potionentity = new PotionEntity(this.wretch.level, this.wretch);
                        potionentity.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion));
                        potionentity.xRot -= -20.0F;
                        potionentity.shoot(d0, d1 + (double) (f * 0.2F), d2, 0.75F, 8.0F);
                        if (!this.wretch.isSilent()) {
                            this.wretch.level.playSound((PlayerEntity) null, this.wretch.getX(), this.wretch.getY(), this.wretch.getZ(), SoundEvents.WITCH_THROW, this.wretch.getSoundSource(), 1.0F, 0.8F + this.wretch.random.nextFloat() * 0.4F);
                        }
                        this.wretch.level.addFreshEntity(potionentity);
                        this.seeTime = 0;
                        this.wretch.Executed = 1;
                        this.wretch.getLookControl().setLookAt(livingentity.getX(), livingentity.getY(), livingentity.getZ(), this.wretch.getMaxHeadYRot(), this.wretch.getMaxHeadXRot());
                        this.wretch.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                    } else {
                        ModifiableAttributeInstance modifiableattributeinstance = this.wretch.getAttribute(Attributes.MOVEMENT_SPEED);
                        modifiableattributeinstance.removeModifier(MODIFIER);
                        modifiableattributeinstance.addTransientModifier(MODIFIER);
                        this.wretch.getLookControl().setLookAt(livingentity.getX(), livingentity.getY(), livingentity.getZ(), this.wretch.getMaxHeadYRot(), this.wretch.getMaxHeadXRot());
                    }
                }
            }
        }
    }
}
