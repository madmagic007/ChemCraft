package me.madmagic.chemcraft.util;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConnectionHandler {

    public static Direction isTouching(BlockPos pos, Level level, Class<? extends Block> targetBlock) {
        for (Direction dir : Direction.values()) {
            BlockPos relativePos = pos.relative(dir);
            BlockState state = level.getBlockState(relativePos);

            if (isStateOfType(state, targetBlock))
                return dir;
        }

        return null;
    }

    public static List<Direction> getTouchingDirectionsWhereType(BlockPos pos, Level level, Class<? extends Block> targetBlock) {
        List<Direction> dirs = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            BlockState state = level.getBlockState(pos.relative(direction));

            if (isStateOfType(state, targetBlock))
                dirs.add(direction);
        }

        return dirs;
    }

    public static List<BlockPos> getAllConnectedWhereType(BlockPos pos, Level Level, Class<? extends Block> targetBlock) {
        Set<BlockPos> connected = new HashSet<>();
        getTouchingBlockPosWhereTypeRecursive(pos, Level, targetBlock, connected);
        return new ArrayList<>(connected);
    }

    public static List<BlockPos> getTouchingBlockPosWhereType(BlockPos pos, Level Level, Class<? extends Block> targetBlock) {
        List<BlockPos> blocks = new ArrayList<>();

        for (Direction direction : getTouchingDirectionsWhereType(pos, Level, targetBlock)) {
            blocks.add(pos.relative(direction));
        }

        return blocks;
    }

    private static void getTouchingBlockPosWhereTypeRecursive(BlockPos pos, Level Level, Class<? extends Block> targetBlock, Set<BlockPos> connectedPositions) {
        connectedPositions.add(pos);

        for (BlockPos touchingPosition : getTouchingBlockPosWhereType(pos, Level, targetBlock)) {
            if (connectedPositions.contains(touchingPosition)) continue;
            getTouchingBlockPosWhereTypeRecursive(touchingPosition, Level, targetBlock, connectedPositions);
        }
    }

    public static boolean isStateOfType(BlockState state, Class<? extends Block> targetBlock) {
        return targetBlock.isInstance(state.getBlock());
    }
}
