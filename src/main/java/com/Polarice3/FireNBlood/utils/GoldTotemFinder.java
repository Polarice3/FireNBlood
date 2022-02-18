package com.Polarice3.FireNBlood.utils;

import com.Polarice3.FireNBlood.compat.CuriosLoaded;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

public class GoldTotemFinder {
    private static boolean isMatchingItem(ItemStack itemStack) {
        return itemStack.getItem() == RegistryHandler.GOLDTOTEM.get();
    }

    public static ItemStack FindTotem(PlayerEntity playerEntity){
        if (CuriosLoaded.CURIOS.isLoaded()) {
            return CuriosApi.getCuriosHelper().findEquippedCurio(GoldTotemFinder::isMatchingItem, playerEntity).map(
                    ImmutableTriple::getRight).orElse(ItemStack.EMPTY);
        }
        ItemStack foundStack = ItemStack.EMPTY;

        for (int i = 0; i <= 9; i++) {
            ItemStack itemStack = playerEntity.inventory.getItem(i);
            if (!itemStack.isEmpty() && isMatchingItem(itemStack)) {
                foundStack = itemStack;
                break;
            }
        }

        return foundStack;
    }
}
