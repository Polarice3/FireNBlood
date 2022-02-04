package com.Polarice3.FireNBlood.entities.hostile.tailless.masters;

import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.ServantTaillessEntity;
import com.Polarice3.FireNBlood.entities.projectiles.SoulFireballEntity;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class MinotaurEntity extends ServantTaillessEntity {
    private static final UUID MODIFIER_UUID = UUID.fromString("22cf9908-b8f8-11eb-8529-0242ac130003");
    private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "ChargeUp", -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof MinotaurEntity);
    };
    protected static final DataParameter<Byte> MINOTAUR_FLAGS = EntityDataManager.defineId(MinotaurEntity.class, DataSerializers.BYTE);
    private int chargeTick;

    public MinotaurEntity(EntityType<? extends ServantTaillessEntity> type, World worldIn){
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9D)
                .add(Attributes.ARMOR, 7.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new MinotaurEntity.BuffGoal());
        this.goalSelector.addGoal(3, new MinotaurEntity.ChargeGoal());
        this.goalSelector.addGoal(3, new MinotaurEntity.RangeAttackGoal());
        this.goalSelector.addGoal(3, new MinotaurEntity.SecondPhaseIndicator());
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractIllagerEntity.class, true));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setAlertOthers());
    }

    public AbstractTaillessEntity.ArmPose getArmPose() {
        if (this.isAggressive()){
            return AbstractTaillessEntity.ArmPose.ATTACKING;
        } else {
            return AbstractTaillessEntity.ArmPose.NEUTRAL;
        }
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(RegistryHandler.DIAMOND_MACE.get()));
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

    public boolean isOnFire() {
        return false;
    }

    public int charging() {
        return this.chargeTick;
    }

    protected boolean SecondPhase(){
        return MinotaurEntity.this.getHealth() <= MinotaurEntity.this.getMaxHealth() / 2.0F;
    }

    public boolean ignoreExplosion(){
        return MinotaurEntity.this.SecondPhase();
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MINOTAUR_FLAGS, (byte)0);
    }

    private boolean getMinotaurFlag(int mask) {
        int i = this.entityData.get(MINOTAUR_FLAGS);
        return (i & mask) != 0;
    }

    private void setMinotaurFlag(int mask, boolean value) {
        int i = this.entityData.get(MINOTAUR_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(MINOTAUR_FLAGS, (byte)(i & 255));
    }

    public boolean isWarning(){
        return this.getMinotaurFlag(1);
    }

    public void setWarning(boolean warning){
        this.setMinotaurFlag(1,warning);
    }

    protected void customServerAiStep(){
        if (SecondPhase()) {
            if (this.tickCount % 20 == 0) {
                this.heal(1.0F);
            }
        } super.customServerAiStep();
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide && this.isWarning()) {
            for (int i = 0; i < 2; ++i) {
                float f = this.yBodyRot * ((float) Math.PI / 180F) + MathHelper.cos((float) this.tickCount * 0.6662F) * 0.25F;
                float f1 = MathHelper.cos(f);
                float f2 = MathHelper.sin(f);
                this.level.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getRandomX(1.0D), this.getY() + 2.6D, this.getRandomZ(1.0D), 0.7D, 0.5D, 0.2D);
                this.level.addParticle(ParticleTypes.FLAME, this.getX() + (double) f1 * 0.6D, this.getY() + 1.8D, this.getZ() + (double) f2 * 0.6D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        ItemEntity itementity = this.spawnAtLocation(RegistryHandler.PADRE_EFFIGY.get());
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }

    }

    class RangeAttackGoal extends Goal {
        public int attackTimer;

        private RangeAttackGoal() {
        }

        public boolean canUse() {
            LivingEntity livingentity = MinotaurEntity.this.getTarget();
            if (livingentity == null) {
                return false;
            } else if(MinotaurEntity.this.SecondPhase()){
                if (MinotaurEntity.this.getY() < livingentity.getY()){
                    return true;
                } else {
                    return false;
                }
            } else if (MinotaurEntity.this.distanceTo(livingentity) > 8.0D){
                return true;
            } else {
                return false;
            }
        }


        public void start() {
            this.attackTimer = 0;
        }

        public void tick(){
            LivingEntity livingentity = MinotaurEntity.this.getTarget();
                ++this.attackTimer;
                World world = MinotaurEntity.this.level;
                if (this.attackTimer == 20) {
                    double d1 = 4.0D;
                    Vector3d vector3d = MinotaurEntity.this.getViewVector( 1.0F);
                    double d2 = livingentity.getX() - (MinotaurEntity.this.getX() + vector3d.x * 4.0D);
                    double d3 = livingentity.getY(0.5D) - (0.5D + MinotaurEntity.this.getY(0.5D));
                    double d4 = livingentity.getZ() - (MinotaurEntity.this.getZ() + vector3d.z * 4.0D);
                    SoulFireballEntity soulfireballEntity = new SoulFireballEntity(world, MinotaurEntity.this, d2, d3, d4);
                    soulfireballEntity.setPos(MinotaurEntity.this.getX() + vector3d.x * 4.0D, MinotaurEntity.this.getY(0.5D) + 0.5D, soulfireballEntity.getZ() + vector3d.z * 4.0D);
                    level.addFreshEntity(soulfireballEntity);
                    if (!MinotaurEntity.this.isSilent()) {
                        MinotaurEntity.this.level.levelEvent(null, 1016, MinotaurEntity.this.blockPosition(), 0);
                    }
                    this.attackTimer = -20;
                }
                double d0 = MinotaurEntity.this.distanceToSqr(livingentity);
                if (d0 > 16.0D){
                MinotaurEntity.this.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
            }
        }
    }

    class BuffGoal extends Goal {
        protected int warmup;
        protected int roar;

        public boolean canUse() {
                if (MinotaurEntity.this.isAggressive()){
                    List<ServantTaillessEntity> list = MinotaurEntity.this.level.getEntitiesOfClass(ServantTaillessEntity.class, MinotaurEntity.this.getBoundingBox().inflate(32.0D, 32.0D, 32.0D), field_213690_b);
                    if (list.isEmpty()) {
                        return false;
                    } else {
                        return true;
                    }
            } else {return false;}
        }

        public void tick(){
            ++this.warmup;
            if (this.warmup >= 240) {
                ++this.roar;
                if(this.roar == 1){
                    ModifiableAttributeInstance modifiableattributeinstance = MinotaurEntity.this.getAttribute(Attributes.MOVEMENT_SPEED);
                    modifiableattributeinstance.removeModifier(MODIFIER);
                    modifiableattributeinstance.addTransientModifier(MODIFIER);
                }
                if(this.roar >= 20) {
                    this.roaring();
                    for (ServantTaillessEntity ally : MinotaurEntity.this.level.getEntitiesOfClass(ServantTaillessEntity.class, MinotaurEntity.this.getBoundingBox().inflate(16.0D), field_213690_b)) {
                        ally.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 1000, 1));
                        ally.addEffect(new EffectInstance(Effects.DIG_SPEED, 1000, 1));
                    }
                    this.roar = 0;
                    this.warmup = 0;
                    MinotaurEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                }
            }
        }

        private void roaring() {
            if (MinotaurEntity.this.isAlive()) {
                MinotaurEntity.this.playSound(SoundEvents.SKELETON_HORSE_DEATH, 1.0F, 1.0F);
                for(Entity entity : MinotaurEntity.this.level.getEntitiesOfClass(LivingEntity.class, MinotaurEntity.this.getBoundingBox().inflate(4.0D), field_213690_b)) {
                    if (!(entity instanceof AbstractTaillessEntity)) {
                        entity.hurt(DamageSource.mobAttack(MinotaurEntity.this), 6.0F);
                    }

                    this.launch(entity);
                }

                Vector3d vector3d = MinotaurEntity.this.getBoundingBox().getCenter();

                for(int i = 0; i < 40; ++i) {
                    double d0 = MinotaurEntity.this.random.nextGaussian() * 0.2D;
                    double d1 = MinotaurEntity.this.random.nextGaussian() * 0.2D;
                    double d2 = MinotaurEntity.this.random.nextGaussian() * 0.2D;
                    MinotaurEntity.this.level.addParticle(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
                }
            }

        }

        private void launch(Entity p_213688_1_) {
            double d0 = p_213688_1_.getX() - MinotaurEntity.this.getX();
            double d1 = p_213688_1_.getZ() - MinotaurEntity.this.getZ();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
            p_213688_1_.push(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
        }
    }

    class ChargeGoal extends Goal {
        protected int warmup;
        protected double distance = 4.0D;
        protected int explodetimes = 4;

        private ChargeGoal(){

        }

        @Override
        public boolean canUse() {
            if (MinotaurEntity.this.isAggressive()) {
                if (MinotaurEntity.this.SecondPhase()){
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingentity = MinotaurEntity.this.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            }  else if (!(livingentity instanceof PlayerEntity) || !((PlayerEntity)livingentity).isSpectator() && !((PlayerEntity)livingentity).isCreative()) {
                return this.canUse();
            } else if (MinotaurEntity.this.hasEffect(Effects.GLOWING)){
                return false;
            } else {
                return false;
            }
        }

        @Override
        public void stop(){
            MinotaurEntity.this.setTarget((LivingEntity)null);
            this.warmup = 0;
            MinotaurEntity.this.chargeTick = 0;
            MinotaurEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
            MinotaurEntity.this.setWarning(false);
        }

        public void tick(){
            super.tick();
            ++this.warmup;
            if (this.warmup >= 120){
                ++MinotaurEntity.this.chargeTick;
                if (MinotaurEntity.this.chargeTick == 1) {
                    this.Warning();
                    MinotaurEntity.this.setWarning(true);
                }
                if (MinotaurEntity.this.chargeTick == 60){
                    this.teleport();
                    MinotaurEntity.this.setWarning(false);
                }
                if (MinotaurEntity.this.chargeTick >= 60){
                    this.explodePos();
                }
                if (MinotaurEntity.this.chargeTick >= 100){
                    this.warmup = 0;
                    MinotaurEntity.this.chargeTick = 0;
                    MinotaurEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                }
            }
        }

        protected void Warning() {
            ModifiableAttributeInstance modifiableattributeinstance = MinotaurEntity.this.getAttribute(Attributes.MOVEMENT_SPEED);
            modifiableattributeinstance.removeModifier(MODIFIER);
            modifiableattributeinstance.addTransientModifier(MODIFIER);
            MinotaurEntity.this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 1.0F, 1.0F);
            MinotaurEntity.this.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 60));
        }

        protected void explodePos() {
            LivingEntity livingentity = MinotaurEntity.this.getTarget();
            assert livingentity != null;
            double d0 = Math.min(livingentity.getY(), MinotaurEntity.this.getY());
            double d1 = Math.max(livingentity.getY(), MinotaurEntity.this.getY()) + 1.0D;
            float f = (float) MathHelper.atan2(livingentity.getZ() - MinotaurEntity.this.getZ(), livingentity.getX() - MinotaurEntity.this.getX());
/*            for (int l = 0; l < explodetimes; ++l) {
                double d2 = distance * (double) (l + 1);
                this.spawnExplosions(MinotaurEntity.this.getX() + (double) MathHelper.cos(f) * d2, MinotaurEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, l);
            }*/
            if (MinotaurEntity.this.chargeTick == 65){
                double d2 = distance * (double) (4 + 1);
                this.spawnExplosions(MinotaurEntity.this.getX() + (double) MathHelper.cos(f) * d2, MinotaurEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, 1);
            }
            if (MinotaurEntity.this.chargeTick == 75){
                double d2 = distance * (double) (3 + 1);
                this.spawnExplosions(MinotaurEntity.this.getX() + (double) MathHelper.cos(f) * d2, MinotaurEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, 2);
            }
            if (MinotaurEntity.this.chargeTick == 85){
                double d2 = distance * (double) (2 + 1);
                this.spawnExplosions(MinotaurEntity.this.getX() + (double) MathHelper.cos(f) * d2, MinotaurEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, 3);
            }
            if (MinotaurEntity.this.chargeTick == 95){
                double d2 = distance * (double) (1 + 1);
                this.spawnExplosions(MinotaurEntity.this.getX() + (double) MathHelper.cos(f) * d2, MinotaurEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, 4);
            }
        }

        protected boolean teleport(){
            LivingEntity livingentity = MinotaurEntity.this.getTarget();
            assert livingentity != null;
            float f = (float) MathHelper.atan2(livingentity.getZ() - MinotaurEntity.this.getZ(), livingentity.getX() - MinotaurEntity.this.getX());
            double x = MinotaurEntity.this.getX() + (double) MathHelper.cos(f) * (distance * (double) (5 + 1));
            double z = MinotaurEntity.this.getZ() + (double) MathHelper.sin(f) * (distance * (double) (5 + 1));
            net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(MinotaurEntity.this, x, MinotaurEntity.this.getY(), z, 0);
            boolean flag2 = MinotaurEntity.this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            return flag2;
        }

        private void spawnExplosions(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = MinotaurEntity.this.level.getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(MinotaurEntity.this.level, blockpos1, Direction.UP)) {
                    if (!MinotaurEntity.this.level.isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = MinotaurEntity.this.level.getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(MinotaurEntity.this.level, blockpos);
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
                MinotaurEntity.this.level.explode(MinotaurEntity.this, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, (float) 3, Explosion.Mode.NONE);
            }

        }
    }

    class SecondPhaseIndicator extends Goal {
        private int executed = 0;

        @Override
        public boolean canUse() {
            if (MinotaurEntity.this.SecondPhase()){
                return true;
            } else {
                return false;
            }
        }

        public void tick(){
                if (executed == 0) {
                    LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, level);
                    lightning.setPos(MinotaurEntity.this.getX(), MinotaurEntity.this.getY(), MinotaurEntity.this.getZ());
                    level.addFreshEntity(lightning);
                    MinotaurEntity.this.level.explode(MinotaurEntity.this, MinotaurEntity.this.getX(), MinotaurEntity.this.getY(), MinotaurEntity.this.getZ(), (float) 3, Explosion.Mode.NONE);
                    MinotaurEntity.this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 1.0F, 1.0F);
                    executed = 1;
                }
            }
    }

}
