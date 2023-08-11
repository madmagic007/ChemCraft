package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.menus.widgets.base.IToolTippedWidget;
import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ToolTippedEditBox extends EditBox implements IToolTippedWidget {

    private final String toolTip;

    public ToolTippedEditBox(Font pFont, int pX, int pY, int pWidth, int pHeight, String toolTip) {
        super(pFont, pX, pY, pWidth, pHeight, Component.literal(""));
        this.toolTip = toolTip;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public void addTo(ScreenHelper screenHelper) {
        setX(getX() + screenHelper.x);
        setY(getY() + screenHelper.y);
        screenHelper.addWidget(this);
    }

    @Override
    public List<Component> getToolTips() {
        return List.of(Component.literal(toolTip));
    }

    @Override
    public boolean isHovered() {
        return super.isHovered();
    }
}
