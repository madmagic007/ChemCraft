package me.madmagic.chemcraft.util.chemistsmanual.pages;

import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.chemistsmanual.Page;

public class IntroductionPage extends Page {

    public IntroductionPage() {
        super("introduction");
    }

    @Override
    protected void pageInit() {
        addTitle("Introduction", 1.2f);

        float scale = .6f;
        int fontHeight = (int) (ScreenHelper.font.lineHeight * scale);
        int yInc = this.yInc + fontHeight -2;

        screenHelper.addString(xStart, yPos.addAndGet(30), "ChemCraft aims to provide the most", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "realistic experience of what process", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "chemistry has to offer, in Minecraft.", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc + 2), "ChemCraft makes use of a custom", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "fluid system that utilises custom pumps", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "for displacement and is therefore not", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "compatible with any other fluid system", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "or pipes from any other mod.", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc + 2), "With ChemCraft, you have to take in", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "account many factors such as ", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "temperatures, solubility and density", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "for both reactions in and the various", scale);
        screenHelper.addString(xStart, yPos.addAndGet(yInc), "ways of separation to take place.", scale);
    }
}
