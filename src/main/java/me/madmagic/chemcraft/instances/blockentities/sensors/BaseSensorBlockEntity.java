package me.madmagic.chemcraft.instances.blockentities.sensors;

import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.networking.INetworkUpdateAble;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseSensorBlockEntity extends BaseBlockEntity implements IRotateAble, MenuProvider, INetworkUpdateAble {

    private int valForRedstone0;
    private int valForRedstone15;
    public int lastOutputted = 0;

    public BaseSensorBlockEntity(BlockEntityType<?> entityType, BlockPos pPos, BlockState pBlockState) {
        super(entityType, pPos, pBlockState);
    }

    @Override
    public void tick() {
        int curSig = getRedstoneSignalOutput();
        if (curSig != lastOutputted)
            level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
    }

    public int getRedstoneSignalOutput() {
        BlockEntity ent = level.getBlockEntity(getBlockPos().relative(getFacing(getBlockState())));
        if (!(ent instanceof IFluidContainer container)) return 0;

        int range = valForRedstone15 - valForRedstone0;
        double mappedValue = GeneralUtil.mapValue(getValueForSignalCalculation(container) - valForRedstone0, range, 15);
        return (int) Math.round(Math.max(0, Math.min(mappedValue, 15)));
    }

    protected abstract double getValueForSignalCalculation(IFluidContainer container);

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        valForRedstone0 = nbt.getInt("chemcraft.redstone0");
        valForRedstone15 = nbt.getInt("chemcraft.redstone15");
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        nbt.putInt("chemcraft.redstone0", valForRedstone0);
        nbt.putInt("chemcraft.redstone15", valForRedstone15);
    }

    @Override
    protected int getDataCount() {
        return 2;
    }

    @Override
    protected int getDataValue(int index) {
        if (index == 0) return valForRedstone0;
        return valForRedstone15;
    }

    @Override
    protected void setDataValue(int index, int value) {
        if (index == 0) valForRedstone0 = value;
        valForRedstone15 = value;
    }

    @Override
    public void updateFromNetworking(int... values) {
        valForRedstone0 = values[0];
        valForRedstone15 = values[1];
        setChanged();
    }

    public abstract String getLabelToolTip();
    public abstract String getEditToolTip();
}
