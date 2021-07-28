package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class MockingEffigyItem extends Item {
    public MockingEffigyItem() {
        super(new Item.Properties().group(FireNBlood.TAB).maxStackSize(1));
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        entityLiving.addPotionEffect(new EffectInstance(RegistryHandler.EVIL_EYE.get(), 12000, 4));
        entityLiving.world.createExplosion(entityLiving, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), 3.0F, Explosion.Mode.NONE);
        entityLiving.world.addParticle(ParticleTypes.FLAME, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
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
        return ActionResult.resultConsume(itemstack);
    }
}
