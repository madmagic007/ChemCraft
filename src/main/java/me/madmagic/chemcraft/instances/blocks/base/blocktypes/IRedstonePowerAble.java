package me.madmagic.chemcraft.instances.blocks.base.blocktypes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface IRedstonePowerAble {

    BooleanProperty powered = BlockStateProperties.POWERED;

    default void addPowerAbleState(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(powered);
    }

    default BlockState setPowered(BlockState state, boolean value) {
        return state.setValue(powered, value);
    }

    default boolean isPowered(BlockState state) {
        return state.getValue(powered);
    }
}
