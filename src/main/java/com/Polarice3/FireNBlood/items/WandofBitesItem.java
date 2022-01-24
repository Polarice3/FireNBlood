package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class WandofBitesItem extends SoulUsingItem {

    public WandofBitesItem() {
        super(new Properties().group(FireNBlood.TAB));
    }

    @Override
    public int SoulCost() {
        return 12;
    }

    @Override
    public int CastDuration() {
        return 40;
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
    }

    @Override
    public ItemStack MagicResults(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        RayTraceResult rayTraceResult = rayTrace(worldIn, playerEntity, RayTraceContext.FluidMode.NONE);
        Vector3d vector3d = rayTraceResult.getHitVec();
        double d0 = Math.min(vector3d.y, entityLiving.getPosY());
        double d1 = Math.max(vector3d.y, entityLiving.getPosY()) + 1.0D;
        float f = (float) MathHelper.atan2(vector3d.z - entityLiving.getPosZ(), vector3d.x - entityLiving.getPosX());
        for(int l = 0; l < 8; ++l) {
            double d2 = 1.25D * (double)(l + 1);
            int j = 1 * l;
            this.spawnFangs(entityLiving,entityLiving.getPosX() + (double)MathHelper.cos(f) * d2, entityLiving.getPosZ() + (double)MathHelper.sin(f) * d2, d0, d1, f, j);
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
        }
        return stack;
    }

    private void spawnFangs(LivingEntity livingEntity, double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
        BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.down();
            BlockState blockstate = livingEntity.world.getBlockState(blockpos1);
            if (blockstate.isSolidSide(livingEntity.world, blockpos1, Direction.UP)) {
                if (!livingEntity.world.isAirBlock(blockpos)) {
                    BlockState blockstate1 = livingEntity.world.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(livingEntity.world, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.getEnd(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.down();
        } while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

        if (flag) {
            livingEntity.world.addEntity(new EvokerFangsEntity(livingEntity.world, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, livingEntity));
        }

    }

}
