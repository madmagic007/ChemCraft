package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.fluids.MultiFluidStorage;
import me.madmagic.chemcraft.util.multiblock.MultiBlockHandler;
import me.madmagic.chemcraft.util.multiblock.instances.ReactorMultiBlock;
import me.madmagic.chemcraft.util.reactions.ChemicalReaction;
import me.madmagic.chemcraft.util.reactions.ChemicalReactionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class ReactorBlockEntity extends BaseBlockEntity implements IFluidContainer {

    private final MultiFluidStorage storage = new MultiFluidStorage(0);
    private HashSet<ChemicalReaction.ReactionCatalyst> catalysts;

    public ReactorBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntities.reactor.get(), pos, state);
    }

    public void multiBlockCreated(int size, HashSet<ChemicalReaction.ReactionCatalyst> catalysts) {
        storage.capacity = size * 1000;
        this.catalysts = catalysts;
    }

    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, LinkedList<Fluid> fluids) {
        storage.add(fluids);
        ChemicalReactionHandler.tryReact(fluids, catalysts);
    }

    @Override
    public double extract(BlockPos pipePos, Direction pipeDir, double amount, LinkedList<Fluid> extractTo) {
        return storage.extract(amount, extractTo);
    }

    @Override
    public MultiFluidStorage getFluidStorage(BlockPos pipePos, Direction pipeDir) {
        return storage;
    }

    @Override
    public List<MultiFluidStorage> getFluidStorages() {
        return List.of(storage);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onLoad() {
        super.onLoad();
        BlockPos pos = getBlockPos();
        if (!level.isClientSide && !MultiBlockHandler.isValidMultiBlock(pos, level))
            new ReactorMultiBlock(pos, level).check(true);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        MultiBlockHandler.remove(getBlockPos(), level, true);
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        storage.loadFromNBT(nbt);
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        storage.saveToNBT(nbt);
    }
}
