package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blockentities.TankControllerBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.instances.items.PipeWrenchItem;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.multiblock.MultiBlockHandler;
import me.madmagic.chemcraft.util.multiblock.instances.TankMultiBlock;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TankControllerBlock extends BaseBlock implements IPipeConnectable, EntityBlock {

    public TankControllerBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = super.getStateForPlacement(pContext);
        state = state.setValue(MultiBlockHandler.property, false);
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(MultiBlockHandler.property);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if (pState.getValue(MultiBlockHandler.property)) return new TankControllerBlockEntity(pPos, pState);
        return null;
    }

    @Override
    public PipeConnectionType connectionType(BlockState state, Direction direction) {
        return state.getValue(MultiBlockHandler.property) ? PipeConnectionType.BOTH : PipeConnectionType.NONE;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.PASS;

        ItemStack itemInHand = pPlayer.getItemInHand(pHand);
        if (!(itemInHand.getItem() instanceof PipeWrenchItem)) return InteractionResult.FAIL;

        if (!new TankMultiBlock(pPos, pLevel).check(false)) {
            Component msg = Component.literal("Inavlid tank structure");
            pPlayer.displayClientMessage(msg, false);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            if (!ConnectionHandler.isStateOfType(pLevel.getBlockState(pPos), TankControllerBlock.class))
                MultiBlockHandler.remove(pPos, pLevel, false);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
