package me.madmagic.chemcraft.util.pipes;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface IPipeConnectable {

    PipeConnectionType connectionType(BlockState state, Direction direction);

    void onPipeConnected(BlockState ownState, BlockPos ownPos, BlockState pipeState, BlockPos pipePos, Direction pipeDir);

    enum PipeConnectionType {
        INPUT(true),
        OUTPUT(true),
        PIPE(true),
        NONE(false);

        public boolean canConnect;

        PipeConnectionType(boolean canConnect) {
            this.canConnect = canConnect;
        }
    }
}
