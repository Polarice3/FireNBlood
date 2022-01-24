package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;

import javax.annotation.Nonnull;

public class PhilosophersStoneItem extends Item implements IForgeItem {
    public PhilosophersStoneItem(){
        super(new Item.Properties().group(FireNBlood.TAB).maxDamage(64));
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        container.setDamage(itemStack.getDamage() + 1);
        return container;
    }

}
