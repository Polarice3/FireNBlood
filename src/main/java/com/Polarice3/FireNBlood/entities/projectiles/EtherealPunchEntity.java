package com.Polarice3.FireNBlood.entities.projectiles;

import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EtherealPunchEntity extends DamagingProjectileEntity {
    public EtherealPunchEntity(EntityType<? extends DamagingProjectileEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public EtherealPunchEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(ModEntityType.ETHEREAL_PUNCH.get(), x, y, z, accelX, accelY, accelZ, worldIn);
    }

    public EtherealPunchEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(ModEntityType.ETHEREAL_PUNCH.get(), shooter, accelX, accelY, accelZ, worldIn);
    }

    protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
        super.onEntityHit(p_213868_1_);
        Entity entity = p_213868_1_.getEntity();
        Entity entity1 = this.func_234616_v_();
        LivingEntity livingentity = entity1 instanceof LivingEntity ? (LivingEntity)entity1 : null;
        assert livingentity != null;
        boolean flag = entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, livingentity).setProjectile(), 4.0F);
        if (flag) {
            this.applyEnchantments(livingentity, entity);
        }

    }

    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 1.0F, false, Explosion.Mode.NONE);
            this.remove();
        }
    }

    public boolean isBurning() {
        return false;
    }

    public boolean isImmuneToExplosions(){return true;}

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    protected IParticleData getParticle() {
        return ParticleTypes.SOUL_FIRE_FLAME;
    }

    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
