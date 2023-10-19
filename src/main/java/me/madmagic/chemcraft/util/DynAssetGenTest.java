package me.madmagic.chemcraft.util;

import dev.lukebemish.dynamicassetgenerator.api.ResourceCache;
import dev.lukebemish.dynamicassetgenerator.api.client.AssetResourceCache;
import dev.lukebemish.dynamicassetgenerator.api.client.generators.TextureGenerator;
import dev.lukebemish.dynamicassetgenerator.api.client.generators.texsources.TextureReaderSource;
import me.madmagic.chemcraft.ChemCraft;
import net.minecraft.resources.ResourceLocation;

public class DynAssetGenTest {

    private static final AssetResourceCache assetCache =
            ResourceCache.register(new AssetResourceCache(new ResourceLocation(ChemCraft.modId, "assets")));

    public static void test() {
        assetCache.planSource(new TextureGenerator(
                new ResourceLocation(ChemCraft.modId, "block/distillery"),
                new TextureReaderSource.Builder().setPath(new ResourceLocation("item/apple")).build()
        ));
    }
}
