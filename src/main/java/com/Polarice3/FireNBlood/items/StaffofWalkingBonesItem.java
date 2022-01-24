package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.ally.SkeletonMinionEntity;
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

public class StaffofWalkingBonesItem extends SummonSoulUsingItem {

    public StaffofWalkingBonesItem() {
        super(new Properties().group(FireNBlood.TAB));
    }

    @Override
    public int SoulCost() {
        return 8;
    }

    @Override
    public int CastDuration() {
        return 60;
    }

    @Override
    public int SummonDownDuration() {
        return 280;
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
    }

    @Override
    public ItemStack MagicResults(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.getPosition();
        for(int i1 = 0; i1 < 2 + entityLiving.world.rand.nextInt(4); ++i1) {
            SkeletonMinionEntity summonedentity = new SkeletonMinionEntity(ModEntityType.SKELETON_MINION.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUniqueID());
            summonedentity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
            summonedentity.setUpgraded(this.NecroPower(entityLiving));
            summonedentity.setLimitedLife(40 * (60 + entityLiving.world.rand.nextInt(180)));
            summonedentity.onInitialSpawn((IServerWorld) worldIn, entityLiving.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            worldIn.addEntity(summonedentity);
        }
        this.SummonDown(entityLiving);
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
        }
        return stack;
    }

}
