package me.madmagic.chemcraft.instances.menus.widgets.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

public interface ITooltipHolder {

    List<Component> getTooltips();

    default void renderToolTip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.renderTooltip(Minecraft.getInstance().font, getTooltips(), Optional.empty(), mouseX, mouseY);
    }

    boolean iIsHovered();
}
