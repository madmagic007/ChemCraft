package me.madmagic.chemcraft.util.chemistsmanual.pages.liquids.pumping;

import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.menus.widgets.CustomLabel;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.chemistsmanual.CraftingVisual;
import me.madmagic.chemcraft.util.chemistsmanual.Page;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

public class PumpingPage extends Page {

    public PumpingPage() {
        super("pumping");
    }

    @Override
    protected void pageInit() {
        new CustomLabel(screenHelper.halfImageWidth, 10, "Pumping").setScale(1.2f).center().addTo(screenHelper);

        float scale = .6f;
        int fontHeight = (int) (ScreenHelper.font.lineHeight * scale);
        int yInc = this.yInc + fontHeight - 2;

        int fact = screenHelper.imageWidth / 3;

        addCraftingGrid(fact - 6, yPos.addAndGet(20), pumpVisual.get());
        addCraftingGrid(fact * 2 + 6, yPos.get(), motorVisual.get());

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 72), "A centrifugal pump is required to", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "displace fluids from any fluid holder", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "to any destination (reactors, tanks etc).", scale);

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 3), "A motor must be present behind the", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "pump for it to work, the motor can", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "accept any power from the top.", scale);

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 3), "The pump has a menu where you can", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "set your desired flowrate in mb/tick.", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "The set flowrate is the exact energy", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "usage of the motor in FE/tick.", scale);

        addNextArrow("pumping1");
    }

    private final Supplier<CraftingVisual> pumpVisual = () -> new CraftingVisual(CustomItems.blockItems.get("centrifugal_pump").get(),
            CustomItems.glassWoolSheet.get(), CustomItems.blockItems.get("pipe").get(), CustomItems.glassWoolSheet.get(),
            CustomItems.blockItems.get("pipe").get(), CustomItems.impeller.get(), CustomItems.axle.get(),
            CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get()
    );

    private final Supplier<CraftingVisual> motorVisual = () -> new CraftingVisual(CustomItems.blockItems.get("motor").get(),
            Items.AIR, CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get(),
            CustomItems.axle.get(), Items.COPPER_INGOT, CustomItems.teflonCoatedIronIngot.get(),
            Items.AIR, CustomItems.teflonCoatedIronIngot.get(), CustomItems.teflonCoatedIronIngot.get()
    );
}
