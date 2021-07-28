package com.Polarice3.FireNBlood.entities.masters;

import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.ServantTaillessEntity;
import com.Polarice3.FireNBlood.entities.ai.TaillessMeleeGoal;
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
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class MinotaurEntity extends ServantTaillessEntity {
    private static final UUID MODIFIER_UUID = UUID.fromString("22cf9908-b8f8-11eb-8529-0242ac130003");
    private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "ChargeUp", -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof MinotaurEntity);
    };
    protected static final DataParameter<Byte> MINOTAUR_FLAGS = EntityDataManager.createKey(MinotaurEntity.class, DataSerializers.BYTE);
    private int chargeTick;

    public MinotaurEntity(EntityType<? extends ServantTaillessEntity> type, World worldIn){
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 150.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.30D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 6.0D)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .createMutableAttribute(Attributes.ATTACK_SPEED, 4.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.9D)
                .createMutableAttribute(Attributes.ARMOR, 7.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(3, new TaillessMeleeGoal(this, 1.0D, true));
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
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, AbstractTaillessEntity.class)).setCallsForHelp());
    }

    public AbstractTaillessEntity.ArmPose getArmPose() {
        if (this.isAggressive()){
            return AbstractTaillessEntity.ArmPose.ATTACKING;
        } else {
            return AbstractTaillessEntity.ArmPose.NEUTRAL;
        }
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(RegistryHandler.DIAMOND_MACE.get()));
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
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.25F, 1.0F);
    }

    public boolean isBurning() {
        return false;
    }

    public int charging() {
        return this.chargeTick;
    }

    protected boolean SecondPhase(){
        return MinotaurEntity.this.getHealth() <= MinotaurEntity.this.getMaxHealth() / 2.0F;
    }

    public boolean isImmuneToExplosions(){
        return MinotaurEntity.this.SecondPhase();
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(MINOTAUR_FLAGS, (byte)0);
    }

    private boolean getMinotaurFlag(int mask) {
        int i = this.dataManager.get(MINOTAUR_FLAGS);
        return (i & mask) != 0;
    }

    private void setMinotaurFlag(int mask, boolean value) {
        int i = this.dataManager.get(MINOTAUR_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(MINOTAUR_FLAGS, (byte)(i & 255));
    }

    public boolean isWarning(){
        return this.getMinotaurFlag(1);
    }

    public void setWarning(boolean warning){
        this.setMinotaurFlag(1,warning);
    }

    protected void updateAITasks(){
        if (SecondPhase()) {
            if (this.ticksExisted % 20 == 0) {
                this.heal(1.0F);
            }
        } super.updateAITasks();
    }

    public void tick() {
        super.tick();
        if (this.world.isRemote && this.isWarning()) {
            for (int i = 0; i < 2; ++i) {
                float f = this.renderYawOffset * ((float) Math.PI / 180F) + MathHelper.cos((float) this.ticksExisted * 0.6662F) * 0.25F;
                float f1 = MathHelper.cos(f);
                float f2 = MathHelper.sin(f);
                this.world.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getPosXRandom(1.0D), this.getPosY() + 2.6D, this.getPosZRandom(1.0D), 0.7D, 0.5D, 0.2D);
                this.world.addParticle(ParticleTypes.FLAME, this.getPosX() + (double) f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() + (double) f2 * 0.6D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
        ItemEntity itementity = this.entityDropItem(RegistryHandler.PADRE_EFFIGY.get());
        if (itementity != null) {
            itementity.setNoDespawn();
        }

    }

    class RangeAttackGoal extends Goal {
        public int attackTimer;

        private RangeAttackGoal() {
        }

        public boolean shouldExecute() {
            LivingEntity livingentity = MinotaurEntity.this.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if(MinotaurEntity.this.SecondPhase()){
                if (MinotaurEntity.this.getPosY() < livingentity.getPosY()){
                    return true;
                } else {
                    return false;
                }
            } else if (MinotaurEntity.this.getDistance(livingentity) > 8.0D){
                return true;
            } else {
                return false;
            }
        }


        public void startExecuting() {
            this.attackTimer = 0;
        }

        public void tick(){
            LivingEntity livingentity = MinotaurEntity.this.getAttackTarget();
                ++this.attackTimer;
                World world = MinotaurEntity.this.world;
                if (this.attackTimer == 20) {
                    double d1 = 4.0D;
                    Vector3d vector3d = MinotaurEntity.this.getLook(1.0F);
                    double d2 = livingentity.getPosX() - (MinotaurEntity.this.getPosX() + vector3d.x * 4.0D);
                    double d3 = livingentity.getPosYHeight(0.5D) - (0.5D + MinotaurEntity.this.getPosYHeight(0.5D));
                    double d4 = livingentity.getPosZ() - (MinotaurEntity.this.getPosZ() + vector3d.z * 4.0D);
                    SoulFireballEntity soulfireballEntity = new SoulFireballEntity(world, MinotaurEntity.this, d2, d3, d4);
                    soulfireballEntity.setPosition(MinotaurEntity.this.getPosX() + vector3d.x * 4.0D, MinotaurEntity.this.getPosYHeight(0.5D) + 0.5D, soulfireballEntity.getPosZ() + vector3d.z * 4.0D);
                    world.addEntity(soulfireballEntity);
                    if (!MinotaurEntity.this.isSilent()) {
                        MinotaurEntity.this.world.playEvent(null, 1016, MinotaurEntity.this.getPosition(), 0);
                    }
                    this.attackTimer = -20;
                }
                double d0 = MinotaurEntity.this.getDistanceSq(livingentity);
                if (d0 > 16.0D){
                MinotaurEntity.this.getMoveHelper().setMoveTo(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), 1.0D);
            }
        }
    }

    class BuffGoal extends Goal {
        protected int warmup;
        protected int roar;

        public boolean shouldExecute() {
                if (MinotaurEntity.this.isAggressive()){
                    List<ServantTaillessEntity> list = MinotaurEntity.this.world.getEntitiesWithinAABB(ServantTaillessEntity.class, MinotaurEntity.this.getBoundingBox().grow(32.0D, 32.0D, 32.0D), field_213690_b);
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
                    modifiableattributeinstance.applyNonPersistentModifier(MODIFIER);
                    ModifiableAttributeInstance modifiableattributeinstance2 = MinotaurEntity.this.getAttribute(Attributes.ATTACK_SPEED);
                    modifiableattributeinstance2.removeModifier(MODIFIER);
                    modifiableattributeinstance2.applyNonPersistentModifier(MODIFIER);
                }
                if(this.roar >= 20) {
                    this.roaring();
                    for (ServantTaillessEntity ally : MinotaurEntity.this.world.getEntitiesWithinAABB(ServantTaillessEntity.class, MinotaurEntity.this.getBoundingBox().grow(16.0D), field_213690_b)) {
                        ally.addPotionEffect(new EffectInstance(Effects.SPEED, 1000, 1));
                        ally.addPotionEffect(new EffectInstance(Effects.HASTE, 1000, 1));
                    }
                    this.roar = 0;
                    this.warmup = 0;
                    MinotaurEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                    MinotaurEntity.this.getAttribute(Attributes.ATTACK_SPEED).removeModifier(MODIFIER);
                }
            }
        }

        private void roaring() {
            if (MinotaurEntity.this.isAlive()) {
                MinotaurEntity.this.playSound(SoundEvents.ENTITY_SKELETON_HORSE_DEATH, 1.0F, 1.0F);
                for(Entity entity : MinotaurEntity.this.world.getEntitiesWithinAABB(LivingEntity.class, MinotaurEntity.this.getBoundingBox().grow(4.0D), field_213690_b)) {
                    if (!(entity instanceof AbstractTaillessEntity)) {
                        entity.attackEntityFrom(DamageSource.causeMobDamage(MinotaurEntity.this), 6.0F);
                    }

                    this.launch(entity);
                }

                Vector3d vector3d = MinotaurEntity.this.getBoundingBox().getCenter();

                for(int i = 0; i < 40; ++i) {
                    double d0 = MinotaurEntity.this.rand.nextGaussian() * 0.2D;
                    double d1 = MinotaurEntity.this.rand.nextGaussian() * 0.2D;
                    double d2 = MinotaurEntity.this.rand.nextGaussian() * 0.2D;
                    MinotaurEntity.this.world.addParticle(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
                }
            }

        }

        private void launch(Entity p_213688_1_) {
            double d0 = p_213688_1_.getPosX() - MinotaurEntity.this.getPosX();
            double d1 = p_213688_1_.getPosZ() - MinotaurEntity.this.getPosZ();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
            p_213688_1_.addVelocity(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
        }
    }

    class ChargeGoal extends Goal {
        protected int warmup;
        protected double distance = 4.0D;
        protected int explodetimes = 4;

        private ChargeGoal(){

        }

        @Override
        public boolean shouldExecute() {
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
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = MinotaurEntity.this.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            }  else if (!(livingentity instanceof PlayerEntity) || !((PlayerEntity)livingentity).isSpectator() && !((PlayerEntity)livingentity).isCreative()) {
                return this.shouldExecute();
            } else if (MinotaurEntity.this.isPotionActive(Effects.GLOWING)){
                return false;
            } else {
                return false;
            }
        }

        @Override
        public void resetTask(){
            MinotaurEntity.this.setAttackTarget((LivingEntity)null);
            this.warmup = 0;
            MinotaurEntity.this.chargeTick = 0;
            MinotaurEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
            MinotaurEntity.this.getAttribute(Attributes.ATTACK_SPEED).removeModifier(MODIFIER);
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
                    MinotaurEntity.this.getAttribute(Attributes.ATTACK_SPEED).removeModifier(MODIFIER);
                }
            }
        }

        protected void Warning() {
            ModifiableAttributeInstance modifiableattributeinstance = MinotaurEntity.this.getAttribute(Attributes.MOVEMENT_SPEED);
            modifiableattributeinstance.removeModifier(MODIFIER);
            modifiableattributeinstance.applyNonPersistentModifier(MODIFIER);
            ModifiableAttributeInstance modifiableattributeinstance2 = MinotaurEntity.this.getAttribute(Attributes.ATTACK_SPEED);
            modifiableattributeinstance2.removeModifier(MODIFIER);
            modifiableattributeinstance2.applyNonPersistentModifier(MODIFIER);
            MinotaurEntity.this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
            MinotaurEntity.this.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 60));
        }

        protected void explodePos() {
            LivingEntity livingentity = MinotaurEntity.this.getAttackTarget();
            assert livingentity != null;
            double d0 = Math.min(livingentity.getPosY(), MinotaurEntity.this.getPosY());
            double d1 = Math.max(livingentity.getPosY(), MinotaurEntity.this.getPosY()) + 1.0D;
            float f = (float) MathHelper.atan2(livingentity.getPosZ() - MinotaurEntity.this.getPosZ(), livingentity.getPosX() - MinotaurEntity.this.getPosX());
/*            for (int l = 0; l < explodetimes; ++l) {
                double d2 = distance * (double) (l + 1);
                this.spawnExplosions(MinotaurEntity.this.getPosX() + (double) MathHelper.cos(f) * d2, MinotaurEntity.this.getPosZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, l);
            }*/
            if (MinotaurEntity.this.chargeTick == 65){
                double d2 = distance * (double) (4 + 1);
                this.spawnExplosions(MinotaurEntity.this.getPosX() + (double) MathHelper.cos(f) * d2, MinotaurEntity.this.getPosZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, 1);
            }
            if (MinotaurEntity.this.chargeTick == 75){
                double d2 = distance * (double) (3 + 1);
                this.spawnExplosions(MinotaurEntity.this.getPosX() + (double) MathHelper.cos(f) * d2, MinotaurEntity.this.getPosZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, 2);
            }
            if (MinotaurEntity.this.chargeTick == 85){
                double d2 = distance * (double) (2 + 1);
                this.spawnExplosions(MinotaurEntity.this.getPosX() + (double) MathHelper.cos(f) * d2, MinotaurEntity.this.getPosZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, 3);
            }
            if (MinotaurEntity.this.chargeTick == 95){
                double d2 = distance * (double) (1 + 1);
                this.spawnExplosions(MinotaurEntity.this.getPosX() + (double) MathHelper.cos(f) * d2, MinotaurEntity.this.getPosZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, 4);
            }
        }

        protected boolean teleport(){
            LivingEntity livingentity = MinotaurEntity.this.getAttackTarget();
            assert livingentity != null;
            float f = (float) MathHelper.atan2(livingentity.getPosZ() - MinotaurEntity.this.getPosZ(), livingentity.getPosX() - MinotaurEntity.this.getPosX());
            double x = MinotaurEntity.this.getPosX() + (double) MathHelper.cos(f) * (distance * (double) (5 + 1));
            double z = MinotaurEntity.this.getPosZ() + (double) MathHelper.sin(f) * (distance * (double) (5 + 1));
            net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(MinotaurEntity.this, x, MinotaurEntity.this.getPosY(), z, 0);
            boolean flag2 = MinotaurEntity.this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            return flag2;
        }

        private void spawnExplosions(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            do {
                BlockPos blockpos1 = blockpos.down();
                BlockState blockstate = MinotaurEntity.this.world.getBlockState(blockpos1);
                if (blockstate.isSolidSide(MinotaurEntity.this.world, blockpos1, Direction.UP)) {
                    if (!MinotaurEntity.this.world.isAirBlock(blockpos)) {
                        BlockState blockstate1 = MinotaurEntity.this.world.getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(MinotaurEntity.this.world, blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.getEnd(Direction.Axis.Y);
                        }
                    }

                    flag = true;
                    break;
                }

                blockpos = blockpos.down();
            } while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

            if (flag) {
                MinotaurEntity.this.world.createExplosion(MinotaurEntity.this, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, (float) 3, Explosion.Mode.NONE);
            }

        }
    }

    class SecondPhaseIndicator extends Goal {
        private int executed = 0;

        @Override
        public boolean shouldExecute() {
            if (MinotaurEntity.this.SecondPhase()){
                return true;
            } else {
                return false;
            }
        }

        public void tick(){
                if (executed == 0) {
                    LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
                    lightning.setPosition(MinotaurEntity.this.getPosX(), MinotaurEntity.this.getPosY(), MinotaurEntity.this.getPosZ());
                    world.addEntity(lightning);
                    MinotaurEntity.this.world.createExplosion(MinotaurEntity.this, MinotaurEntity.this.getPosX(), MinotaurEntity.this.getPosY(), MinotaurEntity.this.getPosZ(), (float) 3, Explosion.Mode.NONE);
                    MinotaurEntity.this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
                    executed = 1;
                }
            }
    }

}
