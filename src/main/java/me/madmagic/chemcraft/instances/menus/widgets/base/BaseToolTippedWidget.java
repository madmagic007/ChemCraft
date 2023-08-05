package me.madmagic.chemcraft.instances.menus.widgets.base;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class BaseToolTippedWidget extends AbstractWidget implements ITooltipHolder {

    protected String toolTip;

    public BaseToolTippedWidget(int pX, int pY, int pWidth, int pHeight) {
        super(pX, pY, pWidth, pHeight, Component.literal(""));
        this.toolTip = "";
    }

    public BaseToolTippedWidget(int pX, int pY, int pWidth, int pHeight, String toolTip) {
        super(pX, pY, pWidth, pHeight, Component.literal(""));
        this.toolTip = toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    @Override
    public List<Component> getTooltips() {
        return List.of(Component.literal(toolTip));
    }

    @Override
    public boolean iIsHovered() {
        return isHovered;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        defaultButtonNarrationText(pNarrationElementOutput);
    }
}
