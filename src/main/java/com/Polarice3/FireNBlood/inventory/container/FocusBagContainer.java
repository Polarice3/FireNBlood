package com.Polarice3.FireNBlood.inventory.container;

import com.Polarice3.FireNBlood.items.handler.FocusBagItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

public class FocusBagContainer extends Container {
    private final ItemStack stack;

    public static FocusBagContainer createContainerClientSide(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new FocusBagContainer(id, inventory, new FocusBagItemHandler(ItemStack.EMPTY), ItemStack.EMPTY);
    }

    public FocusBagContainer(int id, PlayerInventory playerInventory, FocusBagItemHandler handler, ItemStack stack) {
        super(ModContainerType.FOCUSBAG.get(), id);
        this.stack = stack;
        for (int i = 0; i < 5; i++) {
            addSlot(new SlotItemHandler(handler, i, 62 - 18 + i * 18, 25));
        }
        for (int i = 0; i < 5; i++) {
            addSlot(new SlotItemHandler(handler,5 + i, 62 - 18 + i * 18, 44));
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (this.moveItemStackTo(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            } else if (index >= 1 && index < 28) {
                if (!this.moveItemStackTo(itemstack1, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 28 && index < 37) {
                if (!this.moveItemStackTo(itemstack1, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return (player.getMainHandItem() == stack || player.getOffhandItem() == stack) && !stack.isEmpty();
    }
}
