package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blockentities.AirCoolerBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IActivateAble;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IHasRedstonePowerLevel;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRedstoneMode;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.util.DirectionUtil;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AirCoolerBlock extends BaseBlock implements AutoEntityTickerBlock, IPipeConnectable, IRotateAble, IActivateAble, IHasRedstonePowerLevel, IRedstoneMode {

    private static final VoxelShape shape = Block.box(0, 4, 0, 16, 12, 16);

    public AirCoolerBlock() {
        super(
                Properties.copy(Blocks.IRON_BLOCK)
                        .dynamicShape()
                        .forceSolidOn()
                        .noOcclusion(),
                shape
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AirCoolerBlockEntity(pPos, pState);
    }

    @Override
    public PipeConnectionType connectionType(BlockState state, Direction direction) {
        if (direction.getAxis().isVertical()) return PipeConnectionType.NONE;

        return switch (DirectionUtil.facingToRelative(getFacing(state), direction)) {
            case WEST -> PipeConnectionType.INPUT;
            case EAST -> PipeConnectionType.OUTPUT;
            default -> PipeConnectionType.NONE;
        };
    }

    private static final List<Direction> validRedstoneSides = List.of(Direction.NORTH, Direction.SOUTH);

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        if (direction == null) return false;
        return validRedstoneSides.contains(DirectionUtil.facingToRelative(getFacing(state), direction));
    }
}
