package com.Polarice3.FireNBlood.entities.utilities;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FakeSeatEntity extends Entity {
    private int lifetimer;

    public FakeSeatEntity(EntityType<?> type, World worldIn) {
        super(type, worldIn);
    }

    public double getMountedYOffset() {
        return 2.0D;
    }

    public void livingTick() {
        if (this.getRidingEntity() == null){
            --this.lifetimer;
        } else {
            this.lifetimer = 5;
        }
        if (this.lifetimer == 0){
            this.remove();
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    protected void registerData() {
    }

    @Override
    public void readAdditional(CompoundNBT compound) {

    }

    @Override
    public void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return new SSpawnObjectPacket(this);
    }
}
