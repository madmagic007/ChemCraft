package me.madmagic.chemcraft.util.chemistsmanual.pages.liquids.storage;

import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.menus.widgets.CustomLabel;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.chemistsmanual.CraftingVisual;
import me.madmagic.chemcraft.util.chemistsmanual.Page;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

public class StoragePage extends Page {

    public StoragePage() {
        super("storage");
    }

    @Override
    protected void pageInit() {
        new CustomLabel(screenHelper.halfImageWidth, 10, "Storage").setScale(1.2f).center().addTo(screenHelper);

        float scale = .6f;
        int fontHeight = (int) (ScreenHelper.font.lineHeight * scale);
        int yInc = this.yInc + fontHeight - 2;

        int fact = screenHelper.imageWidth / 3;

        addCraftingGrid(fact - 6, yPos.addAndGet(20), tankVisual.get());
        addCraftingGrid(fact * 2 + 6, yPos.get(), insulatedBlockVisual.get());

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 72), "To store liquids, a multiblock tank", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "structure is used.", scale);

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 3), "A valid multiblock structure must have", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "a square base of any dimension, made", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "of insulated blocks, it can be any", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "height. Have one tank controller block", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "present and right click it with a wrench.", scale);

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 3), "If invalid, a chat message will notify,", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "if valid, any pipes will now connect.", scale);


        addNextArrow("storage1");
    }

    private final Supplier<CraftingVisual> tankVisual = () -> new CraftingVisual(CustomItems.blockItems.get("tank_controller").get(),
            CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get(),
            CustomItems.teflonCoatedIronIngot.get(), Items.BUCKET, CustomItems.teflonCoatedIronIngot.get(),
            CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get()
    );
}
