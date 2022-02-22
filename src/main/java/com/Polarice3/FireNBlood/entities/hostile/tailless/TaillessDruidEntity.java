package com.Polarice3.FireNBlood.entities.hostile.tailless;

import com.Polarice3.FireNBlood.entities.hostile.NeophyteEntity;
import com.Polarice3.FireNBlood.entities.hostile.cultists.AbstractCultistEntity;
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
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
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
    protected static final DataParameter<Byte> DRUID_FLAGS = EntityDataManager.defineId(TaillessDruidEntity.class, DataSerializers.BYTE);
    private AbstractTaillessEntity AllyTarget;
    private VillagerEntity TemptTarget;
    private CallerEntity CallerTarget;
    public int Fire;

    public TaillessDruidEntity(EntityType<? extends SpellcastingTaillessEntity> type, World worldIn) {
        super(type, worldIn);
        this.setrandom(2);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 48.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 14.0D);
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
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractPiglinEntity.class)).setAlertOthers());


    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DRUID_FLAGS, (byte)0);
    }

    private boolean getDruidFlag(int mask){
        int i = this.entityData.get(DRUID_FLAGS);
        return (i & mask) != 0;
    }

    private void setDruidFlag(int mask, boolean value) {
        int i = this.entityData.get(DRUID_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(DRUID_FLAGS, (byte)(i & 255));
    }

    public boolean isFiring(){
        return this.getDruidFlag(1);
    }

    public void setFiring(boolean firing){
        this.setDruidFlag(1,firing);
    }

    @Override
    protected int getExperienceReward(PlayerEntity player){
        return 10 + this.level.random.nextInt(10);
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

    @Override
    protected SoundEvent getCastingSoundEvent () {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    private void setAllyTarget(@Nullable AbstractTaillessEntity AllyTargetIn) {
        this.AllyTarget = AllyTargetIn;
    }

    private void setTemptTarget(@Nullable VillagerEntity TemptTargetIn) {
        this.TemptTarget = TemptTargetIn;
    }

    private void setCallerTarget(@Nullable CallerEntity CallerTarget) {
        this.CallerTarget = CallerTarget;
    }

    @Nullable
    private AbstractTaillessEntity getAllyTarget() {
        return this.AllyTarget;
    }

    @Nullable
    private VillagerEntity getTemptTarget() {
        return this.TemptTarget;
    }

    @Nullable
    private CallerEntity getCallerTarget() {
        return this.CallerTarget;
    }

    private final EntityPredicate ally = (new EntityPredicate().range(64.0D).allowSameTeam());

    public boolean hurt(DamageSource source, float amount){
        List<ServantTaillessEntity> list = TaillessDruidEntity.this.level.getNearbyEntities(ServantTaillessEntity.class, this.ally, TaillessDruidEntity.this, TaillessDruidEntity.this.getBoundingBox().inflate(32.0D, 8.0D, 32.0D));
        if (list.isEmpty()) {
            return super.hurt(source, amount);
        } else {
            return false;
        }
    }

    public boolean isPowered(){
        List<ServantTaillessEntity> list = TaillessDruidEntity.this.level.getNearbyEntities(ServantTaillessEntity.class, this.ally, TaillessDruidEntity.this, TaillessDruidEntity.this.getBoundingBox().inflate(32.0D, 8.0D, 32.0D));
        return !list.isEmpty();
    }

    public void aiStep() {
        if (this.isFiring()) {
            ++this.Fire;
            if (this.Fire % 2 == 0 && this.Fire < 10) {
                Vector3d vector3d = this.getViewVector( 1.0F);
                double d0 = this.getX();
                double d1 = this.getY(0.5D);
                double d2 = this.getZ();
                for (int i = 0; i < 8; ++i) {
                    double d3 = d0 + this.getRandom().nextGaussian() / 2.0D;
                    double d4 = d1 + this.getRandom().nextGaussian() / 2.0D;
                    double d5 = d2 + this.getRandom().nextGaussian() / 2.0D;

                    for (int j = 0; j < 6; ++j) {
                        this.level.addParticle(ParticleTypes.FLAME, d3, d4, d5, vector3d.x * (double) 0.08F * (double) j, -vector3d.y * (double)0.6F, vector3d.z * (double) 0.08F * (double) j);
                    }
                }
                for(Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D), field_213690_b)) {
                    if (!(entity instanceof AbstractTaillessEntity)) {
                        if (entity instanceof VexEntity){
                            entity.hurt(DamageSource.mobAttack(this), 20.0F);
                        }
                        entity.setSecondsOnFire(10);
                        double d6 = entity.getX() - this.getX();
                        double d7 = entity.getZ() - this.getZ();
                        double d8 = Math.max(d6 * d6 + d7 * d7, 0.001D);
                        entity.push(d6 / d8 * 4.0D, 0.2D, d7 / d8 * 4.0D);
                    }
                }
            }
            if (this.Fire >= 10){
                this.setFiring(false);
                this.Fire = 0;
            }
        }

        if (this.getTarget() != null) {
            for (Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(32.0D, 8.0D, 32.0D), field_213690_b)) {
                if (entity instanceof ServantTaillessEntity) {
                    ((ServantTaillessEntity) entity).addEffect(new EffectInstance(Effects.WEAKNESS, 60, 1));
                    ((ServantTaillessEntity) entity).addEffect(new EffectInstance(Effects.GLOWING, 60));
                    if (this.level.getDifficulty() == Difficulty.EASY || entity instanceof BlackBullEntity) {
                        entity.hurt(DamageSource.STARVE, 1.0F);
                    }
                }
            }
        }
        List<CallerEntity> list = this.level.getNearbyEntities(CallerEntity.class, this.ally, this, this.getBoundingBox().inflate(64.0D, 8.0D, 64.0D));
        if (!list.isEmpty()){
            this.setCallerTarget(list.get(this.random.nextInt(list.size())));
            if (this.getHealth() == this.getMaxHealth()) {
                if (this.distanceTo(this.getCallerTarget()) <= 8.0D) {
                    this.setTarget(null);
                    this.getLookControl().setLookAt(this.getCallerTarget(), (float) this.getMaxHeadYRot(), (float) this.getMaxHeadXRot());
                    this.navigation.stop();
                    this.prayingTicks = 60;
                    this.spellTicks = 60;
                    this.noActionTime = 0;
                    for (int i = 0; i < 2; ++i) {
                        this.level.addParticle(ParticleTypes.ENCHANT, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
                    }
                } else {
                    Vector3d vector3d = getCallerTarget().position();
                    this.getNavigation().moveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                }
            }
        } else {
            this.prayingTicks = 0;
        }

        super.aiStep();
    }

    protected boolean isMovementNoisy() {
        return this.prayingTicks == 0;
    }

    public boolean isPushable() {
        return !this.isPraying();
    }

    class CastingSpellGoal extends SpellcastingTaillessEntity.CastingASpellGoal{
        private CastingSpellGoal() {
        }

        public void tick() {
            if (TaillessDruidEntity.this.getTarget() != null) {
                TaillessDruidEntity.this.getLookControl().setLookAt(TaillessDruidEntity.this.getTarget(), (float)TaillessDruidEntity.this.getMaxHeadYRot(), (float)TaillessDruidEntity.this.getMaxHeadXRot());
            }
            else if (TaillessDruidEntity.this.getAllyTarget() != null) {
                TaillessDruidEntity.this.getLookControl().setLookAt(TaillessDruidEntity.this.getAllyTarget(), (float)TaillessDruidEntity.this.getMaxHeadYRot(), (float)TaillessDruidEntity.this.getMaxHeadXRot());
            } else if (TaillessDruidEntity.this.getCallerTarget() != null){
                TaillessDruidEntity.this.getLookControl().setLookAt(TaillessDruidEntity.this.getCallerTarget(), (float)TaillessDruidEntity.this.getMaxHeadYRot(), (float)TaillessDruidEntity.this.getMaxHeadXRot());
            }
        }
    }

    class TeleportSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{
        private final TaillessDruidEntity druid;
        private TeleportSpellGoal(TaillessDruidEntity druid){
            this.druid = druid;
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.druid.getTarget();
           if (!super.canUse()) {
                return false;
           } else if (livingentity == null){
               return false;
           } else return this.druid.distanceTo(livingentity) < 16.0D;
        }

        @Override
        protected void castSpell() {
            this.teleportrandomly();
        }

        protected boolean teleportrandomly() {
            if (!this.druid.level.isClientSide() && this.druid.isAlive()) {
                double d0 = this.druid.getX() + (this.druid.random.nextDouble() - 0.5D) * 32.0D;
                double d1 = this.druid.getY() + (double)(this.druid.random.nextInt(32) - 16);
                double d2 = this.druid.getZ() + (this.druid.random.nextDouble() - 0.5D) * 32.0D;
                return this.teleportTo(d0, d1, d2);
            } else {
                return false;
            }
        }

        private boolean teleportTo(double x, double y, double z) {
            BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);

            while(blockpos$mutable.getY() > 0 && !this.druid.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
                blockpos$mutable.move(Direction.DOWN);
            }

            BlockState blockstate = this.druid.level.getBlockState(blockpos$mutable);
            boolean flag = blockstate.getMaterial().blocksMotion();
            boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
            if (flag && !flag1) {
                net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this.druid, x, y, z, 0);
                if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
                boolean flag2 = this.druid.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
                if (flag2 && !this.druid.isSilent()) {
                    this.druid.level.playSound((PlayerEntity)null, this.druid.xo, this.druid.yo, this.druid.zo, SoundEvents.ENDERMAN_TELEPORT, this.druid.getSoundSource(), 1.0F, 1.0F);
                    this.druid.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
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
            return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
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
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else if (TaillessDruidEntity.this.getTarget() == null) {
                return false;
            } else if (TaillessDruidEntity.this.getTarget().getId() == this.lastTargetId) {
                return false;
            } else {
                return true;
            }
        }

        public void start() {
            super.start();
            this.lastTargetId = TaillessDruidEntity.this.getTarget().getId();
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 180;
        }

        public void castSpell() {
            TaillessDruidEntity.this.getTarget().addEffect(new EffectInstance(RegistryHandler.CURSED.get(), 600));
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.SLOWNESS;
        }
    }

    class RegenSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{
        private final EntityPredicate ally = (new EntityPredicate().range(32.0D).allowInvulnerable().selector((health) -> health.getHealth() <= 30.0F).selector((mob) -> !(mob instanceof BulletEntity) && !(mob instanceof RoyalBulletEntity)));

        public boolean canUse() {
           if (TaillessDruidEntity.this.isSpellcasting()) {
                return false;
            } else if (TaillessDruidEntity.this.tickCount < this.spellCooldown) {
                return false;
            } else {
                List<AbstractTaillessEntity> list = TaillessDruidEntity.this.level.getNearbyEntities(AbstractTaillessEntity.class, this.ally, TaillessDruidEntity.this, TaillessDruidEntity.this.getBoundingBox().inflate(32.0D, 32.0D, 32.0D));
                if (list.isEmpty()) {
                    return false;
                } else {
                    TaillessDruidEntity.this.setAllyTarget(list.get(TaillessDruidEntity.this.random.nextInt(list.size())));
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
                abstractTaillessEntity.addEffect(new EffectInstance(Effects.REGENERATION, 1000, 1));
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.REGEN;
        }
    }

    class TemptSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal {
        private final EntityPredicate villager = (new EntityPredicate().range(32.0D).allowInvulnerable());

        public boolean canUse() {
            if (TaillessDruidEntity.this.getTarget() != null) {
                return false;
            } else if (TaillessDruidEntity.this.isSpellcasting()) {
                return false;
            } else if (TaillessDruidEntity.this.tickCount < this.spellCooldown) {
                return false;
            }else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(TaillessDruidEntity.this.level, TaillessDruidEntity.this)) {
                return false;
            } 
            else {
                List<VillagerEntity> list = TaillessDruidEntity.this.level.getNearbyEntities(VillagerEntity.class, this.villager, TaillessDruidEntity.this, TaillessDruidEntity.this.getBoundingBox().inflate(32.0D, 4.0D, 32.0D));
                if (list.isEmpty()) {
                    return false;
                }
                else {
                    TaillessDruidEntity.this.setTemptTarget(list.get(TaillessDruidEntity.this.random.nextInt(list.size())));
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

        public boolean canContinueToUse() {
            return TaillessDruidEntity.this.getTemptTarget() != null && this.spellWarmup > 0;
        }

        public void stop() {
            super.stop();
            TaillessDruidEntity.this.setTemptTarget(null);
        }

        public void castSpell() {
            VillagerEntity villagerEntity = TaillessDruidEntity.this.getTemptTarget();
            if (villagerEntity != null && villagerEntity.isAlive()) {
                int random2 = TaillessDruidEntity.this.random.nextInt(2);
                if (random2 == 1) {
                    NeophyteEntity witchEntity = new NeophyteEntity(ModEntityType.NEOPHYTE.get(), level);
                    witchEntity.moveTo(villagerEntity.getX(), villagerEntity.getY(), villagerEntity.getZ(), villagerEntity.yRot, villagerEntity.xRot);
                    witchEntity.finalizeSpawn((IServerWorld) level, level.getCurrentDifficultyAt(witchEntity.blockPosition()), SpawnReason.CONVERSION, null, null);
                    witchEntity.setNoAi(villagerEntity.isNoAi());
                    if (villagerEntity.hasCustomName()) {
                        witchEntity.setCustomName(villagerEntity.getCustomName());
                        witchEntity.setCustomNameVisible(villagerEntity.isCustomNameVisible());
                    }
                    witchEntity.setPersistenceRequired();
                    level.addFreshEntity(witchEntity);
                    for (int i = 0; i < 5; ++i) {
                        double d0 = random.nextGaussian() * 0.02D;
                        double d1 = random.nextGaussian() * 0.02D;
                        double d2 = random.nextGaussian() * 0.02D;
                        level.addParticle(ParticleTypes.WITCH, witchEntity.getRandomX(1.0D), witchEntity.getRandomY() + 1.0D, witchEntity.getRandomZ(1.0D), d0, d1, d2);
                    }
                } else {
                    WitchEntity witchEntity = new WitchEntity(EntityType.WITCH, level);
                    witchEntity.moveTo(villagerEntity.getX(), villagerEntity.getY(), villagerEntity.getZ(), villagerEntity.yRot, villagerEntity.xRot);
                    witchEntity.finalizeSpawn((IServerWorld) level, level.getCurrentDifficultyAt(witchEntity.blockPosition()), SpawnReason.CONVERSION, null, null);
                    witchEntity.setNoAi(villagerEntity.isNoAi());
                    if (villagerEntity.hasCustomName()) {
                        witchEntity.setCustomName(villagerEntity.getCustomName());
                        witchEntity.setCustomNameVisible(villagerEntity.isCustomNameVisible());
                    }
                    witchEntity.setPersistenceRequired();
                    level.addFreshEntity(witchEntity);
                    for (int i = 0; i < 5; ++i) {
                        double d0 = random.nextGaussian() * 0.02D;
                        double d1 = random.nextGaussian() * 0.02D;
                        double d2 = random.nextGaussian() * 0.02D;
                        level.addParticle(ParticleTypes.WITCH, witchEntity.getRandomX(1.0D), witchEntity.getRandomY() + 1.0D, witchEntity.getRandomZ(1.0D), d0, d1, d2);
                    }
                }
                villagerEntity.releasePoi(MemoryModuleType.HOME);
                villagerEntity.releasePoi(MemoryModuleType.JOB_SITE);
                villagerEntity.releasePoi(MemoryModuleType.POTENTIAL_JOB_SITE);
                villagerEntity.releasePoi(MemoryModuleType.MEETING_POINT);
                villagerEntity.remove();
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.RAID_HORN;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.TEMPT;
        }
    }

    class FlameBurstSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{

        public boolean canUse() {
            LivingEntity livingentity = TaillessDruidEntity.this.getTarget();
            if (!super.canUse()) {
                return false;
            } else {
                assert livingentity != null;
                return TaillessDruidEntity.this.distanceTo(livingentity) <= 4.0D;
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
            TaillessDruidEntity.this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 1.0F, 1.0F);
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected SpellcastingTaillessEntity.SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.LIGHTNING;
        }
    }

    class SummonSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal {
        private final EntityPredicate field_220843_e = (new EntityPredicate()).range(32.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

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
                int i = TaillessDruidEntity.this.level.getNearbyEntities(BulletEntity.class, this.field_220843_e, TaillessDruidEntity.this, TaillessDruidEntity.this.getBoundingBox().inflate(32.0D)).size();
                return TaillessDruidEntity.this.random.nextInt(8) + 1 > i;
            }
        }

        protected int getCastingTime() {
            return 100;
        }

        protected int getCastingInterval() {
            if (isPowered()){
                return 700;
            }
            else {
                return 600;
            }
        }

        protected void castSpell() {
            ServerWorld serverworld = (ServerWorld)TaillessDruidEntity.this.level;

            for(int i = 0; i < 5; ++i) {
                BlockPos blockpos = TaillessDruidEntity.this.blockPosition().offset(-2 + TaillessDruidEntity.this.random.nextInt(5), 1, -2 + TaillessDruidEntity.this.random.nextInt(5));
                BulletEntity bulletEntity = new BulletEntity(ModEntityType.BULLET.get(), level);
                bulletEntity.moveTo(blockpos, 0.0F, 0.0F);
                bulletEntity.finalizeSpawn(serverworld, TaillessDruidEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
                bulletEntity.setOwner(TaillessDruidEntity.this);
                bulletEntity.setBoundOrigin(blockpos);
                bulletEntity.setLimitedLife(20 * (30 + TaillessDruidEntity.this.random.nextInt(90)));
                serverworld.addFreshEntityWithPassengers(bulletEntity);
            }

        }


        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected SpellcastingTaillessEntity.SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.SUMMON_BULLET;
        }
    }

    class PoisonSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{

        private PoisonSpellGoal(){
        }

        public boolean canUse() {
            if (TaillessDruidEntity.this.getTarget() == null) {
                return false;
            }
            else if (TaillessDruidEntity.this.isSpellcasting()) {
                return false;
            }
            else if (TaillessDruidEntity.this.tickCount < this.spellCooldown) {
                return false;
            }
            else if (TaillessDruidEntity.this.getTarget() != null && TaillessDruidEntity.this.getTarget().isAlive()){
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
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.WITHER;
        }

        public void castSpell(){
            LivingEntity livingentity = TaillessDruidEntity.this.getTarget();
            if (!TaillessDruidEntity.this.level.isClientSide) {
                AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(TaillessDruidEntity.this.level, livingentity.getX(), livingentity.getY(), livingentity.getZ());
                areaeffectcloudentity.setParticle(ParticleTypes.ENTITY_EFFECT);
                areaeffectcloudentity.setRadius(1.5F);
                areaeffectcloudentity.setDuration(300);
                areaeffectcloudentity.addEffect(new EffectInstance(Effects.POISON, 90));
                TaillessDruidEntity.this.level.addFreshEntity(areaeffectcloudentity);
            }
        }
    }

}

