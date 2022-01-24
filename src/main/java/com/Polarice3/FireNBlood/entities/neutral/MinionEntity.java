package com.Polarice3.FireNBlood.entities.neutral;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MinionEntity extends CreatureEntity {
    protected static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.createKey(MinionEntity.class, DataSerializers.BYTE);
    private LivingEntity owner;
    @Nullable
    private BlockPos boundOrigin;

    public MinionEntity(EntityType<? extends MinionEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.navigator = this.createNavigation(p_i50190_2_);
        this.moveController = new MinionEntity.MoveHelperController(this);
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.doBlockCollisions();
    }

    protected PathNavigator createNavigation(World worldIn) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanEnterDoors(true);
        return flyingpathnavigator;
    }

    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(VEX_FLAGS, (byte)0);
    }

    private boolean getVexFlag(int mask) {
        int i = this.dataManager.get(VEX_FLAGS);
        return (i & mask) != 0;
    }

    private void setVexFlag(int mask, boolean value) {
        int i = this.dataManager.get(VEX_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.dataManager.set(VEX_FLAGS, (byte)(i & 255));
    }

    public boolean isCharging() {
        return this.getVexFlag(1);
    }

    public void setCharging(boolean charging) {
        this.setVexFlag(1, charging);
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(MinionEntity vex) {
            super(vex);
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.posX - MinionEntity.this.getPosX(), this.posY - MinionEntity.this.getPosY(), this.posZ - MinionEntity.this.getPosZ());
                double d0 = vector3d.length();
                if (d0 < MinionEntity.this.getBoundingBox().getAverageEdgeLength()) {
                    this.action = MovementController.Action.WAIT;
                    MinionEntity.this.setMotion(MinionEntity.this.getMotion().scale(0.5D));
                } else {
                    MinionEntity.this.setMotion(MinionEntity.this.getMotion().add(vector3d.scale(this.speed * 0.05D / d0)));
                    if (MinionEntity.this.getAttackTarget() == null) {
                        Vector3d vector3d1 = MinionEntity.this.getMotion();
                        MinionEntity.this.rotationYaw = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                        MinionEntity.this.renderYawOffset = MinionEntity.this.rotationYaw;
                    } else {
                        double d2 = MinionEntity.this.getAttackTarget().getPosX() - MinionEntity.this.getPosX();
                        double d1 = MinionEntity.this.getAttackTarget().getPosZ() - MinionEntity.this.getPosZ();
                        MinionEntity.this.rotationYaw = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                        MinionEntity.this.renderYawOffset = MinionEntity.this.rotationYaw;
                    }
                }

            }
        }
    }

}
