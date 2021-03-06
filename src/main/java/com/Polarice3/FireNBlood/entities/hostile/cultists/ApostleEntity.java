package com.Polarice3.FireNBlood.entities.hostile.cultists;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.entities.hostile.tailless.BlackBullEntity;
import com.Polarice3.FireNBlood.entities.neutral.protectors.AbstractProtectorEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
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
import java.util.Objects;
import java.util.function.Predicate;

public class ApostleEntity extends SpellcastingCultistEntity {
    private int f;
    private int cooldown;
    private int spellcycle;
    private final Predicate<Entity> field_213690_b = Entity::isAlive;
    private boolean roarparticles;

    public ApostleEntity(EntityType<? extends SpellcastingCultistEntity> type, World worldIn) {
        super(type, worldIn);
        this.f = 0;
        this.cooldown = 200;
        this.spellcycle = 0;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(3, new CastingSpellGoal());
        this.goalSelector.addGoal(3, new FireballSpellGoal());
        this.goalSelector.addGoal(3, new ZombieSpellGoal());
        this.goalSelector.addGoal(3, new SkeletonSpellGoal());
        this.goalSelector.addGoal(3, new RoarSpellGoal());
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
                .add(Attributes.MAX_HEALTH, 32.0D)
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
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(RegistryHandler.SOULSTAFF.get()));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(RegistryHandler.APOSTLEARMOREDHELM.get()));
        this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(RegistryHandler.APOSTLEARMOREDROBE.get()));
        this.setDropChance(EquipmentSlotType.HEAD, 0.0F);
        this.setDropChance(EquipmentSlotType.CHEST, 0.0F);
        switch (random){
            case 0:
                this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
                break;
            case 1:
                this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.DIAMOND_BOOTS));
                break;
        }
    }

    public boolean isFiring(){
        return this.roarparticles;
    }

    public void setFiring(boolean firing){
        this.roarparticles = firing;
    }

    @Override
    protected SoundEvent getCastingSoundEvent () {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public void aiStep() {
        super.aiStep();
        if (this.cooldown < 200){
            ++this.cooldown;
        } else {
            this.spellcycle = 0;
        }
        if (this.isFiring()) {
            ++this.f;
            if (this.f % 2 == 0 && this.f < 10) {
                for (Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0D), field_213690_b)) {
                    if (!(entity instanceof AbstractCultistEntity)) {
                        entity.hurt(DamageSource.mobAttack(this), 6.0F);
                        this.launch(entity, this);
                    }
                }
                Vector3d vector3d = this.getBoundingBox().getCenter();
                Minecraft MINECRAFT = Minecraft.getInstance();
                for(int i = 0; i < 40; ++i) {
                    double d0 = this.random.nextGaussian() * 0.2D;
                    double d1 = this.random.nextGaussian() * 0.2D;
                    double d2 = this.random.nextGaussian() * 0.2D;
                    assert MINECRAFT.level != null;
                    MINECRAFT.level.addParticle(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
                }
            }
            if (this.f >= 10){
                this.setFiring(false);
                this.f = 0;
            }
        }
    }

    private void launch(Entity p_213688_1_, LivingEntity livingEntity) {
        double d0 = p_213688_1_.getX() - livingEntity.getX();
        double d1 = p_213688_1_.getZ() - livingEntity.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.push(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }

    class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
            if (ApostleEntity.this.getTarget() != null) {
                ApostleEntity.this.getLookControl().setLookAt(ApostleEntity.this.getTarget(), (float) ApostleEntity.this.getMaxHeadYRot(), (float) ApostleEntity.this.getMaxHeadXRot());
            }
        }
    }

    class FireballSpellGoal extends UseSpellGoal {
        private int lastTargetId;
        private FireballSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else if (ApostleEntity.this.getTarget() == null) {
                return false;
            } else return ApostleEntity.this.getTarget().getId() != this.lastTargetId
                    && ApostleEntity.this.spellcycle == 0;
        }

        public void start() {
            super.start();
            this.lastTargetId = ApostleEntity.this.getTarget().getId();
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 60;
        }

        public void castSpell() {
            LivingEntity livingentity = ApostleEntity.this.getTarget();
            if (livingentity != null) {
                double d0 = ApostleEntity.this.distanceToSqr(livingentity);
                float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                double d1 = livingentity.getX() - ApostleEntity.this.getX();
                double d2 = livingentity.getY(0.5D) - ApostleEntity.this.getY(0.5D);
                double d3 = livingentity.getZ() - ApostleEntity.this.getZ();
                for(int i = 0; i < 6; ++i) {
                    SmallFireballEntity smallfireballentity = new SmallFireballEntity(ApostleEntity.this.level, ApostleEntity.this, d1 + ApostleEntity.this.getRandom().nextGaussian() * (double)f, d2, d3 + ApostleEntity.this.getRandom().nextGaussian() * (double)f);
                    smallfireballentity.setPos(smallfireballentity.getX(), ApostleEntity.this.getY(0.5), smallfireballentity.getZ());
                    ApostleEntity.this.level.addFreshEntity(smallfireballentity);
                }
                if (!ApostleEntity.this.isSilent()) {
                    ApostleEntity.this.level.levelEvent(null, 1016, ApostleEntity.this.blockPosition(), 0);
                }
                ApostleEntity.this.cooldown = 0;
                ++ApostleEntity.this.spellcycle;
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_CAST_SPELL;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.FIRE;
        }
    }

    class ZombieSpellGoal extends UseSpellGoal {
        private int lastTargetId;
        private ZombieSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else if (ApostleEntity.this.getTarget() == null) {
                return false;
            } else return ApostleEntity.this.getTarget().getId() != this.lastTargetId &&
                    ApostleEntity.this.cooldown >= 180 && ApostleEntity.this.spellcycle == 1;
        }

        public void start() {
            super.start();
            this.lastTargetId = ApostleEntity.this.getTarget().getId();
        }

        protected int getCastingTime() {
            return FNBConfig.ZombieDuration.get();
        }

        protected int getCastingInterval() {
            return FNBConfig.ZombieCooldown.get();
        }

        public void castSpell() {
            LivingEntity livingentity = ApostleEntity.this.getTarget();
            if (livingentity != null) {
                BlockPos blockpos = ApostleEntity.this.blockPosition();
                ZombieVillagerMinionEntity summonedentity = new ZombieVillagerMinionEntity(ModEntityType.ZOMBIE_VILLAGER_MINION.get(), ApostleEntity.this.level);
                summonedentity.moveTo(blockpos, 0.0F, 0.0F);
                summonedentity.setOwnerId(ApostleEntity.this.getUUID());
                summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                summonedentity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
                summonedentity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                summonedentity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                summonedentity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
                summonedentity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
                summonedentity.finalizeSpawn((IServerWorld) ApostleEntity.this.level, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                summonedentity.setTarget(livingentity);
                ApostleEntity.this.level.addFreshEntity(summonedentity);
                ApostleEntity.this.cooldown = 0;
                ++ApostleEntity.this.spellcycle;
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_CAST_SPELL;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.ZOMBIE;
        }
    }

    class SkeletonSpellGoal extends UseSpellGoal {
        private int lastTargetId;
        private SkeletonSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else if (ApostleEntity.this.getTarget() == null) {
                return false;
            } else return ApostleEntity.this.getTarget().getId() != this.lastTargetId &&
                    ApostleEntity.this.cooldown >= 180 && ApostleEntity.this.spellcycle == 1;
        }

        public void start() {
            super.start();
            this.lastTargetId = ApostleEntity.this.getTarget().getId();
        }

        protected int getCastingTime() {
            return FNBConfig.SkeletonDuration.get();
        }

        protected int getCastingInterval() {
            return FNBConfig.SkeletonCooldown.get();
        }

        public void castSpell() {
            LivingEntity livingentity = ApostleEntity.this.getTarget();
            if (livingentity != null) {
                BlockPos blockpos = ApostleEntity.this.blockPosition();
                SkeletonVillagerMinionEntity summonedentity = new SkeletonVillagerMinionEntity(ModEntityType.SKELETON_VILLAGER_MINION.get(), ApostleEntity.this.level);
                ItemStack item = summonedentity.getMainHandItem();
                if (item.getItem() instanceof BowItem){
                    item.enchant(Enchantments.FLAMING_ARROWS, 1);
                }
                summonedentity.moveTo(blockpos, 0.0F, 0.0F);
                summonedentity.setOwnerId(ApostleEntity.this.getUUID());
                summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                summonedentity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
                summonedentity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                summonedentity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                summonedentity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
                summonedentity.finalizeSpawn((IServerWorld) ApostleEntity.this.level, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                summonedentity.setTarget(livingentity);
                ApostleEntity.this.level.addFreshEntity(summonedentity);
                ApostleEntity.this.cooldown = 0;
                ++ApostleEntity.this.spellcycle;
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_CAST_SPELL;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.ZOMBIE;
        }
    }

    class RoarSpellGoal extends UseSpellGoal {
        private int lastTargetId;
        private RoarSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else if (ApostleEntity.this.getTarget() == null) {
                return false;
            } else if (ApostleEntity.this.getTarget().getId() == this.lastTargetId) {
                return false;
            } else return ApostleEntity.this.distanceTo(ApostleEntity.this.getTarget()) < 4.0F;
        }

        public void start() {
            super.start();
            this.lastTargetId = Objects.requireNonNull(ApostleEntity.this.getTarget()).getId();
        }

        protected int getCastingTime() {
            return FNBConfig.RoarDuration.get();
        }

        protected int getCastingInterval() {
            return 120;
        }

        public void castSpell() {
            ApostleEntity.this.setFiring(true);
            ApostleEntity.this.cooldown = 0;
            ApostleEntity.this.playSound(SoundEvents.RAVAGER_ROAR, 1.0F, 1.0F);
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_CAST_SPELL;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.CRIPPLE;
        }
    }
}
