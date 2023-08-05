package me.madmagic.chemcraft.instances.menus.widgets.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class VerticalGradientWidget extends AbstractWidget implements ITooltipHolder {

    private final int colorFrom, colorTo;
    protected final int max;

    protected VerticalGradientWidget(int x, int y, int width, int height, int max, int colorFrom, int colorTo) {
        super(x, y, width, height, Component.literal(""));
        this.max = max;
        this.colorFrom = prependFF(colorFrom);
        this.colorTo = prependFF(colorTo);
    }

    protected abstract int getValue();

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int scaledValue = (int) (height * (getValue() / (float) max));
        pGuiGraphics.fillGradient(
                getX(), getY() + (height - scaledValue),
                getX() + width, getY() + height,
                colorFrom, colorTo
        );
    }

    @Override
    public boolean iIsHovered() {
        return isHovered();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        defaultButtonNarrationText(pNarrationElementOutput);
    }

    private static int prependFF(int value) {
        String hex = "ff" + Integer.toHexString(value);
        return (int) Long.parseLong(hex, 16);
    }
}
