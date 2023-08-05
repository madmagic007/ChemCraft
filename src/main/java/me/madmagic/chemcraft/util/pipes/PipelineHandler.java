package me.madmagic.chemcraft.util.pipes;

import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class PipelineHandler {

    public static PipeLine findPipeline(BlockPos origin, Level level, IPipeConnectable.PipeConnectionType connectionType) {
        PipeLine pipeline = new PipeLine();
        Set<BlockPos> pipes = new HashSet<>();
        pipes.add(origin);

        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(origin);

        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();
            BlockState currentState = level.getBlockState(currentPos);

            // Check all 6 directions around the current position
            for (Direction direction : Direction.values()) {
                BlockPos neighbor = currentPos.relative(direction);

                if (!PipeConnectionHandler.isDirConnected(currentState, direction) ||
                        !PipeConnectionHandler.isPipeAt(currentPos, level, direction) ||
                        pipes.contains(neighbor)) continue;

                queue.add(neighbor);
                pipes.add(neighbor);
            }
        }

        for (BlockPos pos : pipes) {
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = pos.relative(dir);

                if (isDesiredPos(pos, level, dir, connectionType)) {
                    BlockEntity ent = level.getBlockEntity(neighborPos);

                    if (ent instanceof IFluidContainer container) {
                        pipeline.addSet(new PipeConnectionSet(pos, neighborPos, dir, container, level));
                    }
                }
            }
        }

        return pipeline;
    }

    private static boolean isDesiredPos(BlockPos pos, Level level, Direction dir, IPipeConnectable.PipeConnectionType connectionType) {
        return PipeConnectionHandler.getConnectionTypeAtDir(pos, dir, level).equals(connectionType);
    }
}
