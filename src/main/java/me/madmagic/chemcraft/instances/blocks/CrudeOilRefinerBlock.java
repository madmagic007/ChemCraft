package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blockentities.CrudeOilRefinerBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IMultiBlockComponent;
import me.madmagic.chemcraft.instances.items.PipeWrenchItem;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.multiblock.MultiBlockHandler;
import me.madmagic.chemcraft.util.multiblock.instances.CrudeOilRefinerMultiBlock;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CrudeOilRefinerBlock extends BaseBlock implements IPipeConnectable, IMultiBlockComponent, AutoEntityTickerBlock {

    public CrudeOilRefinerBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if (isMultiBlock(pState)) return new CrudeOilRefinerBlockEntity(pPos, pState);
        return null;
    }

    @Override
    public PipeConnectionType connectionType(BlockState state, Direction direction) {
        return isMultiBlock(state) ? PipeConnectionType.BOTH : PipeConnectionType.NONE;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemInHand = pPlayer.getItemInHand(pHand);
        if (!(itemInHand.getItem() instanceof PipeWrenchItem)) return InteractionResult.FAIL;

        if (pLevel.isClientSide) return InteractionResult.SUCCESS;

        if (!new CrudeOilRefinerMultiBlock(pPos, pLevel).check(false)) {
            Component msg = Component.literal("Invalid Crude Oil refinery structure");
            pPlayer.displayClientMessage(msg, false);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            if (!ConnectionHandler.isStateOfType(pLevel.getBlockState(pPos), CrudeOilRefinerBlock.class))
                MultiBlockHandler.remove(pPos, pLevel, false);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
