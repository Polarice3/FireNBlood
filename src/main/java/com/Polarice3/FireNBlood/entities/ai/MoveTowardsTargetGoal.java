package com.Polarice3.FireNBlood.entities.ai;

import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.List;

public class MoveTowardsTargetGoal<T extends AbstractTaillessEntity> extends Goal {
    private final T tailless;
    private final EntityPredicate target = (new EntityPredicate().range(128.0D));


    public MoveTowardsTargetGoal(T tailless){
        this.tailless = tailless;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        List<PlayerEntity> list = this.tailless.level.getNearbyEntities(PlayerEntity.class, this.target, this.tailless, this.tailless.getBoundingBox().inflate(128.0D, 32.0D, 128.0D));
        if (!list.isEmpty()){
            this.tailless.setTarget(list.get(this.tailless.level.random.nextInt(list.size())));
            LivingEntity livingEntity = this.tailless.getTarget();
            if (livingEntity != null) {
                return this.tailless.distanceTo(livingEntity) > 32.0D;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void tick(){
        LivingEntity livingentity = this.tailless.getTarget();
            if (!this.tailless.isPathFinding() && livingentity != null) {
                Vector3d vector3d = livingentity.position();
                this.tailless.getNavigation().moveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            }
        }
}
