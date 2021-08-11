package com.Polarice3.FireNBlood.structures;

import com.Polarice3.FireNBlood.init.ModEntityType;
import com.Polarice3.FireNBlood.utils.RegistryStructures;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

import java.util.List;
import java.util.Random;

/*public class ProfanedTowerStructure extends Structure<NoFeatureConfig> {
    private static final List<MobSpawnInfo.Spawners> PROFANEDTOWER_ENEMIES = ImmutableList.of(new MobSpawnInfo.Spawners(ModEntityType.BLACK_BULL.get(), 2, 1, 1), new MobSpawnInfo.Spawners(ModEntityType.TAILLESS_WRETCH.get(), 1, 1, 1));

    public ProfanedTowerStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public  IStartFactory<NoFeatureConfig> getStartFactory() {
        return ProfanedTowerStructure.Start::new;
    }

    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return PROFANEDTOWER_ENEMIES;
    }

    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    protected boolean func_230363_a_(ChunkGenerator chunkGenerator, BiomeProvider p_230363_2_, long p_230363_3_, SharedSeedRandom p_230363_5_, int chunkX, int chunkZ, Biome p_230363_8_, ChunkPos p_230363_9_, NoFeatureConfig p_230363_10_) {
        for(Biome biome : p_230363_2_.getBiomes(chunkX * 16 + 9, chunkGenerator.getSeaLevel(), chunkZ * 16 + 9, 32)) {
            if (!biome.getGenerationSettings().hasStructure(this)) {
                return false;
            }
        }

        return true;
    }

    private boolean func_242782_a(ChunkGenerator p_242782_1_, long p_242782_2_, SharedSeedRandom p_242782_4_, int p_242782_5_, int p_242782_6_) {
        StructureSeparationSettings structureseparationsettings = p_242782_1_.func_235957_b_().func_236197_a_(Structure.VILLAGE);
        StructureSeparationSettings structureseparationsettings2 = p_242782_1_.func_235957_b_().func_236197_a_(RegistryStructures.PROFANEDTOWER.get());
        if (structureseparationsettings2 == null) {
            return true;
        } else {
            for(int i = p_242782_5_ - 20; i <= p_242782_5_ + 20; ++i) {
                for(int j = p_242782_6_ - 20; j <= p_242782_6_ + 20; ++j) {
                    ChunkPos chunkpos = Structure.VILLAGE.getChunkPosForStructure(structureseparationsettings, p_242782_2_, p_242782_4_, i, j);
                    ChunkPos chunkpos2 = RegistryStructures.PROFANEDTOWER.get().getChunkPosForStructure(structureseparationsettings2, p_242782_2_, p_242782_4_, i, j);
                    if (i == chunkpos2.x && j == chunkpos2.z && i == chunkpos.x && j == chunkpos.z) {
                        return false;
                    }
                }
            }

            return false;
        }
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            Rotation rotation = Rotation.randomRotation(this.rand);
            int i = 5;
            int j = 5;
            if (rotation == Rotation.CLOCKWISE_90) {
                i = -5;
            } else if (rotation == Rotation.CLOCKWISE_180) {
                i = -5;
                j = -5;
            } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
                j = -5;
            }

            int k = (chunkX << 4) + 7;
            int l = (chunkZ << 4) + 7;
            int i1 = chunkGenerator.getNoiseHeightMinusOne(k, l, Heightmap.Type.WORLD_SURFACE_WG);
            int j1 = chunkGenerator.getNoiseHeightMinusOne(k, l + j, Heightmap.Type.WORLD_SURFACE_WG);
            int k1 = chunkGenerator.getNoiseHeightMinusOne(k + i, l, Heightmap.Type.WORLD_SURFACE_WG);
            int l1 = chunkGenerator.getNoiseHeightMinusOne(k + i, l + j, Heightmap.Type.WORLD_SURFACE_WG);
            int i2 = Math.min(Math.min(i1, j1), Math.min(k1, l1));
            if (i2 >= 60) {
                BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i2 + 1, chunkZ * 16 + 8);
                ProfanedTowerPiece.func_236991_a_(templateManagerIn, blockpos, rotation, this.components, this.rand);
                this.recalculateStructureSize();
            }

        }

        public void func_230366_a_(ISeedReader p_230366_1_, StructureManager p_230366_2_, ChunkGenerator p_230366_3_, Random p_230366_4_, MutableBoundingBox p_230366_5_, ChunkPos p_230366_6_) {
            super.func_230366_a_(p_230366_1_, p_230366_2_, p_230366_3_, p_230366_4_, p_230366_5_, p_230366_6_);
            int i = this.bounds.minY;

            for(int j = p_230366_5_.minX; j <= p_230366_5_.maxX; ++j) {
                for(int k = p_230366_5_.minZ; k <= p_230366_5_.maxZ; ++k) {
                    BlockPos blockpos = new BlockPos(j, i, k);
                    if (!p_230366_1_.isAirBlock(blockpos) && this.bounds.isVecInside(blockpos)) {
                        boolean flag = false;

                        for(StructurePiece structurepiece : this.components) {
                            if (structurepiece.getBoundingBox().isVecInside(blockpos)) {
                                flag = true;
                                break;
                            }
                        }

                        if (flag) {
                            for(int l = i - 1; l > 1; --l) {
                                BlockPos blockpos1 = new BlockPos(j, l, k);
                                if (!p_230366_1_.isAirBlock(blockpos1) && !p_230366_1_.getBlockState(blockpos1).getMaterial().isLiquid()) {
                                    break;
                                }

                                p_230366_1_.setBlockState(blockpos1, Blocks.COBBLESTONE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

        }
    }
}*/
