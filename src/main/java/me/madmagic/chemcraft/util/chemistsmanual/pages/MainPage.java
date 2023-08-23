package me.madmagic.chemcraft.util.chemistsmanual.pages;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.menus.widgets.CustomLabel;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.chemistsmanual.Page;

public class MainPage extends Page {

    public MainPage() {
        super("main");
    }

    @Override
    protected void pageInit() {
        addTitle("All inclusive manual for", .9f);
        new CustomLabel(screenHelper.halfImageWidth, 19, ChemCraft.modName).setScale(1.2f)
                .centerHorizontally(screenHelper).addTo(screenHelper);

        addLink(xStart, ScreenHelper.incFontY(yPos, 30), "1. Introduction", "introduction");
        addLink(xStart, ScreenHelper.incFontY(yPos, yInc), "2. Getting started", "starting");
        addLink(xStart, ScreenHelper.incFontY(yPos, yInc), "3. Liquids and pipes", "liquids");
        addLink(xStart, ScreenHelper.incFontY(yPos, yInc), "4. Gasses", "gasses");
        addLink(xStart, ScreenHelper.incFontY(yPos, yInc), "5. Reactors", "reactors");
        addLink(xStart, ScreenHelper.incFontY(yPos, yInc), "6. Separation", "separation");
    }
}
