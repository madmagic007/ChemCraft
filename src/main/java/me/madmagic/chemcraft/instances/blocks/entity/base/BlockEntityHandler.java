package me.madmagic.chemcraft.instances.blocks.entity.base;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.blocks.base.BlockHandler;
import me.madmagic.chemcraft.instances.blocks.entity.MotorBlockEntity;
import me.madmagic.chemcraft.instances.blocks.entity.CentrifugalPumpBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityHandler {

    private static final DeferredRegister<BlockEntityType<?>> entities = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ChemCraft.modId);

    public static final RegistryObject<BlockEntityType<MotorBlockEntity>> motorBlockEntity = entities.register(MotorBlockEntity.name, () ->
            BlockEntityType.Builder.of(MotorBlockEntity::new, BlockHandler.Motor.get()).build(null));

    public static final RegistryObject<BlockEntityType<CentrifugalPumpBlockEntity>> pumpBlockEntity = entities.register(CentrifugalPumpBlockEntity.name, () ->
            BlockEntityType.Builder.of(CentrifugalPumpBlockEntity::new, BlockHandler.CentrifugalPump.get()).build(null));

    public static void register(IEventBus eventBus) {
        entities.register(eventBus);
    }
}
