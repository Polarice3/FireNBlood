package com.Polarice3.FireNBlood.entities.hostile.cultists;

import com.Polarice3.FireNBlood.entities.hostile.tailless.BlackBullEntity;
import com.Polarice3.FireNBlood.entities.neutral.protectors.AbstractProtectorEntity;
import com.Polarice3.FireNBlood.entities.projectiles.WitchBombEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class FanaticEntity extends AbstractCultistEntity {

    public FanaticEntity(EntityType<? extends FanaticEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 16.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, -1.0F);
        ((GroundPathNavigator)this.getNavigator()).setBreakDoors(true);
        this.getNavigator().setCanSwim(true);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(2, new ThrowBombsGoal(this));
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

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_EVOKER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_EVOKER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_EVOKER_DEATH;
    }

    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    public boolean hasBomb(){
        return this.getHeldItem(Hand.OFF_HAND).getItem() == RegistryHandler.WITCHBOMB.get();
    }

    static class ThrowBombsGoal extends Goal{
        public int bombTimer;
        public FanaticEntity fanatic;

        public ThrowBombsGoal(FanaticEntity fanatic){
            this.fanatic = fanatic;
        }

        @Override
        public boolean shouldExecute() {
            if (this.fanatic.getAttackTarget() != null && this.fanatic.hasBomb()){
                LivingEntity entity = this.fanatic.getAttackTarget();
                return this.fanatic.getDistance(entity) >= 8.0;
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.fanatic.getAttackTarget() != null && this.fanatic.hasBomb();
        }

        @Override
        public void resetTask() {
            this.bombTimer = 0;
        }

        @Override
        public void tick() {
            super.tick();
            ++this.bombTimer;
            if (this.bombTimer >= 200) {
                LivingEntity livingEntity = this.fanatic.getAttackTarget();
                WitchBombEntity snowballentity = new WitchBombEntity(this.fanatic.world, this.fanatic);
                Vector3d vector3d = livingEntity.getMotion();
                double d0 = livingEntity.getPosX() + vector3d.x - this.fanatic.getPosX();
                double d1 = livingEntity.getPosYEye() - (double) 1.1F - this.fanatic.getPosY();
                double d2 = livingEntity.getPosZ() + vector3d.z - this.fanatic.getPosZ();
                float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
                snowballentity.shoot(d0, d1 + (double) (f * 0.2F), d2, 0.75F, 2.0F);
                this.fanatic.playSound(SoundEvents.ENTITY_WITCH_THROW, 1.0F, 0.4F / (this.fanatic.getRNG().nextFloat() * 0.4F + 0.8F));
                this.fanatic.world.addEntity(snowballentity);
                this.bombTimer = 0;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        if (this.isAggressive()) {
            if (!hasBomb()) {
                return ArmPose.ATTACKING;
            } else {
                return ArmPose.BOMB_AND_WEAPON;
            }
        } else {
            return ArmPose.CROSSED;
        }
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
        int random = this.rand.nextInt(9);
        int random2 = this.rand.nextInt(5);
        int random3 = this.rand.nextInt(4);
        switch (random){
            case 0:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
                break;
            case 1:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_AXE));
                break;
            case 2:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_PICKAXE));
                break;
            case 3:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
                break;
            case 4:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
                break;
            case 5:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_PICKAXE));
                break;
            case 6:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                break;
            case 7:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
                break;
            case 8:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_PICKAXE));
        }
        switch (random2){
            case 0:
            case 1:
            case 2:
                break;
            case 3:
                this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                break;
            case 4:
                this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        }
        if (random3 == 0) {
            this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(RegistryHandler.WITCHBOMB.get()));
        }
    }

    @Override
    public SoundEvent getRaidLossSound() {
        return SoundEvents.ENTITY_EVOKER_CELEBRATE;
    }
}
