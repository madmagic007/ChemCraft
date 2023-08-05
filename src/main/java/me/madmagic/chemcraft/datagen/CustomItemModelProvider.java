package me.madmagic.chemcraft.datagen;

import me.madmagic.chemcraft.ChemCraft;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class CustomItemModelProvider extends ItemModelProvider {

    private static final List<RegistryObject<? extends Item>> itemRegs = new ArrayList<>();
    private static final List<RegistryObject<? extends Block>> specialBlockItemReg = new ArrayList<>();

    public static void addItem(RegistryObject<? extends Item> itemReg) {
        itemRegs.add(itemReg);
    }

    public static void addSpecialBlockItem(RegistryObject<? extends Block> itemReg) {
        specialBlockItemReg.add(itemReg);
    }

    public CustomItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChemCraft.modId, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        itemRegs.forEach(itemReg -> {
            String id = itemReg.getId().getPath();
            withExistingParent(id,
                    new ResourceLocation("item/generated")).texture("layer0",
                    new ResourceLocation(ChemCraft.modId, "item/" + id));
        });

        specialBlockItemReg.forEach(itemReg -> {
            String id = itemReg.getId().getPath();
            withExistingParent(id, new ResourceLocation(ChemCraft.modId, "block/" + id));
        });
    }
}
