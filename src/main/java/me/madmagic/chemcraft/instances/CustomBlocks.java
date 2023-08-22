package me.madmagic.chemcraft.instances;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.datagen.*;
import me.madmagic.chemcraft.instances.blocks.*;
import me.madmagic.chemcraft.instances.blocks.base.BaseNineCraftingBlock;
import me.madmagic.chemcraft.instances.blocks.ores.FluoriteOre;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CustomBlocks {

    public static final DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, ChemCraft.modId);

    //pipe related
    public static final RegistryObject<Block> pipe = customModelNoVariant("pipe", PipeBlock::new);

    //process machines
    public static final RegistryObject<Block> fluoriteCoater = rotateAbleCustomModel("teflon_coater", TeflonCoaterBlock::new);

    //fluid machines
    public static final RegistryObject<Block> centrifugalPump = rotateAbleCustomModel("centrifugal_pump", CentrifugalPumpBlock::new);
    public static final RegistryObject<Block> motor = rotateAbleCustomModel("motor", MotorBlock::new);
    public static final RegistryObject<Block> waterExtractor = customModel("water_extractor", WaterExtractorBlock::new);

    //process apparatus
    public static final RegistryObject<Block> tankController = customModel("tank_controller", TankControllerBlock::new);
    public static final RegistryObject<Block> insulatedBlock = basicBlock("insulated_block", InsulatedBlock::new);
    public static final RegistryObject<Block> airCoolerBlock = customModelNoVariant("air_cooler", AirCoolerBlock::new);

    //ores
    public static final RegistryObject<Block> fluorite_ore = ore("fluorite_ore", FluoriteOre::new, CustomItems.fluorite);

    //mainly for crafting
    public static final RegistryObject<Block> glassWool = basicBlock("glass_wool", () -> new BaseNineCraftingBlock(BlockBehaviour.Properties.copy(Blocks.YELLOW_WOOL), CustomItems.glassWoolSheet));
    public static final RegistryObject<Block> teflonCoatedIronBlock = basicBlock("teflon_coated_iron_block", () -> new BaseNineCraftingBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), CustomItems.teflonCoatedIronIngot));

    private static RegistryObject<Block> basicBlock(String name, Supplier<Block> blockSup) {
        RegistryObject<Block> blockReg = registerBlock(name, blockSup);
        CustomLootTableProvider.addDropSelf(blockReg);
        CustomBlockStateProvider.addCubeAll(blockReg);
        return blockReg;
    }

    private static RegistryObject<Block> customModelNoVariant(String name, Supplier<Block> blockSup) {
        RegistryObject<Block> blockReg = registerBlock(name, blockSup);
        CustomLootTableProvider.addDropSelf(blockReg);
        CustomItemModelProvider.addSpecialBlockItem(blockReg);
        return blockReg;
    }

    private static RegistryObject<Block> customModel(String name, Supplier<Block> blockSup) {
        RegistryObject<Block> blockReg = registerBlock(name, blockSup);
        CustomLootTableProvider.addDropSelf(blockReg);
        CustomItemModelProvider.addSpecialBlockItem(blockReg);
        CustomBlockStateProvider.addSingleVariant(blockReg);
        return blockReg;
    }

    private static RegistryObject<Block> rotateAbleCustomModel(String name, Supplier<Block> blockSup) {
        RegistryObject<Block> blockReg = registerBlock(name, blockSup);
        CustomLootTableProvider.addDropSelf(blockReg);
        CustomItemModelProvider.addSpecialBlockItem(blockReg);
        CustomBlockStateProvider.addRotatable(blockReg);
        return blockReg;
    }

    private static RegistryObject<Block> ore(String name, Supplier<Block> blockSup, RegistryObject<? extends Item> itemReg) {
        RegistryObject<Block> blockReg = registerBlock(name, blockSup);
        CustomLootTableProvider.addDropOther(blockReg, itemReg);
        CustomRecipeProvider.addSmeltingRecipe(blockReg, itemReg, name);
        CustomBlockStateProvider.addCubeAll(blockReg);
        CustomBlockTagProvider.addOre(name, blockReg);
        return blockReg;
    }

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> sup) {
        RegistryObject<Block> blockReg = blocks.register(name, sup);
        CustomItems.registerBlockItem(name, blockReg);
        CustomBlockTagProvider.addMineAble(blockReg);
        return blockReg;
    }

    public static void register(IEventBus eventBus) {
        blocks.register(eventBus);
    }
}
