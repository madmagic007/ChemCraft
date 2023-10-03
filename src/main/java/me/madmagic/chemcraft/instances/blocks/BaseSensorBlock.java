package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.CustomBlocks;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.instances.blockentities.sensors.BaseSensorBlockEntity;
import me.madmagic.chemcraft.instances.blockentities.sensors.SensorReceiverBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.instances.items.PipeWrenchItem;
import me.madmagic.chemcraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class BaseSensorBlock extends BaseBlock implements AutoEntityTickerBlock, IRotateAble {

    public static final VoxelShape shape = Block.box(5, 5, -6, 11, 11, 16);

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

        return sensor.getRedstoneSignalOutput();
    }

    @Override
    public int getDirectSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        BlockEntity ent = pLevel.getBlockEntity(pPos);
        if (!(ent instanceof SensorReceiverBlockEntity sensor)) return 0; // only make receivers power the block they're connected to.

        return sensor.getRedstoneSignalOutput();
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

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.SUCCESS;

        Component msg = Component.literal("Failed to set sensor source.");
        BaseBlockEntity sensor = (BaseBlockEntity) pLevel.getBlockEntity(pPos);
        ItemStack itemInHand = pPlayer.getItemInHand(pHand);

        CompoundTag itemNBT = itemInHand.getTag();
        if (itemNBT == null) itemNBT = new CompoundTag();
        CompoundTag locTag = itemNBT.getCompound("setPos");

        if (itemInHand.isEmpty() && pPlayer.isCrouching() && sensor instanceof SensorReceiverBlockEntity sensorReceiverEnt) {
            msg = Component.literal(String.format("Removed source sensor"));
            sensorReceiverEnt.sourcePos = null;
            pLevel.sendBlockUpdated(pPos, pState, pState, Block.UPDATE_ALL);
            sensor.setChanged();
        } else if (itemInHand.getItem() instanceof PipeWrenchItem) {
            if (locTag.isEmpty() || (!locTag.isEmpty() && !(sensor instanceof SensorReceiverBlockEntity))) {
                locTag = new CompoundTag();
                int x = pPos.getX();
                int y = pPos.getY();
                int z = pPos.getZ();

                locTag.putInt("x", x);
                locTag.putInt("y", y);
                locTag.putInt("z", z);

                msg = Component.literal(String.format("Selected %s at (%s, %s, %s)",
                        ((MenuProvider) sensor).getDisplayName().getString(), x, y, z));

                itemNBT.put("setPos", locTag);
            } else if (sensor instanceof SensorReceiverBlockEntity sensorReceiverEnt) {

                int x = locTag.getInt("x");
                int y = locTag.getInt("y");
                int z = locTag.getInt("z");
                BlockPos pos = new BlockPos(x, y, z);
                MenuProvider menuEnt = (MenuProvider) pLevel.getBlockEntity(pos);

                if (pos.equals(pPos)) {
                    msg = Component.literal("Cant set self as a source.");
                } else {
                    msg = Component.literal(String.format("Sucesfully set source to %s at (%s, %s, %s)",
                            menuEnt.getDisplayName().getString(), x, y, z));

                    sensorReceiverEnt.sourcePos = pos;
                    pLevel.scheduleTick(pPos, this, 0);

                    pLevel.sendBlockUpdated(pPos, pState, pState, Block.UPDATE_ALL);
                    sensor.setChanged();

                    itemNBT.remove("setPos");
                }
            }
        } else
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);

        itemInHand.setTag(itemNBT);
        pPlayer.displayClientMessage(msg, false);
        return InteractionResult.CONSUME;
    }

    public static RegistryObject<Block> registerSensorBlock(String name, BiFunction<BlockPos, BlockState, BlockEntity> entitySupplier) {
        return CustomBlocks.rotateAbleCustomModel(name, () -> new BaseSensorBlock(entitySupplier));
    }
}
