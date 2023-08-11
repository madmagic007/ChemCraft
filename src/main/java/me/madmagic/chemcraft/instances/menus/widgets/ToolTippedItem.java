package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.menus.widgets.base.CustomWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ToolTippedItem extends CustomWidget<ToolTippedItem> {

    private ItemStack itemStack;
    private ResourceLocation overlay;

    public ToolTippedItem(int x, int y, Item item, String toolTip) {
        this(x, y, 16, item, toolTip);
    }

    public ToolTippedItem(int x, int y, int size, Item item, String toolTip) {
        super(x, y);
        width = size;
        height = size;
        itemStack = new ItemStack(item, 1);
        addToolTip(Component.translatable(toolTip));
    }

    public boolean isItem(Item item) {
        return itemStack.getItem().equals(item);
    }

    public void setItem(Item item) {
        itemStack = new ItemStack(item, 1);
    }

    public void setOverLay(ResourceLocation location) {
        overlay = location;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        checkHovered(pMouseX, pMouseY);
        pGuiGraphics.renderItem(itemStack, x, y);

        if (overlay != null)
            pGuiGraphics.blit(overlay, x + 4, y + 4, 200, 0, 0, 10, 10, 10, 10);
    }
}
