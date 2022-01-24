package com.Polarice3.FireNBlood.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class CursedCageBlock extends Block {
    public CursedCageBlock() {
        super(AbstractBlock.Properties.create(Material.ROCK)
                .hardnessAndResistance(3.5F)
                .sound(SoundType.METAL)
                .setRequiresTool()
                .notSolid());
    }

    public boolean isTransparent(BlockState state) {
        return true;
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
