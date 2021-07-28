package com.Polarice3.FireNBlood.blocks;

import net.minecraft.block.*;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.Position;
import net.minecraft.dispenser.ProxyBlockSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class FlamerBlock extends DirectionalBlock {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public FlamerBlock(AbstractBlock.Properties builder) {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(TRIGGERED, Boolean.valueOf(false)));
    }

    public static IPosition getFlamePosition(IBlockSource coords) {
        Direction direction = coords.getBlockState().get(FACING);
        double d0 = coords.getX() + 0.7D * (double)direction.getXOffset();
        double d1 = coords.getY() + 0.7D * (double)direction.getYOffset();
        double d2 = coords.getZ() + 0.7D * (double)direction.getZOffset();
        return new Position(d0, d1, d2);
    }

    protected void shoot(ServerWorld worldIn, BlockPos pos, IBlockSource source) {
        ProxyBlockSource proxyblocksource = new ProxyBlockSource(worldIn, pos);
        Direction direction = source.getBlockState().get(FlamerBlock.FACING);
        IPosition iposition = FlamerBlock.getShootPosition(source);
        double d0 = iposition.getX() + (double)((float)direction.getXOffset() * 0.3F);
        double d1 = iposition.getY() + (double)((float)direction.getYOffset() * 0.3F);
        double d2 = iposition.getZ() + (double)((float)direction.getZOffset() * 0.3F);
        World world = source.getWorld();
        Random random = world.rand;
        double d3 = random.nextGaussian() * 0.05D + (double)direction.getXOffset();
        double d4 = random.nextGaussian() * 0.05D + (double)direction.getYOffset();
        double d5 = random.nextGaussian() * 0.05D + (double)direction.getZOffset();
        world.addEntity(new SmallFireballEntity(world, d0, d1, d2, d3, d4, d5));
    }

    public void tick(IBlockSource source, ServerWorld worldIn, BlockPos pos) {
        this.shoot(worldIn, pos, source);
    }


    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
        boolean flag1 = state.get(TRIGGERED);
        if (flag && !flag1) {
            worldIn.getPendingBlockTicks().scheduleTick(pos, this, 4);
            worldIn.setBlockState(pos, state.with(TRIGGERED, Boolean.valueOf(true)), 4);
        } else if (!flag && flag1) {
            worldIn.setBlockState(pos, state.with(TRIGGERED, Boolean.valueOf(false)), 4);
        }

    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }

    public static IPosition getShootPosition(IBlockSource coords) {
        Direction direction = coords.getBlockState().get(FACING);
        double d0 = coords.getX() + 0.7D * (double)direction.getXOffset();
        double d1 = coords.getY() + 0.7D * (double)direction.getYOffset();
        double d2 = coords.getZ() + 0.7D * (double)direction.getZOffset();
        return new Position(d0, d1, d2);
    }

    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }


}
