package me.madmagic.chemcraft.instances.blocks.base.blocktypes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface IActivateAble {

    BooleanProperty active = BooleanProperty.create("active");

    default void addActivateAbleState(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(active);
    }

    default BlockState setActive(BlockState state, boolean value) {
        return state.setValue(active, value);
    }

    default boolean isActive(BlockState state) {
        return state.getValue(active);
    }
}
