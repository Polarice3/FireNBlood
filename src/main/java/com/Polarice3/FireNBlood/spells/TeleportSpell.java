package com.Polarice3.FireNBlood.spells;

import com.Polarice3.FireNBlood.FNBConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class TeleportSpell extends InstantCastSpells{

    @Override
    public int SoulCost() {
        return FNBConfig.TeleportCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.ENDERMAN_TELEPORT;
    }

    @Override
    public void WandResult(World worldIn, LivingEntity entityLiving) {
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        RayTraceResult rayTraceResult = rayTrace(worldIn, playerEntity, RayTraceContext.FluidMode.NONE);
        Vector3d vector3d = rayTraceResult.getLocation();
        playerEntity.teleportTo(vector3d.x, vector3d.y, vector3d.z);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        RayTraceResult rayTraceResult = rayTrace(worldIn, playerEntity, RayTraceContext.FluidMode.NONE);
        Vector3d vector3d = rayTraceResult.getLocation();
        playerEntity.teleportTo(vector3d.x, vector3d.y, vector3d.z);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    protected static BlockRayTraceResult rayTrace(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode) {
        float f = player.xRot;
        float f1 = player.yRot;
        Vector3d vector3d = player.getEyePosition(1.0F);
        float f2 = MathHelper.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * ((float)Math.PI / 180F));
        float f5 = MathHelper.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4 * 8;
        float f7 = f2 * f4 * 8;
        double d0 = player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();
        Vector3d vector3d1 = vector3d.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return worldIn.clip(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, player));
    }
}
