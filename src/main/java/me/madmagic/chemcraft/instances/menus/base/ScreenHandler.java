package me.madmagic.chemcraft.instances.menus.base;

import me.madmagic.chemcraft.instances.menus.CentrifugalPumpMenu;
import me.madmagic.chemcraft.instances.menus.base.MenuHandler;
import net.minecraft.client.gui.screens.MenuScreens;

public class ScreenHandler {

    public static void setup() {
        MenuScreens.register(MenuHandler.centrifugalPumpMenu.get(), CentrifugalPumpMenu.Screen::new);
    }
}
