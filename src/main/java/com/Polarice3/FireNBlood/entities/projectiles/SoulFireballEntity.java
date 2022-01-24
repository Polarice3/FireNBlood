package com.Polarice3.FireNBlood.entities.projectiles;

import com.Polarice3.FireNBlood.entities.masters.TaillessAnathemaEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SoulFireballEntity extends DamagingProjectileEntity {
    public SoulFireballEntity(EntityType<? extends DamagingProjectileEntity> type, World world) {
        super(type, world);
    }

    public SoulFireballEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(ModEntityType.SOUL_FIREBALL.get(), x, y, z, accelX, accelY, accelZ, worldIn);
    }

    public SoulFireballEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(ModEntityType.SOUL_FIREBALL.get(), shooter, accelX, accelY, accelZ, worldIn);
    }

    protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
        super.onEntityHit(p_213868_1_);
        if (!this.world.isRemote) {
            Entity entity = p_213868_1_.getEntity();
            if (!entity.isImmuneToFire()) {
                Entity entity1 = this.getShooter();
                int i = entity.getFireTimer();
                entity.setFire(15);
                boolean flag = entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, entity1), 6.0F);
                if (!flag) {
                    entity.forceFireTicks(i);
                } else if (entity1 instanceof LivingEntity) {
                    this.applyEnchantments((LivingEntity)entity1, entity);
                }
            } else if (entity instanceof TaillessAnathemaEntity){
                ((TaillessAnathemaEntity) entity).heal(5.0F);
            }

        }
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            this.world.createExplosion(null, this.getPosX(), this.getPosY(), this.getPosZ(), 3.0F, Explosion.Mode.NONE);
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
