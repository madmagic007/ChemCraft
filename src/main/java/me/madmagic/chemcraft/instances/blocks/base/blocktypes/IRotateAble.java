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
}
