package me.madmagic.chemcraft.instances;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.datagen.CustomItemModelProvider;
import me.madmagic.chemcraft.instances.items.PipeWrenchItem;
import me.madmagic.chemcraft.instances.items.base.BasicItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Supplier;

public class CustomItems {

    //items
    private static final DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, ChemCraft.modId);
    public static final HashMap<String, RegistryObject<Item>> blockItems = new HashMap<>();

    //tools
    public static final RegistryObject<Item> pipeWrench = register("pipe_wrench", PipeWrenchItem::new);

    //reagents
    public static final RegistryObject<Item> fluorite = register("fluorite_piece", BasicItem::new);

    //mainly for crafting
    public static final RegistryObject<Item> glassWoolSheet = items.register("glass_wool_sheet", BasicItem::new);
    public static final RegistryObject<Item> teflonCoatedIronIngot = registerCraftingOnlyItem("teflon_coated_iron_ingot");
    public static final RegistryObject<Item> axle = registerCraftingOnlyCustomModelItem("axle");
    public static final RegistryObject<Item> impeller = registerCraftingOnlyCustomModelItem("impeller");

    //creative tabs
    private static final DeferredRegister<CreativeModeTab> customTabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChemCraft.modId);

    private static RegistryObject<Item> registerCraftingOnlyItem(String name) {
        Supplier<Item> sup = BasicItem::new;
        return register(name, sup);
    }

    private static RegistryObject<Item> registerCraftingOnlyCustomModelItem(String name) {
        Supplier<Item> sup = BasicItem::new;
        return items.register(name, sup);
    }

    private static RegistryObject<Item> register(String name, Supplier<Item> itemSup) {
        RegistryObject<Item> itemReg = items.register(name, itemSup);
        CustomItemModelProvider.addItem(itemReg);
        return itemReg;
    }

    public static void registerBlockItem(String name, RegistryObject<Block> block) {
        RegistryObject<Item> itemReg = items.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        blockItems.put(name, itemReg);
    }

    public static final RegistryObject<CreativeModeTab> customTab = customTabs.register(ChemCraft.modId,
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(CustomBlocks.pipe.get()))
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
