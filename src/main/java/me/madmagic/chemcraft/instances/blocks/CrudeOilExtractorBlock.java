package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.CustomBlocks;
import me.madmagic.chemcraft.instances.blockentities.CrudeOilExtractorBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrudeOilExtractorBlock extends BaseBlock implements IPipeConnectable, AutoEntityTickerBlock {

    public CrudeOilExtractorBlock() {
        super(Properties.copy(CustomBlocks.waterExtractor.get()).noOcclusion());
    }

    @Override
    public PipeConnectionType connectionType(BlockState state, Direction direction) {
        if (direction.equals(Direction.UP)) return PipeConnectionType.OUTPUT;
        return PipeConnectionType.NONE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CrudeOilExtractorBlockEntity(pPos, pState);
    }
}
