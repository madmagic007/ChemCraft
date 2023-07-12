package me.madmagic.chemcraft.instances.blocks.base;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.blocks.MotorBlock;
import me.madmagic.chemcraft.instances.blocks.PipeBlock;
import me.madmagic.chemcraft.instances.blocks.CentrifugalPumpBlock;
import me.madmagic.chemcraft.instances.items.base.ItemHandler;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockHandler {

    private static final DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, ChemCraft.modId);

    public static RegistryObject<Block> Pipe = registerBlock(PipeBlock.blockName, PipeBlock::new);
    public static RegistryObject<Block> CentrifugalPump = registerBlock(CentrifugalPumpBlock.blockName, CentrifugalPumpBlock::new);
    public static RegistryObject<Block> Motor = registerBlock(MotorBlock.blockName, MotorBlock::new);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = blocks.register(name, block);

        ItemHandler.items.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties()));
        return toReturn;
    }

    public static void register(IEventBus eventBus) {
        blocks.register(eventBus);
    }
}
