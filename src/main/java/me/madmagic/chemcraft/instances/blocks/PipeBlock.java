package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.instances.items.PipeWrenchItem;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import me.madmagic.chemcraft.util.pipes.PipeConnectionHandler;
import me.madmagic.chemcraft.util.pipes.PipeShapeHandler;
import me.madmagic.chemcraft.util.pipes.PipeWrenchHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.concurrent.atomic.AtomicReference;

public class PipeBlock extends BaseBlock implements IPipeConnectable {

    public PipeBlock() {
        super(
                BlockBehaviour.Properties.copy(Blocks.IRON_BARS)
        );
        AtomicReference<BlockState> state = new AtomicReference<>(this.stateDefinition.any());
        PipeConnectionHandler.connectionProperties.values().forEach(property ->
                state.set(state.get().setValue(property, 0)));
        registerDefaultState(state.get());

        stateDefinition.getPossibleStates().forEach(PipeShapeHandler::of);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        return PipeConnectionHandler.updateAllConnectionStates(state, pos, level);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return PipeConnectionHandler.updateConnectionStateAtDir(pState, pNeighborState, pDirection);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        PipeConnectionHandler.connectionProperties.values().forEach(pBuilder::add);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return PipeShapeHandler.of(pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.PASS;

        ItemStack itemInHand = pPlayer.getItemInHand(pHand);
        if (!(itemInHand.getItem() instanceof PipeWrenchItem)) return InteractionResult.FAIL;

        Direction clickedDir = PipeWrenchHandler.getClickedDirection(pHit);
        PipeConnectionHandler.updateConnection(pState, pPos, clickedDir, pLevel);

        return InteractionResult.SUCCESS;
    }

    @Override
    public PipeConnectionType connectionType(BlockState state, Direction direction) {
        return PipeConnectionType.PIPE;
    }
}
