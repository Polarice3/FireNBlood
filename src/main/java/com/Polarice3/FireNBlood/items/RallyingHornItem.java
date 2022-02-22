package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.neutral.protectors.*;
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
        super(new Item.Properties().tab(FireNBlood.TAB).stacksTo(1));
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        BlockPos blockpos = entityLiving.blockPosition().offset(-10 + worldIn.random.nextInt(15), 0, -10 + worldIn.random.nextInt(15));
        int random0 = worldIn.random.nextInt(4);
        int random1 = worldIn.random.nextInt(3);
        int random2 = worldIn.random.nextInt(10);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.RAID_HORN, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for (int i = 0; i < random0; ++i) {
            ProtectorEntity entity = new ProtectorEntity(ModEntityType.PROTECTOR.get(), worldIn);
            entity.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.CROSSBOW));
            entity.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*            entity.setSummoned(true);
            entity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
            worldIn.addFreshEntity(entity);
        }
        for (int i = 0; i < random0; ++i) {
            RedemptorEntity entity = new RedemptorEntity(ModEntityType.REDEMPTOR.get(), worldIn);
            entity.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
            entity.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*            entity.setSummoned(true);
            entity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
            worldIn.addFreshEntity(entity);
        }
        for (int i = 0; i < random1; ++i) {
            BrewerEntity entity = new BrewerEntity(ModEntityType.BREWER.get(), worldIn);
            entity.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*            entity.setSummoned(true);
            entity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
            worldIn.addFreshEntity(entity);
        }
        for (int i = 0; i < random1; ++i) {
            HexerEntity entity = new HexerEntity(ModEntityType.HEXER.get(), worldIn);
            if (random2 == 0 || random2 == 1 || random2 == 2 || random2 == 3 || random2 == 4){
                entity.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.CROSSBOW));
            } else {
                entity.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
            }
            entity.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*            entity.setSummoned(true);
            entity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
            worldIn.addFreshEntity(entity);
        }
        SavagerEntity savagerEntity = new SavagerEntity(ModEntityType.SAVAGER.get(), worldIn);
        savagerEntity.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*            savagerEntity.setSummoned(true);
            savagerEntity.summonedTimer = AbstractProtectorEntity.SummonedTimer();*/
        worldIn.addFreshEntity(savagerEntity);
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

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        playerIn.playSound(SoundEvents.RAID_HORN, 1.0F, 1.0F);
        return ActionResult.consume(itemstack);
    }
}
