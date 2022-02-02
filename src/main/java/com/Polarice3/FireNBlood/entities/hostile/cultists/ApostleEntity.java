package com.Polarice3.FireNBlood.entities.hostile.cultists;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.entities.hostile.tailless.BlackBullEntity;
import com.Polarice3.FireNBlood.entities.neutral.protectors.AbstractProtectorEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.inventory.EquipmentSlotType;
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
    private int spellInt;
    private int f;
    private final Predicate<Entity> field_213690_b = Entity::isAlive;
    private boolean roarparticles;

    public ApostleEntity(EntityType<? extends SpellcastingCultistEntity> type, World worldIn) {
        super(type, worldIn);
        int rand = this.rand.nextInt(3);
        this.setSpellInt(rand);
        this.f = 0;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(3, new CastingSpellGoal());
        this.goalSelector.addGoal(3, new FireballSpellGoal());
        this.goalSelector.addGoal(3, new ZombieSpellGoal());
        this.goalSelector.addGoal(3, new RoarSpellGoal());
        this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractCultistEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, WitchEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractProtectorEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        if ((double)worldIn.getRandom().nextFloat() < 0.05D) {
            BlackBullEntity blackBullEntity = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), world);
            blackBullEntity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
            blackBullEntity.onInitialSpawn(worldIn, difficultyIn, SpawnReason.JOCKEY, (ILivingEntityData)null, (CompoundNBT)null);
            this.startRiding(blackBullEntity);
            worldIn.addEntity(blackBullEntity);
        }
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        int random = this.world.rand.nextInt(3);
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(RegistryHandler.SOULWAND.get()));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
        this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(RegistryHandler.APOSTLEHELM.get()));
        this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(RegistryHandler.APOSTLEROBE.get()));
        switch (random){
            case 0:
                break;
            case 1:
                this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
                this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.LEATHER_BOOTS));
                break;
            case 2:
                this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
                break;
        }
    }

    public void setSpellInt(int spellInt){
        this.spellInt = spellInt;
    }

    public int getSpellInt(){
        return this.spellInt;
    }

    public boolean isFiring(){
        return this.roarparticles;
    }

    public void setFiring(boolean firing){
        this.roarparticles = firing;
    }

    @Override
    protected SoundEvent getSpellSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }

    public void livingTick() {
        super.livingTick();
        if (this.isFiring()) {
            ++this.f;
            if (this.f % 2 == 0 && this.f < 10) {
                for (Entity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(8.0D), field_213690_b)) {
                    if (!(entity instanceof AbstractCultistEntity)) {
                        entity.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0F);
                        this.launch(entity, this);
                    }
                }
                Vector3d vector3d = this.getBoundingBox().getCenter();
                Minecraft MINECRAFT = Minecraft.getInstance();
                for(int i = 0; i < 40; ++i) {
                    double d0 = this.rand.nextGaussian() * 0.2D;
                    double d1 = this.rand.nextGaussian() * 0.2D;
                    double d2 = this.rand.nextGaussian() * 0.2D;
                    assert MINECRAFT.world != null;
                    MINECRAFT.world.addParticle(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
                }
            }
            if (this.f >= 10){
                this.setFiring(false);
                this.f = 0;
            }
        }
    }

    private void launch(Entity p_213688_1_, LivingEntity livingEntity) {
        double d0 = p_213688_1_.getPosX() - livingEntity.getPosX();
        double d1 = p_213688_1_.getPosZ() - livingEntity.getPosZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.addVelocity(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }

    class CastingSpellGoal extends SpellcastingCultistEntity.CastingASpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
            if (ApostleEntity.this.getAttackTarget() != null) {
                ApostleEntity.this.getLookController().setLookPositionWithEntity(ApostleEntity.this.getAttackTarget(), (float) ApostleEntity.this.getHorizontalFaceSpeed(), (float) ApostleEntity.this.getVerticalFaceSpeed());
            }
        }
    }

    class FireballSpellGoal extends SpellcastingCultistEntity.UseSpellGoal {
        private int lastTargetId;
        private FireballSpellGoal() {
        }

        @Override
        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            } else if (ApostleEntity.this.getAttackTarget() == null) {
                return false;
            } else if (ApostleEntity.this.getAttackTarget().getEntityId() == this.lastTargetId) {
                return false;
            } else return ApostleEntity.this.getSpellInt() == 0;
        }

        public void startExecuting() {
            super.startExecuting();
            this.lastTargetId = ApostleEntity.this.getAttackTarget().getEntityId();
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 60;
        }

        public void castSpell() {
            LivingEntity livingentity = ApostleEntity.this.getAttackTarget();
            if (livingentity != null) {
                double d0 = ApostleEntity.this.getDistanceSq(livingentity);
                float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                double d1 = livingentity.getPosX() - ApostleEntity.this.getPosX();
                double d2 = livingentity.getPosYHeight(0.5D) - ApostleEntity.this.getPosYHeight(0.5D);
                double d3 = livingentity.getPosZ() - ApostleEntity.this.getPosZ();
                for(int i = 0; i < 3; ++i) {
                    SmallFireballEntity smallfireballentity = new SmallFireballEntity(ApostleEntity.this.world, ApostleEntity.this, d1 + ApostleEntity.this.getRNG().nextGaussian() * (double)f, d2, d3 + ApostleEntity.this.getRNG().nextGaussian() * (double)f);
                    smallfireballentity.setPosition(smallfireballentity.getPosX(), ApostleEntity.this.getPosYHeight(0.5), smallfireballentity.getPosZ());
                    ApostleEntity.this.world.addEntity(smallfireballentity);
                }
                if (!ApostleEntity.this.isSilent()) {
                    ApostleEntity.this.world.playEvent(null, 1016, ApostleEntity.this.getPosition(), 0);
                }
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
        }

        @Override
        protected SpellcastingCultistEntity.SpellType getSpellType() {
            return SpellType.FIRE;
        }
    }

    class ZombieSpellGoal extends SpellcastingCultistEntity.UseSpellGoal {
        private int lastTargetId;
        private ZombieSpellGoal() {
        }

        @Override
        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            } else if (ApostleEntity.this.getAttackTarget() == null) {
                return false;
            } else if (ApostleEntity.this.getAttackTarget().getEntityId() == this.lastTargetId) {
                return false;
            } else return ApostleEntity.this.getSpellInt() == 1;
        }

        public void startExecuting() {
            super.startExecuting();
            this.lastTargetId = ApostleEntity.this.getAttackTarget().getEntityId();
        }

        protected int getCastingTime() {
            return FNBConfig.ZombieDuration.get();
        }

        protected int getCastingInterval() {
            return FNBConfig.ZombieCooldown.get();
        }

        public void castSpell() {
            LivingEntity livingentity = ApostleEntity.this.getAttackTarget();
            if (livingentity != null) {
                BlockPos blockpos = ApostleEntity.this.getPosition();
                ZombieVillagerMinionEntity summonedentity = new ZombieVillagerMinionEntity(ModEntityType.ZOMBIE_VILLAGER_MINION.get(), ApostleEntity.this.world);
                summonedentity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
                summonedentity.setOwnerId(ApostleEntity.this.getUniqueID());
                summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.world.rand.nextInt(180)));
                summonedentity.onInitialSpawn((IServerWorld) ApostleEntity.this.world, ApostleEntity.this.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                summonedentity.setAttackTarget(livingentity);
                ApostleEntity.this.world.addEntity(summonedentity);
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
        }

        @Override
        protected SpellcastingCultistEntity.SpellType getSpellType() {
            return SpellType.ZOMBIE;
        }
    }

    class RoarSpellGoal extends SpellcastingCultistEntity.UseSpellGoal {
        private int lastTargetId;
        private RoarSpellGoal() {
        }

        @Override
        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            } else if (ApostleEntity.this.getAttackTarget() == null) {
                return false;
            } else if (ApostleEntity.this.getAttackTarget().getEntityId() == this.lastTargetId) {
                return false;
            } else return ApostleEntity.this.getSpellInt() == 2 && ApostleEntity.this.getDistance(ApostleEntity.this.getAttackTarget()) < 4.0F;
        }

        public void startExecuting() {
            super.startExecuting();
            this.lastTargetId = Objects.requireNonNull(ApostleEntity.this.getAttackTarget()).getEntityId();
        }

        protected int getCastingTime() {
            return FNBConfig.RoarDuration.get();
        }

        protected int getCastingInterval() {
            return 120;
        }

        public void castSpell() {
            ApostleEntity.this.setFiring(true);
            ApostleEntity.this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
        }

        @Override
        protected SpellcastingCultistEntity.SpellType getSpellType() {
            return SpellType.CRIPPLE;
        }
    }
}
