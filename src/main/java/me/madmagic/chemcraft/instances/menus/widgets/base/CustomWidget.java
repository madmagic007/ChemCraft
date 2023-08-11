package me.madmagic.chemcraft.instances.menus.widgets.base;

import me.madmagic.chemcraft.util.MouseUtil;
import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class CustomWidget<T extends CustomWidget<T>> implements Renderable, IToolTippedWidget {

    protected List<Component> toolTips = new ArrayList<>();

    protected ResourceLocation texture;
    protected int x, y, width, height;
    protected boolean isHovered;
    protected Runnable onPress;

    public CustomWidget(int x, int y, int width, int height, ResourceLocation defaultTexture) {
        this(x, y);
        this.width = width;
        this.height = height;
        texture = defaultTexture;
    }

    public CustomWidget(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public T center() {
        x -= width / 2;
        return (T) this;
    }

    public T setOnClick(Runnable runnable) {
        onPress = runnable;
        return (T) this;
    }

    public T addToolTip(String text) {
        toolTips.add(Component.literal(text));
        return (T) this;
    }

    public T addToolTip(Component component) {
        toolTips.add(component);
        return (T) this;
    }

    public T setToolTips(String... toolTips) {
        this.toolTips.clear();

        for (String toolTip : toolTips) {
            this.toolTips.add(Component.literal(toolTip));
        }
        return (T) this;
    }

    public void addTo(ScreenHelper screenHelper) {
        x += screenHelper.x;
        y += screenHelper.y;
        screenHelper.addWidget(this);
    }

    @Override
    public List<Component> getToolTips() {
        return toolTips;
    }

    @Override
    public boolean isHovered() {
        return isHovered;
    }

    public boolean onClicked() {
        if (!isHovered || onPress == null) return false;
        onPress.run();
        return true;
    }

    protected boolean checkHovered(int mouseX, int mouseY) {
        isHovered = MouseUtil.isMouseOver(mouseX, mouseY, x, y, width, height);
        return isHovered;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        checkHovered(pMouseX, pMouseY);
        pGuiGraphics.blit(texture, x, y, 0, 0, 0, width, height, width, height);
    }
}
