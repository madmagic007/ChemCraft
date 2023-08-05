package me.madmagic.chemcraft.datagen;

import me.madmagic.chemcraft.ChemCraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CustomBlockTagProvider extends BlockTagsProvider {

    private static final List<RegistryObject<? extends Block>> mineAbleRegs = new ArrayList<>();
    private static final Map<String, RegistryObject<? extends Block>> oreRegs = new HashMap<>();
    public static final List<TagKey<Block>> createdOreTags = new ArrayList<>();

    public static void addMineAble(RegistryObject<? extends Block> blockReg) {
        mineAbleRegs.add(blockReg);
    }

    public static void addOre(String name, RegistryObject<? extends Block> blockReg) {
        oreRegs.put(name, blockReg);
    }

    public CustomBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ChemCraft.modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        mineAbleRegs.forEach(blockReg ->
            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .replace(false)
                    .add(blockReg.get())
        );

        oreRegs.forEach((name, oreReg) -> {
            Block ore = oreReg.get();

            tag(Tags.Blocks.ORES).add(ore);
            tag(createOreTag(name)).add(ore);
        });
    }

    private static TagKey<Block> createOreTag(String name) {
        TagKey<Block> oreTag = BlockTags.create(new ResourceLocation("forge", "ores/" + name.replace("_ore", "")));
        createdOreTags.add(oreTag);
        return oreTag;
    }
}
