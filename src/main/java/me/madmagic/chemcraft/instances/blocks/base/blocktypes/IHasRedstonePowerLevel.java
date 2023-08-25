package me.madmagic.chemcraft.instances.blocks.base.blocktypes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface IHasRedstonePowerLevel {

    IntegerProperty powerLevel = BlockStateProperties.POWER;

    default void addRedstonePowerAbleState(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(powerLevel);
    }

    default BlockState setPowerLevel(BlockState state, int level) {
        return state.setValue(powerLevel, level);
    }

    default int getRedstoneLevel(BlockState state) {
        if (!state.hasProperty(powerLevel)) return 0;
        return state.getValue(powerLevel);
    }
}
