package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.menus.widgets.base.CustomWidget;
import me.madmagic.chemcraft.util.MouseUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class CustomLabel extends CustomWidget<CustomLabel> {

    private static final Font font = Minecraft.getInstance().font;
    private float scale = 1;
    private final MutableComponent component;

    public CustomLabel(int x, int y, String text) {
        this(x, y, Component.literal(text));
    }

    public CustomLabel(int x, int y, MutableComponent component) {
        super(x, y);
        this.component = component;
        width = font.width(component.getString());
    }

    public CustomLabel setScale(float scale) {
        this.scale = scale;
        width *= scale;
        return this;
    }

    public CustomLabel underline() {
        component.withStyle(ChatFormatting.UNDERLINE);
        return this;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int width = (int) (font.width(component.getString()) * scale);
        int height = (int) (font.lineHeight * scale);

        isHovered = MouseUtil.isMouseOver(pMouseX, pMouseY, x, y, width, height);

        int renderX = x;
        int renderY = y;

        if (scale != 1) { // only scale when necessary
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, scale);
            float fact = 1 / scale;

            renderX *= fact;
            renderY *= fact;
        }

        guiGraphics.drawString(font, component, renderX, renderY, 4210752, false);

        if (scale != 1) guiGraphics.pose().popPose();
    }
}
