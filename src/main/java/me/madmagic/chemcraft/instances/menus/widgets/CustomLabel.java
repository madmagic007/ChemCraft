package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.menus.widgets.base.CustomWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Supplier;

public class CustomLabel extends CustomWidget<CustomLabel> {

    private static final Font font = Minecraft.getInstance().font;
    private MutableComponent component;
    private Supplier<?> lateValue;

    public CustomLabel(int x, int y, String text) {
        this(x, y, Component.literal(text));
    }

    public CustomLabel(int x, int y, MutableComponent component) {
        super(x, y);
        this.component = component;
        width = font.width(component.getString());
    }

    public CustomLabel setValue(Supplier<?> supplier) {
        lateValue = supplier;
        return this;
    }

    public CustomLabel underline() {
        component.withStyle(ChatFormatting.UNDERLINE);
        return this;
    }

    @Override
    public void customRender(GuiGraphics guiGraphics, int x, int y) {
        if (lateValue != null) component = Component.literal(String.valueOf(lateValue.get()));
        width = (int) (font.width(component) * scale);
        height = (int) (font.lineHeight * scale);
        guiGraphics.drawString(font, component, x, y, 4210752, false);
    }
}
