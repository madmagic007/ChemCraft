package me.madmagic.chemcraft.instances.blockentities.sensors;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.util.networking.INetworkUpdateAble;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SensorReceiverBlockEntity extends BaseBlockEntity implements IRotateAble, MenuProvider, INetworkUpdateAble {

    public int lastOutputted = 0;

    public SensorReceiverBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.sensorReceiver.get(), pPos, pBlockState);
    }

    @Override
    public void tick() {
        int curSig = getRedstoneSignalOutput();
        if (curSig != lastOutputted)
            level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
    }

    public int getRedstoneSignalOutput() {
//        BlockEntity ent = level.getBlockEntity(getBlockPos().relative(getFacing(getBlockState())));
//        if (!(ent instanceof IFluidContainer container)) return 0;
//
//        int range = valForRedstone15 - valForRedstone0;
//        double mappedValue = GeneralUtil.mapValue(getValueForSignalCalculation(container) - valForRedstone0, range, 15);
//        return (int) Math.round(Math.max(0, Math.min(mappedValue, 15)));

        return 0;
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Sensor Receiver");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }

//    @Override
//    protected int getDataCount() {
//        return 2;
//    }
//
//    @Override
//    protected int getDataValue(int index) {
//        if (index == 0) return valForRedstone0;
//        return valForRedstone15;
//    }
//
//    @Override
//    protected void setDataValue(int index, int value) {
//        if (index == 0) valForRedstone0 = value;
//        valForRedstone15 = value;
//    }

//    @Override
//    public void updateFromNetworking(int... values) {
//        valForRedstone0 = values[0];
//        valForRedstone15 = values[1];
//        setChanged();
//    }

//    public abstract String getLabelToolTip();
//    public abstract String getEditToolTip();
}
