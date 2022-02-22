package com.Polarice3.FireNBlood.entities.hostile.tailless;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.entities.ally.FriendlyVexEntity;
import com.Polarice3.FireNBlood.entities.ally.SummonedEntity;
import com.Polarice3.FireNBlood.entities.hostile.TankEntity;
import com.Polarice3.FireNBlood.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AbstractTaillessEntity extends MonsterEntity {
    private LivingEntity Target;
    public int randomint;

    protected AbstractTaillessEntity(EntityType<? extends AbstractTaillessEntity> type, World worldIn){
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    public int getRandomInt() {
        return randomint;
    }

    public void setrandom(int random) {
        randomint = random;
    }

    public boolean EvilEyeGiver(){
        return randomint > 0;
    }

    public AbstractTaillessEntity.ArmPose getArmPose() {
        return AbstractTaillessEntity.ArmPose.NEUTRAL;
    }

    public void setTarget(@Nullable LivingEntity TargetIn) {
        this.Target = TargetIn;
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof AbstractTaillessEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof WitchEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof AbstractCultistEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof TankEntity){
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof AbstractPiglinEntity){
            return this.isAlliedTo(entityIn);
        }  else {
            return false;
        }
    }

    public void die(DamageSource cause) {
        Entity entity = cause.getEntity();
        PlayerEntity playerentity;
        if (this.EvilEyeGiver() && FNBConfig.EvilEyeEvent.get()) {
            if (entity instanceof PlayerEntity) {
                playerentity = (PlayerEntity) entity;
                int random = this.level.random.nextInt(this.getRandomInt());
                if (random == 0) {
                    EffectInstance effectinstance1 = playerentity.getEffect(RegistryHandler.EVIL_EYE.get());
                    if (effectinstance1 == null) {
                        EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                        playerentity.addEffect(effectinstance);
                    } else {
                        int amp = effectinstance1.getAmplifier();
                        int i = amp + 1;
                        i = MathHelper.clamp(i, 0, 5);
                        playerentity.removeEffectNoUpdate(RegistryHandler.EVIL_EYE.get());
                        EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                        playerentity.addEffect(effectinstance);
                    }
                }
            } else if (entity instanceof WolfEntity) {
                WolfEntity wolfentity = (WolfEntity) entity;
                LivingEntity livingentity = wolfentity.getOwner();
                if (wolfentity.isTame() && livingentity instanceof PlayerEntity) {
                    playerentity = (PlayerEntity) livingentity;
                    int random = this.level.random.nextInt(this.getRandomInt());
                    if (random == 0) {
                        EffectInstance effectinstance1 = playerentity.getEffect(RegistryHandler.EVIL_EYE.get());
                        if (effectinstance1 == null) {
                            EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                            playerentity.addEffect(effectinstance);
                        } else {
                            int amp = effectinstance1.getAmplifier();
                            int i = amp + 1;
                            i = MathHelper.clamp(i, 0, 5);
                            playerentity.removeEffectNoUpdate(RegistryHandler.EVIL_EYE.get());
                            EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                            playerentity.addEffect(effectinstance);
                        }
                    }
                }
            } else if (entity instanceof SummonedEntity) {
                SummonedEntity wolfentity = (SummonedEntity) entity;
                LivingEntity livingentity = wolfentity.getTrueOwner();
                if (wolfentity.getTrueOwner() != null && livingentity instanceof PlayerEntity) {
                    playerentity = (PlayerEntity) livingentity;
                    int random = this.level.random.nextInt(this.getRandomInt());
                    if (random == 0) {
                        EffectInstance effectinstance1 = playerentity.getEffect(RegistryHandler.EVIL_EYE.get());
                        if (effectinstance1 == null) {
                            EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                            playerentity.addEffect(effectinstance);
                        } else {
                            int amp = effectinstance1.getAmplifier();
                            int i = amp + 1;
                            i = MathHelper.clamp(i, 0, 5);
                            playerentity.removeEffectNoUpdate(RegistryHandler.EVIL_EYE.get());
                            EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                            playerentity.addEffect(effectinstance);
                        }
                    }
                }
            } else if (entity instanceof FriendlyVexEntity) {
                FriendlyVexEntity wolfentity = (FriendlyVexEntity) entity;
                LivingEntity livingentity = wolfentity.getTrueOwner();
                if (wolfentity.getTrueOwner() != null && livingentity instanceof PlayerEntity) {
                    playerentity = (PlayerEntity) livingentity;
                    int random = this.level.random.nextInt(this.getRandomInt());
                    if (random == 0) {
                        EffectInstance effectinstance1 = playerentity.getEffect(RegistryHandler.EVIL_EYE.get());
                        if (effectinstance1 == null) {
                            EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 0);
                            playerentity.addEffect(effectinstance);
                        } else {
                            int amp = effectinstance1.getAmplifier();
                            int i = amp + 1;
                            i = MathHelper.clamp(i, 0, 5);
                            playerentity.removeEffectNoUpdate(RegistryHandler.EVIL_EYE.get());
                            EffectInstance effectinstance = new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, i);
                            playerentity.addEffect(effectinstance);
                        }
                    }
                }
            }
        }
        super.die(cause);
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
