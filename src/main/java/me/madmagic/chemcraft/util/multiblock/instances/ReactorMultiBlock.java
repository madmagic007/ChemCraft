package me.madmagic.chemcraft.util.multiblock.instances;

import me.madmagic.chemcraft.instances.blockentities.ReactorBlockEntity;
import me.madmagic.chemcraft.instances.blockentities.machines.CatalystRefinerBlockEntity;
import me.madmagic.chemcraft.instances.blocks.InsulatedBlock;
import me.madmagic.chemcraft.instances.blocks.ReactorBlock;
import me.madmagic.chemcraft.instances.blocks.base.CatalystBlock;
import me.madmagic.chemcraft.util.multiblock.MultiBlockStructure;
import me.madmagic.chemcraft.util.reactions.ChemicalReaction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReactorMultiBlock extends MultiBlockStructure {

    private List<ChemicalReaction.ReactionCatalyst> foundCatalysts = new ArrayList<>();

    public ReactorMultiBlock(BlockPos masterPos, Level level) {
        super(masterPos, level);
        addAllConnectedOfType(InsulatedBlock.class, CatalystBlock.class);
    }

    @Override
    public void created(boolean isExisting) {
        super.created(isExisting);

        BlockEntity entity = level.getBlockEntity(masterPos);
        if (!(entity instanceof ReactorBlockEntity reactorBlockEntity)) return;
        reactorBlockEntity.multiBlockCreated(allBlocks.size(), new ArrayList<>()); //TODO don't forget to populate the list
    }

    @Override
    public boolean validate() {
        int bottomLevel = blocks.firstKey();
        AABB bottomDim = dimAtLevel(bottomLevel);

        int topLevel = blocks.lastKey();
        AABB topDim = dimAtLevel(topLevel);

        if (!isFilled(bottomDim, InsulatedBlock.class, ReactorBlock.class) || !isFilled(topDim, InsulatedBlock.class) || bottomDim.getXsize() < 2 || blocks.size() < 3) return false;

        for (int key : blocks.keySet()) {
            AABB dim = dimAtLevel(key);
            if (!isSame(bottomDim, dim)) return false;
        }

        AABB combined = new AABB(bottomDim.minX, bottomDim.minY, bottomDim.minZ, topDim.maxX, topDim.maxY, topDim.maxZ);
        AABB centerDim = combined.deflate(1, 1, 1);
        List<BlockState> centerDimStates = level.getBlockStates(centerDim).toList();

        foundCatalysts = CatalystRefinerBlockEntity.validCatalysts.values().stream()
                .map(blockItem -> {
                    Block catalystBlock = Block.byItem(blockItem);

                    return new ChemicalReaction.ReactionCatalyst(
                            catalystBlock.getDescriptionId().replace("block.chemcraft.", ""),
                            (int) centerDimStates.stream().filter(state ->
                                state.getBlock().equals(catalystBlock)
                            ).count()
                    );
                }).collect(Collectors.toList());

        return isSquare(combined, false) && isFilled(combined, ReactorBlock.class, InsulatedBlock.class, CatalystBlock.class);
    }
}
