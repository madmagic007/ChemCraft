package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.menus.widgets.base.IToolTippedWidget;
import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

public class ToolTippedEditBox extends EditBox implements IToolTippedWidget {

    private final String toolTip;
    private Supplier<?> lateValue;

    public ToolTippedEditBox(Font pFont, int pX, int pY, int pWidth, int pHeight, String toolTip) {
        super(pFont, pX, pY, pWidth, pHeight, Component.literal(""));
        this.toolTip = toolTip;
    }

    public ToolTippedEditBox setValue(Supplier<?> supplier) {
        lateValue = supplier;
        return this;
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        ChemCraft.info("char typed");
        return super.charTyped(pCodePoint, pModifiers);
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        ChemCraft.info("1 onClick");
        super.onClick(pMouseX, pMouseY);
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

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (lateValue != null) setValue(String.valueOf(lateValue.get()));
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
