package com.Polarice3.FireNBlood.spells;

import com.Polarice3.FireNBlood.FNBConfig;
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
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class FangSpell extends Spells{

    public int SoulCost() {
        return FNBConfig.FangCost.get();
    }

    public int CastDuration() {
        return FNBConfig.FangDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
    }

    public ItemStack WandResult(World worldIn, LivingEntity entityLiving){
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        RayTraceResult rayTraceResult = rayTrace(worldIn, playerEntity, RayTraceContext.FluidMode.NONE);
        Vector3d vector3d = rayTraceResult.getHitVec();
        double d0 = Math.min(vector3d.y, entityLiving.getPosY());
        double d1 = Math.max(vector3d.y, entityLiving.getPosY()) + 1.0D;
        float f = (float) MathHelper.atan2(vector3d.z - entityLiving.getPosZ(), vector3d.x - entityLiving.getPosX());
        for(int l = 0; l < 16; ++l) {
            double d2 = 1.25D * (double)(l + 1);
            int j = l;
            this.spawnFangs(entityLiving,entityLiving.getPosX() + (double)MathHelper.cos(f) * d2, entityLiving.getPosZ() + (double)MathHelper.sin(f) * d2, d0, d1, f, j);
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
        }
        return null;
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving){
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        RayTraceResult rayTraceResult = rayTrace(worldIn, playerEntity, RayTraceContext.FluidMode.NONE);
        Vector3d vector3d = rayTraceResult.getHitVec();
        double d0 = Math.min(vector3d.y, entityLiving.getPosY());
        double d1 = Math.max(vector3d.y, entityLiving.getPosY()) + 1.0D;
        float f = (float) MathHelper.atan2(vector3d.z - entityLiving.getPosZ(), vector3d.x - entityLiving.getPosX());
        for(int l = 0; l < 16; ++l) {
            double d2 = 1.25D * (double)(l + 1);
            float fleft = f + 0.4F;
            float fright = f - 0.4F;
            this.spawnFangs(entityLiving,entityLiving.getPosX() + (double)MathHelper.cos(f) * d2, entityLiving.getPosZ() + (double)MathHelper.sin(f) * d2, d0, d1, f, l);
            this.spawnFangs(entityLiving,entityLiving.getPosX() + (double)MathHelper.cos(fleft) * d2, entityLiving.getPosZ() + (double)MathHelper.sin(fleft) * d2, d0, d1, fleft, l);
            this.spawnFangs(entityLiving,entityLiving.getPosX() + (double)MathHelper.cos(fright) * d2, entityLiving.getPosZ() + (double)MathHelper.sin(fright) * d2, d0, d1, fright, l);
        }
        for(int i = 0; i < 5; ++i) {
            float f1 = f + (float)i * (float)Math.PI * 0.4F;
            this.spawnFangs(entityLiving,entityLiving.getPosX() + (double)MathHelper.cos(f1) * 1.5D, entityLiving.getPosZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
        }

        for(int k = 0; k < 8; ++k) {
            float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
            this.spawnFangs(entityLiving,entityLiving.getPosX() + (double)MathHelper.cos(f2) * 2.5D, entityLiving.getPosZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
        }
    }

    protected static BlockRayTraceResult rayTrace(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode) {
        float f = player.rotationPitch;
        float f1 = player.rotationYaw;
        Vector3d vector3d = player.getEyePosition(1.0F);
        float f2 = MathHelper.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * ((float)Math.PI / 180F));
        float f5 = MathHelper.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();;
        Vector3d vector3d1 = vector3d.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return worldIn.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, player));
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
