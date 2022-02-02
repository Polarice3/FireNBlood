package com.Polarice3.FireNBlood.entities.hostile.tailless;

import com.Polarice3.FireNBlood.entities.ally.FriendlyVexEntity;
import com.Polarice3.FireNBlood.entities.ally.SummonedEntity;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AbstractTaillessEntity extends MonsterEntity {
    private LivingEntity Target;
    public int Random;

    protected AbstractTaillessEntity(EntityType<? extends AbstractTaillessEntity> type, World worldIn){
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    public int getRandom() {
        return Random;
    }

    public void setRandom(int random) {
        Random = random;
    }

    public boolean EvilEyeGiver(){
        return Random > 0;
    }

    public AbstractTaillessEntity.ArmPose getArmPose() {
        return AbstractTaillessEntity.ArmPose.NEUTRAL;
    }

    public void setTarget(@Nullable LivingEntity TargetIn) {
        this.Target = TargetIn;
    }

    public void onDeath(DamageSource cause) {
        Entity entity = cause.getTrueSource();
        PlayerEntity playerentity;
        if (this.EvilEyeGiver()) {
            if (entity instanceof PlayerEntity) {
                playerentity = (PlayerEntity) entity;
                int random = this.world.rand.nextInt(this.getRandom());
                if (random == 0) {
                    EffectInstance effectinstance1 = playerentity.getActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                    if (effectinstance1 == null) {
                        EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                        playerentity.addPotionEffect(effectinstance);
                    } else {
                        int amp = effectinstance1.getAmplifier();
                        int i = amp + 1;
                        i = MathHelper.clamp(i, 0, 5);
                        playerentity.removeActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                        EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                        playerentity.addPotionEffect(effectinstance);
                    }
                }
            } else if (entity instanceof WolfEntity) {
                WolfEntity wolfentity = (WolfEntity) entity;
                LivingEntity livingentity = wolfentity.getOwner();
                if (wolfentity.isTamed() && livingentity instanceof PlayerEntity) {
                    playerentity = (PlayerEntity) livingentity;
                    int random = this.world.rand.nextInt(this.getRandom());
                    if (random == 0) {
                        EffectInstance effectinstance1 = playerentity.getActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                        if (effectinstance1 == null) {
                            EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                            playerentity.addPotionEffect(effectinstance);
                        } else {
                            int amp = effectinstance1.getAmplifier();
                            int i = amp + 1;
                            i = MathHelper.clamp(i, 0, 5);
                            playerentity.removeActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                            EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                            playerentity.addPotionEffect(effectinstance);
                        }
                    }
                }
            } else if (entity instanceof SummonedEntity) {
                SummonedEntity wolfentity = (SummonedEntity) entity;
                LivingEntity livingentity = wolfentity.getTrueOwner();
                if (wolfentity.getTrueOwner() != null && livingentity instanceof PlayerEntity) {
                    playerentity = (PlayerEntity) livingentity;
                    int random = this.world.rand.nextInt(this.getRandom());
                    if (random == 0) {
                        EffectInstance effectinstance1 = playerentity.getActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                        if (effectinstance1 == null) {
                            EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                            playerentity.addPotionEffect(effectinstance);
                        } else {
                            int amp = effectinstance1.getAmplifier();
                            int i = amp + 1;
                            i = MathHelper.clamp(i, 0, 5);
                            playerentity.removeActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                            EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                            playerentity.addPotionEffect(effectinstance);
                        }
                    }
                }
            } else if (entity instanceof FriendlyVexEntity) {
                FriendlyVexEntity wolfentity = (FriendlyVexEntity) entity;
                LivingEntity livingentity = wolfentity.getTrueOwner();
                if (wolfentity.getTrueOwner() != null && livingentity instanceof PlayerEntity) {
                    playerentity = (PlayerEntity) livingentity;
                    int random = this.world.rand.nextInt(this.getRandom());
                    if (random == 0) {
                        EffectInstance effectinstance1 = playerentity.getActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                        if (effectinstance1 == null) {
                            EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                            playerentity.addPotionEffect(effectinstance);
                        } else {
                            int amp = effectinstance1.getAmplifier();
                            int i = amp + 1;
                            i = MathHelper.clamp(i, 0, 5);
                            playerentity.removeActivePotionEffect(RegistryHandler.EVIL_EYE.get());
                            EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                            playerentity.addPotionEffect(effectinstance);
                        }
                    }
                }
            }
        }
        super.onDeath(cause);
    }

    @Nullable
    public LivingEntity getTarget() {
        return this.Target;
    }

    public static enum ArmPose {
        ATTACKING,
        SPELLCASTING,
        NEUTRAL,
        SHOOT;
    }
}
