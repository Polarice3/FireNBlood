package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class RocketBoosterItem extends Item {
    public RocketBoosterItem() {
        super(new Item.Properties().group(FireNBlood.TAB).maxDamage(256));
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Items.FIRE_CHARGE;
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        PlayerEntity playerIn = context.getPlayer();
        if (!world.isRemote) {
            ItemStack itemstack = context.getItem();
            Vector3d vector3d = context.getHitVec();
            Direction direction = context.getFace();
            FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(world, context.getPlayer(), vector3d.x + (double)direction.getXOffset() * 0.15D, vector3d.y + (double)direction.getYOffset() * 0.15D, vector3d.z + (double)direction.getZOffset() * 0.15D, itemstack);
            world.addEntity(fireworkrocketentity);
        }

        return ActionResultType.func_233537_a_(world.isRemote);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (playerIn.isElytraFlying()) {
            if (!worldIn.isRemote) {
                worldIn.addEntity(new FireworkRocketEntity(worldIn, itemstack, playerIn));
            }
            itemstack.damageItem(1, playerIn, (player) ->
                    player.sendBreakAnimation(player.getActiveHand()));
            return ActionResult.func_233538_a_(playerIn.getHeldItem(handIn), worldIn.isRemote());
        } else {
            itemstack.damageItem(1, playerIn, (player) ->
                    player.sendBreakAnimation(player.getActiveHand()));
            return ActionResult.resultPass(playerIn.getHeldItem(handIn));
        }
    }
}
