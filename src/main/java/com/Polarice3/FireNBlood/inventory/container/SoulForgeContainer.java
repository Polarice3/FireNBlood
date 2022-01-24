package com.Polarice3.FireNBlood.inventory.container;
/*import com.Polarice3.FireNBlood.utils.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SoulForgeContainer extends Container {
    private final IInventory tileSoulForge;
    private final IIntArray field_216983_d;
    protected final World world;
    private final IRecipeType<? extends AbstractCookingRecipe> recipeType;

    public SoulForgeContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(3), new IntArray(4));
    }

    public SoulForgeContainer(int id, PlayerInventory playerInventory, IInventory inventory, IIntArray p_i50096_4_) {
        super(ModContainerType.SOULFORGE, id);
        assertInventorySize(inventory, 3);
        assertIntArraySize(p_i50096_4_, 4);
        this.recipeType = IRecipeType.SMELTING;
        this.tileSoulForge = inventory;
        this.field_216983_d = p_i50096_4_;
        this.world = playerInventory.player.world;
        this.addSlot(new InputSlot(inventory, 0, 56, 17));
        this.addSlot(new FuelSlot(this, inventory, 1, 56, 53));
        this.addSlot(new ResultSlot(playerInventory.player, inventory, 2, 116, 35));
        this.trackIntArray(p_i50096_4_);

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.trackIntArray(p_i50096_4_);

    }

    protected boolean hasRecipe(ItemStack stack) {
        return this.world.getRecipeManager().getRecipe((IRecipeType)this.recipeType, new Inventory(stack), this.world).isPresent();
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0) {
                if (this.hasRecipe(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else
                if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
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

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileSoulForge.isUsableByPlayer(playerIn);
    }

    @OnlyIn(Dist.CLIENT)
    public int func_216982_e() {
        return this.field_216983_d.get(1);
    }

    @OnlyIn(Dist.CLIENT)
    public int func_216981_f() {
        return this.field_216983_d.get(0);
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.field_216983_d.get(2);
        int j = this.field_216983_d.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int i = this.field_216983_d.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.field_216983_d.get(0) * 13 / i;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isBurning() {
        return this.field_216983_d.get(0) > 0;
    }

    static class InputSlot extends Slot {
        public InputSlot(IInventory iInventoryIn, int index, int xPosition, int yPosition) {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        *
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.

        public boolean isItemValid(ItemStack stack) {
            return net.minecraftforge.common.brewing.BrewingRecipeRegistry.isValidIngredient(stack);
        }

        *
         * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the
         * case of armor slots)

        public int getSlotStackLimit() {
            return 64;
        }
    }

    class FuelSlot extends Slot {
        private final SoulForgeContainer furnaceContainer;

        public FuelSlot(SoulForgeContainer furnaceContainer, IInventory furnaceInventory, int p_i50084_3_, int p_i50084_4_, int p_i50084_5_) {
            super(furnaceInventory, p_i50084_3_, p_i50084_4_, p_i50084_5_);
            this.furnaceContainer = furnaceContainer;
        }

        *
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.

        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() == RegistryHandler.GOLDTOTEM.get();
        }

    }

    public class ResultSlot extends Slot {
        private final PlayerEntity player;
        private int removeCount;

        public ResultSlot(PlayerEntity player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
            super(inventoryIn, slotIndex, xPosition, yPosition);
            this.player = player;
        }

        public boolean isItemValid(ItemStack stack) {
            return false;
        }

        public ItemStack decrStackSize(int amount) {
            if (this.getHasStack()) {
                this.removeCount += Math.min(amount, this.getStack().getCount());
            }

            return super.decrStackSize(amount);
        }

        public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
            this.onCrafting(stack);
            super.onTake(thePlayer, stack);
            return stack;
        }

        protected void onCrafting(ItemStack stack, int amount) {
            this.removeCount += amount;
            this.onCrafting(stack);
        }

        protected void onCrafting(ItemStack stack) {
            stack.onCrafting(this.player.world, this.player, this.removeCount);
            if (!this.player.world.isRemote && this.inventory instanceof AbstractFurnaceTileEntity) {
                ((AbstractFurnaceTileEntity)this.inventory).unlockRecipes(this.player);
            }

            this.removeCount = 0;
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
        }
    }


}*/
