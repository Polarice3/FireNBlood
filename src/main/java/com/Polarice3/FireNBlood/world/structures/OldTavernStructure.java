package com.Polarice3.FireNBlood.world.structures;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

public class OldTavernStructure extends Structure<NoFeatureConfig> {
    public OldTavernStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public  IStartFactory<NoFeatureConfig> getStartFactory() {
        return OldTavernStructure.Start::new;
    }

    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    protected boolean func_230363_a_(ChunkGenerator chunkGenerator, BiomeProvider p_230363_2_, long p_230363_3_, SharedSeedRandom p_230363_5_, int chunkX, int chunkZ, Biome p_230363_8_, ChunkPos p_230363_9_, NoFeatureConfig p_230363_10_) {
        BlockPos centerOfChunk = new BlockPos((chunkX >> 4), 0, (chunkZ >> 4));
        int landHeight = chunkGenerator.getNoiseHeightMinusOne(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        IBlockReader columnOfBlocks = chunkGenerator.func_230348_a_(centerOfChunk.getX(), centerOfChunk.getZ());
        BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.up(landHeight));
        BlockState bottomBlock = columnOfBlocks.getBlockState(centerOfChunk.down(landHeight));
        return topBlock.getFluidState().isEmpty() && !topBlock.isAir() && this.func_242782_a(chunkGenerator, p_230363_3_, p_230363_5_, chunkX, chunkZ);
    }

    private boolean func_242782_a(ChunkGenerator p_242782_1_, long p_242782_2_, SharedSeedRandom p_242782_4_, int p_242782_5_, int p_242782_6_) {
        StructureSeparationSettings structureseparationsettings = p_242782_1_.func_235957_b_().func_236197_a_(Structure.VILLAGE);
        if (structureseparationsettings == null) {
            return false;
        } else {
            for(int i = p_242782_5_ - 10; i <= p_242782_5_ + 10; ++i) {
                for(int j = p_242782_6_ - 10; j <= p_242782_6_ + 10; ++j) {
                    ChunkPos chunkpos = Structure.VILLAGE.getChunkPosForStructure(structureseparationsettings, p_242782_2_, p_242782_4_, i, j);
                    if (i == chunkpos.x && j == chunkpos.z) {
                        return true;
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
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            BlockPos blockpos = new BlockPos(x, 0, z);
            Rotation rotation = Rotation.randomRotation(this.rand);
            TavernPiece.func_236991_a_(templateManagerIn, blockpos, rotation, this.components, this.rand);
            this.recalculateStructureSize();

        }
    }


    }
