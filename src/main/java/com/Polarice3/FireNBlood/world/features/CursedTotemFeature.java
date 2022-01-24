package com.Polarice3.FireNBlood.world.features;

import com.Polarice3.FireNBlood.utils.RegistryHandler;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class CursedTotemFeature extends Feature<NoFeatureConfig> {
    private static final BlockStateMatcher IS_GRASS = BlockStateMatcher.forBlock(Blocks.GRASS_BLOCK);
    private final BlockState cursedtiles = RegistryHandler.CURSED_TILES_BLOCK.get().getDefaultState();
    private final BlockState cursedtotem = RegistryHandler.CURSED_TOTEM_BLOCK.get().getDefaultState();
    private final BlockState fanghead = RegistryHandler.FANG_TOTEM.get().getDefaultState();
    private final BlockState mutatehead = RegistryHandler.MUTATE_TOTEM.get().getDefaultState();

    public CursedTotemFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        int h = rand.nextInt(3) + 3;
        int totem = rand.nextInt(2);

        reader.setBlockState(pos.add(0, -1, 0), this.cursedtiles, 2);

        for (int t = 0; t < h; ++t){
            reader.setBlockState(pos.add(0, t, 0), this.cursedtotem, 2);
        }

        if (totem == 1){
            reader.setBlockState(pos.add(0, h, 0), this.fanghead, 2);
        } else {
            reader.setBlockState(pos.add(0, h, 0), this.mutatehead, 2);
        }

        return true;
    }


}
