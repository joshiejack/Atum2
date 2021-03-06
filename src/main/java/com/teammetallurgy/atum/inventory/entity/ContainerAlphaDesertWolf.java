package com.teammetallurgy.atum.inventory.entity;

import com.teammetallurgy.atum.entity.animal.EntityDesertWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerAlphaDesertWolf extends Container {
    private final IInventory wolfInventory;
    private final EntityDesertWolf desertWolf;

    public ContainerAlphaDesertWolf(IInventory playerInventory, IInventory wolfInventory, final EntityDesertWolf desertWolf, EntityPlayer player) {
        this.wolfInventory = wolfInventory;
        this.desertWolf = desertWolf;
        wolfInventory.openInventory(player);
        this.addSlotToContainer(new Slot(wolfInventory, 0, 8, 18) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Items.SADDLE && !this.getHasStack();
            }

            @Override
            public boolean isEnabled() {
                return desertWolf.isAlpha();
            }
        });
        this.addSlotToContainer(new Slot(wolfInventory, 1, 8, 36) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return desertWolf.isArmor(stack);
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < 9; ++slot) {
                this.addSlotToContainer(new Slot(playerInventory, slot + row * 9 + 9, 8 + slot * 18, 102 + row * 18 + -18));
            }
        }
        for (int slot = 0; slot < 9; ++slot) {
            this.addSlotToContainer(new Slot(playerInventory, slot, 8 + slot * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return this.wolfInventory.isUsableByPlayer(player) && this.desertWolf.isEntityAlive() && this.desertWolf.getDistance(player) < 8.0F;
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (index < this.wolfInventory.getSizeInventory()) {
                if (!this.mergeItemStack(slotStack, this.wolfInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).isItemValid(slotStack) && !this.getSlot(1).getHasStack()) {
                if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).isItemValid(slotStack)) {
                if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.wolfInventory.getSizeInventory() <= 2 || !this.mergeItemStack(slotStack, 2, this.wolfInventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        this.wolfInventory.closeInventory(player);
    }
}