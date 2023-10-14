package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blockentities.BurnerHeadBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BurnerHeadBlock extends BaseBlock implements IPipeConnectable, AutoEntityTickerBlock {

    public BurnerHeadBlock() {
        super(
                Properties.copy(Blocks.IRON_BLOCK)
                        .noOcclusion()
        );
    }

    @Override
    public PipeConnectionType connectionType(BlockState state, Direction direction) {
        if (direction == Direction.DOWN) return PipeConnectionType.INPUT;
        return PipeConnectionType.NONE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BurnerHeadBlockEntity(pPos, pState);
    }
}
