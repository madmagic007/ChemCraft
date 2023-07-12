package me.madmagic.chemcraft.util.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PipelineHandler {

    public static Set<BlockPos> getPipesWithDevices(BlockPos pipePos, Level world) {
        Set<BlockPos> possibleEndings = new HashSet<>();

        List<BlockPos> connectedPipes = PipeConnectionHandler.getAllConnectedPipes(pipePos, world);
        connectedPipes.forEach(pos -> {
            BlockState stateOfPos = world.getBlockState(pos);
            if (PipeConnectionHandler.isConnectedToDevice(pos, world, stateOfPos))
                possibleEndings.add(pos);
        });

        return possibleEndings;
    }

    public static Set<BlockPos> getDestinationsIfCanFlow(Set<BlockPos> deviceConnections, Level world) {
        boolean hasInput = false;

        Set<BlockPos> destinations = new HashSet<>();

        for (BlockPos pos : deviceConnections) {
            for (Direction direction : Direction.values()) {
                switch (PipeConnectionHandler.getConnectionType(pos, world, direction)) {
                    case INPUT -> hasInput = true;
                    case OUTPUT -> destinations.add(pos.relative(direction));
                }
            }
        }

        if (hasInput && !destinations.isEmpty()) return destinations;
        return null;
    }
}
