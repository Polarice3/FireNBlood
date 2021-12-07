package com.Polarice3.FireNBlood.items;

import com.Polarice3.FireNBlood.FireNBlood;
import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class GoldTotemItem extends Item {
    private static final String SOULSAMOUNT = "Souls";
    public static final int MAXSOULS = 10000;

    public GoldTotemItem() {
        super(new Item.Properties().group(FireNBlood.TAB).maxStackSize(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null){
            CompoundNBT compound = stack.getOrCreateTag();
            compound.putInt(SOULSAMOUNT, 0);
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    private static boolean isFull(ItemStack itemStack) {
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULSAMOUNT);
        return Soulcount == MAXSOULS;
    }

    public static void handleKill(PlayerEntity playerEntity) {
        ItemStack foundStack = ItemStack.EMPTY;

        for (int i = 0; i <= 9; i++) {
            ItemStack itemStack = playerEntity.inventory.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                foundStack = itemStack;
                break;
            }
        }

        if (!foundStack.isEmpty()) {
            increaseSouls(foundStack, 1);
        }
    }

    public static void increaseSouls(ItemStack itemStack, int souls) {
        if (itemStack.getItem() != RegistryHandler.GOLDTOTEM.get()) {
            return;
        }
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULSAMOUNT);
        if (!isFull(itemStack)) {
            Soulcount += souls;
            itemStack.getTag().putInt(SOULSAMOUNT, Soulcount);
        }
    }

   @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getTag() != null;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        if (stack.getTag() != null) {
            int Soulcount = stack.getTag().getInt(SOULSAMOUNT);
            return 1.0D - (Soulcount / (double) MAXSOULS);
        } else {
            return 1.0D;
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null) {
            int Soulcounts = stack.getTag().getInt(SOULSAMOUNT);
            tooltip.add(new TranslationTextComponent("info.firenblood.goldtotem.souls", Soulcounts, MAXSOULS));
        }
    }

}
