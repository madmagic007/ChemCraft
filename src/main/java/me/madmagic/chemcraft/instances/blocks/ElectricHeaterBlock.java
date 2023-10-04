package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blockentities.ElectricHeaterBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IHasRedstonePowerLevel;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRedstoneMode;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElectricHeaterBlock extends BaseBlock implements AutoEntityTickerBlock, IPipeConnectable, IRotateAble, IHasRedstonePowerLevel, IRedstoneMode {

    public ElectricHeaterBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ElectricHeaterBlockEntity(pPos, pState);
    }

    @Override
    public IPipeConnectable.PipeConnectionType connectionType(BlockState state, Direction direction) {
        if (direction.getAxis().isVertical()) return IPipeConnectable.PipeConnectionType.NONE;

        return switch (getRelativeDirFromAbsolute(state, direction)) {
            case WEST -> IPipeConnectable.PipeConnectionType.INPUT;
            case EAST -> IPipeConnectable.PipeConnectionType.OUTPUT;
            default -> IPipeConnectable.PipeConnectionType.NONE;
        };
    }

    private static final List<Direction> validRedstoneSides = List.of(Direction.NORTH, Direction.SOUTH);

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        if (direction == null) return false;
        return validRedstoneSides.contains(getRelativeDirFromAbsolute(state, direction));
    }
}
