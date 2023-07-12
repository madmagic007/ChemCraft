package me.madmagic.chemcraft.instances.menus.base;

import me.madmagic.chemcraft.ChemCraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class BaseScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected final ResourceLocation texture;
    protected int x, y;

    public BaseScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, ResourceLocation texture, int width, int height) {
        super(pMenu, pPlayerInventory, pTitle);
        this.texture = texture;

        imageWidth = width;
        imageHeight = height;
    }

    @Override
    protected void init() {
        x = (width - imageWidth) / 2;
        y = (height - imageHeight) / 2;

        super.init();
    }

    protected static ResourceLocation defineTexture(String name) {
        return new ResourceLocation(ChemCraft.modId, String.format("textures/gui/%s.png", name));
    }

    protected void addImageButton(int x, int y, int width, int height, ResourceLocation texture, Button.OnPress onPress) {
        addRenderableWidget(new ImageButton(x, y, width, height, 0, 0, 0, texture, width, height, onPress));
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
