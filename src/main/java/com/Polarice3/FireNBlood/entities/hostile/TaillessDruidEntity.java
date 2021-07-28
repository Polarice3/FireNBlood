package com.Polarice3.FireNBlood.entities.hostile;

import com.Polarice3.FireNBlood.entities.neutral.NeophyteEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IChargeableMob.class
)

public class TaillessDruidEntity extends SpellcastingTaillessEntity implements IChargeableMob {
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof TaillessDruidEntity);
    };
    protected static final DataParameter<Byte> DRUID_FLAGS = EntityDataManager.createKey(TaillessDruidEntity.class, DataSerializers.BYTE);
    private AbstractTaillessEntity AllyTarget;
    private VillagerEntity TemptTarget;
    public int Fire;

    public TaillessDruidEntity(EntityType<? extends SpellcastingTaillessEntity> type, World worldIn) {
        super(type, worldIn);
    }

    //func_233666_p_ -> registerAttributes()
    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 48.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 14.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new TaillessDruidEntity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, IronGolemEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(3, new TaillessDruidEntity.TeleportSpellGoal(this));
        this.goalSelector.addGoal(3, new TaillessDruidEntity.FlameBurstSpellGoal());
        this.goalSelector.addGoal(4, new TaillessDruidEntity.SlownessSpellGoal());
        this.goalSelector.addGoal(5, new TaillessDruidEntity.SummonSpellGoal());
        this.goalSelector.addGoal(6, new TaillessDruidEntity.RegenSpellGoal());
        this.goalSelector.addGoal(7, new TaillessDruidEntity.TemptSpellGoal());
        this.goalSelector.addGoal(7, new TaillessDruidEntity.PoisonSpellGoal());
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllagerEntity.class, true));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractPiglinEntity.class)).setCallsForHelp());


    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(DRUID_FLAGS, (byte)0);
    }

    private boolean getDruidFlag(int mask){
        int i = this.dataManager.get(DRUID_FLAGS);
        return (i & mask) != 0;
    }

    private void setDruidFlag(int mask, boolean value) {
        int i = this.dataManager.get(DRUID_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(DRUID_FLAGS, (byte)(i & 255));
    }

    public boolean isFiring(){
        return this.getDruidFlag(1);
    }

    public void setFiring(boolean firing){
        this.setDruidFlag(1,firing);
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player){
        return 10 + this.world.rand.nextInt(10);
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

    @Override
    protected SoundEvent getSpellSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }

    private void setAllyTarget(@Nullable AbstractTaillessEntity AllyTargetIn) {
        this.AllyTarget = AllyTargetIn;
    }

    private void setTemptTarget(@Nullable VillagerEntity TemptTargetIn) {
        this.TemptTarget = TemptTargetIn;
    }

    @Nullable
    private AbstractTaillessEntity getAllyTarget() {
        return this.AllyTarget;
    }

    @Nullable
    private VillagerEntity getTemptTarget() {
        return this.TemptTarget;
    }

    private final EntityPredicate ally = (new EntityPredicate().setDistance(32.0D));

    public boolean isOnSameTeam(Entity entityIn) {
        if (super.isOnSameTeam(entityIn)) {
            return true;
        }  else if (entityIn instanceof AbstractPiglinEntity){
            return this.isOnSameTeam(entityIn);
        }  else {
            return false;
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount){
        List<ServantTaillessEntity> list = TaillessDruidEntity.this.world.getTargettableEntitiesWithinAABB(ServantTaillessEntity.class, this.ally, TaillessDruidEntity.this, TaillessDruidEntity.this.getBoundingBox().grow(32.0D, 32.0D, 32.0D));
        if (list.isEmpty()) {
            return super.attackEntityFrom(source, amount);
        } else {
            return false;
        }
    }

    public boolean isCharged(){
        List<ServantTaillessEntity> list = TaillessDruidEntity.this.world.getTargettableEntitiesWithinAABB(ServantTaillessEntity.class, this.ally, TaillessDruidEntity.this, TaillessDruidEntity.this.getBoundingBox().grow(32.0D, 32.0D, 32.0D));
        if (list.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void onDeath(DamageSource cause) {
        Entity entity = cause.getTrueSource();
        PlayerEntity playerentity;
        if (entity instanceof PlayerEntity) {
            playerentity = (PlayerEntity)entity;
            int random = this.world.rand.nextInt(2);
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
                int random = this.world.rand.nextInt(2);
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
    }
    public void livingTick() {
        if (this.isFiring()) {
            ++this.Fire;
            if (this.Fire % 2 == 0 && this.Fire < 10) {
                Vector3d vector3d = this.getLook(1.0F);
                double d0 = this.getPosX();
                double d1 = this.getPosYHeight(0.5D);
                double d2 = this.getPosZ();
                for (int i = 0; i < 8; ++i) {
                    double d3 = d0 + this.getRNG().nextGaussian() / 2.0D;
                    double d4 = d1 + this.getRNG().nextGaussian() / 2.0D;
                    double d5 = d2 + this.getRNG().nextGaussian() / 2.0D;

                    for (int j = 0; j < 6; ++j) {
                        this.world.addParticle(ParticleTypes.FLAME, d3, d4, d5, vector3d.x * (double) 0.08F * (double) j, -vector3d.y * (double)0.6F, vector3d.z * (double) 0.08F * (double) j);
                    }
                }
                for(Entity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(4.0D), field_213690_b)) {
                    if (!(entity instanceof AbstractTaillessEntity)) {
                        if (entity instanceof VexEntity){
                            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 20.0F);
                        }
                        entity.setFire(10);
                        double d6 = entity.getPosX() - this.getPosX();
                        double d7 = entity.getPosZ() - this.getPosZ();
                        double d8 = Math.max(d6 * d6 + d7 * d7, 0.001D);
                        entity.addVelocity(d6 / d8 * 4.0D, 0.2D, d7 / d8 * 4.0D);
                    }
                }
            }
            if (this.Fire >= 10){
                this.setFiring(false);
                this.Fire = 0;
            }
        }
        super.livingTick();
    }

    class CastingSpellGoal extends SpellcastingTaillessEntity.CastingASpellGoal{
        private CastingSpellGoal() {
        }

        public void tick() {
            if (TaillessDruidEntity.this.getAttackTarget() != null) {
                TaillessDruidEntity.this.getLookController().setLookPositionWithEntity(TaillessDruidEntity.this.getAttackTarget(), (float)TaillessDruidEntity.this.getHorizontalFaceSpeed(), (float)TaillessDruidEntity.this.getVerticalFaceSpeed());
            }
            else if (TaillessDruidEntity.this.getAllyTarget() != null) {
                TaillessDruidEntity.this.getLookController().setLookPositionWithEntity(TaillessDruidEntity.this.getAllyTarget(), (float)TaillessDruidEntity.this.getHorizontalFaceSpeed(), (float)TaillessDruidEntity.this.getVerticalFaceSpeed());
            }
        }
    }

    class TeleportSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{
        private final TaillessDruidEntity druid;
        private TeleportSpellGoal(TaillessDruidEntity druid){
            this.druid = druid;
        }

        @Override
        public boolean shouldExecute() {
            LivingEntity livingentity = this.druid.getAttackTarget();
           if (!super.shouldExecute()) {
                return false;
           } else if (livingentity == null){
               return false;
           } else return this.druid.getDistance(livingentity) < 16.0D;
        }

        @Override
        protected void castSpell() {
            this.teleportRandomly();
        }

        protected boolean teleportRandomly() {
            if (!this.druid.world.isRemote() && this.druid.isAlive()) {
                double d0 = this.druid.getPosX() + (this.druid.rand.nextDouble() - 0.5D) * 32.0D;
                double d1 = this.druid.getPosY() + (double)(this.druid.rand.nextInt(32) - 16);
                double d2 = this.druid.getPosZ() + (this.druid.rand.nextDouble() - 0.5D) * 32.0D;
                return this.teleportTo(d0, d1, d2);
            } else {
                return false;
            }
        }

        private boolean teleportTo(double x, double y, double z) {
            BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);

            while(blockpos$mutable.getY() > 0 && !this.druid.world.getBlockState(blockpos$mutable).getMaterial().blocksMovement()) {
                blockpos$mutable.move(Direction.DOWN);
            }

            BlockState blockstate = this.druid.world.getBlockState(blockpos$mutable);
            boolean flag = blockstate.getMaterial().blocksMovement();
            boolean flag1 = blockstate.getFluidState().isTagged(FluidTags.WATER);
            if (flag && !flag1) {
                net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this.druid, x, y, z, 0);
                if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
                boolean flag2 = this.druid.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
                if (flag2 && !this.druid.isSilent()) {
                    this.druid.world.playSound((PlayerEntity)null, this.druid.prevPosX, this.druid.prevPosY, this.druid.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.druid.getSoundCategory(), 1.0F, 1.0F);
                    this.druid.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }

                return flag2;
            } else {
                return false;
            }
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 180;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_ILLUSIONER_PREPARE_MIRROR;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.TELEPORT;
        }
    }

    class SlownessSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal {
        private int lastTargetId;
        private SlownessSpellGoal() {
        }

        @Override
        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            } else if (TaillessDruidEntity.this.getAttackTarget() == null) {
                return false;
            } else if (TaillessDruidEntity.this.getAttackTarget().getEntityId() == this.lastTargetId) {
                return false;
            } else {
                return true;
            }
        }

        public void startExecuting() {
            super.startExecuting();
            this.lastTargetId = TaillessDruidEntity.this.getAttackTarget().getEntityId();
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 180;
        }

        public void castSpell() {
            TaillessDruidEntity.this.getAttackTarget().addPotionEffect(new EffectInstance(Effects.SLOWNESS, 2000));
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_ILLUSIONER_PREPARE_BLINDNESS;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.SLOWNESS;
        }
    }

    class RegenSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{
        private final EntityPredicate ally = (new EntityPredicate().setDistance(32.0D).allowInvulnerable().setCustomPredicate((health) -> health.getHealth() <= 30.0F));

        public boolean shouldExecute() {
           if (TaillessDruidEntity.this.isSpellcasting()) {
                return false;
            } else if (TaillessDruidEntity.this.ticksExisted < this.spellCooldown) {
                return false;
            } else {
                List<AbstractTaillessEntity> list = TaillessDruidEntity.this.world.getTargettableEntitiesWithinAABB(AbstractTaillessEntity.class, this.ally, TaillessDruidEntity.this, TaillessDruidEntity.this.getBoundingBox().grow(32.0D, 32.0D, 32.0D));
                if (list.isEmpty()) {
                    return false;
                } else {
                    TaillessDruidEntity.this.setAllyTarget(list.get(TaillessDruidEntity.this.rand.nextInt(list.size())));
                    return true;
                }
            }
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 340;
        }

        public void castSpell() {
            AbstractTaillessEntity abstractTaillessEntity = TaillessDruidEntity.this.getAllyTarget();
            if (abstractTaillessEntity != null && abstractTaillessEntity.isAlive()) {
                abstractTaillessEntity.addPotionEffect(new EffectInstance(Effects.REGENERATION, 1000, 1));
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_ILLUSIONER_PREPARE_BLINDNESS;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.REGEN;
        }
    }

    class TemptSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal {
        private final EntityPredicate villager = (new EntityPredicate().setDistance(32.0D).allowInvulnerable());

        public boolean shouldExecute() {
            if (TaillessDruidEntity.this.getAttackTarget() != null) {
                return false;
            } else if (TaillessDruidEntity.this.isSpellcasting()) {
                return false;
            } else if (TaillessDruidEntity.this.ticksExisted < this.spellCooldown) {
                return false;
            }else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(TaillessDruidEntity.this.world, TaillessDruidEntity.this)) {
                return false;
            } 
            else {
                List<VillagerEntity> list = TaillessDruidEntity.this.world.getTargettableEntitiesWithinAABB(VillagerEntity.class, this.villager, TaillessDruidEntity.this, TaillessDruidEntity.this.getBoundingBox().grow(32.0D, 4.0D, 32.0D));
                if (list.isEmpty()) {
                    return false;
                }
                else {
                    TaillessDruidEntity.this.setTemptTarget(list.get(TaillessDruidEntity.this.rand.nextInt(list.size())));
                    return true;
                }
            }
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 280;
        }

        public boolean shouldContinueExecuting() {
            return TaillessDruidEntity.this.getTemptTarget() != null && this.spellWarmup > 0;
        }

        public void resetTask() {
            super.resetTask();
            TaillessDruidEntity.this.setTemptTarget(null);
        }

        public void castSpell() {
            VillagerEntity villagerEntity = TaillessDruidEntity.this.getTemptTarget();
            if (villagerEntity != null && villagerEntity.isAlive()) {
                int random = TaillessDruidEntity.this.rand.nextInt(2);
                if (random == 1) {
                    NeophyteEntity witchEntity = new NeophyteEntity(ModEntityType.NEOPHYTE.get(), world);
                    witchEntity.setLocationAndAngles(villagerEntity.getPosX(), villagerEntity.getPosY(), villagerEntity.getPosZ(), villagerEntity.rotationYaw, villagerEntity.rotationPitch);
                    witchEntity.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(witchEntity.getPosition()), SpawnReason.CONVERSION, null, null);
                    witchEntity.setNoAI(villagerEntity.isAIDisabled());
                    if (villagerEntity.hasCustomName()) {
                        witchEntity.setCustomName(villagerEntity.getCustomName());
                        witchEntity.setCustomNameVisible(villagerEntity.isCustomNameVisible());
                    }
                    witchEntity.enablePersistence();
                    world.addEntity(witchEntity);
                    for (int i = 0; i < 5; ++i) {
                        double d0 = rand.nextGaussian() * 0.02D;
                        double d1 = rand.nextGaussian() * 0.02D;
                        double d2 = rand.nextGaussian() * 0.02D;
                        world.addParticle(ParticleTypes.WITCH, witchEntity.getPosXRandom(1.0D), witchEntity.getPosYRandom() + 1.0D, witchEntity.getPosZRandom(1.0D), d0, d1, d2);
                    }
                } else {
                    WitchEntity witchEntity = new WitchEntity(EntityType.WITCH, world);
                    witchEntity.setLocationAndAngles(villagerEntity.getPosX(), villagerEntity.getPosY(), villagerEntity.getPosZ(), villagerEntity.rotationYaw, villagerEntity.rotationPitch);
                    witchEntity.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(witchEntity.getPosition()), SpawnReason.CONVERSION, null, null);
                    witchEntity.setNoAI(villagerEntity.isAIDisabled());
                    if (villagerEntity.hasCustomName()) {
                        witchEntity.setCustomName(villagerEntity.getCustomName());
                        witchEntity.setCustomNameVisible(villagerEntity.isCustomNameVisible());
                    }
                    witchEntity.enablePersistence();
                    world.addEntity(witchEntity);
                    for (int i = 0; i < 5; ++i) {
                        double d0 = rand.nextGaussian() * 0.02D;
                        double d1 = rand.nextGaussian() * 0.02D;
                        double d2 = rand.nextGaussian() * 0.02D;
                        world.addParticle(ParticleTypes.WITCH, witchEntity.getPosXRandom(1.0D), witchEntity.getPosYRandom() + 1.0D, witchEntity.getPosZRandom(1.0D), d0, d1, d2);
                    }
                }
                villagerEntity.resetMemoryPoint(MemoryModuleType.HOME);
                villagerEntity.resetMemoryPoint(MemoryModuleType.JOB_SITE);
                villagerEntity.resetMemoryPoint(MemoryModuleType.POTENTIAL_JOB_SITE);
                villagerEntity.resetMemoryPoint(MemoryModuleType.MEETING_POINT);
                villagerEntity.remove();
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVENT_RAID_HORN;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.TEMPT;
        }
    }

    class FlameBurstSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{

        public boolean shouldExecute() {
            LivingEntity livingentity = TaillessDruidEntity.this.getAttackTarget();
            if (!super.shouldExecute()) {
                return false;
            } else {
                assert livingentity != null;
                return TaillessDruidEntity.this.getDistance(livingentity) <= 4.0D;
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 200;
        }

        protected void castSpell() {
            TaillessDruidEntity.this.setFiring(true);
            TaillessDruidEntity.this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
        }

        protected SpellcastingTaillessEntity.SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.LIGHTNING;
        }
    }

    class SummonSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal {
        private final EntityPredicate field_220843_e = (new EntityPredicate()).setDistance(32.0D).setLineOfSiteRequired().setUseInvisibilityCheck().allowInvulnerable().allowFriendlyFire();

        private SummonSpellGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            } else {
                int i = TaillessDruidEntity.this.world.getTargettableEntitiesWithinAABB(BulletEntity.class, this.field_220843_e, TaillessDruidEntity.this, TaillessDruidEntity.this.getBoundingBox().grow(32.0D)).size();
                return TaillessDruidEntity.this.rand.nextInt(8) + 1 > i;
            }
        }

        protected int getCastingTime() {
            return 100;
        }

        protected int getCastingInterval() {
            if (isCharged()){
                return 700;
            }
            else {
                return 600;
            }
        }

        protected void castSpell() {
            ServerWorld serverworld = (ServerWorld)TaillessDruidEntity.this.world;

            for(int i = 0; i < 5; ++i) {
                BlockPos blockpos = TaillessDruidEntity.this.getPosition().add(-2 + TaillessDruidEntity.this.rand.nextInt(5), 1, -2 + TaillessDruidEntity.this.rand.nextInt(5));
                BulletEntity bulletEntity = new BulletEntity(ModEntityType.BULLET.get(), world);
                bulletEntity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
                bulletEntity.onInitialSpawn(serverworld, TaillessDruidEntity.this.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
                bulletEntity.setOwner(TaillessDruidEntity.this);
                bulletEntity.setBoundOrigin(blockpos);
                bulletEntity.setLimitedLife(20 * (30 + TaillessDruidEntity.this.rand.nextInt(90)));
                serverworld.func_242417_l(bulletEntity);
            }

        }


        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
        }

        protected SpellcastingTaillessEntity.SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.SUMMON_BULLET;
        }
    }

    class PoisonSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{

        private PoisonSpellGoal(){
        }

        public boolean shouldExecute() {
            if (TaillessDruidEntity.this.getAttackTarget() == null) {
                return false;
            }
            else if (TaillessDruidEntity.this.isSpellcasting()) {
                return false;
            }
            else if (TaillessDruidEntity.this.ticksExisted < this.spellCooldown) {
                return false;
            }
            else if (TaillessDruidEntity.this.getAttackTarget() != null && TaillessDruidEntity.this.getAttackTarget().isAlive()){
                return true;
            }
            else {
                return false;
            }
        }
        protected int getCastWarmupTime() {
            return 30;
        }

        protected int getCastingTime() {
            return 120;
        }

        protected int getCastingInterval() {
            return 200;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.WITHER;
        }

        public void castSpell(){
            LivingEntity livingentity = TaillessDruidEntity.this.getAttackTarget();
            if (!TaillessDruidEntity.this.world.isRemote) {
                AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(TaillessDruidEntity.this.world, livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
                areaeffectcloudentity.setParticleData(ParticleTypes.ENTITY_EFFECT);
                areaeffectcloudentity.setRadius(1.5F);
                areaeffectcloudentity.setDuration(300);
                areaeffectcloudentity.addEffect(new EffectInstance(Effects.POISON, 90));
                TaillessDruidEntity.this.world.addEntity(areaeffectcloudentity);
            }
        }
    }

}

