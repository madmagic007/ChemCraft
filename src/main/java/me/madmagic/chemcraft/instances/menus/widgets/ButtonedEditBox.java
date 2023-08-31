package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.menus.widgets.base.CustomWidget;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ButtonedEditBox extends CustomWidget<ButtonedEditBox> {

    private final CustomEditBox editBox;
    private final CustomWidget<?> btnUp, btnDown;

    private final Supplier<Integer> value;

    private final int maxVal, buttonVal;

    public ButtonedEditBox(int x, int y, int maxVal, int buttonVal, ScreenHelper screenHelper, Supplier<Integer> value, Consumer<Integer> onValueChanged) {
        super(x, y);
        this.maxVal = maxVal;
        this.buttonVal = buttonVal;
        this.value = value;

        width = 41;
        int flowBoxHeight = 12;
        int bY = y + flowBoxHeight + 3;

        height = flowBoxHeight + 19;

        btnUp = new CustomWidget(x + 2, bY, 16, 16, ScreenHelper.buttonUp)
                .setOnClick(() -> {
                    onValueChanged.accept(newVal(true));
                })
                .addToolTip("Add " + buttonVal)
                .addTo(screenHelper);

        btnDown = new CustomWidget(x + 22, bY, 16, 16, ScreenHelper.buttonDown)
                .setOnClick(() -> {
                    onValueChanged.accept(newVal(false));
                })
                .addToolTip("Subtract " + buttonVal)
                .addTo(screenHelper);

        editBox = screenHelper.createEditorBox(x, y, width, flowBoxHeight)
                .setValue(value)
                .setMaxValue(maxVal)
                .setOnValueChanged(onValueChanged);
    }

    private int newVal(boolean increase) {
        int val = value.get();

        if (increase) val += buttonVal;
        else val -= buttonVal;

        return GeneralUtil.clamp(val, maxVal);
    }

    public CustomEditBox getEditBox() {
        return editBox;
    }

    @Override
    public void customRender(GuiGraphics guiGraphics, int x, int y) {
        btnUp.customRender(guiGraphics, x, y);
        btnDown.customRender(guiGraphics, x, y);
    }

    @Override
    public ButtonedEditBox centerHorizontally(ScreenHelper screenHelper) {
        editBox.centerHorizontally(screenHelper);
        btnUp.setX(editBox.getX() + 2);
        btnDown.setX(editBox.getX() + 23);
        return super.centerHorizontally(screenHelper);
    }

    @Override
    public ButtonedEditBox centerVertically(ScreenHelper screenHelper) {
        editBox.centerVertically(screenHelper);
        int y = editBox.getY() + editBox.getHeight() + 3;
        btnUp.setY(y);
        btnDown.setY(y);
        return super.centerVertically(screenHelper);
    }
}
