package me.madmagic.chemcraft.instances.items.base;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.blocks.base.BlockHandler;
import me.madmagic.chemcraft.instances.items.PipeWrenchItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemHandler {

    //items
    public static final DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, ChemCraft.modId);

    public static final RegistryObject<Item> PipeWrench = items.register(PipeWrenchItem.itemName, PipeWrenchItem::new);

    //creative tabs
    private static final DeferredRegister<CreativeModeTab> customTabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChemCraft.modId);

    public static final RegistryObject<CreativeModeTab> customTab = customTabs.register(ChemCraft.modId,
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BlockHandler.Pipe.get()))
                    .title(Component.literal(ChemCraft.modName))
                    .displayItems((pParameters, pOutput) ->
                        items.getEntries().forEach(itemRegistry -> pOutput.accept(itemRegistry.get()))
                    )
                    .build()
    );

    public static void register(IEventBus eventBus) {
        items.register(eventBus);
        customTabs.register(eventBus);
    }
}
