package com.Polarice3.FireNBlood.entities.neutral;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FakeSeatEntity extends CreatureEntity {
    private int lifetimer;

    public FakeSeatEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 200.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 0D)
                .createMutableAttribute(Attributes.ATTACK_SPEED, 0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return false;
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

    protected boolean isMovementBlocked() {
        return super.isMovementBlocked();
    }

    @Override
    protected void registerData() {
        super.registerData();
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return null;
    }
}
