package me.madmagic.chemcraft.instances.menus.base;

import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class BaseMenuScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected final ScreenHelper screenHelper;

    public BaseMenuScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, ResourceLocation texture, int width, int height) {
        super(pMenu, pPlayerInventory, pTitle);

        screenHelper = new ScreenHelper(texture);

        imageWidth = width;
        imageHeight = height;
    }

    @Override
    protected void init() {
        super.init();
        screenHelper.init(leftPos, topPos, imageWidth, imageHeight, this);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        screenHelper.renderGuiImage(pGuiGraphics);
    }

    protected void renderOnlyTitle(GuiGraphics pGuiGraphics) {
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        screenHelper.renderDarkBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        screenHelper.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (screenHelper.handleClicked()) return true;
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
