package me.madmagic.chemcraft.util;

import dev.lukebemish.dynamicassetgenerator.api.DataResourceCache;
import dev.lukebemish.dynamicassetgenerator.api.ResourceCache;
import dev.lukebemish.dynamicassetgenerator.api.client.AssetResourceCache;
import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.util.patchoulibookgen.CustomInputSource;
import net.minecraft.resources.ResourceLocation;

public class DynAssetGenTest {

    public static final AssetResourceCache assetCache =
            ResourceCache.register(new AssetResourceCache(new ResourceLocation(ChemCraft.modId, "assets")));

    public static final DataResourceCache dataCache =
            ResourceCache.register(new DataResourceCache(new ResourceLocation(ChemCraft.modId, "data")));


    public static void test() {
        assetCache.planSource(
                new CustomInputSource()
        );
    }
}
