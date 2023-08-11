package me.madmagic.chemcraft.instances.menus.widgets.base;

import net.minecraft.client.gui.GuiGraphics;

public abstract class VerticalGradientWidget<T extends VerticalGradientWidget<T>> extends CustomWidget<VerticalGradientWidget<T>> {

    private final int colorFrom, colorTo;
    protected final int max;

    protected VerticalGradientWidget(int x, int y, int width, int height, int max, int colorFrom, int colorTo) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.max = max;
        this.colorFrom = prependFF(colorFrom);
        this.colorTo = prependFF(colorTo);
    }

    protected abstract int getValue();

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        checkHovered(pMouseX, pMouseY);
        int scaledValue = (int) (height * (getValue() / (float) max));
        pGuiGraphics.fillGradient(
                x, y + (height - scaledValue),
                x + width, y + height,
                colorFrom, colorTo
        );
    }

    private static int prependFF(int value) {
        String hex = "ff" + Integer.toHexString(value);
        return (int) Long.parseLong(hex, 16);
    }
}
