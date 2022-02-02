package com.Polarice3.FireNBlood.inventory.container;

import com.Polarice3.FireNBlood.items.handler.FocusBagItemHandler;
import com.Polarice3.FireNBlood.items.handler.SoulUsingItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.items.SlotItemHandler;

public class WandandBagContainer extends Container {
    private final ItemStack stack;
    private final Hand hand;

    public static WandandBagContainer createContainerClientSide(int id, PlayerInventory inventory, PacketBuffer buffer) {
        Hand hand = buffer.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
        return new WandandBagContainer(id, new SoulUsingItemHandler(ItemStack.EMPTY), new FocusBagItemHandler(ItemStack.EMPTY), ItemStack.EMPTY, hand);
    }

    public WandandBagContainer(int id, SoulUsingItemHandler soulUsingItemHandler, FocusBagItemHandler bagItemHandler, ItemStack stack, Hand hand) {
        super(ModContainerType.WAND.get(), id);
        this.stack = stack;
        this.hand = hand;
        this.addSlot(new SlotItemHandler(soulUsingItemHandler, 0, 80, 35));

        for (int i = 0; i < 5; i++) {
            addSlot(new SlotItemHandler(bagItemHandler, i, 62 - 18 + i * 18, 104));
        }
        for (int i = 0; i < 5; i++) {
            addSlot(new SlotItemHandler(bagItemHandler,5 + i, 62 - 18 + i * 18, 122));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return (player.getHeldItemMainhand() == stack || player.getHeldItemOffhand() == stack) && !stack.isEmpty();
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (this.mergeItemStack(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            } else if (index >= 1 && index < 28) {
                if (!this.mergeItemStack(itemstack1, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 28 && index < 37) {
                if (!this.mergeItemStack(itemstack1, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

}
