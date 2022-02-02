package com.Polarice3.FireNBlood.spells;

import com.Polarice3.FireNBlood.FNBConfig;
import com.Polarice3.FireNBlood.entities.ally.FriendlyVexEntity;
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

public class VexSpell extends SummonSpells {

    public int SoulCost() {
        return FNBConfig.VexCost.get();
    }

    public int CastDuration() {
        return FNBConfig.VexDuration.get();
    }

    public int SummonDownDuration() {
        return FNBConfig.VexCooldown.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
    }

    public ItemStack WandResult(World worldIn, LivingEntity entityLiving){
        BlockPos blockpos = entityLiving.getPosition();
        FriendlyVexEntity vexentity = new FriendlyVexEntity(ModEntityType.FRIENDLY_VEX.get(), worldIn);
        vexentity.setOwnerId(entityLiving.getUniqueID());
        vexentity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
        vexentity.onInitialSpawn((IServerWorld) worldIn, entityLiving.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
        vexentity.setBoundOrigin(blockpos);
        vexentity.setLimitedLife(20 * (30 + entityLiving.world.rand.nextInt(90)));
        worldIn.addEntity(vexentity);
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
        }
        this.SummonDown(entityLiving);
        return null;
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving){
        for(int i1 = 0; i1 < 3; ++i1) {
            BlockPos blockpos = entityLiving.getPosition();
            FriendlyVexEntity vexentity = new FriendlyVexEntity(ModEntityType.FRIENDLY_VEX.get(), worldIn);
            vexentity.setOwnerId(entityLiving.getUniqueID());
            vexentity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
            vexentity.onInitialSpawn((IServerWorld) worldIn, entityLiving.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            vexentity.setBoundOrigin(blockpos);
            vexentity.setLimitedLife(20 * (30 + entityLiving.world.rand.nextInt(90)));
            worldIn.addEntity(vexentity);
        }
        this.SummonDown(entityLiving);
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
        }
    }
}
