package com.Polarice3.FireNBlood.entities.utilities;

import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.ParticleUtil;
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
        this.noPhysics = true;
    }

    public LightningTrapEntity(World worldIn, double x, double y, double z) {
        this(ModEntityType.LIGHTNINGTRAP.get(), worldIn);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        this.duration = compound.getInt("Duration");
        this.durationOnUse = compound.getInt("DurationOnUse");

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
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
        float f = 3.0F;
        float f5 = (float)Math.PI * f * f;
        for(int k1 = 0; (float)k1 < f5; ++k1) {
            float f6 = this.random.nextFloat() * ((float)Math.PI * 2F);
            float f7 = MathHelper.sqrt(this.random.nextFloat()) * f;
            float f8 = MathHelper.cos(f6) * f7;
            float f9 = MathHelper.sin(f6) * f7;
            new ParticleUtil(ParticleTypes.CLOUD, this.getX() + (double)f8, this.getY(), this.getZ() + (double)f9, (0.5D - this.random.nextDouble()) * 0.15D, (double)0.01F, (0.5D - this.random.nextDouble()) * 0.15D);
        }
        if (this.tickCount >= this.getDuration()) {
            LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, level);
            lightning.setPos(this.getX(),this.getY(),this.getZ());
            level.addFreshEntity(lightning);
            this.remove();
        }
    }

    public void setOwner(@Nullable LivingEntity ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.ownerUniqueId);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return new SSpawnObjectPacket(this);
    }
}
