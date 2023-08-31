package me.madmagic.chemcraft.instances.blocks.base.blocktypes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface IMultiBlockComponent {

    BooleanProperty property = BooleanProperty.create("is_multi_block");

    default void addMultiblockState(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(property);
    }

    default boolean isMultiBlock(BlockState state) {
        return state.getValue(property);
    }

    default BlockState setIsMultiBlock(BlockState state, boolean isMultiBlock) {
        return state.setValue(property, isMultiBlock);
    }
}
