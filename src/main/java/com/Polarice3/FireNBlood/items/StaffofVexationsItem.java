package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.entities.ally.FriendlyVexEntity;
import com.Polarice3.FireNBlood.init.ModEntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class StaffofVexationsItem extends SoulUsingItem {

    public StaffofVexationsItem() {
        super(new Item.Properties().group(FireNBlood.TAB).maxDamage(256));
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        ServerWorld serverworld = (ServerWorld)entityLiving.world;
        for(int i1 = 0; i1 < 3; ++i1) {
            BlockPos blockpos = entityLiving.getPosition();
            FriendlyVexEntity vexentity = ModEntityType.FRIENDLY_VEX.get().create(entityLiving.world);
            assert vexentity != null;
            vexentity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
            vexentity.onInitialSpawn(serverworld, entityLiving.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            vexentity.setOwner((PlayerEntity) entityLiving);
            vexentity.setBoundOrigin(blockpos);
            vexentity.setLimitedLife(20 * (30 + entityLiving.world.rand.nextInt(90)));
            serverworld.func_242417_l(vexentity);
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
            entityLiving.world.addParticle(ParticleTypes.POOF, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
        }
        stack.damageItem(1, entityLiving, (player) -> {
            player.sendBreakAnimation(entityLiving.getActiveHand());
        });
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
        playerIn.world.addParticle(ParticleTypes.PORTAL, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), 0.0F, 0.0F, 0.0F);
        return ActionResult.resultConsume(itemstack);
    }

}
