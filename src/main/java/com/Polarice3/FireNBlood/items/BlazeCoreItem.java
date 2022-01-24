package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;

public class BlazeCoreItem extends Item {
    public BlazeCoreItem(){
        super(new Properties().group(FireNBlood.TAB).isImmuneToFire());
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity playerentity = context.getPlayer();
        World world = context.getWorld();
        ItemStack itemstack = context.getItem();
        BlockPos blockpos = context.getPos();
        Direction direction = context.getFace();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.matchesBlock(Blocks.SPAWNER)) {
            TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity instanceof MobSpawnerTileEntity) {
                AbstractSpawner abstractspawner = ((MobSpawnerTileEntity)tileentity).getSpawnerBaseLogic();
                EntityType<?> entitytype1 = EntityType.BLAZE;
                abstractspawner.setEntityType(entitytype1);
                tileentity.markDirty();
                world.notifyBlockUpdate(blockpos, blockstate, blockstate, 3);
                itemstack.shrink(1);
                return ActionResultType.CONSUME;
            }
        }
        if (CampfireBlock.canBeLit(blockstate)) {
            world.playSound(playerentity, blockpos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
            world.setBlockState(blockpos, blockstate.with(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
            return ActionResultType.func_233537_a_(world.isRemote());
        }else {
            BlockPos blockpos1 = blockpos.offset(context.getFace());
            if (AbstractFireBlock.canLightBlock(world, blockpos1, context.getPlacementHorizontalFacing())) {
                world.playSound(playerentity, blockpos1, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
                BlockState blockstate1 = AbstractFireBlock.getFireForPlacement(world, blockpos1);
                world.setBlockState(blockpos1, blockstate1, 11);
                return ActionResultType.func_233537_a_(world.isRemote());
            } else {
                return ActionResultType.FAIL;
            }
        }
    }
}
