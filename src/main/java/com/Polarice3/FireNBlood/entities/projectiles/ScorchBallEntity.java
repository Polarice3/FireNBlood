package com.Polarice3.FireNBlood.entities.projectiles;

import com.Polarice3.FireNBlood.entities.hostile.ScorchEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class ScorchBallEntity extends DamagingProjectileEntity {
    public ScorchBallEntity(EntityType<? extends DamagingProjectileEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public ScorchBallEntity(World world, LivingEntity livingEntity, double d1, double d2, double d3) {
        super(ModEntityType.SCORCHBALL.get(), livingEntity, d1, d2, d3, world);
    }

    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (result.getType() != RayTraceResult.Type.ENTITY) {
            if (!this.world.isRemote) {
                for(int i = 0; i < 3; ++i) {
                    ScorchEntity scorchEntity = new ScorchEntity(ModEntityType.SCORCH.get(), this.world);
                    BlockPos blockpos = this.getPosition().add(-2 + this.rand.nextInt(5), 1, -2 + this.rand.nextInt(5));
                    scorchEntity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
                    scorchEntity.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
                    scorchEntity.setBoundOrigin(blockpos);
                    scorchEntity.setLimitedLife(20 * (30 + this.rand.nextInt(90)));
                    this.world.addEntity(scorchEntity);
                }
                this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 5.0F, Explosion.Mode.NONE);
                this.remove();
            }

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
        return ParticleTypes.FLAME;
    }

    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
