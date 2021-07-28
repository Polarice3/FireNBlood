package com.Polarice3.FireNBlood.items;

import java.util.Random;
import java.util.function.Predicate;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.ally.FriendlyTankEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class FlameGunItem extends Item {

    public FlameGunItem() {
        super(new Item.Properties().group(FireNBlood.TAB).maxDamage(256));
    }

    private static float getCharge(int useTime, ItemStack stack) {
        float f = (float)useTime / (float)25;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public static int getChargeTime(ItemStack stack) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? 25 : 25 - 5 * i;
    }

    public static boolean isCharged(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTag();
        return compoundnbt != null && compoundnbt.getBoolean("Charged");
    }

    public static void setCharged(ItemStack stack, boolean chargedIn) {
        CompoundNBT compoundnbt = stack.getOrCreateTag();
        compoundnbt.putBoolean("Charged", chargedIn);
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft){
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) entityLiving;
            int i = this.getUseDuration(stack) - timeLeft;
            float f = getCharge(i, stack);
            if (f >= 1.0) {
                setCharged(stack, true);
                if (!worldIn.isRemote && isCharged(stack)) {
                    Vector3d vector3d = playerentity.getLook(1.0F);
                    Random random = worldIn.rand;
                    double d2 = random.nextGaussian() * 0.01D + vector3d.x;
                    double d3 = random.nextGaussian() * 0.01D + vector3d.y;
                    double d4 = random.nextGaussian() * 0.01D + vector3d.z;
                    FireballEntity fireballEntity = new FireballEntity(worldIn, playerentity, d2, d3, d4);
                    fireballEntity.setPosition(playerentity.getPosX() + vector3d.x * 2.0D, playerentity.getPosYHeight(0.5D) + 0.5D, playerentity.getPosZ() + vector3d.z * 2.0D);
                    worldIn.addEntity(fireballEntity);
                    if (!playerentity.isSilent()) {
                        playerentity.world.playEvent(null, 1016, playerentity.getPosition(), 0);
                    }
                    stack.damageItem(1, playerentity, (player) -> {
                        player.sendBreakAnimation(playerentity.getActiveHand());
                    });
                }
            } else {
                if (!worldIn.isRemote && !isCharged(stack)) {
                    Vector3d vector3d = playerentity.getLook(1.0F);
                    Random random = worldIn.rand;
                    double d2 = random.nextGaussian() * 0.05D + (double) vector3d.x;
                    double d3 = random.nextGaussian() * 0.05D + (double) vector3d.y;
                    double d4 = random.nextGaussian() * 0.05D + (double) vector3d.z;
                    SmallFireballEntity fireballEntity = new SmallFireballEntity(worldIn, playerentity, d2, d3, d4);
                    fireballEntity.setPosition(playerentity.getPosX() + vector3d.x * 2.0D, playerentity.getPosYHeight(0.5D) + 0.5D, playerentity.getPosZ() + vector3d.z * 2.0D);
                    worldIn.addEntity(fireballEntity);
                    if (!playerentity.isSilent()) {
                        playerentity.world.playEvent(null, 1016, playerentity.getPosition(), 0);
                    }
                    stack.damageItem(1, playerentity, (player) -> {
                        player.sendBreakAnimation(playerentity.getActiveHand());
                    });
                }
            }
        }
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.setActiveHand(handIn);
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (isCharged(itemstack)) {
            setCharged(itemstack, false);
        }
        return ActionResult.resultConsume(itemstack);
    }

}
