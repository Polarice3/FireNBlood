package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.projectiles.WarpedSpearEntity;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WarpedSpearItem extends Item implements IVanishable {
    private final Multimap<Attribute, AttributeModifier> warpedspearattributes;

    public WarpedSpearItem() {
        super(new Properties().maxDamage(250).group(FireNBlood.TAB));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 9.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
        this.warpedspearattributes = builder.build();
    }

    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)entityLiving;
            int i = this.getUseDuration(stack) - timeLeft;
            if (i >= 10) {
                stack.damageItem(1, playerentity, (player) -> {
                    player.sendBreakAnimation(entityLiving.getActiveHand());
                });
                WarpedSpearEntity warpedSpearEntity = new WarpedSpearEntity(worldIn, playerentity, stack);
                warpedSpearEntity.func_234612_a_(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, 2.5F + (float)3 * 0.5F, 1.0F);
                if (playerentity.abilities.isCreativeMode) {
                    warpedSpearEntity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                }

                worldIn.addEntity(warpedSpearEntity);
                worldIn.playMovingSound((PlayerEntity)null, warpedSpearEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (!playerentity.abilities.isCreativeMode) {
                    playerentity.inventory.deleteStack(stack);
                }
            }
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (itemstack.getDamage() >= itemstack.getMaxDamage() - 1) {
            return ActionResult.resultFail(itemstack);
        } else {
            playerIn.setActiveHand(handIn);
            return ActionResult.resultConsume(itemstack);
        }
    }

    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(1, attacker, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        target.setFire(30);
        return true;
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double)state.getBlockHardness(worldIn, pos) != 0.0D) {
            stack.damageItem(2, entityLiving, (entity) -> {
                entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.warpedspearattributes : super.getAttributeModifiers(equipmentSlot);
    }

    public int getItemEnchantability() {
        return 1;
    }
}
