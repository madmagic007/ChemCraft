package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.instances.menus.TemperatureSensorMenu;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.networking.INetworkUpdateAble;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TemperatureSensorBlockEntity extends BaseBlockEntity implements IRotateAble, MenuProvider, INetworkUpdateAble {

    private int tempForRedstone0 = 0;
    private int tempForRedstone15 = 100;

    public TemperatureSensorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.temperatureSensor.get(), pPos, pBlockState);
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        tempForRedstone0 = nbt.getInt("chemcraft.redstone0");
        tempForRedstone15 = nbt.getInt("chemcraft.redstone15");
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        nbt.putInt("chemcraft.redstone0", tempForRedstone0);
        nbt.putInt("chemcraft.redstone15", tempForRedstone15);
    }

    @Override
    protected int getDataCount() {
        return 2;
    }

    @Override
    protected int getDataValue(int index) {
        if (index == 0) return tempForRedstone0;
        return tempForRedstone15;
    }

    @Override
    protected void setDataValue(int index, int value) {
        if (index == 0) tempForRedstone0 = value;
        tempForRedstone15 = value;
    }

    @Override
    public void tick() {
        BlockEntity ent = level.getBlockEntity(getBlockPos().relative(getFacing(getBlockState())));
        if (ent == null || !(ent instanceof IFluidContainer container)) return;

        double containerTemp = container.getFluidStorage().temperature;
        int tempRange = tempForRedstone15 - tempForRedstone0;

        double mappedValue = GeneralUtil.mapValue(containerTemp - tempForRedstone0, tempRange, 15);
        int sig = (int) Math.round(Math.max(0, Math.min(mappedValue, 15)));

    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Temperature sensor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new TemperatureSensorMenu(pContainerId, this, containerData);
    }

    @Override
    public void updateFromNetworking(int... values) {
        tempForRedstone0 = values[0];
        tempForRedstone15 = values[1];
    }
}
