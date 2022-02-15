package com.Polarice3.FireNBlood.spells;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.utils.ParticleUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BrainEaterSpell extends ChargingSpells{

    @Override
    public int Cooldown() {
        return FNBConfig.BrainEaterDuration.get();
    }

    public int SoulCost() {
        return FNBConfig.BrainEaterCost.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (player.experienceProgress > 0 && player.getHealth() < player.getMaxHealth()){
                if (player.isCrouching()){
                    player.giveExperiencePoints(-FNBConfig.BrainEaterXPCost.get() * 4);
                    player.heal(4.0F);
                } else {
                    player.giveExperiencePoints(-FNBConfig.BrainEaterXPCost.get());
                    player.heal(1.0F);
                }
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_DRINK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    new ParticleUtil(ParticleTypes.HAPPY_VILLAGER, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
                }
            } else {
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    new ParticleUtil(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
                }
            }
        }

    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (player.experienceProgress > 0 && player.getHealth() < player.getMaxHealth()){
                if (player.isCrouching()){
                    player.giveExperiencePoints(-FNBConfig.BrainEaterXPCost.get() * 4);
                    player.heal(8.0F);
                } else {
                    player.giveExperiencePoints(-FNBConfig.BrainEaterXPCost.get());
                    player.heal(2.0F);
                }
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_DRINK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    new ParticleUtil(ParticleTypes.HAPPY_VILLAGER, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
                }
            } else {
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    new ParticleUtil(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
                }
            }
        }

    }


}
