package me.madmagic.chemcraft.instances.menus.base;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CustomItemSlot extends SlotItemHandler {

    private final CustomItemSlotTemplate template;

    public CustomItemSlot(IItemHandler itemHandler, CustomItemSlotTemplate template, int index) {
        super(itemHandler, index, template.x, template.y);
        this.template = template;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return template.mayPlace(stack, false);
    }
}
