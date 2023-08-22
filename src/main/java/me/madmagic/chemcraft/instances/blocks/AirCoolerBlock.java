package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blockentities.AirCoolerBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.RotatableBlock;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AirCoolerBlock extends RotatableBlock implements AutoEntityTickerBlock, EntityBlock, IPipeConnectable {

    private static final VoxelShape shape = Block.box(0, 4, 0, 16, 12, 16);

    public AirCoolerBlock() {
        super(
                Properties.copy(Blocks.IRON_BLOCK)
                        .dynamicShape()
                        .forceSolidOn()
                        .noOcclusion()
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(BlockStateProperties.POWERED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.POWERED);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shape;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AirCoolerBlockEntity(pPos, pState);
    }

    @Override
    public PipeConnectionType connectionType(BlockState state, Direction direction) {
        Direction facing = state.getValue(RotatableBlock.facing);
        Direction relativeDir = Direction.from2DDataValue(
                (direction.get2DDataValue() - facing.get2DDataValue()) % 4);

        return switch (relativeDir) {
            case WEST -> PipeConnectionType.OUTPUT;
            case EAST -> PipeConnectionType.INPUT;
            default -> PipeConnectionType.NONE;
        };
    }
}
