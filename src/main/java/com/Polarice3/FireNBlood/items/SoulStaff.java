package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SoulStaff extends SoulWand{
    public SoulStaff() {
    }

    public void MagicResults(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        ItemStack foundStack = ItemStack.EMPTY;
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        for (int i = 0; i <= 9; i++) {
            ItemStack itemStack = playerEntity.inventory.getItem(i);
            if (!itemStack.isEmpty() && itemStack.getItem() == RegistryHandler.GOLDTOTEM.get()) {
                foundStack = itemStack;
                break;
            }
        }
        if (this.getSpell(stack) != null && !foundStack.isEmpty() && GoldTotemItem.currentSouls(foundStack) >= SoulUse(entityLiving, stack)) {
            GoldTotemItem.decreaseSouls(foundStack, SoulUse(entityLiving, stack));
            this.getSpell(stack).StaffResult(worldIn, entityLiving);
        } else {
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                double d = worldIn.random.nextGaussian() * 0.2D;
                entityLiving.level.addParticle(ParticleTypes.CLOUD, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), d, d, d);
            }
        }
    }
}
