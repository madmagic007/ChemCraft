package me.madmagic.chemcraft.util;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.menus.widgets.*;
import me.madmagic.chemcraft.instances.menus.widgets.base.CustomWidget;
import me.madmagic.chemcraft.instances.menus.widgets.base.IToolTippedWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ScreenHelper {

    public static final ResourceLocation noInvPower = getTexture("no_inv_power");
    public static final ResourceLocation cross = getTexture("cross");
    public static final ResourceLocation buttonUp = getTexture("button_up");
    public static final ResourceLocation buttonDown = getTexture("button_down");
    public static final Font font = Minecraft.getInstance().font;

    private final ResourceLocation texture;
    public int x, y, imageWidth, imageHeight, halfImageWidth;
    private final List<Renderable> widgets = new ArrayList<>();
    private Screen screen;

    public ScreenHelper(ResourceLocation texture) {
        this.texture = texture;
    }

    public void init(int x, int y, int imageWidth, int imageHeight, Screen screen) {
        this.screen = screen;
        this.x = x;
        this.y = y;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.halfImageWidth = imageWidth / 2;

        widgets.clear();
    }

    public void addString(int x, int y, String text, float scale) {
        new CustomLabel(x, y, text).setScale(scale).addTo(this);
    }

    //region widgets
    public CustomWidget addImageButton(int x, int y, int width, int height, ResourceLocation texture, Runnable onPress, String toolTip) {
        CustomWidget widget = new CustomWidget(x, y, width, height, texture).setOnClick(onPress).addToolTip(toolTip);
        widget.addTo(this);
        return widget;
    }

    public void addContainerDataVerticalWidget(int x, int y, int width, int height, String itemName, String suffix, ContainerData data, int valuePos, int maxValue, int colorFrom, int colorTo) {
        ContainerDataVerticalGradientWidget widget = new ContainerDataVerticalGradientWidget(x, y, width, height, itemName, suffix, data, valuePos, maxValue, colorFrom, colorTo);
        widget.addTo(this);
    }

    public ToolTippedEditBox addEditorBox(int x, int y, int width, int height, String toolTip) {
        ToolTippedEditBox widget = new ToolTippedEditBox(font, x, y, width, height, toolTip);
        widget.addTo(this);
        return widget;
    }

    public ToolTippedItem addItem(int x, int y, Item item, String defaultToolTip) {
        ToolTippedItem toolTippedItem = new ToolTippedItem(this.x + x, this.y + y, item, defaultToolTip);
        widgets.add(toolTippedItem);
        return toolTippedItem;
    }

    public void addWidget(Renderable widget) {
        widgets.add(widget);
    }

    public void removeWidget(Renderable widget) {
        widgets.remove(widget);
    }
    //endregion

    public void renderDarkBackground(GuiGraphics guiGraphics) {
        screen.renderBackground(guiGraphics);
    }

    public void renderGuiImage(GuiGraphics guiGraphics) {
        guiGraphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        widgets.forEach(widget -> {
            widget.render(guiGraphics, mouseX, mouseY, partialTick);

            if (widget instanceof IToolTippedWidget customWidget && customWidget.isHovered())
                customWidget.renderToolTip(guiGraphics, mouseX, mouseY);
        });
    }

    public boolean handleClicked() {
        for (Renderable widget : widgets) {
            if (widget instanceof CustomWidget customWidget && customWidget.isHovered())
                return customWidget.onClicked();
        }
        return false;
    }

    public static int incFontY(AtomicInteger y, int extraValue) {
        return y.addAndGet(font.lineHeight + extraValue);
    }

    public static ResourceLocation getTexture(String name) {
        return new ResourceLocation(ChemCraft.modId, String.format("textures/gui/%s.png", name));
    }
}
