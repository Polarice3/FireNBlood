package com.Polarice3.FireNBlood.client.model;

import com.Polarice3.FireNBlood.entities.ally.ZombieMinionEntity;
import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import net.minecraft.entity.monster.ZombieEntity;

public class ZombieMinionModel extends AbstractZombieModel<ZombieMinionEntity> {
    public ZombieMinionModel(float modelSize, boolean p_i1168_2_) {
        this(modelSize, 0.0F, 64, p_i1168_2_ ? 32 : 64);
    }

    protected ZombieMinionModel(float p_i48914_1_, float p_i48914_2_, int p_i48914_3_, int p_i48914_4_) {
        super(p_i48914_1_, p_i48914_2_, p_i48914_3_, p_i48914_4_);
    }

    @Override
    public boolean isAggressive(ZombieMinionEntity entityIn) {
        return entityIn.isAggressive();
    }

}
