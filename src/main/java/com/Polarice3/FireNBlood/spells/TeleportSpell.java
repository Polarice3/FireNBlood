package com.Polarice3.FireNBlood.spells;

import com.Polarice3.FireNBlood.FNBConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import javax.annotation.Nullable;

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
        PlayerEntity player = (PlayerEntity) entityLiving;
        RayTraceResult trace = player.pick(32, 0, true);
        BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) trace;
        Direction face = blockRayTraceResult.getDirection();
        BlockPos newPos = blockRayTraceResult.getBlockPos().relative(face);
        enderTeleportEvent(entityLiving, worldIn, newPos);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        PlayerEntity player = (PlayerEntity) entityLiving;
        RayTraceResult trace = player.pick(64, 0, true);
        BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) trace;
        Direction face = blockRayTraceResult.getDirection();
        BlockPos newPos = blockRayTraceResult.getBlockPos().relative(face);
        enderTeleportEvent(entityLiving, worldIn, newPos);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    public static void enderTeleportEvent(LivingEntity player, World world, BlockPos target) {
        enderTeleportEvent(player, world, target.getX() + .5F, target.getY() + .5F, target.getZ() + .5F);
    }

    private static void enderTeleportEvent(LivingEntity player, World world, double x, double y, double z) {
        EnderTeleportEvent event = new EnderTeleportEvent(player, x, y, z, 0);
        boolean wasCancelled = MinecraftForge.EVENT_BUS.post(event);
        if (!wasCancelled) {
            teleportWallSafe(player, world, event.getTargetX(), event.getTargetY(), event.getTargetZ());
        }
    }

    private static void teleportWallSafe(LivingEntity player, World world, double x, double y, double z) {
        BlockPos coords = new BlockPos(x, y, z);
        world.getChunk(coords).setUnsaved(true);
        player.moveTo(x, y, z);
        moveEntityWallSafe(player, world);
    }

    public static void moveEntityWallSafe(Entity entity, World world) {
        while (!world.noCollision(entity)) {
            entity.moveTo(entity.xo, entity.yo + 1.0D, entity.zo);
        }
    }

}
