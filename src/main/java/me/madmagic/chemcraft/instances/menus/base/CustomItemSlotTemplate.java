package me.madmagic.chemcraft.instances.menus.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class CustomItemSlotTemplate {

    public final int  x, y;
    public final boolean isOutput;
    private final List<Item> acceptedItems;
    private Predicate<ItemStack> itemPredicate;

    public CustomItemSlotTemplate(int x, int y, Predicate<ItemStack> itemPredicate) {
        this(x, y, false);
        this.itemPredicate = itemPredicate;
    }

    public CustomItemSlotTemplate(int x, int y, Item... acceptedItems) {
        this(x, y, false, acceptedItems);
    }

    public CustomItemSlotTemplate(int x, int y, boolean isOutput, Item... acceptedItems) {
        this.x = x;
        this.y = y;
        this.isOutput = isOutput;
        this.acceptedItems = List.of(acceptedItems);
    }

    private boolean itemValid(ItemStack stack) {
        return acceptedItems.contains(stack.getItem()) || (itemPredicate != null && itemPredicate.test(stack));
    }

    public boolean mayPlace(ItemStack stack, boolean force) {
        if (force) {
            // Ignore the value of isOutput and acceptedItems if force is true
            return acceptedItems.isEmpty() || itemValid(stack);
        } else {
            // Check the regular conditions
            return itemValid(stack) && !isOutput;
        }
    }
}
