package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.item.Item;

public class BlazeCoreItem extends Item {
    public BlazeCoreItem(){
        super(new Properties().group(FireNBlood.TAB).isImmuneToFire());
    }
}
