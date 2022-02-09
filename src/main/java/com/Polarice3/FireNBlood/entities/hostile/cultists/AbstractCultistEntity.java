package com.Polarice3.FireNBlood.entities.hostile.cultists;

import com.Polarice3.FireNBlood.entities.hostile.tailless.AbstractTaillessEntity;
import com.Polarice3.FireNBlood.entities.neutral.protectors.AbstractProtectorEntity;
import com.Polarice3.FireNBlood.entities.projectiles.SpearEntity;
import com.Polarice3.FireNBlood.items.SpearItem;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.TieredItem;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AbstractCultistEntity extends AbstractRaiderEntity {

    protected AbstractCultistEntity(EntityType<? extends AbstractRaiderEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractCultistEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, WitchEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractProtectorEntity.class, true));
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        return ArmPose.CROSSED;
    }

    @Override
    public void applyRaidBuffs(int wave, boolean p_213660_2_) {
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VILLAGER_CELEBRATE;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.62F;
    }

    public boolean canBeLeader() {
        return false;
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
        } else if (entityIn instanceof AbstractPiglinEntity){
            return this.isAlliedTo(entityIn);
        }  else {
            return false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static enum ArmPose {
        CROSSED,
        ATTACKING,
        ZOMBIE,
        SPELLCASTING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        BOMB_AND_WEAPON,
        THROW_SPEAR,
        NEUTRAL;
    }
}
