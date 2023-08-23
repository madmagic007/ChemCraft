package me.madmagic.chemcraft.util.chemistsmanual.pages.liquids;

import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.chemistsmanual.Page;

public class LiquidsPage extends Page {

    public LiquidsPage() {
        super("liquids");
    }

    @Override
    protected void pageInit() {
        addTitle("Liquids and pipes", 1.2f);

        float scale = .6f;
        int fontHeight = (int) (ScreenHelper.font.lineHeight * scale);
        int yInc = this.yInc + fontHeight -2;

        screenHelper.addString(xStart, yPos.addAndGet(30), "ChemCraft uses custom fluids and", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "pipes that are not compatible with any", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "other mod by default.", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc + 5), "Read the following chapters for", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "information about the specifics:", scale);

        addLink(xStart, ScreenHelper.incFontY(yPos, 10), "1. Pipes", "pipes");
        addLink(xStart, ScreenHelper.incFontY(yPos, this.yInc), "2. Pumping liquids", "pumping");
        addLink(xStart, ScreenHelper.incFontY(yPos, this.yInc), "3. Storage", "storage");
        addLink(xStart, ScreenHelper.incFontY(yPos, this.yInc), "4. Temperature", "temperature");
    }
}
