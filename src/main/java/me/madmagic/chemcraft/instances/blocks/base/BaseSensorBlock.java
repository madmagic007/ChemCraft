package me.madmagic.chemcraft.instances.blocks.base;

import me.madmagic.chemcraft.instances.CustomBlocks;
import me.madmagic.chemcraft.instances.blockentities.sensors.BaseSensorBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class BaseSensorBlock extends BaseBlock implements AutoEntityTickerBlock, IRotateAble {

    public static final VoxelShape shape = Block.box(5, 5, -4, 11, 11, 16);

    private final BiFunction<BlockPos, BlockState, BlockEntity> entitySupplier;

    public BaseSensorBlock(BiFunction<BlockPos, BlockState, BlockEntity> entitySupplier) {
        super(
                BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                        .noOcclusion()
                        .dynamicShape()
                        .forceSolidOn(),
                ShapeUtil.createRotatedShapesMap(shape)
        );
        this.entitySupplier = entitySupplier;
    }

    @Override
    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        BlockEntity ent = pLevel.getBlockEntity(pPos);
        if (!(ent instanceof BaseSensorBlockEntity sensor)) return 0;

        int toOutput = sensor.getRedstoneSignalOutput();
        sensor.lastOutputted = toOutput;

        return toOutput;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return entitySupplier.apply(pPos, pState);
    }

    public static RegistryObject<Block> registerSensorBlock(String name, BiFunction<BlockPos, BlockState, BlockEntity> entitySupplier) {
        return CustomBlocks.rotateAbleCustomModel(name, () -> new BaseSensorBlock(entitySupplier));
    }
}
