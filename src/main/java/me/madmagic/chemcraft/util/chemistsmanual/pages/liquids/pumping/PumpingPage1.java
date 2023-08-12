package me.madmagic.chemcraft.util.chemistsmanual.pages.liquids.pumping;

import me.madmagic.chemcraft.instances.menus.widgets.CustomLabel;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.chemistsmanual.Page;

public class PumpingPage1 extends Page {

    public PumpingPage1() {
        super("pumping1");
    }

    @Override
    protected void pageInit() {
        new CustomLabel(screenHelper.halfImageWidth, 10, "Pumping").setScale(1.2f).center().addTo(screenHelper);

        float scale = .6f;
        int fontHeight = (int) (ScreenHelper.font.lineHeight * scale);
        int yInc = this.yInc + fontHeight - 2;

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 30), "The front of the pump is the \"suck\"", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "side where it will extract liquid evenly", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "from all fluid containers connected", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "and moving the fluid evenly to all", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "containers that are connected to the", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "\"press\" side on top.", scale);

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 3), "When a container holds multiple liquids,", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "relative proportions of each will be", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "evenly extracted and displaced.", scale);

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 3), "A maximum flowrate of 100 mb/tick", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "can be configured.", scale);

        screenHelper.addString(xStart, yPos.addAndGet(yInc + 5), "Challenge:", .8f);
        screenHelper.addString(xStart, yPos.addAndGet(yInc + 1), "Try placing all pumps on the ground", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "level and having most of the apparatus", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "on floors above. This is how most real", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "chemical production plants are built.", scale);
    }
}
