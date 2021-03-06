package com.Polarice3.FireNBlood.entities.hostile.illagers;

import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.neutral.protectors.AbstractProtectorEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class EnviokerEntity extends SpellcastingIllagerEntity {

    public EnviokerEntity(EntityType<? extends EnviokerEntity> p_i50207_1_, World p_i50207_2_) {
        super(p_i50207_1_, p_i50207_2_);
        this.xpReward = 10;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new EnviokerEntity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new EnviokerEntity.AttackGoal(this));
        this.goalSelector.addGoal(4, new EnviokerEntity.SummonSpellGoal());
        this.goalSelector.addGoal(5, new EnviokerEntity.AttackSpellGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, AbstractTaillessEntity.class, false));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractProtectorEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    protected boolean isMagic(){
        return this.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        ILivingEntityData ilivingentitydata = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        ((GroundPathNavigator)this.getNavigation()).setCanOpenDoors(true);
        this.populateDefaultEquipmentSlots(pDifficulty);
        this.populateDefaultEquipmentEnchantments(pDifficulty);
        return ilivingentitydata;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        if (this.getCurrentRaid() == null) {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
            this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
        }

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AbstractIllagerEntity.ArmPose getArmPose() {
        if (this.isCastingSpell() && this.isMagic()) {
            return AbstractIllagerEntity.ArmPose.SPELLCASTING;
        } else if (this.isAggressive() && !this.isMagic()) {
            return AbstractIllagerEntity.ArmPose.ATTACKING;
        } else {
            return this.isCelebrating() ? AbstractIllagerEntity.ArmPose.CELEBRATING : AbstractIllagerEntity.ArmPose.CROSSED;
        }
    }

    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity == this) {
            return true;
        } else if (super.isAlliedTo(pEntity)) {
            return true;
        } else if (pEntity instanceof VexEntity) {
            return this.isAlliedTo(((VexEntity)pEntity).getOwner());
        } else if (pEntity instanceof LivingEntity && ((LivingEntity)pEntity).getMobType() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && pEntity.getTeam() == null;
        } else {
            return false;
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.EVOKER_HURT;
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public void applyRaidBuffs(int pWave, boolean p_213660_2_) {
    }

    class AttackSpellGoal extends SpellcastingIllagerEntity.UseSpellGoal {
        private AttackSpellGoal() {
        }

        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                return EnviokerEntity.this.isMagic();
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 100;
        }

        protected void performSpellCasting() {
            LivingEntity livingentity = EnviokerEntity.this.getTarget();
            double d0 = Math.min(livingentity.getY(), EnviokerEntity.this.getY());
            double d1 = Math.max(livingentity.getY(), EnviokerEntity.this.getY()) + 1.0D;
            float f = (float) MathHelper.atan2(livingentity.getZ() - EnviokerEntity.this.getZ(), livingentity.getX() - EnviokerEntity.this.getX());
            if (EnviokerEntity.this.distanceToSqr(livingentity) < 9.0D) {
                for(int i = 0; i < 5; ++i) {
                    float f1 = f + (float)i * (float)Math.PI * 0.4F;
                    this.createSpellEntity(EnviokerEntity.this.getX() + (double)MathHelper.cos(f1) * 1.5D, EnviokerEntity.this.getZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
                }

                for(int k = 0; k < 8; ++k) {
                    float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
                    this.createSpellEntity(EnviokerEntity.this.getX() + (double)MathHelper.cos(f2) * 2.5D, EnviokerEntity.this.getZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
                }

                for(int k = 0; k < 11; ++k) {
                    float f2 = f + (float)k * (float)Math.PI * 4.0F / 16.0F + 2.5133462F;
                    this.createSpellEntity(EnviokerEntity.this.getX() + (double)MathHelper.cos(f2) * 3.5D, EnviokerEntity.this.getZ() + (double)MathHelper.sin(f2) * 3.5D, d0, d1, f2, 6);
                }
            } else {
                for(int l = 0; l < 16; ++l) {
                    double d2 = 1.25D * (double)(l + 1);
                    float fleft = f + 0.4F;
                    float fright = f - 0.4F;
                    int j = 1 * l;
                    this.createSpellEntity(EnviokerEntity.this.getX() + (double)MathHelper.cos(f) * d2, EnviokerEntity.this.getZ() + (double)MathHelper.sin(f) * d2, d0, d1, f, j);
                    this.createSpellEntity(EnviokerEntity.this.getX() + (double)MathHelper.cos(fleft) * d2, EnviokerEntity.this.getZ() + (double)MathHelper.sin(fleft) * d2, d0, d1, f, j);
                    this.createSpellEntity(EnviokerEntity.this.getX() + (double)MathHelper.cos(fright) * d2, EnviokerEntity.this.getZ() + (double)MathHelper.sin(fright) * d2, d0, d1, f, j);
                }
            }

        }

        private void createSpellEntity(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = EnviokerEntity.this.level.getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(EnviokerEntity.this.level, blockpos1, Direction.UP)) {
                    if (!EnviokerEntity.this.level.isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = EnviokerEntity.this.level.getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(EnviokerEntity.this.level, blockpos);
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
                EnviokerEntity.this.level.addFreshEntity(new EvokerFangsEntity(EnviokerEntity.this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, EnviokerEntity.this));
            }

        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected SpellcastingIllagerEntity.SpellType getSpell() {
            return SpellcastingIllagerEntity.SpellType.FANGS;
        }
    }

    class CastingSpellGoal extends SpellcastingIllagerEntity.CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (EnviokerEntity.this.getTarget() != null) {
                EnviokerEntity.this.getLookControl().setLookAt(EnviokerEntity.this.getTarget(), (float)EnviokerEntity.this.getMaxHeadYRot(), (float)EnviokerEntity.this.getMaxHeadXRot());
            }

        }
    }

    class SummonSpellGoal extends SpellcastingIllagerEntity.UseSpellGoal {
        private final EntityPredicate vexCountTargeting = (new EntityPredicate()).range(16.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

        private SummonSpellGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                int i = EnviokerEntity.this.level.getNearbyEntities(VexEntity.class, this.vexCountTargeting, EnviokerEntity.this, EnviokerEntity.this.getBoundingBox().inflate(16.0D)).size();
                return EnviokerEntity.this.random.nextInt(16) + 1 > i && EnviokerEntity.this.isMagic();
            }
        }

        protected int getCastingTime() {
            return 100;
        }

        protected int getCastingInterval() {
            return 340;
        }

        protected void performSpellCasting() {
            ServerWorld serverworld = (ServerWorld)EnviokerEntity.this.level;

            for(int i = 0; i < 3 + serverworld.random.nextInt(3); ++i) {
                BlockPos blockpos = EnviokerEntity.this.blockPosition().offset(-2 + EnviokerEntity.this.random.nextInt(5), 1, -2 + EnviokerEntity.this.random.nextInt(5));
                VexEntity vexentity = EntityType.VEX.create(EnviokerEntity.this.level);
                vexentity.moveTo(blockpos, 0.0F, 0.0F);
                vexentity.finalizeSpawn(serverworld, EnviokerEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
                vexentity.setOwner(EnviokerEntity.this);
                vexentity.setBoundOrigin(blockpos);
                vexentity.setLimitedLife(20 * (30 + EnviokerEntity.this.random.nextInt(90)));
                serverworld.addFreshEntityWithPassengers(vexentity);
            }

        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected SpellcastingIllagerEntity.SpellType getSpell() {
            return SpellcastingIllagerEntity.SpellType.SUMMON_VEX;
        }
    }

    class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(EnviokerEntity p_i50577_2_) {
            super(p_i50577_2_, 1.0D, false);
        }

        @Override
        public boolean canUse() {
            return !EnviokerEntity.this.isMagic();
        }
    }

}
