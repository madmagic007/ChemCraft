package me.madmagic.chemcraft.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.fluids.FluidHandler;
import me.madmagic.chemcraft.util.fluids.FluidType;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PatchouilPageProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public PatchouilPageProvider(PackOutput pOutput) {
        pathProvider = pOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "patchouli_book/chemists_manual/en_us/fluid_types");
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput pOutput) {
        List<CompletableFuture<?>> list = new ArrayList<>();

        List<Map.Entry<String, FluidType>> entryList = new ArrayList<>(FluidHandler.fluidTypes.entrySet());
        entryList.sort(Map.Entry.comparingByKey());

        List<FluidType> sortedFluids = entryList.stream().map(Map.Entry::getValue).toList();

        System.out.println("genning fluid type");
        GeneralUtil.forEachIndexed(sortedFluids, (fluidType, i) -> {
            System.out.println("FLUID TYPE");
            JsonObject o = new JsonObject();
            o.addProperty("name", fluidType.name());
            o.addProperty("icon", "chemcraft:gui/drop.png");
            o.addProperty("category", "chemcraft:fluid_types");
            o.addProperty("sortnum", i);

            JsonObject page = new JsonObject();
            page.addProperty("type", "patchouli:text");
            page.addProperty("text", "test");


            JsonArray pages = new JsonArray();
            pages.add(page);

            o.add("pages", pages);

            list.add(DataProvider.saveStable(pOutput, o, pathProvider.json(new ResourceLocation(ChemCraft.modId, fluidType.name()))));
        });

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    @Override
    public @NotNull String getName() {
        return "Patchouli Pages";
    }
}
