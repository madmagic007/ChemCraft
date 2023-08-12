package me.madmagic.chemcraft.util.chemistsmanual.pages.liquids.storage;

import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.menus.widgets.CustomLabel;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.chemistsmanual.CraftingVisual;
import me.madmagic.chemcraft.util.chemistsmanual.Page;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

public class StoragePage1 extends Page {

    public StoragePage1() {
        super("storage1");
    }

    @Override
    protected void pageInit() {
        new CustomLabel(screenHelper.halfImageWidth, 10, "Storage").setScale(1.2f).center().addTo(screenHelper);

        float scale = .6f;
        int fontHeight = (int) (ScreenHelper.font.lineHeight * scale);
        int yInc = this.yInc + fontHeight - 2;

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 30), "The capacity of a tank is determined", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "by the amount of blocks its made of,", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "100k mb capacity per block.", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "There is no limit to how many blocks a", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "tank multiblock can be made out of.", scale);

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 3), "If any block is broken or moved,", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "the tank will become invalid and all", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "stored fluids are lost.", scale);

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 3), "Pipes can be connected at any place,", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "both for extraction and input.", scale);

    }

    private final Supplier<CraftingVisual> tankVisual = () -> new CraftingVisual(CustomItems.blockItems.get("tank_controller").get(),
            CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get(),
            CustomItems.teflonCoatedIronIngot.get(), Items.BUCKET, CustomItems.teflonCoatedIronIngot.get(),
            CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get()
    );
}
