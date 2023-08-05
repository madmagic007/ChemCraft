package me.madmagic.chemcraft.instances.menus.widgets.base;

import net.minecraft.client.gui.GuiGraphics;

public abstract class VerticalGradientWidget extends BaseToolTippedWidget {

    private final int colorFrom, colorTo;
    protected final int max;

    protected VerticalGradientWidget(int x, int y, int width, int height, int max, int colorFrom, int colorTo) {
        super(x, y, width, height);
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

    private static int prependFF(int value) {
        String hex = "ff" + Integer.toHexString(value);
        return (int) Long.parseLong(hex, 16);
    }
}
