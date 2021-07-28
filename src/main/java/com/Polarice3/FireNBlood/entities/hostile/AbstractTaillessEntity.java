package com.Polarice3.FireNBlood.entities.hostile;

import com.Polarice3.FireNBlood.entities.ai.MoveTowardsTargetGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MoveTowardsRaidGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AbstractTaillessEntity extends MonsterEntity {
    private LivingEntity Target;
    protected AbstractTaillessEntity(EntityType<? extends AbstractTaillessEntity> type, World worldIn){
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    public AbstractTaillessEntity.ArmPose getArmPose() {
        return AbstractTaillessEntity.ArmPose.NEUTRAL;
    }

    public void setTarget(@Nullable LivingEntity TargetIn) {
        this.Target = TargetIn;
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
