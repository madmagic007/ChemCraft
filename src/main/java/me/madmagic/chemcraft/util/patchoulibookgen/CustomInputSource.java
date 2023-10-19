package me.madmagic.chemcraft.util.patchoulibookgen;

import dev.lukebemish.dynamicassetgenerator.api.PathAwareInputStreamSource;
import dev.lukebemish.dynamicassetgenerator.api.ResourceGenerationContext;
import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.util.DynAssetGenTest;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class CustomInputSource implements PathAwareInputStreamSource {

    @Override
    public @Nullable IoSupplier<InputStream> get(ResourceLocation outRl, ResourceGenerationContext context) {
        return () -> {
            return new ByteArrayInputStream("{}".getBytes());
        };
    }

    @Override
    public @NotNull Set<ResourceLocation> getLocations() {
        Set<ResourceLocation> set = new HashSet<>();

        DynAssetGenTest.assetCache.getContext().listResources("chemcraft", "chemistry/products", ((resourceLocation, inputStreamIoSupplier) -> {
            ChemCraft.info(resourceLocation.getPath());
            set.add(toLocation(resourceLocation.getPath().replace("chemistry/products/", "")));
        }));

        return set;
    }

    private ResourceLocation toLocation(String name) {
        return new ResourceLocation(ChemCraft.modId, "patchouli_books/chemists_manual/en_us/entries/fluid_types/" + name);
    }
}
