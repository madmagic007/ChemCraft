package me.madmagic.chemcraft.util.reloaders;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;

public abstract class BasePreparableReloadListener<T> extends SimplePreparableReloadListener<HashSet<JsonElement>> {

    private static final Gson GSON = new Gson();

    private final String path;
    private final Codec<T> codec;

    public BasePreparableReloadListener(String path, Codec<T> codec) {
        this.path = path;
        this.codec = codec;
    }

    @Override
    protected HashSet<JsonElement> prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        HashSet<JsonElement> set = new HashSet<>();

        boolean val = FMLEnvironment.dist.isClient();


        Map<ResourceLocation, Resource> resourceLocationResourceMap = pResourceManager.listResources(path, this::isJson);
        resourceLocationResourceMap.forEach((loc, resource) -> {
            try (InputStream inputStream = resource.open();
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                JsonElement json = GsonHelper.fromJson(GSON, reader, JsonElement.class);
                set.add(json);
            } catch (Exception ignored) {}
        });
        return set;
    }

    @Override
    protected void apply(HashSet<JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        removeAllElements();
        pObject.forEach(element ->
                codec.parse(JsonOps.INSTANCE, element).get().ifLeft(this::registerElement)
        );
    }

    protected abstract void removeAllElements();
    protected abstract void registerElement(T element);

    private boolean isJson(final ResourceLocation filename) {
        return filename.toString().contains(path) && filename.toString().endsWith(".json");
    }
}
