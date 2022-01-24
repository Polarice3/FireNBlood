package com.Polarice3.FireNBlood.blocks;
/*
import com.Polarice3.FireNBlood.tileentities.SoulForgeTileEntity;
import net.minecraft.block.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class SoulForgeBlock extends ContainerBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    protected SoulForgeBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(LIT, Boolean.FALSE));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new SoulForgeTileEntity();
    }

    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY();
            double d2 = (double)pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d6 = rand.nextDouble() * 6.0D / 16.0D;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d4, d1 + d6, d2 + d4, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d4, d1 + d6, d2 + d4, 0.0D, 0.0D, 0.0D);
        }
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

}*/
