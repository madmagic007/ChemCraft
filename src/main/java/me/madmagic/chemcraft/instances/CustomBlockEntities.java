package me.madmagic.chemcraft.instances;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.blockentities.*;
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
    public static final RegistryObject<BlockEntityType<BlockEntity>> tankController = register(TankControllerBlockEntity::new, CustomBlocks.tankController);
    public static final RegistryObject<BlockEntityType<BlockEntity>> waterExtractor = register(WaterExtractorBlockEntity::new, CustomBlocks.waterExtractor);
    public static final RegistryObject<BlockEntityType<BlockEntity>> fluoriteCoater = register(TeflonCoaterBlockEntity::new, CustomBlocks.fluoriteCoater);
    public static final RegistryObject<BlockEntityType<BlockEntity>> airCooler = register(AirCoolerBlockEntity::new, CustomBlocks.airCoolerBlock);
    public static final RegistryObject<BlockEntityType<BlockEntity>> temperatureSensor = register(TemperatureSensorBlockEntity::new, CustomBlocks.temperatureSensor);

    private static RegistryObject<BlockEntityType<BlockEntity>> register(BlockEntityType.BlockEntitySupplier<BlockEntity> sup, RegistryObject<Block> blockReg) {
        String name = blockReg.getId().getPath();
        return entities.register(name + "_entity", () -> BlockEntityType.Builder.of(sup, blockReg.get()).build(null));
    }

    public static void register(IEventBus eventBus) {
        entities.register(eventBus);
    }
}
