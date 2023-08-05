package me.madmagic.chemcraft.instances.blocks.base;

import me.madmagic.chemcraft.datagen.CustomRecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class BaseNineCraftingBlock extends Block {

    public BaseNineCraftingBlock(Properties pProperties, RegistryObject<? extends Item> itemReg) {
        super(pProperties);
        CustomRecipeProvider.addNineBlockRecipe(this, itemReg);
    }
}
