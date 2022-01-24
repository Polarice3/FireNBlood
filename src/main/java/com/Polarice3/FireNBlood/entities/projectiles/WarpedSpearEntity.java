package com.Polarice3.FireNBlood.entities.projectiles;

import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class WarpedSpearEntity extends AbstractArrowEntity {
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof WarpedSpearEntity);
    };
    private static final DataParameter<Boolean> field_226571_aq_ = EntityDataManager.createKey(WarpedSpearEntity.class, DataSerializers.BOOLEAN);
    private ItemStack thrownStack = new ItemStack(RegistryHandler.WARPED_SPEAR.get());
    private boolean dealtDamage;
    public int returningTicks;

    public WarpedSpearEntity(EntityType<? extends WarpedSpearEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public WarpedSpearEntity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(ModEntityType.WARPED_SPEAR.get(), thrower, worldIn);
        this.thrownStack = thrownStackIn.copy();
        this.dataManager.set(field_226571_aq_, thrownStackIn.hasEffect());
    }

    @OnlyIn(Dist.CLIENT)
    public WarpedSpearEntity(World worldIn, double x, double y, double z) {
        super(ModEntityType.WARPED_SPEAR.get(), x, y, z, worldIn);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(field_226571_aq_, false);
    }

    public void tick() {
        if (this.timeInGround > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getShooter();
        if ((this.dealtDamage || this.getNoClip()) && entity != null) {
            int i = 3;
            if (!this.shouldReturnToThrower()) {
                if (!this.world.isRemote && this.pickupStatus == AbstractArrowEntity.PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1F);
                }

                this.remove();
            } else {
                this.setNoClip(true);
                Vector3d vector3d = new Vector3d(entity.getPosX() - this.getPosX(), entity.getPosYEye() - this.getPosY(), entity.getPosZ() - this.getPosZ());
                this.setRawPosition(this.getPosX(), this.getPosY() + vector3d.y * 0.015D * (double)i, this.getPosZ());
                if (this.world.isRemote) {
                    this.lastTickPosY = this.getPosY();
                }

                double d0 = 0.05D * (double)i;
                this.setMotion(this.getMotion().scale(0.95D).add(vector3d.normalize().scale(d0)));
                if (this.returningTicks == 0) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returningTicks;
            }
        }

        super.tick();
    }

    private boolean shouldReturnToThrower() {
        Entity entity = this.getShooter();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    protected ItemStack getArrowStack() {
        return this.thrownStack.copy();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean func_226572_w_() {
        return this.dataManager.get(field_226571_aq_);
    }

    @Nullable
    protected EntityRayTraceResult rayTraceEntities(Vector3d startVec, Vector3d endVec) {
        return this.dealtDamage ? null : super.rayTraceEntities(startVec, endVec);
    }

    protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
        Entity entity = p_213868_1_.getEntity();
        float f = 8.0F;
        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity;
            f += EnchantmentHelper.getModifierForCreature(this.thrownStack, livingentity.getCreatureAttribute());
        }

        Entity entity1 = this.getShooter();
        DamageSource damagesource = DamageSource.causeTridentDamage(this, (Entity)(entity1 == null ? this : entity1));
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.ITEM_TRIDENT_HIT;
        if (entity.attackEntityFrom(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity)entity;
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.applyThornEnchantments(livingentity1, entity1);
                    EnchantmentHelper.applyArthropodEnchantments((LivingEntity)entity1, livingentity1);
                    livingentity1.setFire(30);
                    livingentity1.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200));
                }
                this.arrowHit(livingentity1);
            }
        }

        this.setMotion(this.getMotion().mul(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        if (this.world instanceof ServerWorld) {
            this.shockwave();
        }

        this.playSound(soundevent, f1, 1.0F);
    }

    private void shockwave() {
        for(Entity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(4.0D), field_213690_b)) {
            entity.attackEntityFrom(DamageSource.causeTridentDamage(this, entity), 6.0F);
            this.launch(entity);
        }
        this.playSound(SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F, 1.0F);

        Vector3d vector3d = this.getBoundingBox().getCenter();

        for(int i = 0; i < 40; ++i) {
            double d0 = this.rand.nextGaussian() * 0.2D;
            double d1 = this.rand.nextGaussian() * 0.2D;
            double d2 = this.rand.nextGaussian() * 0.2D;
            this.world.addParticle(ParticleTypes.FLAME, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
        }

    }

    private void launch(Entity p_213688_1_) {
        double d0 = p_213688_1_.getPosX() - this.getPosX();
        double d1 = p_213688_1_.getPosZ() - this.getPosZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.addVelocity(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }

    protected SoundEvent getHitEntitySound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    public void onCollideWithPlayer(PlayerEntity entityIn) {
        Entity entity = this.getShooter();
        if (entity == null || entity.getUniqueID() == entityIn.getUniqueID()) {
            super.onCollideWithPlayer(entityIn);
        }
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("WarpedSpear", 10)) {
            this.thrownStack = ItemStack.read(compound.getCompound("WarpedSpear"));
        }

        this.dealtDamage = compound.getBoolean("DealtDamage");
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("WarpedSpear", this.thrownStack.write(new CompoundNBT()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void func_225516_i_() {
        if (this.pickupStatus != AbstractArrowEntity.PickupStatus.ALLOWED) {
            super.func_225516_i_();
        }

    }

    protected float getWaterDrag() {
        return 0.01F;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
