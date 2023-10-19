package me.madmagic.chemcraft.util.patchoulibookgen;

import com.google.gson.JsonObject;
import dev.lukebemish.dynamicassetgenerator.api.InputStreamSource;
import dev.lukebemish.dynamicassetgenerator.api.ResourceGenerationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CustomInputSource implements InputStreamSource {

    private final JsonObject o;

    public CustomInputSource(JsonObject o) {
        this.o = o;
    }

    @Override
    public @Nullable IoSupplier<InputStream> get(ResourceLocation outRl, ResourceGenerationContext context) {
        return () -> new ByteArrayInputStream(o.toString().getBytes());
    }
}
