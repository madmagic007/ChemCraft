package me.madmagic.chemcraft.util.chemistsmanual.pages.liquids;

import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.chemistsmanual.CraftingVisual;
import me.madmagic.chemcraft.util.chemistsmanual.Page;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

public class PipesPage extends Page {

    public PipesPage() {
        super("pipes");
    }

    @Override
    protected void pageInit() {
        addTitle("Pipes", 1.2f);

        float scale = .6f;
        int fontHeight = (int) (ScreenHelper.font.lineHeight * scale);
        int yInc = this.yInc + fontHeight - 2;

        int fact = screenHelper.imageWidth / 3;

        addCraftingGrid(fact - 6, yPos.addAndGet(10), pipeVisual.get());
        addCraftingGrid(fact * 2 + 6, yPos.get(), wrenchVisual.get());

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 72), "Pipes will automatically connect to", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "anything they can connect to.", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc + 2), "A centrifugal pump is the only factor", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "that provides displacement and", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "direction of the displacement.", scale);

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 5), "A pipe wrench can be used to break", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "and re-create connections, preventing", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "unwanted flow paths or mixing of fluids", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "and allowing for different lines to be", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "placed next to each other.", scale);

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 3), "Right click with the wrench on", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "the side you wish to (dis/re)connect.", scale);
    }

    private final Supplier<CraftingVisual> pipeVisual = () -> new CraftingVisual(CustomItems.blockItems.get("pipe").get(),
            Items.AIR, CustomItems.glassWoolSheet.get(), Items.AIR,
            CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get(),
            Items.AIR, CustomItems.glassWoolSheet.get(), Items.AIR
    );

    private final Supplier<CraftingVisual> wrenchVisual = () -> new CraftingVisual(CustomItems.pipeWrench.get(),
            Items.AIR, CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get(),
            Items.AIR, CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get(),
            Items.AIR, Items.AIR, CustomItems.teflonCoatedIronIngot.get()
    );
}
