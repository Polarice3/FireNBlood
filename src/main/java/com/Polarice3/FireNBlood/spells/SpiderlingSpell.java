package com.Polarice3.FireNBlood.spells;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.entities.ally.SpiderlingMinionEntity;
import com.Polarice3.FireNBlood.entities.ally.ZombieMinionEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

public class SpiderlingSpell extends ChargingSpells{

    @Override
    public int Cooldown() {
        return FNBConfig.SpiderlingDuration.get();
    }

    public int SoulCost() {
        return FNBConfig.SpiderlingCost.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.blockPosition();
        SpiderlingMinionEntity summonedentity = new SpiderlingMinionEntity(ModEntityType.SPIDERLING_MINION.get(), worldIn);
        summonedentity.setOwnerId(entityLiving.getUUID());
        summonedentity.moveTo(blockpos, 0.0F, 0.0F);
        summonedentity.setLimitedLife(180);
        worldIn.addFreshEntity(summonedentity);
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            entityLiving.level.addParticle(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.blockPosition();
        for(int i1 = 0; i1 < 2 + entityLiving.level.random.nextInt(4); ++i1) {
            SpiderlingMinionEntity summonedentity = new SpiderlingMinionEntity(ModEntityType.SPIDERLING_MINION.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUUID());
            summonedentity.moveTo(blockpos, 0.0F, 0.0F);
            summonedentity.setLimitedLife(360);
            worldIn.addFreshEntity(summonedentity);
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            entityLiving.level.addParticle(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
    }


}
