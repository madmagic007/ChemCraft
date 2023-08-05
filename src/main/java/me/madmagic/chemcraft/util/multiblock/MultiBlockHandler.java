package me.madmagic.chemcraft.util.multiblock;

import me.madmagic.chemcraft.util.multiblock.instances.MultiBlockStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockHandler {

    public static final BooleanProperty property = BooleanProperty.create("is_multi_block");
    public static final List<MultiBlockStructure> multiBlocks = new ArrayList<>();

    public static void add(MultiBlockStructure structure, boolean isExisting) {
        remove(structure);
        multiBlocks.add(structure);
        structure.created(isExisting);
    }

    public static void remove(MultiBlockStructure structure) {
        List<MultiBlockStructure> toRemove = new ArrayList<>();

        for (MultiBlockStructure multiBlock : multiBlocks) {
            for (BlockPos block : multiBlock.allBlocks) {
                if (structure.contains(block, multiBlock.level)) {
                    multiBlock.destroyed(false);
                    toRemove.add(multiBlock);
                    break;
                }
            }
        }

        multiBlocks.removeAll(toRemove);
    }

    public static void remove(BlockPos pos, Level level, boolean unload) {
        List<MultiBlockStructure> toRemove = new ArrayList<>();

        for (MultiBlockStructure multiBlock : multiBlocks) {
            if (multiBlock.contains(pos, level)) {
                multiBlock.destroyed(unload);
                toRemove.add(multiBlock);
            }
        }

        multiBlocks.removeAll(toRemove);
    }

    public static boolean isValidMultiBlock(BlockPos pos, Level level) {
        for (MultiBlockStructure multiBlock : multiBlocks) {
            if (multiBlock.contains(pos, level)) return true;
        }

        return false;
    }

    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        for (MultiBlockStructure multiBlock : multiBlocks) {
            if (multiBlock.contains(pos, state))
                return multiBlock.getBlockEntity();
        }

        return null;
    }
}
