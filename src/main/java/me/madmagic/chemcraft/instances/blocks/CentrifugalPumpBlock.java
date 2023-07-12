package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.blocks.base.RotatableEntityBlock;
import me.madmagic.chemcraft.instances.blocks.entity.CentrifugalPumpBlockEntity;
import me.madmagic.chemcraft.instances.blocks.entity.base.BlockEntityHandler;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import me.madmagic.chemcraft.util.pipes.PipeConnectionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CentrifugalPumpBlock extends RotatableEntityBlock implements IPipeConnectable {

    public static String blockName = "centrifugal_pump";

    private static final VoxelShape shapeNorth = Stream.of(
            Block.box(7, 7, 10, 9, 9, 16),
            Block.box(1, 0, 6, 15, 4, 10),
            Block.box(3, 3, 5, 13, 13, 11),
            Block.box(4, 13, 4, 12, 16, 12),
            Block.box(4, 4, -1, 12, 12, 5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public CentrifugalPumpBlock() {
        super(
                BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                        .noOcclusion()
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
    public void updateIndirectNeighbourShapes(BlockState pState, LevelAccessor pLevel, BlockPos pPos, int pFlags, int pRecursionLeft) {
        if (!(pLevel instanceof Level level)) return;
        PipeConnectionHandler.updateNeighbours(pPos, level); //todo check if pipes nearby? possible this code works already
    }

    @Override
    public PipeConnectionType connectionType(BlockState state, Direction direction) {
        if (direction.equals(Direction.UP)) return PipeConnectionType.OUTPUT;
        if (direction.equals(state.getValue(facing))) return PipeConnectionType.INPUT;
        return PipeConnectionType.NONE;
    }

    @Override
    public void onPipeConnected(BlockState ownState, BlockPos ownPos, BlockState pipeState, BlockPos pipePos, Direction pipeDir) {

    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        ChemCraft.info("new ent called");
        return new CentrifugalPumpBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, BlockEntityHandler.pumpBlockEntity.get(), CentrifugalPumpBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity entity = pLevel.getBlockEntity(pPos);
        if (!(entity instanceof CentrifugalPumpBlockEntity ent)) return InteractionResult.FAIL;

        NetworkHooks.openScreen((ServerPlayer) pPlayer, ent, pPos);

        return InteractionResult.CONSUME;
    }
}
