package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
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

public class UncookedMutatedItem extends Item {
    public UncookedMutatedItem() {
        super(new Properties()
                .tab(FireNBlood.TAB)
                .durability(8)
        );
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        int random = worldIn.random.nextInt(4);
        if (random == 0) {
            EffectInstance effectinstance1 = entityLiving.getEffect(Effects.HUNGER);
            if (effectinstance1 == null) {
                EffectInstance effectinstance = new EffectInstance(Effects.HUNGER, 600, 0);
                entityLiving.addEffect(effectinstance);
                EffectInstance effectinstance2 = new EffectInstance(RegistryHandler.HOSTED.get(), 600, 0);
                entityLiving.addEffect(effectinstance2);
            } else {
                int amp = effectinstance1.getAmplifier();
                int i = amp + 1;
                i = MathHelper.clamp(i, 0, 5);
                entityLiving.removeEffectNoUpdate(Effects.HUNGER);
                EffectInstance effectinstance = new EffectInstance(Effects.HUNGER, 600, i);
                entityLiving.addEffect(effectinstance);
                EffectInstance effectinstance2 = new EffectInstance(RegistryHandler.HOSTED.get(), 600, i);
                entityLiving.addEffect(effectinstance2);
            }
        }
        PlayerEntity playerentity = (PlayerEntity) entityLiving;
        playerentity.getFoodData().eat(4, 0.8F);
        stack.hurtAndBreak(1, playerentity, (player) -> {
            player.broadcastBreakEvent(playerentity.getUsedItemHand());
        });
        return stack;
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.startUsingItem(handIn);
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        return ActionResult.consume(itemstack);
    }


    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.EAT;
    }

}
