package com.Polarice3.FireNBlood.entities.hostile.tailless.masters;

import com.Polarice3.FireNBlood.entities.hostile.tailless.*;
import com.Polarice3.FireNBlood.entities.projectiles.SoulFireballEntity;
import com.Polarice3.FireNBlood.entities.utilities.LightningTrapEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
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

public class TaillessProphetEntity extends SpellcastingTaillessEntity implements IChargeableMob {
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof TaillessProphetEntity);
    };
    private AbstractTaillessEntity AllyTarget;
    protected static final DataParameter<Byte> PROPHET_FLAGS = EntityDataManager.defineId(TaillessProphetEntity.class, DataSerializers.BYTE);
    public int Launch;

    public TaillessProphetEntity(EntityType<? extends SpellcastingTaillessEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new TaillessProphetEntity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, IronGolemEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(4, new TaillessProphetEntity.LightningSpellGoal());
        this.goalSelector.addGoal(4, new TaillessProphetEntity.BarrageSpellGoal());
        this.goalSelector.addGoal(5, new TaillessProphetEntity.RegenSpellGoal());
        this.goalSelector.addGoal(6, new TaillessProphetEntity.SummonSpellGoal());
        this.goalSelector.addGoal(7, new TaillessProphetEntity.WitherSpellGoal());
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllagerEntity.class, true));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setAlertOthers());

    }

    @Override
    protected int getExperienceReward(PlayerEntity player){
        return 50 + this.level.random.nextInt(50);
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

    private final EntityPredicate ally = (new EntityPredicate().range(32.0D));

    @Nullable
    private AbstractTaillessEntity getAllyTarget() {
        return this.AllyTarget;
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    protected boolean SecondPhase(){
        return TaillessProphetEntity.this.getHealth() <= TaillessProphetEntity.this.getMaxHealth() / 2.0F;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PROPHET_FLAGS, (byte)0);
    }

    private boolean getProphetFlag(int mask) {
        int i = this.entityData.get(PROPHET_FLAGS);
        return (i & mask) != 0;
    }

    private void setProphetFlag(int mask, boolean value) {
        int i = this.entityData.get(PROPHET_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(PROPHET_FLAGS, (byte)(i & 255));
    }

    public boolean isLaunching(){
        return this.getProphetFlag(1);
    }

    public void setLaunching(boolean launching){
        this.setProphetFlag(1,launching);
    }

    public boolean canBeAffected(EffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() != Effects.WITHER && super.canBeAffected(potioneffectIn);
    }

    public boolean hurt(DamageSource source, float amount){
        List<ServantTaillessEntity> list = TaillessProphetEntity.this.level.getNearbyEntities(ServantTaillessEntity.class, this.ally, TaillessProphetEntity.this, TaillessProphetEntity.this.getBoundingBox().inflate(32.0D, 8.0D, 32.0D));
        if (list.isEmpty()) {
            boolean flag = super.hurt(source, amount);
            if (TaillessProphetEntity.this.SecondPhase()) {
                this.teleportRandomly();
            }
            return flag;
        } else {
            return false;
        }
    }

    public boolean isPowered(){
        List<ServantTaillessEntity> list = TaillessProphetEntity.this.level.getNearbyEntities(ServantTaillessEntity.class, this.ally, TaillessProphetEntity.this, TaillessProphetEntity.this.getBoundingBox().inflate(32.0D, 8.0D, 32.0D));
        if (list.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        }  else if (entityIn instanceof AbstractPiglinEntity){
            return this.isAlliedTo(entityIn);
        }  else {
            return false;
        }
    }

    protected void customServerAiStep(){
        if (SecondPhase()) {
            if (this.tickCount % 20 == 0) {
                this.heal(1.0F);
            }
        }
        super.customServerAiStep();
    }

    protected boolean teleportRandomly() {
        if (!this.level.isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 32.0D;
            double d1 = this.getY() + (double)(this.random.nextInt(32) - 16);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 32.0D;
            return this.teleportto(d0, d1, d2);
        } else {
            return false;
        }
    }

    private boolean teleportto(double x, double y, double z) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);

        while(blockpos$mutable.getY() > 0 && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = this.level.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2 && !this.isSilent()) {
                this.level.playSound((PlayerEntity)null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }

            return flag2;
        } else {
            return false;
        }
    }

    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        ItemEntity itementity = this.spawnAtLocation(RegistryHandler.PADRE_EFFIGY.get());
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }

    }

    public void tick(){
        super.tick();
        if (this.isLaunching()) {
            ++this.Launch;
            for (Entity entity : TaillessProphetEntity.this.level.getEntitiesOfClass(LivingEntity.class, TaillessProphetEntity.this.getBoundingBox().inflate(8.0D, 0.0D, 8.0D), field_213690_b)) {
                if (!(entity instanceof AbstractTaillessEntity)) {
                    entity.push(0.0D, 4.0D, 0.0D);
                    entity.playSound(SoundEvents.FIREWORK_ROCKET_SHOOT, 1.0F, 2.0F);
                }
            }
        }

        if (this.Launch >= 1){
            this.Launch = 0;
            this.setLaunching(false);
        }

        for(Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(32.0D, 8.0D, 32.0D), field_213690_b)) {
            if (entity instanceof ServantTaillessEntity) {
                ((ServantTaillessEntity) entity).addEffect(new EffectInstance(Effects.WEAKNESS, 60));
                ((ServantTaillessEntity) entity).addEffect(new EffectInstance(Effects.GLOWING, 60));
                if (this.level.getDifficulty() == Difficulty.EASY){
                    entity.hurt(DamageSource.STARVE, 1.0F);
                }
            }
        }

    }

    class CastingSpellGoal extends SpellcastingTaillessEntity.CastingASpellGoal{
        private CastingSpellGoal() {
        }

        public void tick() {
            if (TaillessProphetEntity.this.getTarget() != null) {
                TaillessProphetEntity.this.getLookControl().setLookAt(TaillessProphetEntity.this.getTarget(), (float)TaillessProphetEntity.this.getMaxHeadYRot(), (float)TaillessProphetEntity.this.getMaxHeadXRot());
            }
            else if (TaillessProphetEntity.this.getAllyTarget() != null) {
                TaillessProphetEntity.this.getLookControl().setLookAt(TaillessProphetEntity.this.getAllyTarget(), (float)TaillessProphetEntity.this.getMaxHeadYRot(), (float)TaillessProphetEntity.this.getMaxHeadXRot());
            }
        }
    }

    class JumpSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{

        public boolean canUse() {
            if (TaillessProphetEntity.this.getTarget() == null) {
                return false;
            }
            else if (TaillessProphetEntity.this.isSpellcasting()) {
                return false;
            }
            else if (TaillessProphetEntity.this.tickCount < this.spellCooldown) {
                return false;
            }
            else return TaillessProphetEntity.this.getTarget().isAlive();
        }

        protected int getCastingTime() {
            return 20;
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
            return SpellcastingTaillessEntity.SpellType.LIGHTNING;
        }

        public void castSpell(){
            LivingEntity livingentity = TaillessProphetEntity.this.getTarget();
            TaillessProphetEntity.this.setLaunching(true);
            if (TaillessProphetEntity.this.SecondPhase()){
                TaillessProphetEntity.this.teleportRandomly();
            }
        }

    }

    class LightningSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{

        private LightningSpellGoal(){
        }

        public boolean canUse() {
            if (TaillessProphetEntity.this.getTarget() == null) {
                return false;
            }
            else if (TaillessProphetEntity.this.isSpellcasting()) {
                return false;
            }
            else if (TaillessProphetEntity.this.tickCount < this.spellCooldown) {
                return false;
            }
            else if (TaillessProphetEntity.this.getTarget().isAlive()){
                return true;
            }
            else {
                return false;
            }
        }

        protected int getCastingTime() {
            return 60;
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
            return SpellcastingTaillessEntity.SpellType.LIGHTNING;
        }

        public void castSpell(){
            LivingEntity livingentity = TaillessProphetEntity.this.getTarget();
            if (!TaillessProphetEntity.this.level.isClientSide) {
                assert livingentity != null;
                LightningTrapEntity lightningrune = new LightningTrapEntity(TaillessProphetEntity.this.level, livingentity.getX(), livingentity.getY(), livingentity.getZ());
                lightningrune.setDuration(60);
                TaillessProphetEntity.this.level.addFreshEntity(lightningrune);
                if (TaillessProphetEntity.this.SecondPhase()){
                    TaillessProphetEntity.this.teleportRandomly();
                }
            }
        }
    }

    class RegenSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{
        private final EntityPredicate ally = (new EntityPredicate().range(32.0D).allowInvulnerable().selector((health) -> health.getHealth() <= 30.0F).selector((mob) -> !(mob instanceof BulletEntity) && !(mob instanceof RoyalBulletEntity)));

        public boolean canUse() {
            if (TaillessProphetEntity.this.isSpellcasting()) {
                return false;
            } else if (TaillessProphetEntity.this.tickCount < this.spellCooldown) {
                return false;
            } else {
                List<AbstractTaillessEntity> list = TaillessProphetEntity.this.level.getNearbyEntities(AbstractTaillessEntity.class, this.ally, TaillessProphetEntity.this, TaillessProphetEntity.this.getBoundingBox().inflate(32.0D, 32.0D, 32.0D));
                if (list.isEmpty()) {
                    return false;
                } else {
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
            for (AbstractTaillessEntity ally : TaillessProphetEntity.this.level.getEntitiesOfClass(AbstractTaillessEntity.class, TaillessProphetEntity.this.getBoundingBox().inflate(16.0D), field_213690_b)) {
                ally.addEffect(new EffectInstance(Effects.REGENERATION, 1000, 1));
                ally.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 1000, 1));
            }
            if (TaillessProphetEntity.this.SecondPhase()){
                TaillessProphetEntity.this.teleportRandomly();
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

    class SummonSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal {
        private final EntityPredicate field_220843_e = (new EntityPredicate()).range(32.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

        private SummonSpellGoal() {
        }

        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                int i = TaillessProphetEntity.this.level.getNearbyEntities(RoyalBulletEntity.class, this.field_220843_e, TaillessProphetEntity.this, TaillessProphetEntity.this.getBoundingBox().inflate(32.0D)).size();
                return TaillessProphetEntity.this.random.nextInt(8) + 1 > i;
            }
        }

        protected int getCastingTime() {
            return 100;
        }

        protected int getCastingInterval() {
            if (isPowered()){
                return 600;
            }
            else {
                return 500;
            }
        }

        protected void castSpell() {
            ServerWorld serverworld = (ServerWorld)TaillessProphetEntity.this.level;

            for(int i = 0; i < 5; ++i) {
                BlockPos blockpos = TaillessProphetEntity.this.blockPosition().offset(-2 + TaillessProphetEntity.this.random.nextInt(5), 1, -2 + TaillessProphetEntity.this.random.nextInt(5));
                RoyalBulletEntity bulletEntity = new RoyalBulletEntity(ModEntityType.ROYALBULLET.get(), level);
                bulletEntity.moveTo(blockpos, 0.0F, 0.0F);
                bulletEntity.finalizeSpawn(serverworld, TaillessProphetEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
                bulletEntity.setOwner(TaillessProphetEntity.this);
                bulletEntity.setBoundOrigin(blockpos);
                bulletEntity.setLimitedLife(20 * (30 + TaillessProphetEntity.this.random.nextInt(90)));
                serverworld.addFreshEntityWithPassengers(bulletEntity);
            }
            if (TaillessProphetEntity.this.SecondPhase()){
                TaillessProphetEntity.this.teleportRandomly();
            }

        }


        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected SpellcastingTaillessEntity.SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.SUMMON_BULLET;
        }
    }

    class BarrageSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal {
        private int attackStep;
        private int attackTime;
        private int firedRecentlyTimer;

        private final EntityPredicate field_220843_e = (new EntityPredicate()).range(32.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

        private BarrageSpellGoal() {
        }

        public boolean canUse() {
            if (TaillessProphetEntity.this.getTarget() == null) {
                return false;
            }
            else if (TaillessProphetEntity.this.isSpellcasting()) {
                return false;
            }
            else if (TaillessProphetEntity.this.tickCount < this.spellCooldown) {
                return false;
            }
            else if (TaillessProphetEntity.this.getTarget().isAlive() && TaillessProphetEntity.this.SecondPhase()){
                return true;
            }
            else {
                return false;
            }
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 120;
        }

        protected void castSpell() {
            LivingEntity livingentity = TaillessProphetEntity.this.getTarget();
            ServerWorld serverworld = (ServerWorld) TaillessProphetEntity.this.level;
            assert livingentity != null;
            for(int i = 0; i < 3; ++i) {
                SoulFireballEntity soulfireballEntity = new SoulFireballEntity(level, TaillessProphetEntity.this, 0, -900D, 0);
                soulfireballEntity.setPos(livingentity.getX() + TaillessProphetEntity.this.random.nextInt(5), livingentity.getY() + 32.0D, livingentity.getZ() + TaillessProphetEntity.this.random.nextInt(5));
                level.addFreshEntity(soulfireballEntity);
            }
            if (!TaillessProphetEntity.this.isSilent()) {
                TaillessProphetEntity.this.level.levelEvent(null, 1016, TaillessProphetEntity.this.blockPosition(), 0);
            }
            TaillessProphetEntity.this.teleportRandomly();
            }



        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected SpellcastingTaillessEntity.SpellType getSpellType() {
            return SpellcastingTaillessEntity.SpellType.BARRAGE;
        }
    }

    class WitherSpellGoal extends SpellcastingTaillessEntity.UseSpellGoal{
        private int wait;

        private WitherSpellGoal(){
        }

        public boolean canUse() {
            if (TaillessProphetEntity.this.getTarget() == null) {
                return false;
            }
            else if (TaillessProphetEntity.this.isSpellcasting()) {
                return false;
            }
            else if (TaillessProphetEntity.this.tickCount < this.spellCooldown) {
                return false;
            }
            else if (TaillessProphetEntity.this.getTarget().isAlive() && TaillessProphetEntity.this.SecondPhase()){
                return true;
            }
            else {
                return false;
            }
        }

        protected int getCastingTime() {
            return 60;
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
            LivingEntity livingentity = TaillessProphetEntity.this.getTarget();
            if (!TaillessProphetEntity.this.level.isClientSide) {
                assert livingentity != null;
                AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(TaillessProphetEntity.this.level, livingentity.getX(), livingentity.getY(), livingentity.getZ());
                areaeffectcloudentity.setParticle(ParticleTypes.SOUL);
                areaeffectcloudentity.setRadius(6.0F);
                areaeffectcloudentity.setDuration(600);
                areaeffectcloudentity.setRadiusPerTick((7.0F - areaeffectcloudentity.getRadius()) / (float)areaeffectcloudentity.getDuration());
                areaeffectcloudentity.addEffect(new EffectInstance(Effects.WITHER, 90));
                areaeffectcloudentity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 90, 2));
                TaillessProphetEntity.this.level.addFreshEntity(areaeffectcloudentity);
                TaillessProphetEntity.this.teleportRandomly();
            }
        }
    }


}
