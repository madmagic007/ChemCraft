package me.madmagic.chemcraft.instances;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.datagen.*;
import me.madmagic.chemcraft.instances.blockentities.sensors.LevelSensorBlockEntity;
import me.madmagic.chemcraft.instances.blockentities.sensors.SensorReceiverBlockEntity;
import me.madmagic.chemcraft.instances.blockentities.sensors.TemperatureSensorBlockEntity;
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
    public static final RegistryObject<Block> crudeOilExtractor = customModel("crude_oil_extractor", CrudeOilExtractorBlock::new);

    //process apparatus
    public static final RegistryObject<Block> tank = customModel("tank", TankBlock::new);
    public static final RegistryObject<Block> distillery = customModel("distillery", DistilleryBlock::new);
    public static final RegistryObject<Block> sievePlate = customModel("sieve_plate", SievePlateBlock::new);
    public static final RegistryObject<Block> insulatedBlock = basicBlock("insulated_block", InsulatedBlock::new);
    public static final RegistryObject<Block> airCooler = customModelNoVariant("air_cooler", AirCoolerBlock::new);
    public static final RegistryObject<Block> electricHeater = customModelNoVariant("electric_heater", ElectricHeaterBlock::new);

    //controlling related
    public static final RegistryObject<Block> levelSensor = BaseSensorBlock.registerSensorBlock("level_sensor", LevelSensorBlockEntity::new);
    public static final RegistryObject<Block> temperatureSensor = BaseSensorBlock.registerSensorBlock("temperature_sensor", TemperatureSensorBlockEntity::new);
    public static final RegistryObject<Block> sensorReceiver = BaseSensorBlock.registerSensorBlock("sensor_receiver", SensorReceiverBlockEntity::new);

    //ores
    public static final RegistryObject<Block> fluorite_ore = ore("fluorite_ore", FluoriteOre::new, CustomItems.fluorite);

    //mainly for crafting
    public static final RegistryObject<Block> insulationBlock = basicBlock("block_of_insulation", () -> new BaseNineCraftingBlock(BlockBehaviour.Properties.copy(Blocks.YELLOW_WOOL), CustomItems.insulationSheet));
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

    public static RegistryObject<Block> rotateAbleCustomModel(String name, Supplier<Block> blockSup) {
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
