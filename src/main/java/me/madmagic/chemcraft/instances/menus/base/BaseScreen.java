package me.madmagic.chemcraft.instances.menus.base;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.menus.widgets.ContainerDataVerticalGradientWidget;
import me.madmagic.chemcraft.instances.menus.widgets.ToolTippedEditBox;
import me.madmagic.chemcraft.instances.menus.widgets.ToolTippedImageButton;
import me.madmagic.chemcraft.instances.menus.widgets.ToolTippedItem;
import me.madmagic.chemcraft.instances.menus.widgets.base.ITooltipHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;

public class BaseScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {


    protected static final ResourceLocation cross = defineTexture("cross");
    protected static final ResourceLocation buttonUp = defineTexture("button_up");
    protected static final ResourceLocation buttonDown = defineTexture("button_down");

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

    protected void addImageButton(int x, int y, int width, int height, ResourceLocation texture, Button.OnPress onPress, String toolTip) {
        addRenderableWidget(new ToolTippedImageButton(x, y, width, height, texture, onPress, toolTip));
    }

    protected void addContainerDataVerticalWidget(int x, int y, int width, int height, String itemName, String suffix, ContainerData data, int valuePos, int maxValue, int colorFrom, int colorTo) {
        addRenderableWidget(new ContainerDataVerticalGradientWidget(this.x + x, this.y + y, width, height, itemName, suffix, data, valuePos, maxValue, colorFrom, colorTo));
    }

    protected ToolTippedEditBox addEditorBox(int x, int y, int width, int height, String toolTip) {
        ToolTippedEditBox toolTippedEditBox = new ToolTippedEditBox(Minecraft.getInstance().font, x, y, width, height, toolTip);
        addRenderableWidget(toolTippedEditBox);
        return toolTippedEditBox;
    }

    protected ToolTippedItem addItem(int x, int y, Item item, String defaultToolTip) {
        ToolTippedItem toolTippedItem = new ToolTippedItem(x, y, item, defaultToolTip);
        addRenderableWidget(toolTippedItem);
        return toolTippedItem;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight);
    }


    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    protected void renderOnlyTitle(GuiGraphics pGuiGraphics) {
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);

        renderables.forEach(renderable -> {
            if (renderable instanceof ITooltipHolder tooltipHolder && tooltipHolder.iIsHovered())
                tooltipHolder.renderToolTip(pGuiGraphics, pX, pY);
        });
    }
}
