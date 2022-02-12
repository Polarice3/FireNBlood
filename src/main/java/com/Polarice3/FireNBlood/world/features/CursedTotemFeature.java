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
    private final BlockState cursedtiles = RegistryHandler.CURSED_TILES_BLOCK.get().defaultBlockState();
    private final BlockState cursedtotem = RegistryHandler.CURSED_TOTEM_BLOCK.get().defaultBlockState();
    private final BlockState fanghead = RegistryHandler.FANG_TOTEM.get().defaultBlockState();
    private final BlockState mutatehead = RegistryHandler.MUTATE_TOTEM.get().defaultBlockState();
    private final BlockState undeadhead = RegistryHandler.UNDEAD_TOTEM.get().defaultBlockState();

    public CursedTotemFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(ISeedReader reader, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        int h = random.nextInt(3) + 3;
        int totem = random.nextInt(3);

        reader.setBlock(pos.offset(0, -1, 0), this.cursedtiles, 2);

        for (int t = 0; t < h; ++t){
            reader.setBlock(pos.offset(0, t, 0), this.cursedtotem, 2);
        }

        switch (totem){
            case 0:
                reader.setBlock(pos.offset(0, h, 0), this.fanghead, 2);
                break;
            case 1:
                reader.setBlock(pos.offset(0, h, 0), this.mutatehead, 2);
                break;
            case 2:
                reader.setBlock(pos.offset(0, h, 0), this.undeadhead, 2);
        }

        return true;
    }


}
