package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MutatedMuttonItem extends Item {
    public MutatedMuttonItem() {
        super(new Properties()
                .group(FireNBlood.TAB)
                .maxDamage(8)
        );
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        int random = worldIn.rand.nextInt(16);
        if (random == 0) {
            EffectInstance effectinstance1 = entityLiving.getActivePotionEffect(Effects.HUNGER);
            if (effectinstance1 == null) {
                EffectInstance effectinstance = new EffectInstance(Effects.HUNGER, 600, 0);
                entityLiving.addPotionEffect(effectinstance);
            } else {
                int amp = effectinstance1.getAmplifier();
                int i = amp + 1;
                i = MathHelper.clamp(i, 0, 5);
                entityLiving.removeActivePotionEffect(Effects.HUNGER);
                EffectInstance effectinstance = new EffectInstance(Effects.HUNGER, 600, i);
                entityLiving.addPotionEffect(effectinstance);
            }
        }
        PlayerEntity playerentity = (PlayerEntity) entityLiving;
        playerentity.getFoodStats().addStats(8, 1);
        stack.damageItem(1, playerentity, (player) -> {
            player.sendBreakAnimation(playerentity.getActiveHand());
        });
        return stack;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.setActiveHand(handIn);
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        return ActionResult.resultConsume(itemstack);
    }


    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

}
