package com.Polarice3.FireNBlood.entities.ai;

import com.Polarice3.FireNBlood.entities.hostile.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.hostile.TaillessDruidEntity;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class MoveTowardsTargetGoal<T extends AbstractTaillessEntity> extends Goal {
    private final T tailless;
    private final EntityPredicate target = (new EntityPredicate().setDistance(128.0D));


    public MoveTowardsTargetGoal(T tailless){
        this.tailless = tailless;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        List<PlayerEntity> list = this.tailless.world.getTargettableEntitiesWithinAABB(PlayerEntity.class, this.target, this.tailless, this.tailless.getBoundingBox().grow(128.0D, 32.0D, 128.0D));
        if (!list.isEmpty()){
            this.tailless.setTarget(list.get(this.tailless.world.rand.nextInt(list.size())));
            LivingEntity livingEntity = this.tailless.getTarget();
            assert livingEntity != null;
            return this.tailless.getDistance(livingEntity) > 32.0D;
        } else {
            return false;
        }
    }

    public void tick(){
        LivingEntity livingentity = this.tailless.getTarget();
        assert livingentity != null;
            if (!this.tailless.hasPath()) {
                Vector3d vector3d = livingentity.getPositionVec();
                this.tailless.getNavigator().tryMoveToXYZ(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            }
        }
}
