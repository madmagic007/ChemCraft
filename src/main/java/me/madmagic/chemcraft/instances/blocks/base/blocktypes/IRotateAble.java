package me.madmagic.chemcraft.instances.blocks.base.blocktypes;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public interface IRotateAble {
    DirectionProperty facing = BlockStateProperties.HORIZONTAL_FACING;

    default void addRotateableState(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(facing);
    }

    default Direction getFacing(BlockState state) {
        return state.getValue(facing);
    }

    default Direction getAbsoluteDirFromRelative(BlockState state, Direction relative) {
        int val = getFacing(state).get2DDataValue() + relative.get2DDataValue() - 2;
        if (val < 0) val += 4;
        if (val > 3) val -= 4;
        return Direction.from2DDataValue(val);
    }

    default Direction getRelativeDirFromAbsolute(BlockState state, Direction absolute) {
        int val = (absolute.get2DDataValue() - getFacing(state).get2DDataValue() + 2);
        if (val < 0) val += 4;

        return Direction.from2DDataValue(val);
    }
}
