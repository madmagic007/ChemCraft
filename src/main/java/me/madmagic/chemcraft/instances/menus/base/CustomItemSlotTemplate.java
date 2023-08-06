package me.madmagic.chemcraft.instances.menus.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomItemSlotTemplate {

    public final int  x, y;
    public final boolean isOutput;
    private final List<Item> acceptedItems;

    public CustomItemSlotTemplate(int x, int y, Item... acceptedItems) {
        this(x, y, false, acceptedItems);
    }

    public CustomItemSlotTemplate(int x, int y, boolean isOutput, Item... acceptedItems) {
        this.x = x;
        this.y = y;
        this.isOutput = isOutput;
        this.acceptedItems = List.of(acceptedItems);
    }

    public boolean mayPlace(@NotNull ItemStack stack, boolean force) {
        if (force) {
            // Ignore the value of isOutput and acceptedItems if force is true
            return acceptedItems.isEmpty() || acceptedItems.contains(stack.getItem());
        } else {
            // Check the regular conditions
            return acceptedItems.contains(stack.getItem()) && !isOutput;
        }
    }
}
