package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.RotatableBlock;
import me.madmagic.chemcraft.instances.blockentities.CentrifugalPumpBlockEntity;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CentrifugalPumpBlock extends RotatableBlock implements AutoEntityTickerBlock, IPipeConnectable {

    private static final VoxelShape shapeNorth = Stream.of(
            Block.box(7, 7, 10, 9, 9, 16),
            Block.box(1, 0, 6, 15, 4, 10),
            Block.box(3, 3, 5, 13, 13, 11),
            Block.box(4, 13, 4, 12, 16, 12),
            Block.box(4, 4, 1, 12, 12, 5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public CentrifugalPumpBlock() {
        super(
                BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                        .noOcclusion()
                        .forceSolidOn()
                        .dynamicShape(),
                shapeNorth
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state =  super.getStateForPlacement(context);
        BlockPos selfPos = context.getClickedPos();
        Level world = context.getLevel();
        assert state != null;

        Direction motorAtDir = ConnectionHandler.isTouching(selfPos, world, MotorBlock.class);
        if (motorAtDir == null) return state;

        BlockPos motorPos = selfPos.relative(motorAtDir);
        BlockState motorState = world.getBlockState(motorPos);
        Direction motorDir = motorState.getValue(facing);

        if (motorDir.equals(motorAtDir.getOpposite()))
            state = state.setValue(facing, motorAtDir.getOpposite());

        return state;
    }

    @Override
    public PipeConnectionType connectionType(BlockState state, Direction direction) {
        if (direction.equals(Direction.UP)) return PipeConnectionType.OUTPUT;
        if (direction.equals(state.getValue(facing))) return PipeConnectionType.INPUT;
        return PipeConnectionType.NONE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CentrifugalPumpBlockEntity(pPos, pState);
    }
}
