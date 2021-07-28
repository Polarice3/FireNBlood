package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.neutral.*;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RallyingHornItem extends Item {
    public RallyingHornItem() {
        super(new Item.Properties().group(FireNBlood.TAB).maxStackSize(1));
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        BlockPos blockpos = entityLiving.getPosition().add(-10 + worldIn.rand.nextInt(15), 0, -10 + worldIn.rand.nextInt(15));
        int Random0 = worldIn.rand.nextInt(4);
        int Random1 = worldIn.rand.nextInt(3);
        int Random2 = worldIn.rand.nextInt(10);
        worldIn.playSound(null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.EVENT_RAID_HORN, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for (int i = 0; i < Random0; ++i) {
            ProtectorEntity entity = new ProtectorEntity(ModEntityType.PROTECTOR.get(), worldIn);
            entity.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.CROSSBOW));
            entity.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*            entity.setSummoned(true);
            entity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
            worldIn.addEntity(entity);
        }
        for (int i = 0; i < Random0; ++i) {
            RedemptorEntity entity = new RedemptorEntity(ModEntityType.REDEMPTOR.get(), worldIn);
            entity.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
            entity.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*            entity.setSummoned(true);
            entity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
            worldIn.addEntity(entity);
        }
        for (int i = 0; i < Random1; ++i) {
            BrewerEntity entity = new BrewerEntity(ModEntityType.BREWER.get(), worldIn);
            entity.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*            entity.setSummoned(true);
            entity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
            worldIn.addEntity(entity);
        }
        for (int i = 0; i < Random1; ++i) {
            HexerEntity entity = new HexerEntity(ModEntityType.HEXER.get(), worldIn);
            if (Random2 == 1 || Random2 == 2 || Random2 == 3 || Random2 == 4 || Random2 == 5){
                entity.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.CROSSBOW));
            } else {
                entity.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
            }
            entity.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*            entity.setSummoned(true);
            entity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
            worldIn.addEntity(entity);
        }
        for (int i = 0; i < Random1; ++i) {
            SavagerEntity savagerEntity = new SavagerEntity(ModEntityType.SAVAGER.get(), worldIn);
            savagerEntity.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*            savagerEntity.setSummoned(true);
            savagerEntity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
            worldIn.addEntity(savagerEntity);
            if (Random2 == 1){
                ProtectorEntity protectorEntity = new ProtectorEntity(ModEntityType.PROTECTOR.get(), worldIn);
                protectorEntity.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.CROSSBOW));
                protectorEntity.setPosition(savagerEntity.getPosX(), savagerEntity.getPosY(), savagerEntity.getPosZ());
                protectorEntity.startRiding(savagerEntity);
/*                protectorEntity.setSummoned(true);
                protectorEntity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
                worldIn.addEntity(protectorEntity);
            } else if (Random2 == 2){
                RedemptorEntity redemptorEntity = new RedemptorEntity(ModEntityType.REDEMPTOR.get(), worldIn);
                redemptorEntity.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
                redemptorEntity.setPosition(savagerEntity.getPosX(), savagerEntity.getPosY(), savagerEntity.getPosZ());
                redemptorEntity.startRiding(savagerEntity);
/*                redemptorEntity.setSummoned(true);
                redemptorEntity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
                worldIn.addEntity(redemptorEntity);
            } else if (Random2 == 3){
                HexerEntity hexerEntity = new HexerEntity(ModEntityType.HEXER.get(), worldIn);
                hexerEntity.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.CROSSBOW));
                hexerEntity.startRiding(savagerEntity);
/*                hexerEntity.setSummoned(true);
                hexerEntity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
                worldIn.addEntity(hexerEntity);
            }
        }
        if (entityLiving instanceof PlayerEntity){
            if (!((PlayerEntity) entityLiving).isCreative()){
                stack.setCount(0);
            }
        } else {
            stack.setCount(0);
        }
        return stack;
    }

    public int getUseDuration(ItemStack stack) {
        return 25;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        playerIn.playSound(SoundEvents.EVENT_RAID_HORN, 1.0F, 1.0F);
        return ActionResult.resultConsume(itemstack);
    }
}
