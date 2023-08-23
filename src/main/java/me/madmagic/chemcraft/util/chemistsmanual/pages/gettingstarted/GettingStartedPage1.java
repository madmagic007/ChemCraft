package me.madmagic.chemcraft.util.chemistsmanual.pages.gettingstarted;

import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.chemistsmanual.CraftingVisual;
import me.madmagic.chemcraft.util.chemistsmanual.Page;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

public class GettingStartedPage1 extends Page {

    public GettingStartedPage1() {
        super("starting1");
    }

    @Override
    protected void pageInit() {
        addTitle("Getting Started", 1.2f);

        float scale = .6f;
        int fontHeight = (int) (ScreenHelper.font.lineHeight * scale);
        int yInc = this.yInc + fontHeight -2;

        addCraftingGrid(yPos.addAndGet(25), visual.get());

        screenHelper.addString(xStart, yPos.addAndGet(80), "The teflon coater is the starter way", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "to get teflon coated iron ingots and", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "blocks, which are used for pretty much", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "everything in this mod.", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc + 5), "Supply it with fluorite, coal and iron", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "through the gui or hoppers/pipes", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "and it will coat the iron, producing ", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "calcium waste as a byproduct.", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "The coated iron and the waste can be", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "extracted with pipes or hoppers.", scale);
    }

    private final Supplier<CraftingVisual> visual = () -> new CraftingVisual(
            CustomItems.blockItems.get("teflon_coater").get(),
            CustomItems.glassWoolSheet.get(), Items.IRON_INGOT,  CustomItems.glassWoolSheet.get(),
            Items.IRON_INGOT, CustomItems.fluorite.get(), Items.IRON_INGOT,
            CustomItems.glassWoolSheet.get(), Items.IRON_INGOT,  CustomItems.glassWoolSheet.get()
    );
}
