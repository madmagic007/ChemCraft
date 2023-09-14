package me.madmagic.chemcraft.instances;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.menus.*;
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

    public static final RegistryObject<MenuType<CentrifugalPumpMenu>> centrifugalPump = register("centrifugal_pump", CentrifugalPumpMenu::new);
    public static final RegistryObject<MenuType<TeflonCoaterMenu>> teflonCoater = register("teflon_coater", TeflonCoaterMenu::new);
    public static final RegistryObject<MenuType<MotorMenu>> motor = register("motor", MotorMenu::new);
    public static final RegistryObject<MenuType<AirCoolerMenu>> airCooler = register("air_cooler", AirCoolerMenu::new);
    public static final RegistryObject<MenuType<SensorMenu>> sensor = register("sensor", SensorMenu::new);
    public static final RegistryObject<MenuType<SensorReceiverMenu>> sensorReceiver = register("sensor_receiver", SensorReceiverMenu::new);
    public static final RegistryObject<MenuType<ElectricHeaterMenu>> electricHeater = register("electric_heater", ElectricHeaterMenu::new);
    public static final RegistryObject<MenuType<FurnaceHeaterMenu>> furnaceHeater = register("coal_powered_heater", FurnaceHeaterMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return menus.register(name + "_menu", () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        menus.register(eventBus);
    }

    public static void setupScreens() {
        MenuScreens.register(teflonCoater.get(), TeflonCoaterMenu.Screen::new);
        MenuScreens.register(centrifugalPump.get(), CentrifugalPumpMenu.Screen::new);
        MenuScreens.register(motor.get(), MotorMenu.Screen::new);
        MenuScreens.register(airCooler.get(), AirCoolerMenu.Screen::new);
        MenuScreens.register(sensor.get(), SensorMenu.Screen::new);
        MenuScreens.register(sensorReceiver.get(), SensorReceiverMenu.Screen::new);
        MenuScreens.register(electricHeater.get(), ElectricHeaterMenu.Screen::new);
        MenuScreens.register(furnaceHeater.get(), FurnaceHeaterMenu.Screen::new);
    }
}
