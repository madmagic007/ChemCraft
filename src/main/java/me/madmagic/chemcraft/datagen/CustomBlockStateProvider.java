package me.madmagic.chemcraft.datagen;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class CustomBlockStateProvider extends BlockStateProvider {

    private static final List<RegistryObject<? extends Block>> singleVariantRegs = new ArrayList<>();
    private static final List<RegistryObject<? extends Block>> cubeAllRegs = new ArrayList<>();
    private static final List<RegistryObject<? extends Block>> rotatableRegs = new ArrayList<>();

    public static void addSingleVariant(RegistryObject<? extends Block> blockReg) {
        singleVariantRegs.add(blockReg);
    }

    public static void addCubeAll(RegistryObject<? extends Block> blockReg) {
        cubeAllRegs.add(blockReg);
    }

    public static void addRotatable(RegistryObject<? extends Block> blockReg) {
        rotatableRegs.add(blockReg);
    }

    private final ExistingFileHelper exFileHelper;
    public CustomBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ChemCraft.modId, exFileHelper);
        this.exFileHelper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        singleVariantRegs.forEach(blockReg ->
            simpleBlockWithItem(blockReg.get(), getModelFile(blockReg.getId()))
        );

        cubeAllRegs.forEach(blockReg -> {
            Block block = blockReg.get();
            simpleBlockWithItem(block, cubeAll(block));
        });

        rotatableRegs.forEach(blockReg ->
                getVariantBuilder(blockReg.get())
                        .forAllStates(state ->
                                ConfiguredModel.builder()
                                        .modelFile(getModelFile(blockReg.getId()))
                                        .rotationY((int) state.getValue(IRotateAble.facing).toYRot())
                                        .build()
                        ));
    }

    private ModelFile getModelFile(ResourceLocation oldLocation) {
        ResourceLocation newLoc = new ResourceLocation(ChemCraft.modId, "block/" + oldLocation.getPath());
        return new ModelFile.ExistingModelFile(newLoc, exFileHelper);
    }
}
