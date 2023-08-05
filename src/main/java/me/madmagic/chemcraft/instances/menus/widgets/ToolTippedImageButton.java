package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.menus.widgets.base.ITooltipHolder;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;


public class ToolTippedImageButton extends ImageButton implements ITooltipHolder {

    private final String toolTip;

    public ToolTippedImageButton(int pX, int pY, int pWidth, int pHeight, ResourceLocation pResourceLocation, OnPress pOnPress, String toolTip) {
        super(pX, pY, pWidth, pHeight, 0, 0, 0, pResourceLocation, 16, 16, pOnPress);
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
