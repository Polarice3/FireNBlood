package com.Polarice3.FireNBlood.potions;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectType;

import java.util.ArrayList;
import java.util.List;

public class EvilEyeEffect extends ModEffects{
    public EvilEyeEffect() {
        super(EffectType.NEUTRAL, 745784);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

}
