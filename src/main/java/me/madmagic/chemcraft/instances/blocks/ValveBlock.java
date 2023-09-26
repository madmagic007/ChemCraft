package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.CustomBlocks;
import me.madmagic.chemcraft.instances.blockentities.ValveBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IHasRedstonePowerLevel;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRedstoneMode;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.util.DirectionUtil;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.ShapeUtil;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ValveBlock extends BaseBlock implements IPipeConnectable, IRotateAble, AutoEntityTickerBlock, IRedstoneMode, IHasRedstonePowerLevel {

    private static final VoxelShape defaultShape = Shapes.join(Block.box(0, 4, 4, 16, 12, 12), Block.box(3, 12, 3, 13, 15, 13), BooleanOp.OR);

    public ValveBlock() {
        super(Properties.copy(CustomBlocks.pipe.get()), ShapeUtil.createRotatedShapesMap(defaultShape));
    }

    @Override
    public PipeConnectionType connectionType(BlockState state, Direction direction) {
        Direction relativeDir = DirectionUtil.facingToRelative(getFacing(state), direction);
        if (GeneralUtil.isAny(relativeDir, Direction.WEST, Direction.EAST)) return PipeConnectionType.BOTH;
        return PipeConnectionType.NONE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ValveBlockEntity(pPos, pState);
    }
}
