package com.Polarice3.FireNBlood.entities.utilities;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FakeSeatEntity extends CreatureEntity {
    private int lifetimer;

    public FakeSeatEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.lifetimer = 60;
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    public double getPassengersRidingOffset() {
        return -0.5D;
    }

    public void aiStep() {
        if (!this.isVehicle()){
            --this.lifetimer;
        }
        if (this.lifetimer == 0){
            this.remove();
        }
        super.aiStep();
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D);
    }

    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount);
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
    protected void defineSynchedData() {
        super.defineSynchedData();
    }
}
