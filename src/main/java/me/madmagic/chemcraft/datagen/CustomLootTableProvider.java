package me.madmagic.chemcraft.datagen;

import me.madmagic.chemcraft.instances.CustomBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CustomLootTableProvider extends BlockLootSubProvider {

    private static final List<RegistryObject<? extends Block>> dropSelfRegs = new ArrayList<>();
    private static final Map<RegistryObject<? extends Block>, RegistryObject<? extends Item>> dropOtherRegs = new HashMap();

    public static void addDropSelf(RegistryObject<? extends Block> blockReg) {
        dropSelfRegs.add(blockReg);
    }

    public static void addDropOther(RegistryObject<? extends Block> blockReg, RegistryObject<? extends Item> itemReg) {
        dropOtherRegs.put(blockReg, itemReg);
    }

    protected CustomLootTableProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelfRegs.forEach(reg -> dropSelf(reg.get()));

        dropOtherRegs.forEach((blockReg, itemReg) -> {
            add(blockReg.get(), block -> createOreDrop(block, itemReg.get()));
        });
    }

    public static LootTableProvider get(PackOutput packOutput) {
        return new LootTableProvider(packOutput, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(CustomLootTableProvider::new,
                        LootContextParamSets.BLOCK)));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return CustomBlocks.blocks.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
