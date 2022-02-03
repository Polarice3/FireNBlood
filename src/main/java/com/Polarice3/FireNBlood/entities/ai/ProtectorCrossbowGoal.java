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
    private int updatePathDelay;

    public ProtectorCrossbowGoal(T shooter, double speed, float p_i50322_4_) {
        this.protector = shooter;
        this.field_220750_c = speed;
        this.field_220751_d = p_i50322_4_ * p_i50322_4_;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        return this.isValidTarget() && this.isHoldingCrossbow();
    }

    private boolean isHoldingCrossbow() {
        return this.protector.isHolding(Items.CROSSBOW);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        return this.isValidTarget() && (this.canUse() || !this.protector.getNavigation().isDone()) && this.isHoldingCrossbow();
    }

    private boolean isValidTarget() {
        return this.protector.getTarget() != null && this.protector.getTarget().isAlive();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        super.stop();
        this.protector.setAggressive(false);
        this.protector.setTarget((LivingEntity)null);
        this.field_220752_e = 0;
        if (this.protector.isUsingItem()) {
            this.protector.stopUsingItem();
            this.protector.setChargingCrossbow(false);
            CrossbowItem.setCharged(this.protector.getUseItem(), false);
        }

    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        LivingEntity livingentity = this.protector.getTarget();
        if (livingentity != null) {
            boolean flag = this.protector.getSensing().canSee(livingentity);
            boolean flag1 = this.field_220752_e > 0;
            if (flag != flag1) {
                this.field_220752_e = 0;
            }

            if (flag) {
                ++this.field_220752_e;
            } else {
                --this.field_220752_e;
            }

            double d0 = this.protector.distanceToSqr(livingentity);
            boolean flag2 = (d0 > (double)this.field_220751_d || this.field_220752_e < 5) && this.field_220753_f == 0;
            if (flag2) {
                --this.updatePathDelay;
                if (this.updatePathDelay <= 0) {
                    this.protector.getNavigation().moveTo(livingentity, this.func_220747_j() ? this.field_220750_c : this.field_220750_c * 0.5D);
                    this.updatePathDelay = field_241381_a_.randomValue(this.protector.getRandom());
                }
            } else {
                this.updatePathDelay = 0;
                this.protector.getNavigation().stop();
            }

            this.protector.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            if (this.crossbowstate == com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.UNCHARGED) {
                if (!flag2) {
                    this.protector.startUsingItem(ProjectileHelper.getWeaponHoldingHand(this.protector, Items.CROSSBOW));
                    this.crossbowstate = com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.CHARGING;
                    this.protector.setChargingCrossbow(true);
                }
            } else if (this.crossbowstate == com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.CHARGING) {
                if (!this.protector.isUsingItem()) {
                    this.crossbowstate = com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.UNCHARGED;
                }

                int i = this.protector.getTicksUsingItem();
                ItemStack itemstack = this.protector.getUseItem();
                if (i >= CrossbowItem.getChargeDuration(itemstack)) {
                    this.protector.releaseUsingItem();
                    this.crossbowstate = com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.CHARGED;
                    this.field_220753_f = 20 + this.protector.getRandom().nextInt(20);
                    this.protector.setChargingCrossbow(false);
                }
            } else if (this.crossbowstate == com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.CHARGED) {
                --this.field_220753_f;
                if (this.field_220753_f == 0) {
                    this.crossbowstate = com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.READY_TO_ATTACK;
                }
            } else if (this.crossbowstate == com.Polarice3.FireNBlood.entities.ai.ProtectorCrossbowGoal.CrossbowState.READY_TO_ATTACK && flag) {
                this.protector.performRangedAttack(livingentity, 1.0F);
                ItemStack itemstack1 = this.protector.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this.protector, Items.CROSSBOW));
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
