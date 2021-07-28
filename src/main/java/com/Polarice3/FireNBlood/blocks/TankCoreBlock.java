package com.Polarice3.FireNBlood.blocks;

import com.Polarice3.FireNBlood.entities.ally.FriendlyTankEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class TankCoreBlock extends Block {
    @Nullable
    private BlockPattern tankpattern;

    public TankCoreBlock() {
        super(AbstractBlock.Properties.create(Material.ROCK)
                .hardnessAndResistance(3.5F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)

        );
    }

/*    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.isIn(state.getBlock())) {
            this.trySpawnTank(worldIn, pos);
        }
    }*/

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            this.trySpawnTank(worldIn, pos);
            return ActionResultType.CONSUME;
        }
    }
    private void trySpawnTank(World world, BlockPos pos) {
        BlockPattern.PatternHelper blockpattern$patternhelper = this.getTankpattern().match(world, pos);
        if (blockpattern$patternhelper != null) {
            for(int i = 0; i < this.getTankpattern().getThumbLength(); ++i) {
                CachedBlockInfo cachedblockinfo = blockpattern$patternhelper.translateOffset(0, i, 0);
                world.setBlockState(cachedblockinfo.getPos(), Blocks.AIR.getDefaultState(), 2);
                world.playEvent(2001, cachedblockinfo.getPos(), Block.getStateId(cachedblockinfo.getBlockState()));
            }
            FriendlyTankEntity tankEntity = ModEntityType.FRIENDTANK.get().create(world);
            BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(0, 2, 0).getPos();
            assert tankEntity != null;
            tankEntity.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            world.addEntity(tankEntity);
            for(ServerPlayerEntity serverplayerentity : world.getEntitiesWithinAABB(ServerPlayerEntity.class, tankEntity.getBoundingBox().grow(5.0D))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayerentity, tankEntity);
            }

            for(int l = 0; l < this.getTankpattern().getThumbLength(); ++l) {
                CachedBlockInfo cachedblockinfo3 = blockpattern$patternhelper.translateOffset(0, l, 0);
                world.func_230547_a_(cachedblockinfo3.getPos(), Blocks.AIR);
            }
        }
    }

    private BlockPattern getTankpattern(){
        if (this.tankpattern == null) {
            this.tankpattern = BlockPatternBuilder.start().aisle("#", "~", "$").where('~', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(this))).where('#', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(Blocks.DISPENSER))).where('$', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).build();
        }

        return this.tankpattern;
    }
}
