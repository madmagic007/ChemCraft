package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.menus.widgets.HoverAbleImageButton;
import me.madmagic.chemcraft.instances.menus.widgets.base.CustomWidget;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.chemistsmanual.Page;
import me.madmagic.chemcraft.util.chemistsmanual.pages.IntroductionPage;
import me.madmagic.chemcraft.util.chemistsmanual.pages.MainPage;
import me.madmagic.chemcraft.util.chemistsmanual.pages.gettingstarted.GettingStartedPage;
import me.madmagic.chemcraft.util.chemistsmanual.pages.gettingstarted.GettingStartedPage1;
import me.madmagic.chemcraft.util.chemistsmanual.pages.liquids.LiquidsPage;
import me.madmagic.chemcraft.util.chemistsmanual.pages.liquids.PipesPage;
import me.madmagic.chemcraft.util.chemistsmanual.pages.liquids.pumping.PumpingPage;
import me.madmagic.chemcraft.util.chemistsmanual.pages.liquids.pumping.PumpingPage1;
import me.madmagic.chemcraft.util.chemistsmanual.pages.liquids.storage.StoragePage;
import me.madmagic.chemcraft.util.chemistsmanual.pages.liquids.storage.StoragePage1;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ChemistsManualScreen extends Screen {

    private static final int imageWidth = 146;
    private static final int imageHeight = 180;

    private static final ResourceLocation arrowBack = ScreenHelper.getTexture("arrow_back");
    private static final ResourceLocation arrowBackHovered = ScreenHelper.getTexture("arrow_back_hovered");

    public List<String> pageHistory = new ArrayList<>();
    private HoverAbleImageButton prevArrow;

    private int x, y;

    public Page activePage = pages.get("main");

    public ChemistsManualScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        x = (width - imageWidth) / 2;
        y = (height - imageHeight) / 2;

        activePage.init(x, y, imageWidth, imageHeight, this);
    }

    public void pageInited() {
        if (!pageHistory.isEmpty()) {
            prevArrow = new HoverAbleImageButton(x + 2, y + imageHeight + 3, 18, 10, arrowBack, arrowBackHovered, () -> {
                String lastPageName = pageHistory.remove(pageHistory.size() - 1);
                setNewPage(lastPageName, false);
            }).addToolTip("Go to previous page");
        } else {
            prevArrow = null;
        }
    }

    public void setNewPage(String name, boolean addToHistory) {
        if (addToHistory) pageHistory.add(activePage.name);
        activePage = pages.getOrDefault(name, activePage);
        activePage.init(x, y, imageWidth, imageHeight, this);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        activePage.screenHelper.renderDarkBackground(pGuiGraphics);
        activePage.screenHelper.renderGuiImage(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        activePage.screenHelper.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        if (prevArrow != null) {
            prevArrow.render(pGuiGraphics, pMouseX, pMouseY , pPartialTick);
            if (prevArrow.isHovered()) prevArrow.renderToolTip(pGuiGraphics, pMouseX, pMouseY);
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (GeneralUtil.notNullAnd(prevArrow, CustomWidget::isHovered)) return prevArrow.onClicked();
        return activePage.screenHelper.handleClicked();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static final TreeMap<String, Page> pages = new TreeMap<>();

    static {
        definePages(
                new MainPage(),
                new IntroductionPage(),
                new GettingStartedPage(), new GettingStartedPage1(),
                new LiquidsPage(), new PipesPage(),
                new PumpingPage(), new PumpingPage1(),
                new StoragePage(), new StoragePage1()
        );
    }

    protected static void definePages(Page... pagesToAdd) {
        for (Page page : pagesToAdd) {
            pages.put(page.name, page);
        }
    }
}
