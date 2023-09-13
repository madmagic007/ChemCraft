package me.madmagic.chemcraft.util.chemistsmanual;

import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.menus.ChemistsManualScreen;
import me.madmagic.chemcraft.instances.menus.widgets.CraftingVisualWidget;
import me.madmagic.chemcraft.instances.menus.widgets.CustomLabel;
import me.madmagic.chemcraft.instances.menus.widgets.HoverAbleImageButton;
import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public abstract class Page {

    private static final ResourceLocation texture = ScreenHelper.getTexture("chemists_manual");
    private static final ResourceLocation arrowNext = ScreenHelper.getTexture("arrow_next");
    private static final ResourceLocation arrowNextHovered = ScreenHelper.getTexture("arrow_next_hovered");

    public final String name;
    private ChemistsManualScreen masterScreen;
    public final ScreenHelper screenHelper = new ScreenHelper(texture);
    protected final int xStart = 15;
    protected int yInc = 3;
    protected AtomicInteger yPos;

    public Page(String name) {
        this.name = name;
    }

    public final void init(int x, int y, int imageWidth, int imageHeight, ChemistsManualScreen masterScreen) {
        this.masterScreen = masterScreen;
        screenHelper.init(x, y, imageWidth, imageHeight, masterScreen);
        yPos = new AtomicInteger();
        pageInit();
        masterScreen.pageInited();
    }

    protected abstract void pageInit();

    protected void addTitle(String text, float scale) {
        new CustomLabel(screenHelper.halfImageWidth, 10, text).setScale(scale)
                .centerHorizontally(screenHelper).addTo(screenHelper);
    }

    protected void addLink(int x, int y, String text, String changeToPage) {
        new CustomLabel(x, y, text).underline().setOnClick(() -> masterScreen.setNewPage(changeToPage, true))
                .addTo(screenHelper);
    }

    protected void addNextArrow(String gotoPage) {
        new HoverAbleImageButton(screenHelper.imageWidth - 20, screenHelper.imageHeight + 3, 18, 10, arrowNext, arrowNextHovered, () -> masterScreen.setNewPage(gotoPage, true))
                .addToolTip("Go to next page").addTo(screenHelper);
    }

    protected void addCraftingGrid(int x, int y, CraftingVisual visual) {
        new CraftingVisualWidget(x, y, visual).centerHorizontally(screenHelper).addTo(screenHelper);
    }

    protected void addCraftingGrid(int y, CraftingVisual visual) {
        addCraftingGrid(screenHelper.halfImageWidth, y, visual);
    }

    //common visuals
    protected final Supplier<CraftingVisual> insulatedBlockVisual = () -> new CraftingVisual(CustomItems.blockItems.get("insulated_block").get(),
            Items.AIR, CustomItems.insulationSheet.get(), Items.AIR,
            CustomItems.insulationSheet.get(), CustomItems.blockItems.get("teflon_coated_iron_block").get(), CustomItems.insulationSheet.get(),
            Items.AIR, CustomItems.insulationSheet.get(), Items.AIR
    );
}
