package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blockentities.FurnaceHeaterBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IActivateAble;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FurnaceHeaterBlock extends BaseBlock implements AutoEntityTickerBlock, IActivateAble, IRotateAble, IPipeConnectable {

    public FurnaceHeaterBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FurnaceHeaterBlockEntity(pPos, pState);
    }

    @Override
    public PipeConnectionType connectionType(BlockState state, Direction direction) {
        if (direction.getAxis().isVertical()) return IPipeConnectable.PipeConnectionType.NONE;

        return switch (getRelativeDirFromAbsolute(state, direction)) {
            case WEST -> IPipeConnectable.PipeConnectionType.INPUT;
            case EAST -> IPipeConnectable.PipeConnectionType.OUTPUT;
            default -> IPipeConnectable.PipeConnectionType.NONE;
        };
    }
}
