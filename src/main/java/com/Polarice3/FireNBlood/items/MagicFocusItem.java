package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MagicFocusItem extends Item{
    public static final String FOCUS = "Focus";
    public static final String SOULCOST = "Soul Cost";
    public int soulcost;

    public MagicFocusItem(int soulcost){
        super(new Item.Properties()
                .tab(FireNBlood.TAB)
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .stacksTo(1)
        );
        this.soulcost = soulcost;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        CompoundNBT compound = stack.getOrCreateTag();
        compound.putString(FOCUS, stack.getItem().getDescriptionId());
        compound.putInt(SOULCOST, soulcost);
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (soulcost != 0) {
            tooltip.add(new TranslationTextComponent("info.firenblood.soulitems.focuscost", soulcost));
        } else {
            tooltip.add(new TranslationTextComponent("info.firenblood.soulitems.focuscost", 0));
        }
    }

}
