package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IMultiBlockComponent;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.multiblock.MultiBlockHandler;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class InsulatedBlock extends BaseBlock implements IPipeConnectable, IMultiBlockComponent, EntityBlock {

    public InsulatedBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if (!isMultiBlock(pState)) return null;
        return MultiBlockHandler.getBlockEntity(pPos, pState);
    }

    @Override
    public PipeConnectionType connectionType(BlockState state, Direction direction) {
        return isMultiBlock(state) ? PipeConnectionType.BOTH : PipeConnectionType.NONE;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            if (!ConnectionHandler.isStateOfType(pLevel.getBlockState(pPos), InsulatedBlock.class))
                MultiBlockHandler.remove(pPos, pLevel, false);
        }
        
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
