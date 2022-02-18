package com.Polarice3.FireNBlood.events;

import com.Polarice3.FireNBlood.entities.ai.MoveTowardsTargetGoal;
import com.Polarice3.FireNBlood.entities.hostile.TankEntity;
import com.Polarice3.FireNBlood.entities.hostile.tailless.*;
import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.Random;

public class EvilEyeSpawner {
    private int ticksUntilSpawn;

    public int tick(ServerWorld world) {
        Random random = world.random;
        --this.ticksUntilSpawn;
        if (this.ticksUntilSpawn > 0) {
            return 0;
        } else {
            if (world.dimensionType().bedWorks()) {
                int j = world.players().size();
                if (j < 1) {
                    return 0;
                } else {
                    PlayerEntity playerentity = world.players().get(random.nextInt(j));
                    if (playerentity.isSpectator()){
                        return 0;
                    } else if (playerentity.hasEffect(RegistryHandler.EVIL_EYE.get())){
                        int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                        int l = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                        BlockPos.Mutable blockpos$mutable = playerentity.blockPosition().mutable().move(k, 0, l);
                        if (!world.hasChunksAt(blockpos$mutable.getX() - 10, blockpos$mutable.getY() - 10, blockpos$mutable.getZ() - 10, blockpos$mutable.getX() + 10, blockpos$mutable.getY() + 10, blockpos$mutable.getZ() + 10)) {
                            return 0;
                        } else {
                            int i1 = 0;
                            int randomfull = random.nextInt(8);
                            int evileyeLevel = 0;
                            evileyeLevel += playerentity.getEffect(RegistryHandler.EVIL_EYE.get()).getAmplifier() + 1;
                            evileyeLevel = MathHelper.clamp(evileyeLevel, 0, 5);
                            blockpos$mutable.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                            blockpos$mutable.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                            blockpos$mutable.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                            this.ticksUntilSpawn += world.random.nextInt(1200) + 1200/playerentity.getEffect(RegistryHandler.EVIL_EYE.get()).getAmplifier() + 1;
                            if (evileyeLevel >= 1){
                                int random2 = random.nextInt(3);
                                if (random2 == 0 || random2 == 2) {
                                    for (int k1 = 0; k1 < evileyeLevel; ++k1) {
                                        ++i1;
                                        if (k1 == 0) {
                                            if (!this.spawnBlackBulls(world, blockpos$mutable, random)) {
                                                break;
                                            }
                                        } else {
                                            this.spawnBlackBulls(world, blockpos$mutable, random);
                                        }
                                    }
                                }
                                if (random2 == 1 || random2 == 2){
                                    this.spawnWretches(world, blockpos$mutable, random);
                                }
                            }
                            if (evileyeLevel >= 2) {
                                if (randomfull == 0 || randomfull == 2) {
                                    BlockPos.Mutable blockpos$mutable2 = playerentity.blockPosition().mutable().move(k, 16, l);
                                    blockpos$mutable2.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                    blockpos$mutable2.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                                    blockpos$mutable2.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                                    this.spawnHorrors(world, blockpos$mutable2, random);
                                }
                            }

                            if (evileyeLevel >= 3){
                                if (randomfull == 2 || randomfull == 4) {
                                    this.spawnDruids(world, blockpos$mutable, random);
                                }
                            }

                            if (evileyeLevel >= 4){
                                if (randomfull == 0 || randomfull == 2 || randomfull == 3) {
                                    this.spawnTanks(world, blockpos$mutable, random);
                                }
                            }

                            if (evileyeLevel >= 5) {
                                int random3 = random.nextInt(64);
                                if (random3 == 0) {
                                    this.spawnMaster(world, blockpos$mutable, random);
                                }
                            }

                            return i1;
                        }
                    } else {
                        return 0;
                    }
                }
            } else {
                return 0;
            }
        }
    }

    private boolean spawnBlackBulls(ServerWorld worldIn, BlockPos p_222695_2_, Random random) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), ModEntityType.BLACK_BULL.get())) {
            return false;
        } else {
            BlackBullEntity blackBullEntity = new BlackBullEntity(ModEntityType.BLACK_BULL.get(), worldIn);
            blackBullEntity.setPos((double)p_222695_2_.getX(), (double)p_222695_2_.getY(), (double)p_222695_2_.getZ());
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(blackBullEntity, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
            blackBullEntity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
            blackBullEntity.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackBullEntity));
            worldIn.addFreshEntityWithPassengers(blackBullEntity);
            return true;
        }
    }

    private boolean spawnWretches(ServerWorld worldIn, BlockPos p_222695_2_, Random random) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), ModEntityType.TAILLESS_WRETCH.get())) {
            return false;
        } else {
            TaillessWretchEntity blackBullEntity = new TaillessWretchEntity(ModEntityType.TAILLESS_WRETCH.get(), worldIn);
            blackBullEntity.setPos((double)p_222695_2_.getX(), (double)p_222695_2_.getY(), (double)p_222695_2_.getZ());
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(blackBullEntity, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
            blackBullEntity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
            blackBullEntity.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackBullEntity));
            worldIn.addFreshEntityWithPassengers(blackBullEntity);
            return true;
        }
    }

    private boolean spawnHorrors(ServerWorld worldIn, BlockPos p_222695_2_, Random random) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), ModEntityType.TAILLESS_HORROR.get())) {
            return false;
        } else {
            TaillessHorrorEntity blackBullEntity = new TaillessHorrorEntity(ModEntityType.TAILLESS_HORROR.get(), worldIn);
            blackBullEntity.setPos((double)p_222695_2_.getX(), (double)p_222695_2_.getY(), (double)p_222695_2_.getZ());
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(blackBullEntity, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
            blackBullEntity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
            blackBullEntity.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackBullEntity));
            worldIn.addFreshEntityWithPassengers(blackBullEntity);
            return true;
        }
    }

    private boolean spawnDruids(ServerWorld worldIn, BlockPos p_222695_2_, Random random) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), ModEntityType.TAILLESS_DRUID.get())) {
            return false;
        } else {
            TaillessDruidEntity blackBullEntity = new TaillessDruidEntity(ModEntityType.TAILLESS_DRUID.get(), worldIn);
            blackBullEntity.setPos((double)p_222695_2_.getX(), (double)p_222695_2_.getY(), (double)p_222695_2_.getZ());
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(blackBullEntity, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
            blackBullEntity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
            blackBullEntity.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackBullEntity));
            worldIn.addFreshEntityWithPassengers(blackBullEntity);
            return true;
        }
    }

    private boolean spawnTanks(ServerWorld worldIn, BlockPos p_222695_2_, Random random) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), ModEntityType.TANK.get())) {
            return false;
        } else {
            TankEntity blackBullEntity = new TankEntity(ModEntityType.TANK.get(), worldIn);
            blackBullEntity.setPos((double)p_222695_2_.getX(), (double)p_222695_2_.getY(), (double)p_222695_2_.getZ());
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(blackBullEntity, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
            blackBullEntity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
            blackBullEntity.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(blackBullEntity));
            worldIn.addFreshEntityWithPassengers(blackBullEntity);
            return true;
        }
    }

    private boolean spawnMaster(ServerWorld worldIn, BlockPos p_222695_2_, Random random) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), ModEntityType.MINOTAUR.get())) {
            return false;
        } else {
            AbstractTaillessEntity entity = null;
            int random1 = worldIn.random.nextInt(3);
            switch (random1){
                case 0:
                    entity = ModEntityType.MINOTAUR.get().create(worldIn);
                    break;
                case 1:
                    entity = ModEntityType.TAILLESS_PROPHET.get().create(worldIn);
                    break;
                case 2:
                    entity = ModEntityType.ANATHEMA.get().create(worldIn);
                    break;
            }
            AbstractTaillessEntity taillessEntity = entity;
            taillessEntity.setPos((double)p_222695_2_.getX(), (double)p_222695_2_.getY(), (double)p_222695_2_.getZ());
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(taillessEntity, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
            taillessEntity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
            taillessEntity.goalSelector.addGoal(1, new MoveTowardsTargetGoal<>(taillessEntity));
            worldIn.addFreshEntityWithPassengers(taillessEntity);
            return true;
        }
    }
}
