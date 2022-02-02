package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.bosses.VizierEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class DarkScrollItem extends Item {
    public DarkScrollItem() {
        super(new Properties().group(FireNBlood.TAB).maxStackSize(1));
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.EVENT_RAID_HORN, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        VizierEntity vizier = new VizierEntity(ModEntityType.VIZIER.get(), worldIn);
        vizier.setPosition(entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ());
        worldIn.addEntity(vizier);
        stack.setCount(0);
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
        playerIn.world.addParticle(ParticleTypes.ANGRY_VILLAGER, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), 0.0F, 0.0F, 0.0F);
        return ActionResult.resultConsume(itemstack);
    }
}
