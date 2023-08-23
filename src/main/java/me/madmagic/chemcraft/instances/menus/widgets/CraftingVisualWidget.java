package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.menus.widgets.base.CustomWidget;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.chemistsmanual.CraftingVisual;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class CraftingVisualWidget extends CustomWidget<CraftingVisualWidget> {

    private static final ResourceLocation texture = ScreenHelper.getTexture("crafting_grid_book");

    private final CraftingVisual visual;
    private final List<ToolTippedItem> items = new ArrayList<>();

    public CraftingVisualWidget(int x, int y, CraftingVisual visual) {
        super(x, y, 54, 75, texture);
        this.visual = visual;

        addItem(19, 1, visual.toMake);
        GeneralUtil.forEachIndexed(visual.ingredients, (item, i) -> addItem(i, item));
    }

    private void addItem(int i, Item item) {
        if (item == Items.AIR) return;

        int row = i % 3;
        int column = i / 3;

        int x = 1 + row * 18;
        int y = 22 + column * 18;

        addItem(x, y, item);
    }

    private void addItem(int x, int y, Item item) {
        int actualX = this.x - (width / 2);
        items.add(new ToolTippedItem(actualX + x, this. y + y, item, item.getDescriptionId()));
    }

    @Override
    public CraftingVisualWidget addTo(ScreenHelper screenHelper) {
        super.addTo(screenHelper);
        items.forEach(item -> item.addTo(screenHelper));
        return this;
    }

    //    @Override
//    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
//        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
//        items.forEach(item -> item.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick));
//    }
//
//    @Override
//    public void renderToolTip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
//        items.forEach(item -> {
//            if (item.isHovered()) item.renderToolTip(guiGraphics, mouseX, mouseY);
//        });
//    }
}
