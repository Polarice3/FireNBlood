package com.Polarice3.FireNBlood.entities.bosses;

import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.IrkEntity;
import com.Polarice3.FireNBlood.entities.neutral.AbstractProtectorEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class VizierEntity extends SpellcastingIllagerEntity implements IChargeableMob{
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof VizierEntity);
    };
    protected static final DataParameter<Byte> VIZIER_FLAGS = EntityDataManager.createKey(VizierEntity.class, DataSerializers.BYTE);
    private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS));
    public int casting;
    public int finishcasting;
    public int casttimes;

    public VizierEntity(EntityType<? extends VizierEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new VizierEntity.MoveHelperController(this);
        this.experienceValue = 50;
        this.finishcasting = 0;
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.doBlockCollisions();
    }

    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);
        if (!this.isSpellcasting()){
            ++this.casting;
        }
        if (this.finishcasting > 0){
            this.casting = 0;
            this.finishcasting = 0;
        }
    }

    protected SoundEvent getSpellSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new VizierEntity.FangsSpellGoal());
        this.goalSelector.addGoal(1, new VizierEntity.HealGoal());
        this.goalSelector.addGoal(4, new VizierEntity.ChargeAttackGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractTaillessEntity.class, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractProtectorEntity.class, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 200.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(VIZIER_FLAGS, (byte)0);
    }

    public void onDeath(DamageSource cause) {
        for (IrkEntity ally : VizierEntity.this.world.getEntitiesWithinAABB(IrkEntity.class, VizierEntity.this.getBoundingBox().grow(64.0D), field_213690_b)) {
            ally.attackEntityFrom(DamageSource.STARVE, 200.0F);
        }
        super.onDeath(cause);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getTrueSource();
        if (entity instanceof LivingEntity){
            if (entity instanceof IrkEntity){
                return false;
            } else {
                int random = this.world.rand.nextInt(2);
                if (random == 1 || this.world.getDifficulty() == Difficulty.HARD) {
                    IrkEntity irk = new IrkEntity(ModEntityType.IRK.get(), this.world);
                    irk.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.CROSSBOW));
                    irk.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
                    irk.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
                    irk.setOwner(this);
                    this.world.addEntity(irk);
                }
                return super.attackEntityFrom(source, amount);
            }
        } else if (this.isSpellcasting()){
            super.attackEntityFrom(source, amount/2);
            return true;
        } else {
            return super.attackEntityFrom(source, amount);
        }
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (entityIn == this) {
            return true;
        } else if (super.isOnSameTeam(entityIn)) {
            return true;
        } else if (entityIn instanceof IrkEntity) {
            return this.isOnSameTeam(((IrkEntity)entityIn).getOwner());
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getCreatureAttribute() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }

    }

    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    private boolean getVizierFlag(int mask) {
        int i = this.dataManager.get(VIZIER_FLAGS);
        return (i & mask) != 0;
    }

    private void setVizierFlag(int mask, boolean value) {
        int i = this.dataManager.get(VIZIER_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(VIZIER_FLAGS, (byte)(i & 255));
    }

    public boolean isCharging() {
        return this.getVizierFlag(1);
    }

    public void setCharging(boolean charging) {
        this.setVizierFlag(1, charging);
    }

    public boolean isSpellcasting(){
        return this.getVizierFlag(2);
    }

    public void setSpellcasting(boolean spellcasting){
        this.setVizierFlag(2, spellcasting);
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractIllagerEntity.ArmPose getArmPose() {
        if (this.isCharging()) {
            return AbstractIllagerEntity.ArmPose.ATTACKING;
        } else if (this.isSpellcasting()){
            return ArmPose.SPELLCASTING;
        } else {
            return this.getCelebrating() ? AbstractIllagerEntity.ArmPose.CELEBRATING : AbstractIllagerEntity.ArmPose.CROSSED;
        }
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
    }

    protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
        ItemEntity itementity = this.entityDropItem(RegistryHandler.SOULRUBY.get());
        if (itementity != null) {
            itementity.setNoDespawn();
        }

    }

    public void applyWaveBonus(int wave, boolean p_213660_2_) {
    }

    protected void updateAITasks() {
        super.updateAITasks();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public SoundEvent getRaidLossSound() {
        return SoundEvents.ENTITY_EVOKER_CELEBRATE;
    }

    public boolean isNonBoss() {
        return false;
    }

    @Override
    public boolean isCharged() {
        return this.isSpellcasting();
    }

    class FangsSpellGoal extends Goal {
        int duration;
        int duration2;
        private FangsSpellGoal() {
        }

        public boolean shouldExecute() {
            return VizierEntity.this.casting >= 200 && VizierEntity.this.getAttackTarget() != null && !VizierEntity.this.isCharging() && VizierEntity.this.casttimes != 2;
        }

        public void startExecuting() {
            VizierEntity.this.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
            VizierEntity.this.setSpellcasting(true);
        }

/*        public boolean shouldContinueExecuting() {
            return VizierEntity.this.getMoveHelper().isUpdating()
                    && VizierEntity.this.isSpellcasting()
                    && VizierEntity.this.getAttackTarget() != null
                    && VizierEntity.this.getAttackTarget().isAlive();
        }*/

        public void resetTask() {
            VizierEntity.this.setSpellcasting(false);
        }

        public void tick() {
            LivingEntity livingentity = VizierEntity.this.getAttackTarget();
            ++this.duration;
            ++this.duration2;
            if (VizierEntity.this.world.getDifficulty() == Difficulty.HARD) {
                VizierEntity.this.moveController.setMoveTo(livingentity.getPosX() - 4, livingentity.getPosY() + 2.0D, livingentity.getPosZ() - 4, 1.0F);
            } else {
                VizierEntity.this.moveController.setMoveTo(livingentity.getPosX() - 2, livingentity.getPosY() + 2.0D, livingentity.getPosZ() - 2, 1.0F);
            }
            if (VizierEntity.this.getHealth() <= VizierEntity.this.getMaxHealth()/2) {
                if (this.duration >= 5) {
                    this.duration = 0;
                    float f = (float) MathHelper.atan2(livingentity.getPosZ() - VizierEntity.this.getPosZ(), livingentity.getPosX() - VizierEntity.this.getPosX());
                    this.spawnFangs(livingentity.getPosX(), livingentity.getPosZ(), livingentity.getPosY(), livingentity.getPosY() + 1.0D, f, 1);
                }
            } else {
                if (this.duration >= 10) {
                    this.duration = 0;
                    float f = (float) MathHelper.atan2(livingentity.getPosZ() - VizierEntity.this.getPosZ(), livingentity.getPosX() - VizierEntity.this.getPosX());
                    this.spawnFangs(livingentity.getPosX(), livingentity.getPosZ(), livingentity.getPosY(), livingentity.getPosY() + 1.0D, f, 1);
                }
            }
            if (this.duration2 >= 160){
                VizierEntity.this.setSpellcasting(false);
                VizierEntity.this.finishcasting = 1;
                ++VizierEntity.this.casttimes;
                this.duration2 = 0;
                this.duration = 0;
            }
        }

        private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            do {
                BlockPos blockpos1 = blockpos.down();
                BlockState blockstate = VizierEntity.this.world.getBlockState(blockpos1);
                if (blockstate.isSolidSide(VizierEntity.this.world, blockpos1, Direction.UP)) {
                    if (!VizierEntity.this.world.isAirBlock(blockpos)) {
                        BlockState blockstate1 = VizierEntity.this.world.getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(VizierEntity.this.world, blockpos);
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
                VizierEntity.this.world.addEntity(new EvokerFangsEntity(VizierEntity.this.world, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, VizierEntity.this));
            }

        }
    }

    class HealGoal extends Goal {
        int duration3;

        private HealGoal() {
        }

        public boolean shouldExecute(){
            return VizierEntity.this.getAttackTarget() != null && !VizierEntity.this.isCharging() && VizierEntity.this.casting >= 100 && VizierEntity.this.casttimes == 2;
        }

        public void startExecuting() {
            VizierEntity.this.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
            VizierEntity.this.playSound(SoundEvents.ENTITY_EVOKER_CELEBRATE, 1.0F, 1.0F);
            VizierEntity.this.setSpellcasting(true);
        }

        public void resetTask() {
            VizierEntity.this.setSpellcasting(false);
        }

        public void tick() {
            LivingEntity livingentity = VizierEntity.this.getAttackTarget();
            ++this.duration3;
            if (VizierEntity.this.world.getDifficulty() == Difficulty.HARD) {
                VizierEntity.this.moveController.setMoveTo(livingentity.getPosX() - 4, livingentity.getPosY() + 2.0D, livingentity.getPosZ() - 4, 1.0F);
            } else {
                VizierEntity.this.moveController.setMoveTo(livingentity.getPosX() - 2, livingentity.getPosY() + 2.0D, livingentity.getPosZ() - 2, 1.0F);
            }
            if (this.duration3 >= 60){
                VizierEntity.this.playSound(SoundEvents.ITEM_TOTEM_USE, 1.0F, 1.0F);
                for (IrkEntity ally : VizierEntity.this.world.getEntitiesWithinAABB(IrkEntity.class, VizierEntity.this.getBoundingBox().grow(64.0D), field_213690_b)) {
                    ally.attackEntityFrom(DamageSource.STARVE, 200.0F);
                    VizierEntity.this.heal(5.0F);
                }
                this.duration3 = 0;
                VizierEntity.this.setSpellcasting(false);
                VizierEntity.this.casttimes = 0;
                VizierEntity.this.finishcasting = 1;
            }
        }

    }

    class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            if (VizierEntity.this.getAttackTarget() != null
                    && !VizierEntity.this.getMoveHelper().isUpdating()
                    && !VizierEntity.this.isSpellcasting()
                    && VizierEntity.this.casting < 200
                    && VizierEntity.this.rand.nextInt(7) == 0) {
                return VizierEntity.this.getDistanceSq(VizierEntity.this.getAttackTarget()) > 4.0D;
            } else {
                return false;
            }
        }

        public boolean shouldContinueExecuting() {
            return VizierEntity.this.getMoveHelper().isUpdating()
                    && VizierEntity.this.isCharging()
                    && !VizierEntity.this.isSpellcasting()
                    && VizierEntity.this.casting < 200
                    && VizierEntity.this.getAttackTarget() != null
                    && VizierEntity.this.getAttackTarget().isAlive();
        }

        public void startExecuting() {
            LivingEntity livingentity = VizierEntity.this.getAttackTarget();
            Vector3d vector3d = livingentity.getPositionVec();
            VizierEntity.this.moveController.setMoveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            VizierEntity.this.setCharging(true);
            VizierEntity.this.playSound(SoundEvents.ENTITY_EVOKER_CELEBRATE, 1.0F, 1.0F);
        }

        public void resetTask() {
            VizierEntity.this.setCharging(false);
        }

        public void tick() {
            LivingEntity livingentity = VizierEntity.this.getAttackTarget();
            if (VizierEntity.this.getBoundingBox().grow(1.0D).intersects(livingentity.getBoundingBox())) {
                VizierEntity.this.attackEntityAsMob(livingentity);
                if (livingentity instanceof PlayerEntity && livingentity.isActiveItemStackBlocking()){
                    ((PlayerEntity) livingentity).disableShield(true);
                }
                VizierEntity.this.setCharging(false);
            } else {
                double d0 = VizierEntity.this.getDistanceSq(livingentity);
                if (d0 < 9.0D) {
                    Vector3d vector3d = livingentity.getEyePosition(1.0F);
                    VizierEntity.this.moveController.setMoveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                }
            }

        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(VizierEntity vizier) {
            super(vizier);
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.posX - VizierEntity.this.getPosX(), this.posY - VizierEntity.this.getPosY(), this.posZ - VizierEntity.this.getPosZ());
                double d0 = vector3d.length();
                if (d0 < VizierEntity.this.getBoundingBox().getAverageEdgeLength()) {
                    this.action = MovementController.Action.WAIT;
                    VizierEntity.this.setMotion(VizierEntity.this.getMotion().scale(0.5D));
                } else {
                    VizierEntity.this.setMotion(VizierEntity.this.getMotion().add(vector3d.scale(this.speed * 0.05D / d0)));
                    if (VizierEntity.this.getAttackTarget() == null) {
                        Vector3d vector3d1 = VizierEntity.this.getMotion();
                        VizierEntity.this.rotationYaw = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                    } else {
                        double d2 = VizierEntity.this.getAttackTarget().getPosX() - VizierEntity.this.getPosX();
                        double d1 = VizierEntity.this.getAttackTarget().getPosZ() - VizierEntity.this.getPosZ();
                        VizierEntity.this.rotationYaw = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                    }
                    VizierEntity.this.renderYawOffset = VizierEntity.this.rotationYaw;
                }

            }
        }
    }
}
