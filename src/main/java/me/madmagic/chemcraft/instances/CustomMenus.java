package me.madmagic.chemcraft.instances;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.menus.AirCoolerMenu;
import me.madmagic.chemcraft.instances.menus.CentrifugalPumpMenu;
import me.madmagic.chemcraft.instances.menus.MotorMenu;
import me.madmagic.chemcraft.instances.menus.TeflonCoaterMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CustomMenus {

    private static final DeferredRegister<MenuType<?>> menus = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ChemCraft.modId);

    public static final RegistryObject<MenuType<CentrifugalPumpMenu>> centrifugalPumpMenu = register("centrifugal_pump", CentrifugalPumpMenu::new);
    public static final RegistryObject<MenuType<TeflonCoaterMenu>> teflonCoaterMenu = register("teflon_coater", TeflonCoaterMenu::new);
    public static final RegistryObject<MenuType<MotorMenu>> motorMenu = register("motor", MotorMenu::new);
    public static final RegistryObject<MenuType<AirCoolerMenu>> airCoolerMenu = register("air_cooler", AirCoolerMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return menus.register(name + "_menu", () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        menus.register(eventBus);
    }

    public static void setupScreens() {
        MenuScreens.register(teflonCoaterMenu.get(), TeflonCoaterMenu.Screen::new);
        MenuScreens.register(centrifugalPumpMenu.get(), CentrifugalPumpMenu.Screen::new);
        MenuScreens.register(airCoolerMenu.get(), AirCoolerMenu.Screen::new);
    }
}
