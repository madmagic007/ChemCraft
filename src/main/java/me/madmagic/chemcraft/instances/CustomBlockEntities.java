package me.madmagic.chemcraft.instances;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.blockentities.*;
import me.madmagic.chemcraft.instances.blockentities.sensors.LevelSensorBlockEntity;
import me.madmagic.chemcraft.instances.blockentities.sensors.SensorReceiverBlockEntity;
import me.madmagic.chemcraft.instances.blockentities.sensors.TemperatureSensorBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CustomBlockEntities {

    private static final DeferredRegister<BlockEntityType<?>> entities = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ChemCraft.modId);

    public static final RegistryObject<BlockEntityType<BlockEntity>> motor = register(MotorBlockEntity::new, CustomBlocks.motor);
    public static final RegistryObject<BlockEntityType<BlockEntity>> centrifugalPump = register(CentrifugalPumpBlockEntity::new, CustomBlocks.centrifugalPump);
    public static final RegistryObject<BlockEntityType<BlockEntity>> tank = register(TankBlockEntity::new, CustomBlocks.tank);
    public static final RegistryObject<BlockEntityType<BlockEntity>> waterExtractor = register(WaterExtractorBlockEntity::new, CustomBlocks.waterExtractor);
    public static final RegistryObject<BlockEntityType<BlockEntity>> fluoriteCoater = register(TeflonCoaterBlockEntity::new, CustomBlocks.fluoriteCoater);
    public static final RegistryObject<BlockEntityType<BlockEntity>> airCooler = register(AirCoolerBlockEntity::new, CustomBlocks.airCooler);
    public static final RegistryObject<BlockEntityType<BlockEntity>> temperatureSensor = register(TemperatureSensorBlockEntity::new, CustomBlocks.temperatureSensor);
    public static final RegistryObject<BlockEntityType<BlockEntity>> levelSensor = register(LevelSensorBlockEntity::new, CustomBlocks.levelSensor);
    public static final RegistryObject<BlockEntityType<BlockEntity>> sensorReceiver = register(SensorReceiverBlockEntity::new, CustomBlocks.sensorReceiver);
    public static final RegistryObject<BlockEntityType<BlockEntity>> crudeOilExtractor = register(CrudeOilExtractorBlockEntity::new, CustomBlocks.crudeOilExtractor);
    public static final RegistryObject<BlockEntityType<BlockEntity>> distillery = register(DistilleryBlockEntity::new, CustomBlocks.distillery);
    public static final RegistryObject<BlockEntityType<BlockEntity>> electricHeater = register(ElectricHeaterBlockEntity::new, CustomBlocks.electricHeater);
    public static final RegistryObject<BlockEntityType<BlockEntity>> furnaceHeater = register(FurnaceHeaterBlockEntity::new, CustomBlocks.furnaceHeater);

    private static RegistryObject<BlockEntityType<BlockEntity>> register(BlockEntityType.BlockEntitySupplier<BlockEntity> sup, RegistryObject<Block> blockReg) {
        String name = blockReg.getId().getPath();
        return entities.register(name + "_entity", () -> BlockEntityType.Builder.of(sup, blockReg.get()).build(null));
    }

    public static void register(IEventBus eventBus) {
        entities.register(eventBus);
    }
}
