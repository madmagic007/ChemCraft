package me.madmagic.chemcraft.util.chemistsmanual;

import net.minecraft.world.item.Item;

import java.util.List;

public class CraftingVisual {

    public final Item toMake;
    public final List<Item> ingredients;

    public CraftingVisual(Item toMake, Item... ingredients) {
        this.toMake = toMake;
        this.ingredients = List.of(ingredients);
    }
}
