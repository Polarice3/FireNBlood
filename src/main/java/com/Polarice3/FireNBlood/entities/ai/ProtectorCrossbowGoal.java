package com.Polarice3.FireNBlood.entities.ai;

import com.Polarice3.FireNBlood.entities.neutral.protectors.AbstractProtectorEntity;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.RangedInteger;

import java.util.EnumSet;

public class ProtectorCrossbowGoal<T extends AbstractProtectorEntity & IRangedAttackMob & ICrossbowUser> extends Goal {
    public static final RangedInteger field_241381_a_ = new RangedInteger(20, 40);
    private final T protector;
    private com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState crossbowstate = com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.UNCHARGED;
    private final double field_220750_c;
    private final float field_220751_d;
    private int field_220752_e;
    private int field_220753_f;
    private int field_241382_h_;

    public ProtectorCrossbowGoal(T shooter, double speed, float p_i50322_4_) {
        this.protector = shooter;
        this.field_220750_c = speed;
        this.field_220751_d = p_i50322_4_ * p_i50322_4_;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute() {
        return this.func_220746_h() && this.func_220745_g();
    }

    private boolean func_220745_g() {
        return this.protector.canEquip(Items.CROSSBOW);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return this.func_220746_h() && (this.shouldExecute() || !this.protector.getNavigator().noPath()) && this.func_220745_g();
    }

    private boolean func_220746_h() {
        return this.protector.getAttackTarget() != null && this.protector.getAttackTarget().isAlive();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        super.resetTask();
        this.protector.setAggroed(false);
        this.protector.setAttackTarget((LivingEntity)null);
        this.field_220752_e = 0;
        if (this.protector.isHandActive()) {
            this.protector.resetActiveHand();
            this.protector.setCharging(false);
            CrossbowItem.setCharged(this.protector.getActiveItemStack(), false);
        }

    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        LivingEntity livingentity = this.protector.getAttackTarget();
        if (livingentity != null) {
            boolean flag = this.protector.getEntitySenses().canSee(livingentity);
            boolean flag1 = this.field_220752_e > 0;
            if (flag != flag1) {
                this.field_220752_e = 0;
            }

            if (flag) {
                ++this.field_220752_e;
            } else {
                --this.field_220752_e;
            }

            double d0 = this.protector.getDistanceSq(livingentity);
            boolean flag2 = (d0 > (double)this.field_220751_d || this.field_220752_e < 5) && this.field_220753_f == 0;
            if (flag2) {
                --this.field_241382_h_;
                if (this.field_241382_h_ <= 0) {
                    this.protector.getNavigator().tryMoveToEntityLiving(livingentity, this.func_220747_j() ? this.field_220750_c : this.field_220750_c * 0.5D);
                    this.field_241382_h_ = field_241381_a_.getRandomWithinRange(this.protector.getRNG());
                }
            } else {
                this.field_241382_h_ = 0;
                this.protector.getNavigator().clearPath();
            }

            this.protector.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
            if (this.crossbowstate == com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.UNCHARGED) {
                if (!flag2) {
                    this.protector.setActiveHand(ProjectileHelper.getHandWith(this.protector, Items.CROSSBOW));
                    this.crossbowstate = com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.CHARGING;
                    this.protector.setCharging(true);
                }
            } else if (this.crossbowstate == com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.CHARGING) {
                if (!this.protector.isHandActive()) {
                    this.crossbowstate = com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.UNCHARGED;
                }

                int i = this.protector.getItemInUseMaxCount();
                ItemStack itemstack = this.protector.getActiveItemStack();
                if (i >= CrossbowItem.getChargeTime(itemstack)) {
                    this.protector.stopActiveHand();
                    this.crossbowstate = com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.CHARGED;
                    this.field_220753_f = 20 + this.protector.getRNG().nextInt(20);
                    this.protector.setCharging(false);
                }
            } else if (this.crossbowstate == com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.CHARGED) {
                --this.field_220753_f;
                if (this.field_220753_f == 0) {
                    this.crossbowstate = com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.READY_TO_ATTACK;
                }
            } else if (this.crossbowstate == com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.READY_TO_ATTACK && flag) {
                this.protector.attackEntityWithRangedAttack(livingentity, 1.0F);
                ItemStack itemstack1 = this.protector.getHeldItem(ProjectileHelper.getHandWith(this.protector, Items.CROSSBOW));
                CrossbowItem.setCharged(itemstack1, false);
                this.crossbowstate = com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.UNCHARGED;
            }

        }
    }

    private boolean func_220747_j() {
        return this.crossbowstate == com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.UNCHARGED;
    }

    enum CrossbowState {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;
    }
}
