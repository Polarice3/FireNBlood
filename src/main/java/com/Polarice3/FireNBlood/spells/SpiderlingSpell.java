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
        return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
    }

    public ItemStack WandResult(World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.getPosition();
        SpiderlingMinionEntity summonedentity = new SpiderlingMinionEntity(ModEntityType.SPIDERLING_MINION.get(), worldIn);
        summonedentity.setOwnerId(entityLiving.getUniqueID());
        summonedentity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
        summonedentity.setLimitedLife(180);
        worldIn.addEntity(summonedentity);
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
        }
        return null;
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.getPosition();
        for(int i1 = 0; i1 < 2 + entityLiving.world.rand.nextInt(4); ++i1) {
            SpiderlingMinionEntity summonedentity = new SpiderlingMinionEntity(ModEntityType.SPIDERLING_MINION.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUniqueID());
            summonedentity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
            summonedentity.setLimitedLife(360);
            worldIn.addEntity(summonedentity);
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
        }
    }


}
