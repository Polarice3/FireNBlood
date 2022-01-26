package com.Polarice3.FireNBlood.spells;

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

public class ZombieSpell extends SummonSpells{

    public int SoulCost() {
        return 5;
    }

    public int CastDuration() {
        return 60;
    }

    public int SummonDownDuration() {
        return 250;
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.getPosition();
        ZombieMinionEntity summonedentity = new ZombieMinionEntity(ModEntityType.ZOMBIE_MINION.get(), worldIn);
        summonedentity.setOwnerId(entityLiving.getUniqueID());
        summonedentity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
        summonedentity.onInitialSpawn((IServerWorld) worldIn, entityLiving.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
        summonedentity.setLimitedLife(20 * (30 + entityLiving.world.rand.nextInt(90)));
        summonedentity.setUpgraded(this.NecroPower(entityLiving));
        worldIn.addEntity(summonedentity);
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
        }
        this.SummonDown(entityLiving);
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.getPosition();
        for(int i1 = 0; i1 < 2 + entityLiving.world.rand.nextInt(4); ++i1) {
            ZombieMinionEntity summonedentity = new ZombieMinionEntity(ModEntityType.ZOMBIE_MINION.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUniqueID());
            summonedentity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
            summonedentity.onInitialSpawn((IServerWorld) worldIn, entityLiving.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            summonedentity.setLimitedLife(40 * (60 + entityLiving.world.rand.nextInt(180)));
            summonedentity.setUpgraded(this.NecroPower(entityLiving));
            worldIn.addEntity(summonedentity);
        }
        this.SummonDown(entityLiving);
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
        }
    }
}
