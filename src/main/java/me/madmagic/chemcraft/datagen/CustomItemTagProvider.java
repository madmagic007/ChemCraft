package me.madmagic.chemcraft.datagen;

import me.madmagic.chemcraft.ChemCraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CustomItemTagProvider extends ItemTagsProvider {

    public CustomItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockLookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, blockLookup, ChemCraft.modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        copy(Tags.Blocks.ORES, Tags.Items.ORES);

        CustomBlockTagProvider.createdOreTags.forEach(tag -> {
            copy(tag, ItemTags.create(tag.location()));
        });
    }
}
