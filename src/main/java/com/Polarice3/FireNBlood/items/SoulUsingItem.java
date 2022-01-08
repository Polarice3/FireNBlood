package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class SoulUsingItem extends Item {
    public SoulUsingItem(Properties properties) {
        super(properties);
    }

    public abstract int SoulCost();

    public abstract ItemStack MagicResults(ItemStack stack, World worldIn, LivingEntity entityLiving);

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        ItemStack foundStack = ItemStack.EMPTY;
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        for (int i = 0; i <= 9; i++) {
            ItemStack itemStack = playerEntity.inventory.getStackInSlot(i);
            if (!itemStack.isEmpty() && itemStack.getItem() == RegistryHandler.GOLDTOTEM.get()) {
                foundStack = itemStack;
                break;
            }
        }

        if (!foundStack.isEmpty() && GoldTotemItem.currentSouls(foundStack) > SoulCost()) {
            GoldTotemItem.decreaseSouls(foundStack, SoulCost());
            return MagicResults(stack, worldIn, entityLiving);
        } else {
            worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            for(int i = 0; i < entityLiving.world.rand.nextInt(35) + 10; ++i) {
                entityLiving.world.addParticle(ParticleTypes.CLOUD, entityLiving.getPosX(), entityLiving.getPosYEye(), entityLiving.getPosZ(), 0.0F, 0.0F, 0.0F);
            }
            return stack;
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("info.firenblood.soulitems.cost", SoulCost()));
    }


}
