package me.madmagic.chemcraft.util.itemstorage;

import me.madmagic.chemcraft.instances.menus.base.CustomItemSlotTemplate;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.List;

public class WrappedItemStackHandler implements IItemHandlerModifiable {

    private final IItemHandlerModifiable handler;
    private final List<CustomItemSlotTemplate> slotTemplates;

    public WrappedItemStackHandler(IItemHandlerModifiable handler, List<CustomItemSlotTemplate> slotTemplates) {
        this.handler = handler;
        this.slotTemplates = slotTemplates;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        handler.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        return handler.getSlotLimit(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return isItemValid(slot, stack) ? handler.insertItem(slot, stack, simulate) : stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slotTemplates.get(slot).isOutput ? handler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        boolean val = slotTemplates.get(slot).mayPlace(stack, false) && handler.isItemValid(slot, stack);
        return val;
    }
}