package com.Polarice3.FireNBlood.entities.utilities;

import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.UUID;

public class LightningTrapEntity extends Entity {
    private int duration = 600;
    private int durationOnUse;
    private LivingEntity owner;
    private UUID ownerUniqueId;

    public LightningTrapEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.noClip = true;
    }

    public LightningTrapEntity(World worldIn, double x, double y, double z) {
        this(ModEntityType.LIGHTNINGTRAP.get(), worldIn);
        this.setPosition(x, y, z);
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.duration = compound.getInt("Duration");
        this.durationOnUse = compound.getInt("DurationOnUse");

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("Duration", this.duration);
        compound.putInt("DurationOnUse", this.durationOnUse);

    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int durationIn) {
        this.duration = durationIn;
    }

    public void tick() {
        super.tick();
        if (this.world.isRemote){
            IParticleData iparticledata = this.getParticle();
            float f = 3.0F;
            float f5 = (float)Math.PI * f * f;
            for(int k1 = 0; (float)k1 < f5; ++k1) {
                float f6 = this.rand.nextFloat() * ((float)Math.PI * 2F);
                float f7 = MathHelper.sqrt(this.rand.nextFloat()) * f;
                float f8 = MathHelper.cos(f6) * f7;
                float f9 = MathHelper.sin(f6) * f7;
                this.world.addParticle(iparticledata, this.getPosX() + (double)f8, this.getPosY(), this.getPosZ() + (double)f9, (0.5D - this.rand.nextDouble()) * 0.15D, (double)0.01F, (0.5D - this.rand.nextDouble()) * 0.15D);
            }
        }
        if (this.ticksExisted >= this.duration) {
            LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
            lightning.setPosition(this.getPosX(),this.getPosY(),this.getPosZ());
            world.addEntity(lightning);
            this.remove();
        }
    }

    public void setOwner(@Nullable LivingEntity ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.world instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.world).getEntityByUuid(this.ownerUniqueId);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    protected IParticleData getParticle() {
        return ParticleTypes.CLOUD;
    }

    public PushReaction getPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return new SSpawnObjectPacket(this);
    }
}
