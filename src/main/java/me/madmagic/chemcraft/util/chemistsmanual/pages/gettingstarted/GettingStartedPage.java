package me.madmagic.chemcraft.util.chemistsmanual.pages.gettingstarted;

import me.madmagic.chemcraft.instances.menus.widgets.CustomLabel;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.chemistsmanual.Page;

public class GettingStartedPage extends Page {

    public GettingStartedPage() {
        super("starting");
    }

    @Override
    protected void pageInit() {
        new CustomLabel(screenHelper.halfImageWidth, 10, "Getting Started").setScale(1.2f).center().addTo(screenHelper);

        float scale = .6f;
        int fontHeight = (int) (ScreenHelper.font.lineHeight * scale);
        int yInc = this.yInc + fontHeight -2;
        
        addNextArrow("starting1");

        screenHelper.addString(xStart, yPos.addAndGet(30), "Most chemicals are too corrosive for", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "regular iron to withstand. Therefore", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "we will need to treat iron with a coating", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "that can withstand all the chemicals .", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc + 5), "Fluorite ore can be found throughout", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "the world, this mineral holds fluorine", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "atoms that we will use to make teflon", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "and coat iron with.", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc + 5), "To do this, a 'Teflon Coater' machine", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "is used. This is a very complex machine", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "that takes the fluorine from fluorite,", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "and combined with the carbon from coal,", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "it is able to apply a coating of teflon", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "on iron ingots and blocks.", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc + 5), "This is a very inefficient process", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "as we don't have access to the other", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "chemicals yet that provide a more", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "efficient way of creating teflon.", scale);
    }
}
