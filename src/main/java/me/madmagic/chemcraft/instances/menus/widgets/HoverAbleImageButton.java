package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.menus.widgets.base.CustomWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;


public class HoverAbleImageButton extends CustomWidget<HoverAbleImageButton> {

    private ResourceLocation originalTexture, hoveredTexture;

    public HoverAbleImageButton(int x, int y, int width, int height, ResourceLocation texture, ResourceLocation hoveredTexture, Runnable runnable) {
        super(x, y, width, height, texture);
        originalTexture = texture;
        this.hoveredTexture = hoveredTexture;
        setOnClick(runnable);
    }

    @Override
    public void customRender(GuiGraphics guiGraphics, int x, int y) {
        if (isHovered) texture = hoveredTexture;
        else texture = originalTexture;
    }
}
