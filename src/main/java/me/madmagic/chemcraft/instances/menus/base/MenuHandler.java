package me.madmagic.chemcraft.instances.menus.base;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.menus.CentrifugalPumpMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuHandler {

    private static final DeferredRegister<MenuType<?>> menus = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ChemCraft.modId);

    public static final RegistryObject<MenuType<CentrifugalPumpMenu>> centrifugalPumpMenu = register(CentrifugalPumpMenu::new, CentrifugalPumpMenu.name);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(IContainerFactory<T> factory, String name) {
        return menus.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        menus.register(eventBus);
    }
}
