package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.menus.widgets.base.ITooltipHolder;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ToolTippedEditBox extends EditBox implements ITooltipHolder {

    private final String toolTip;

    public ToolTippedEditBox(Font pFont, int pX, int pY, int pWidth, int pHeight, String toolTip) {
        super(pFont, pX, pY, pWidth, pHeight, Component.literal(""));
        this.toolTip = toolTip;
    }

    @Override
    public List<Component> getTooltips() {
        return List.of(Component.literal(toolTip));
    }

    @Override
    public boolean iIsHovered() {
        return isHovered();
    }
}
