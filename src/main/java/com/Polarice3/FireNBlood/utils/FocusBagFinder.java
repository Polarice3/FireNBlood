package com.Polarice3.FireNBlood.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class FocusBagFinder {
    private static boolean isMatchingItem(ItemStack itemStack) {
        return itemStack.getItem() == RegistryHandler.FOCUSBAG.get();
    }
    public static ItemStack findBag(PlayerEntity playerEntity){
        ItemStack foundStack = ItemStack.EMPTY;

        for (int i = 0; i <= playerEntity.inventory.getContainerSize(); i++) {
            ItemStack itemStack = playerEntity.inventory.getItem(i);
            if (!itemStack.isEmpty() && isMatchingItem(itemStack)) {
                foundStack = itemStack;
                break;
            }
        }

        return foundStack;
    }
}
