package me.madmagic.chemcraft.instances.blocks;



import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.blocks.base.RotatableBlock;
import me.madmagic.chemcraft.instances.items.PipeWrenchItem;
import me.madmagic.chemcraft.util.pipes.*;
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

import java.util.Set;

public class PipeBlock extends RotatableBlock implements IPipeConnectable {

    public static String blockName = "pipe";

    public PipeBlock() {
        super(
                BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                        .noOcclusion()
                        .dynamicShape()
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();

        BlockState state = PipeConnectionHandler.checkAndUpdateConnectionState(super.getStateForPlacement(context), pos, world);
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        PipeConnectionHandler.connectionProperties.values().forEach(pBuilder::add);
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState pState, LevelAccessor pLevel, BlockPos pPos, int pFlags, int pRecursionLeft) {
        if (!(pLevel instanceof Level level)) return;
        PipeConnectionHandler.updateNeighbours(pPos, level);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return PipeShapes.of(pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
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

    @Override
    public void onPipeConnected(BlockState ownState, BlockPos ownPos, BlockState pipeState, BlockPos pipePos, Direction pipeDir) {

    }
}
