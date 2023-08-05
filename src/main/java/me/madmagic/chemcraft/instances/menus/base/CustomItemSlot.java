package me.madmagic.chemcraft.instances.menus.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CustomItemSlot extends SlotItemHandler {

    private final List<Item> acceptedItems;
    private final boolean isOutput;

    public CustomItemSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Item... accept) {
        this(itemHandler, index, xPosition, yPosition, false, accept);
    }

    public CustomItemSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean isOutput, Item... accept) {
        super(itemHandler, index, xPosition, yPosition);
        acceptedItems = Arrays.asList(accept);
        this.isOutput = isOutput;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return acceptedItems.contains(stack.getItem()) && !isOutput;
    }
}
