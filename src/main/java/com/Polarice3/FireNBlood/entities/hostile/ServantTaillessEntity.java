package com.Polarice3.FireNBlood.entities.hostile;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class ServantTaillessEntity extends AbstractTaillessEntity {
    protected ServantTaillessEntity(EntityType<? extends ServantTaillessEntity> type, World worldIn){
        super(type, worldIn);
    }
}
