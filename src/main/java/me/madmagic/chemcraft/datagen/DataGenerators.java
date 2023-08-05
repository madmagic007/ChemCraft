package me.madmagic.chemcraft.datagen;

import me.madmagic.chemcraft.ChemCraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = ChemCraft.modId, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        CustomBlockTagProvider customBlockTagProvider = new CustomBlockTagProvider(packOutput, lookupProvider, existingFileHelper);

        generator.addProvider(true, CustomLootTableProvider.get(packOutput));
        generator.addProvider(true, new CustomRecipeProvider(packOutput));
        generator.addProvider(true, new CustomBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(true, new CustomItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(true, customBlockTagProvider);
        generator.addProvider(true, new CustomItemTagProvider(packOutput, lookupProvider, customBlockTagProvider.contentsGetter(), existingFileHelper));
    }
}
