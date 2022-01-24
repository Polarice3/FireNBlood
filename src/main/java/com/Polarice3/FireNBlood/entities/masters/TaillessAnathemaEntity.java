package com.Polarice3.FireNBlood.entities.masters;

import com.Polarice3.FireNBlood.entities.ai.TaillessMeleeGoal;
import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.ServantTaillessEntity;
import com.Polarice3.FireNBlood.entities.projectiles.SoulFireballEntity;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class TaillessAnathemaEntity extends ServantTaillessEntity {
    private static final UUID MODIFIER_UUID = UUID.fromString("22cf9908-b8f8-11eb-8529-0242ac130003");
    private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "ChargeUp", -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof TaillessAnathemaEntity);
    };
    protected static final DataParameter<Byte> ANATHEMA_FLAGS = EntityDataManager.createKey(TaillessAnathemaEntity.class, DataSerializers.BYTE);
    public int attackTimer;
    public int attackTimer2;
    public int firing;
    public int firing2;
    public int cooldown;
    public int roarTick;
    public int meteor = 0;
    private int attackTick;
    public int executed = 0;

    public TaillessAnathemaEntity(EntityType<? extends ServantTaillessEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_SPEED, 12.0D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 150.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.30D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 10.0D)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 2.5D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.9D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(3, new RangeAttackGoal(this));
        this.goalSelector.addGoal(3, new MeteorGoal(this));
        this.goalSelector.addGoal(4, new AnathemaAttackGoal());
        this.goalSelector.addGoal(4, new SecondPhaseIndicator(this));
        this.goalSelector.addGoal(4, new StormGoal(this));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractIllagerEntity.class, true));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setCallsForHelp());
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player){
        return 50 + this.world.rand.nextInt(50);
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
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.25F, 0.25F);
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    public int Attacking(){
        return this.attackTick;
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(ANATHEMA_FLAGS, (byte)0);
    }

    private boolean getAnathemaFlag(int mask) {
        int i = this.dataManager.get(ANATHEMA_FLAGS);
        return (i & mask) != 0;
    }

    private void setAnathemaFlag(int mask, boolean value) {
        int i = this.dataManager.get(ANATHEMA_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(ANATHEMA_FLAGS, (byte)(i & 255));
    }

    public boolean isRoaring(){
        return this.getAnathemaFlag(1);
    }

    public void setRoaring(boolean roaring){
        this.setAnathemaFlag(1,roaring);
    }

    public boolean isBurning() {
        return false;
    }

    public void tick(){
        if (this.attackTick > 0) {
            --this.attackTick;
        }
        if (this.cooldown > 0){
            --this.cooldown;
        }
        if (this.roarTick > 0){
            --this.roarTick;

            Vector3d vector3d = this.getBoundingBox().getCenter();

            for(int i = 0; i < 40; ++i) {
                double d0 = this.rand.nextGaussian() * 0.2D;
                double d1 = this.rand.nextGaussian() * 0.2D;
                double d2 = this.rand.nextGaussian() * 0.2D;
                this.world.addParticle(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
            }
        }
        super.tick();
    }

    public void handleStatusUpdate(byte id) {
        if (id == 4) {
            this.attackTick = 10;
            this.playSound(SoundEvents.ENTITY_RAVAGER_ATTACK, 1.0F, 1.0F);
        }

        super.handleStatusUpdate(id);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        this.attackTick = 10;
        this.world.setEntityState(this, (byte)4);
        this.playSound(SoundEvents.ENTITY_RAVAGER_ATTACK, 1.0F, 1.0F);
        return super.attackEntityAsMob(entityIn);
    }

    public boolean attackEntityFrom(DamageSource source, float amount){
        if (source.isExplosion()){
            return false;
        } else if (source.isProjectile()){
            Entity entity = source.getImmediateSource();
            return !(entity instanceof SoulFireballEntity);
        } else {
            return super.attackEntityFrom(source, amount);
        }
    }

    protected boolean SecondPhase(){
        return this.getHealth() <= this.getMaxHealth() / 2.0F;
    }

    private void roar() {
        if (this.isAlive()) {
            for(Entity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(6.0D), field_213690_b)) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 6.0F);
                if (entity instanceof AbstractTaillessEntity){
                    ((AbstractTaillessEntity) entity).addPotionEffect(new EffectInstance(Effects.RESISTANCE, 1000));
                }

                this.launch(entity);
                this.roarTick = 2;
            }
        }
    }

    private void launch(Entity p_213688_1_) {
        double d0 = p_213688_1_.getPosX() - this.getPosX();
        double d1 = p_213688_1_.getPosZ() - this.getPosZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.addVelocity(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }

    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        this.world.getWorldInfo().setRaining(false);
        this.world.setRainStrength(0.0F);
        this.world.setThunderStrength(0.0F);
    }

    protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
        ItemEntity itementity = this.entityDropItem(RegistryHandler.PADRE_EFFIGY.get());
        if (itementity != null) {
            itementity.setNoDespawn();
        }

    }

    class RangeAttackGoal extends Goal {
        private final TaillessAnathemaEntity anathema;

        public RangeAttackGoal(TaillessAnathemaEntity anathema){
            this.anathema = anathema;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean shouldExecute() {
            return this.anathema.getAttackTarget() != null && this.anathema.cooldown == 0;
        }

        @Override
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = this.anathema.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            }  else if (!(livingentity instanceof PlayerEntity) || !((PlayerEntity)livingentity).isSpectator() && !((PlayerEntity)livingentity).isCreative()) {
                return this.shouldExecute();
            } else {
                return false;
            }
        }

        public void resetTask(){
            this.anathema.attackTimer = 0;
            this.anathema.setAttackTarget((LivingEntity)null);
            this.anathema.firing = 0;
            this.anathema.setRoaring(false);
        }

        public void startExecuting() {
            if (this.anathema.isPotionActive(Effects.INVISIBILITY)){
                this.anathema.removePotionEffect(Effects.INVISIBILITY);
            }
            this.anathema.navigator.clearPath();
            this.anathema.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
        }

        public void tick(){
            LivingEntity livingentity = this.anathema.getAttackTarget();
            if (livingentity != null) {
                this.anathema.getLookController().setLookPositionWithEntity(livingentity, this.anathema.getHorizontalFaceSpeed(), this.anathema.getVerticalFaceSpeed());
                ModifiableAttributeInstance modifiableattributeinstance = this.anathema.getAttribute(Attributes.MOVEMENT_SPEED);
                modifiableattributeinstance.removeModifier(MODIFIER);
                modifiableattributeinstance.applyNonPersistentModifier(MODIFIER);
                ++this.anathema.attackTimer;
                if (this.anathema.attackTimer >= 40) {
                    if (this.anathema.firing < 10) {
                        this.anathema.getLookController().setLookPositionWithEntity(livingentity, this.anathema.getHorizontalFaceSpeed(), this.anathema.getVerticalFaceSpeed());
                        ++this.anathema.firing;
                        this.RangedAttack();
                        this.anathema.setRoaring(true);
                    }
                }
                if (this.anathema.attackTimer == 50) {
                    this.anathema.roar();
                    if (this.anathema.executed == 1) {
                        this.anathema.world.createExplosion(this.anathema, this.anathema.getPosX(), this.anathema.getPosY(), this.anathema.getPosZ(), 6.0F, Explosion.Mode.NONE);
                    } else {
                        this.anathema.world.createExplosion(this.anathema, this.anathema.getPosX(), this.anathema.getPosY(), this.anathema.getPosZ(), 3.0F, Explosion.Mode.NONE);
                    }
                }
                if (this.anathema.attackTimer >= 50) {
                    this.anathema.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                    this.anathema.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 300, 1));
                    this.anathema.setRoaring(false);
                    this.anathema.firing = 0;
                    this.anathema.attackTimer = 0;
                    this.anathema.meteor = this.anathema.meteor + 1;
                    this.anathema.cooldown = 300;
                }
            }
        }

        public void RangedAttack() {
            LivingEntity livingentity = this.anathema.getAttackTarget();
            for (int i = 0; i < 2; ++i) {
                Vector3d vector3d = this.anathema.getLook(1.0F);
                ArrowEntity arrowentity = new ArrowEntity(world, this.anathema.getPosX() + vector3d.x * 1.5D, this.anathema.getPosYHeight(0.55D), this.anathema.getPosZ() + vector3d.z * 1.5D);
                assert livingentity != null;
                if (this.anathema.executed == 1){
                    arrowentity.addEffect(new EffectInstance(Effects.WEAKNESS, 600));
                }
                double d0 = livingentity.getPosX() - this.anathema.getPosX();
                double d1 = livingentity.getPosYHeight(0.3333333333333333D) - arrowentity.getPosY();
                double d2 = livingentity.getPosZ() - this.anathema.getPosZ();
                double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                arrowentity.shoot(d0, d1 + d3 * (double) 0.2F, d2, 2.4F, (float) (14 - this.anathema.world.getDifficulty().getId() * 4));
                playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
                this.anathema.world.addEntity(arrowentity);
            }
        }


    }

    static class MeteorGoal extends Goal {
        private final TaillessAnathemaEntity anathema;
        private AreaEffectCloudEntity darkcloud;

        public MeteorGoal(TaillessAnathemaEntity anathema){
            this.anathema = anathema;
        }

        @Override
        public boolean shouldExecute() {
            return this.anathema.getAttackTarget() != null && this.anathema.meteor >= 2 && this.anathema.executed == 0;
        }

        public void startExecuting() {
            if (this.anathema.isPotionActive(Effects.INVISIBILITY)){
                this.anathema.removePotionEffect(Effects.INVISIBILITY);
            }
            this.anathema.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
        }

        public void tick(){
            ModifiableAttributeInstance modifiableattributeinstance = this.anathema.getAttribute(Attributes.MOVEMENT_SPEED);
            modifiableattributeinstance.removeModifier(MODIFIER);
            modifiableattributeinstance.applyNonPersistentModifier(MODIFIER);
            LivingEntity livingentity = this.anathema.getAttackTarget();
            assert livingentity != null;
            ++this.anathema.attackTimer2;
            if (this.anathema.attackTimer2 == 20){
                if (!this.anathema.world.isRemote) {
                    this.anathema.setRoaring(true);
                    float r = 6.0F;
                    int d = 300;
                    AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.anathema.world, this.anathema.getPosX(), this.anathema.getPosY() + 8.0D, this.anathema.getPosZ());
                    areaeffectcloudentity.setParticleData(ParticleTypes.ENTITY_EFFECT);
                    areaeffectcloudentity.setRadius(r);
                    areaeffectcloudentity.setDuration(d);
                    areaeffectcloudentity.setRadiusPerTick((7.0F - areaeffectcloudentity.getRadius()) / (float)areaeffectcloudentity.getDuration());
                    areaeffectcloudentity.addEffect(new EffectInstance(Effects.BLINDNESS, 90));
                    this.anathema.world.addEntity(areaeffectcloudentity);
                    this.darkcloud = areaeffectcloudentity;
                }
            }
            if (this.anathema.attackTimer2 >= 40){
                this.anathema.setRoaring(false);
                this.anathema.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                ++this.anathema.firing2;
                if (this.anathema.firing2 > 20){
                    if (this.darkcloud.isAlive()) {
                        this.anathema.firing2 = 0;
                        Vector3d vector3d = this.anathema.getLook(1.0F);
                        Random random = this.anathema.world.rand;
                        double d2 = random.nextGaussian() * 0.05D + vector3d.x;
                        double d3 = random.nextGaussian() * 0.01D - 0.75D;
                        double d4 = random.nextGaussian() * 0.05D + vector3d.z;
                        SoulFireballEntity fireball = new SoulFireballEntity(this.anathema.world, this.anathema, d2, d3, d4);
                        fireball.setPosition(this.darkcloud.getPosXRandom(0.5), this.darkcloud.getPosY(), this.darkcloud.getPosZRandom(0.5));
                        this.anathema.world.addEntity(fireball);
                        if (!this.anathema.isSilent()) {
                            this.anathema.world.playEvent(null, 1016, fireball.getPosition(), 0);
                        }
                    } else {
                        this.anathema.firing2 = 0;
                    }
                }
            }
            if (this.anathema.attackTimer2 >= 300){
                this.anathema.attackTimer2 = 0;
                this.anathema.firing2 = 0;
                this.anathema.meteor = 0;
            }

        }

    }

    class AnathemaAttackGoal extends TaillessMeleeGoal {

        public AnathemaAttackGoal() {
            super(TaillessAnathemaEntity.this, 1.0F, true);
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            float f = TaillessAnathemaEntity.this.getWidth() - 0.5F;
            return (double)(f * 4.0F + attackTarget.getWidth());
        }


    }

    class SecondPhaseIndicator extends Goal {
        private final TaillessAnathemaEntity anathema;

        public SecondPhaseIndicator(TaillessAnathemaEntity anathema) {
            this.anathema = anathema;
        }

        @Override
        public boolean shouldExecute() {
            if (this.anathema.SecondPhase()){
                return true;
            } else {
                return false;
            }
        }

        public void tick(){
            if (this.anathema.executed == 0) {
                this.roar();
                this.anathema.removePotionEffect(Effects.INVISIBILITY);
                this.anathema.heal(50);
                this.anathema.playSound(SoundEvents.ENTITY_SKELETON_HORSE_DEATH, 1.0F, 1.0F);
                this.anathema.world.getWorldInfo().setRaining(true);
                this.anathema.world.setRainStrength(1.0F);
                this.anathema.world.setThunderStrength(1.0F);
                this.anathema.executed = 1;
            }
        }

        private void roar() {
            if (this.anathema.isAlive()) {
                for(LivingEntity entity : this.anathema.world.getEntitiesWithinAABB(LivingEntity.class, this.anathema.getBoundingBox().grow(6.0D), field_213690_b)) {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this.anathema), 6.0F);
                    this.launch(entity);
                }

                Vector3d vector3d = this.anathema.getBoundingBox().getCenter();

                for(int i = 0; i < 40; ++i) {
                    double d0 = this.anathema.rand.nextGaussian() * 0.2D;
                    double d1 = this.anathema.rand.nextGaussian() * 0.2D;
                    double d2 = this.anathema.rand.nextGaussian() * 0.2D;
                    this.anathema.world.addParticle(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
                }
            }

        }

        private void launch(LivingEntity p_213688_1_) {
            double d0 = p_213688_1_.getPosX() - this.anathema.getPosX();
            double d1 = p_213688_1_.getPosZ() - this.anathema.getPosZ();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
            p_213688_1_.addVelocity(d0 / d2 * 4.0D, 0.4D, d1 / d2 * 4.0D);
        }
    }

    static class StormGoal extends Goal{
        private final TaillessAnathemaEntity anathema;
        public int thunder;
        public int multi;

        public StormGoal(TaillessAnathemaEntity anathema) {
            this.anathema = anathema;
        }

        @Override
        public boolean shouldExecute() {
            return this.anathema.executed > 0;
        }

        public void tick() {
            ++thunder;
            if (thunder == 20) {
                if (multi < 3) {
                    BlockPos blockPos = this.anathema.getPosition();
                    Random random = this.anathema.world.rand;
                    double d = (this.anathema.world.rand.nextBoolean() ? 1: -1);
                    double e = (this.anathema.world.rand.nextBoolean() ? 1: -1);
                    double d2 = (random.nextInt(900) * d);
                    double d3 = -900.0D;
                    double d4 = (random.nextInt(900) * e);
                    SoulFireballEntity fireball = new SoulFireballEntity(this.anathema.world, this.anathema, d2, d3, d4);
                    fireball.setPosition(this.anathema.getPosXRandom(0.2), this.anathema.getPosY() + 64.0D, this.anathema.getPosZRandom(0.2));
                    this.anathema.world.addEntity(fireball);
                    if (!this.anathema.isSilent()) {
                        this.anathema.world.playEvent(null, 1016, fireball.getPosition(), 0);
                    }
                    thunder = 0;
                    multi = multi + 1;
                } else {
                    for (int i = 0; i < 3; ++i) {
                        BlockPos blockPos = this.anathema.getPosition();
                        Random random = this.anathema.world.rand;
                        double d = (this.anathema.world.rand.nextBoolean() ? 1: -1);
                        double e = (this.anathema.world.rand.nextBoolean() ? 1: -1);
                        double d2 = (random.nextInt(900) * d);
                        double d3 = -900.0D;
                        double d4 = (random.nextInt(900) * e);
                        SoulFireballEntity fireball = new SoulFireballEntity(this.anathema.world, this.anathema, d2, d3, d4);
                        fireball.setPosition(this.anathema.getPosXRandom(0.2), this.anathema.getPosY() + 64.0D, this.anathema.getPosZRandom(0.2));
                        this.anathema.world.addEntity(fireball);
                        if (!this.anathema.isSilent()) {
                            this.anathema.world.playEvent(null, 1016, fireball.getPosition(), 0);
                        }
                    }
                    thunder = 0;
                    multi = 0;
                }
            }
        }
    }

}
