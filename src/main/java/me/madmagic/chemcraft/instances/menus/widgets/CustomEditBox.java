package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CustomEditBox extends EditBox {

    private String lastValue = "";
    private Supplier<?> lateValueSup;
    private String lastSupValue = "";
    private int maxValue = 0;
    private Consumer<Integer> onValueChanged;

    public CustomEditBox(Font pFont, int pX, int pY, int pWidth, int pHeight) {
        super(pFont, pX, pY, pWidth, pHeight, Component.literal(""));
    }

    public CustomEditBox setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public CustomEditBox setOnValueChanged(Consumer<Integer> onValueChanged) {
        this.onValueChanged = onValueChanged;
        return this;
    }

    public CustomEditBox setValue(Supplier<?> supplier) {
        lateValueSup = supplier;
        return this;
    }

    public CustomEditBox setTooltip(String text) {
        setTooltip(Tooltip.create(Component.literal(text)));
        return this;
    }

    public CustomEditBox setMaxCharLength(int length) {
        setMaxLength(length);
        return this;
    }


    public CustomEditBox centerHorizontally(ScreenHelper screenHelper) {
        setX((int) (screenHelper.x + (screenHelper.imageWidth / 2.) - (width / 2.)));
        return this;
    }

    public CustomEditBox centerVertically(ScreenHelper screenHelper) {
        setY(screenHelper.y + screenHelper.imageHeight / 2 - height / 2);
        return this;
    }

    public CustomEditBox center(ScreenHelper screenHelper) {
        centerHorizontally(screenHelper);
        centerVertically(screenHelper);
        return this;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (lateValueSup != null) {
            String supValue = String.valueOf(lateValueSup.get());
            String baseValue = super.getValue();

            if (!lastSupValue.equals(supValue) && baseValue.equals(lastValue)) {
                lastSupValue = supValue;
                lastValue = supValue;
                setValue(supValue);
            } else if (!baseValue.equals(lastValue) && lastSupValue.equals(supValue)) {
                lastValue = baseValue;
            }
        }

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (isFocused()) {
            if (GeneralUtil.isAny(pKeyCode, 257, 335, 256)) {
                setFocused(false);

                int val;

                try {
                    val = GeneralUtil.clamp(Integer.parseInt(getValue()), maxValue);
                } catch (Exception ignored) {
                    return false;
                }

                setValue(String.valueOf(val));

                if (onValueChanged != null) onValueChanged.accept(val);

                return false;
            }
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        if (isFocused() && !Character.isDigit(pCodePoint)) return false;
        return super.charTyped(pCodePoint, pModifiers);
    }
}
