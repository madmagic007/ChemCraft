package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blockentities.MotorBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRedstoneMode;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRedstonePowerAble;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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

public class MotorBlock extends BaseBlock implements AutoEntityTickerBlock, IRotateAble, IRedstonePowerAble, IRedstoneMode {

    private static final VoxelShape shape = Stream.of(
            Block.box(7, 7, 0, 9, 9, 3),
            Block.box(2, 0, 3, 14, 4, 13),
            Block.box(4, 4, 3, 12, 12, 15),
            Block.box(5, 12, 5, 11, 16, 11)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public MotorBlock() {
        super(
                BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                        .noOcclusion()
                        .forceSolidOn()
                        .dynamicShape(),
                ShapeUtil.createRotatedShapesMap(shape)
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state =  super.getStateForPlacement(context);
        BlockPos selfPos = context.getClickedPos();
        Level world = context.getLevel();
        assert state != null;

        Direction pumpAtDir = ConnectionHandler.isTouching(selfPos, world, CentrifugalPumpBlock.class);
        if (pumpAtDir == null) return state;

        BlockPos pumpPos = selfPos.relative(pumpAtDir);
        BlockState pumpState = world.getBlockState(pumpPos);
        Direction pumpDir = pumpState.getValue(facing);

        if (pumpDir.equals(pumpAtDir))
            state = state.setValue(facing, pumpAtDir);

        return state;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MotorBlockEntity(pPos, pState);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return direction.getAxis().isHorizontal();
    }
}
