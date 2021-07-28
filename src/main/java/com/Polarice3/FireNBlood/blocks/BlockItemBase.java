package com.Polarice3.FireNBlood.blocks;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockItemBase extends BlockItem {
    public BlockItemBase(Block blockIn) {
        super(blockIn, new Item.Properties().group(FireNBlood.TAB));
    }
}
