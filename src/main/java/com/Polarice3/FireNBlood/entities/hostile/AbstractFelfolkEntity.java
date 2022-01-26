package com.Polarice3.FireNBlood.entities.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
/*

import java.util.Objects;

public class AbstractFelfolkEntity extends MonsterEntity {
    private static final DataParameter<String> TRIBE = EntityDataManager.createKey(AbstractFelfolkEntity.class, DataSerializers.STRING);
    public String Tribe;

    protected AbstractFelfolkEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, true, true, (entity) ->
                entity instanceof AbstractFelfolkEntity && !Objects.equals(((AbstractFelfolkEntity) entity).getTribe(), this.getTribe())));
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (entityIn instanceof AbstractFelfolkEntity){
            if (Objects.equals(((AbstractFelfolkEntity) entityIn).getTribe(), this.getTribe())){
                return this.isOnSameTeam(entityIn);
            } else {
                return super.isOnSameTeam(entityIn);
            }
        } else {
            return super.isOnSameTeam(entityIn);
        }
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(TRIBE, Type.NONE.name());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setTribe();
        this.Tribe = compound.getString("Tribe");
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        this.dataManager.get(TRIBE);
        compound.putString("Tribe", getTribe());
    }

    public void setTribe(){
        if (this.Tribe != null) {
            this.dataManager.set(TRIBE, this.Tribe);
        } else {
            this.dataManager.set(TRIBE, Type.NONE.name());
        }
    }

    public String getTribe(){
        return this.Tribe;
    }

    public void setAkshantribe(){
        this.Tribe = Type.AKSHAN.name();
    }

    public void setMorkhantribe(){
        this.Tribe = Type.MORKHAN.name();
    }

    public void setTevildotribe(){
        this.Tribe = Type.TEVILDO.name();
    }

    public void setZuradintribe(){
        this.Tribe = Type.ZURADIN.name();
    }

    public static enum Type {
        AKSHAN("akshan"),
        MORKHAN("morkhan"),
        TEVILDO("tevildo"),
        ZURADIN("zuradin"),
        NONE("none");

        private Type(String nameIn) {
        }

    }

}
*/
