package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseEnergyStorageBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IHasRedstonePowerLevel;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRedstoneMode;
import me.madmagic.chemcraft.instances.menus.ElectricHeaterMenu;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.fluids.*;
import me.madmagic.chemcraft.util.networking.INetworkUpdateAble;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

public class ElectricHeaterBlockEntity extends BaseEnergyStorageBlockEntity implements MenuProvider, IFluidContainer, INetworkUpdateAble, IHasRedstonePowerLevel, IRedstoneMode {

    public static int maxHeatingSPT = 50;
    public static final int powerFactor = 4;

    private int heatingSPT = 25;
    private double actualHeating = heatingSPT;

    public ElectricHeaterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.electricHeater.get(), pPos, pBlockState, 1000, 250);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Electric heater");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ElectricHeaterMenu(pContainerId, this, containerData);
    }

    @Override
    public void updateFromNetworking(int... values) {
        heatingSPT = values[0];
    }

    @Override
    protected int getDataValue(int index) {
        if (index == 1) return heatingSPT;
        return super.getDataValue(index);
    }

    @Override
    protected void setDataValue(int index, int value) {
        if (index == 1) heatingSPT = value;
        else super.setDataValue(index, value);
    }

    @Override
    protected int getDataCount() {
        return 2;
    }

    private final MultiFluidStorage fluidStorage = new MultiFluidStorage(1000) {};

    @Override
    public void tick() {
        RedstoneMode mode = getRedstoneMode(getBlockState());
        int redstoneLevel = getRedstoneLevel(getBlockState());

        int energyUsage = heatingSPT * powerFactor;

        actualHeating = switch (mode.matchesRedstoneSignalIgnoringSPT(redstoneLevel) ? RedstoneMode.IGNORED : mode) {
            case IGNORED -> heatingSPT;
            case SPT_WHEN_HIGH -> GeneralUtil.mapValue(redstoneLevel, 15, heatingSPT);
            case SPT_WHEN_LOW -> GeneralUtil.mapValue(15 - redstoneLevel, 15, heatingSPT);
            default -> 0;
        };

        if (hasEnoughEnergy(energyUsage) && actualHeating != 0) {
            if (!useEnergy(energyUsage)) actualHeating = 0;
        }
        else actualHeating = 0;

        if (actualHeating > 0) fluidStorage.fluids.forEach(fluid -> {
            fluid.temperature = Math.max(actualHeating, fluid.temperature + actualHeating / 20);
        });
    }

    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, LinkedList<Fluid> fluids, double amount) {
        if (actualHeating != 0) fluids.forEach(fluid -> {
            fluid.temperature += actualHeating;
        });

        FluidHandler.transferTo(fluidStorage.fluids, fluids);
        DisplacementHandler.tryFeed(worldPosition, pipeDir.getOpposite(), level, fluids, FluidHandler.getStored(fluids));

        //store excess to self
        fluidStorage.add(fluids);
    }

    @Override
    public double extract(BlockPos pipePos, Direction pipeDir, double amount, LinkedList<Fluid> extractTo) {
        return fluidStorage.extract(amount, extractTo);
    }

    @Override
    public MultiFluidStorage getFluidStorage(BlockPos pipePos, Direction pipeDir) {
        return fluidStorage;
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        fluidStorage.saveToNBT(nbt);
        super.saveToNBT(nbt);
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        fluidStorage.loadFromNBT(nbt);
        super.loadFromNBT(nbt);
    }
}
