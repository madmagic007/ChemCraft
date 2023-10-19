package me.madmagic.chemcraft.util.patchoulibookgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.madmagic.chemcraft.util.fluids.FluidType;

public class PatchouliBookGen {

    public static void addFluidType(String name, FluidType type) {
        JsonObject o = new JsonObject();
        o.addProperty("name", name);
        o.addProperty("icon", "chemcraft:gui/drop.png");
        o.addProperty("category", "chemcraft:fluid_types");

        JsonObject page = new JsonObject();
        page.addProperty("type", "patchouli:text");
        page.addProperty("text", "test");

        JsonArray pages = new JsonArray();
        pages.add(page);

        o.add("pages", pages);
    }
}
