package me.madmagic.chemcraft.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CustomRecipeProvider extends RecipeProvider implements IConditionBuilder {

    private static final List<SmeltRecipeGroup> smeltingRecipes = new ArrayList<>();
    private static final Map<Block, RegistryObject<? extends Item>> nineBlockRecipes = new HashMap();

    public static void addSmeltingRecipe(RegistryObject<? extends Block> oreReg, RegistryObject<? extends Item> itemReg, String name) {
        smeltingRecipes.add(new SmeltRecipeGroup(oreReg, itemReg, name));
    }

    public static void addNineBlockRecipe(Block block, RegistryObject<? extends Item> itemReg) {
        nineBlockRecipes.put(block, itemReg);
    }

    public CustomRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        smeltingRecipes.forEach(recipe -> {
            oreBlasting(pWriter, List.of(recipe.oreReg.get()), RecipeCategory.MISC, recipe.itemReg.get(), 0.7f, 100, recipe.name);
            oreSmelting(pWriter, List.of(recipe.oreReg.get()), RecipeCategory.MISC, recipe.itemReg.get(), 0.7f, 200, recipe.name);
        });

        nineBlockRecipes.forEach((block, itemReg) ->
            nineBlockStorageRecipes(pWriter, RecipeCategory.BUILDING_BLOCKS, itemReg.get(), RecipeCategory.MISC, block)
        );
    }

    private static class SmeltRecipeGroup {

        final RegistryObject<? extends Block> oreReg;
        final RegistryObject<? extends Item> itemReg;
        final String name;

        public SmeltRecipeGroup(RegistryObject<? extends Block> oreReg, RegistryObject<? extends Item> itemReg, String name) {
            this.oreReg = oreReg;
            this.itemReg = itemReg;
            this.name = name;
        }
    }
}
