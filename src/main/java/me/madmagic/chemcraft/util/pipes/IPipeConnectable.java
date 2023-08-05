package me.madmagic.chemcraft.util.pipes;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface IPipeConnectable {

    PipeConnectionType connectionType(BlockState state, Direction direction);

    enum PipeConnectionType {
        INPUT(true),
        OUTPUT(true),
        PIPE(true),
        BOTH(true),
        NONE(false);

        public boolean canConnect;

        PipeConnectionType(boolean canConnect) {
            this.canConnect = canConnect;
        }

        public boolean equals(PipeConnectionType other) {
            if (super.equals(other)) return true;
            if (this == BOTH && (!other.equals(NONE) && !other.equals(PIPE))) return true;
            return other == BOTH && (!super.equals(NONE) && !super.equals(PIPE));
        }
    }
}
