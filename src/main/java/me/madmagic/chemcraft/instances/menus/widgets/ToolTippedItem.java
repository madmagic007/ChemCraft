package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.menus.widgets.base.BaseToolTippedWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ToolTippedItem extends BaseToolTippedWidget {

    private final ItemStack itemStack;
    private ResourceLocation overlay;

    public ToolTippedItem(int pX, int pY, int size, Item item, String toolTip) {
        super(pX, pY, size, size, toolTip);
        itemStack = new ItemStack(item, 1);
    }

    public ToolTippedItem(int pX, int pY, Item item, String toolTip) {
        super(pX, pY, 16, 16, toolTip);
        itemStack = new ItemStack(item, 1);
    }

    public void setOverLay(ResourceLocation location) {
        overlay = location;
    }

    @Override
    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int p_283472_, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
        super.renderTexture(pGuiGraphics, pTexture, pX, pY, pUOffset, pVOffset, p_283472_, pWidth, pHeight, pTextureWidth, pTextureHeight);

    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.renderItem(itemStack, getX(), getY());

        if (overlay != null)
            pGuiGraphics.blit(overlay, getX() + 4, getY() + 4, 200, 0, 0, 10, 10, 10, 10);
    }
}
