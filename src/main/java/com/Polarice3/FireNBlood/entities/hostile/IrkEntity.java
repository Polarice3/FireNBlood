package com.Polarice3.FireNBlood.entities.hostile;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class IrkEntity extends MonsterEntity implements ICrossbowUser {
    protected static final DataParameter<Byte> IRK_FLAGS = EntityDataManager.createKey(IrkEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> DATA_CHARGING_STATE = EntityDataManager.createKey(IrkEntity.class, DataSerializers.BOOLEAN);
    private MobEntity owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;
    private final Inventory inventory = new Inventory(5);

    public IrkEntity(EntityType<? extends IrkEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.moveController = new IrkEntity.MoveHelperController(this);
        this.experienceValue = 3;
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
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }

    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new IrkEntity.AttackMoveGoal());
        this.goalSelector.addGoal(4, new RangedCrossbowAttackGoal<>(this, 1.0F, 8.0F));
        this.goalSelector.addGoal(8, new IrkEntity.MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, new IrkEntity.CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 14.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(IRK_FLAGS, (byte)0);
        this.dataManager.register(DATA_CHARGING_STATE, false);
    }

    public boolean func_230280_a_(ShootableItem p_230280_1_) {
        return p_230280_1_ == Items.CROSSBOW;
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        ListNBT listnbt = compound.getList("Inventory", 10);

        for(int i = 0; i < listnbt.size(); ++i) {
            ItemStack itemstack = ItemStack.read(listnbt.getCompound(i));
            if (!itemstack.isEmpty()) {
                this.inventory.addItem(itemstack);
            }
        }

        this.setCanPickUpLoot(true);
        if (compound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
        }

        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }

    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        ListNBT listnbt = new ListNBT();

        for(int i = 0; i < this.inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = this.inventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                listnbt.add(itemstack.write(new CompoundNBT()));
            }
        }

        compound.put("Inventory", listnbt);
        if (this.boundOrigin != null) {
            compound.putInt("BoundX", this.boundOrigin.getX());
            compound.putInt("BoundY", this.boundOrigin.getY());
            compound.putInt("BoundZ", this.boundOrigin.getZ());
        }

        if (this.limitedLifespan) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public boolean isCharging() {
        return this.dataManager.get(DATA_CHARGING_STATE);
    }

    public void setCharging(boolean isCharging) {
        this.dataManager.set(DATA_CHARGING_STATE, isCharging);
    }

    @Override
    public void fireProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) {
        this.func_234279_a_(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractIllagerEntity.ArmPose getArmPose() {
        if (this.isCharging()) {
            return AbstractIllagerEntity.ArmPose.CROSSBOW_CHARGE;
        } else if (this.canEquip(Items.CROSSBOW)) {
            return AbstractIllagerEntity.ArmPose.CROSSBOW_HOLD;
        } else {
            return this.isAggressive() ? AbstractIllagerEntity.ArmPose.ATTACKING : AbstractIllagerEntity.ArmPose.NEUTRAL;
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getTrueSource();
        if (entity instanceof IrkEntity){
            return false;
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
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getCreatureAttribute() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    public MobEntity getOwner() {
        return this.owner;
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOriginIn) {
        this.boundOrigin = boundOriginIn;
    }

    private boolean getIrkFlag(int mask) {
        int i = this.dataManager.get(IRK_FLAGS);
        return (i & mask) != 0;
    }

    private void setIrkFlag(int mask, boolean value) {
        int i = this.dataManager.get(IRK_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(IRK_FLAGS, (byte)(i & 255));
    }


    public void func_230283_U__() {
        this.idleTime = 0;
    }

    public void setOwner(MobEntity ownerIn) {
        this.owner = ownerIn;
    }

    public void setLimitedLife(int limitedLifeTicksIn) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VEX_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    public float getBrightness() {
        return 1.0F;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
    }

    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        this.func_234281_b_(this, 1.6F);
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        if (super.replaceItemInInventory(inventorySlot, itemStackIn)) {
            return true;
        } else {
            int i = inventorySlot - 300;
            if (i >= 0 && i < this.inventory.getSizeInventory()) {
                this.inventory.setInventorySlotContents(i, itemStackIn);
                return true;
            } else {
                return false;
            }
        }
    }

    class AttackMoveGoal extends Goal{

        public boolean shouldExecute() {
            return IrkEntity.this.getAttackTarget() != null;
        }

        public boolean shouldContinueExecuting() {
            return IrkEntity.this.getAttackTarget() != null && !IrkEntity.this.isCharging();
        }

        public void tick() {
            LivingEntity livingEntity = IrkEntity.this.getAttackTarget();
            assert livingEntity != null;
            Vector3d vector3d = livingEntity.getEyePosition(1.0F);
            int Random = IrkEntity.this.world.rand.nextInt(2);
            if (Random == 1) {
                IrkEntity.this.moveController.setMoveTo(vector3d.x - IrkEntity.this.rand.nextInt(8), livingEntity.getPosY() + IrkEntity.this.rand.nextInt(4), vector3d.z - IrkEntity.this.rand.nextInt(8), 1.0D);
            } else {
                IrkEntity.this.moveController.setMoveTo(vector3d.x + IrkEntity.this.rand.nextInt(8), livingEntity.getPosY() + IrkEntity.this.rand.nextInt(4), vector3d.z + IrkEntity.this.rand.nextInt(8), 1.0D);
            }
        }
    }

    class CopyOwnerTargetGoal extends TargetGoal {
        private final EntityPredicate field_220803_b = (new EntityPredicate()).setIgnoresLineOfSight().setUseInvisibilityCheck();

        public CopyOwnerTargetGoal(CreatureEntity creature) {
            super(creature, false);
        }

        public boolean shouldExecute() {
            return IrkEntity.this.owner != null && IrkEntity.this.owner.getAttackTarget() != null && this.isSuitableTarget(IrkEntity.this.owner.getAttackTarget(), this.field_220803_b);
        }

        public void startExecuting() {
            IrkEntity.this.setAttackTarget(IrkEntity.this.owner.getAttackTarget());
            super.startExecuting();
        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(IrkEntity irk) {
            super(irk);
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.posX - IrkEntity.this.getPosX(), this.posY - IrkEntity.this.getPosY(), this.posZ - IrkEntity.this.getPosZ());
                double d0 = vector3d.length();
                if (d0 < IrkEntity.this.getBoundingBox().getAverageEdgeLength()) {
                    this.action = MovementController.Action.WAIT;
                    IrkEntity.this.setMotion(IrkEntity.this.getMotion().scale(0.5D));
                } else {
                    IrkEntity.this.setMotion(IrkEntity.this.getMotion().add(vector3d.scale(this.speed * 0.05D / d0)));
                    if (IrkEntity.this.getAttackTarget() == null) {
                        Vector3d vector3d1 = IrkEntity.this.getMotion();
                        IrkEntity.this.rotationYaw = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                        IrkEntity.this.renderYawOffset = IrkEntity.this.rotationYaw;
                    } else {
                        double d2 = IrkEntity.this.getAttackTarget().getPosX() - IrkEntity.this.getPosX();
                        double d1 = IrkEntity.this.getAttackTarget().getPosZ() - IrkEntity.this.getPosZ();
                        IrkEntity.this.rotationYaw = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                        IrkEntity.this.renderYawOffset = IrkEntity.this.rotationYaw;
                    }
                }

            }
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            return !IrkEntity.this.getMoveHelper().isUpdating() && IrkEntity.this.rand.nextInt(7) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = IrkEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = IrkEntity.this.getPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(IrkEntity.this.rand.nextInt(15) - 7, IrkEntity.this.getPosY() + 1.0D, IrkEntity.this.rand.nextInt(15) - 7);
                if (IrkEntity.this.world.isAirBlock(blockpos1)) {
                    IrkEntity.this.moveController.setMoveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (IrkEntity.this.getAttackTarget() == null) {
                        IrkEntity.this.getLookController().setLookPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

}
