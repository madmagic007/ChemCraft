package me.madmagic.chemcraft.instances.blockentities.sensors;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.instances.menus.SensorReceiverMenu;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.networking.INetworkUpdateAble;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SensorReceiverBlockEntity extends BaseSensorBlockEntity implements IRotateAble, MenuProvider, INetworkUpdateAble {

    public BlockPos sourcePos;

    public SensorReceiverBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.sensorReceiver.get(), pPos, pBlockState);
    }

    public int getRedstoneSignalOutput() {
        if (sourcePos == null) return 0;

        BlockEntity entAtPos = level.getBlockEntity(sourcePos);
        if (entAtPos instanceof BaseSensorBlockEntity sensor) return sensor.getRedstoneSignalOutput();
        else {
            sourcePos = null;

            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }

        return 0;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        ChemCraft.info(nbt);
        String str = nbt.getString("sourcePos");
        if (!str.isEmpty()) sourcePos = BlockPos.of(Long.parseLong(str));
        else sourcePos = null; //very important
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        if (sourcePos != null)
            nbt.putString("sourcePos", String.valueOf(sourcePos.asLong()));
        else
            nbt.putString("sourcePos", ""); //why oh why do i need this
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Sensor Receiver");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new SensorReceiverMenu(pContainerId, this);
    }

    @Override
    protected double getValueForSignalCalculation(IFluidContainer container) {
        return 0;
    }

    @Override
    public String getLabelToolTip() {
        return null;
    }

    @Override
    public String getEditToolTip() {
        return null;
    }

    @Override
    protected int getDataCount() {
        return 0;
    }

    @Override
    protected void setDataValue(int index, int value) {}

    @Override
    protected int getDataValue(int index) {
        return 0;
    }
}
