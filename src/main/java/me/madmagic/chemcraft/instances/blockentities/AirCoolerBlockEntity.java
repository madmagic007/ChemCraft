package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseEnergyStorageBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IActivateAble;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IHasRedstonePowerLevel;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRedstoneMode;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.instances.menus.AirCoolerMenu;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.fluids.*;
import me.madmagic.chemcraft.util.networking.INetworkUpdateAble;
import me.madmagic.chemcraft.util.reactions.ChemicalReactionHandler;
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

public class AirCoolerBlockEntity extends BaseEnergyStorageBlockEntity implements MenuProvider, IFluidContainer, INetworkUpdateAble, IActivateAble, IHasRedstonePowerLevel, IRedstoneMode, IRotateAble {

    public static int maxCoolingSpt = 25;
    private static final int airTemp = 25;
    public static final int powerFactor = 3;

    private boolean active = false;
    private int coolingSpt = 15;
    private double actualCooling = coolingSpt;

    public AirCoolerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.airCooler.get(), pPos, pBlockState, 1000);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Air cooler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new AirCoolerMenu(pContainerId, this, containerData);
    }

    @Override
    public void updateFromNetworking(int... values) {
        coolingSpt = values[0];
    }

    @Override
    protected int getDataValue(int index) {
        if (index == 1) return coolingSpt;
        return super.getDataValue(index);
    }

    @Override
    protected void setDataValue(int index, int value) {
        if (index == 1) coolingSpt = value;
        else super.setDataValue(index, value);
    }

    @Override
    protected int getDataCount() {
        return 2;
    }

    private final MultiFluidStorage fluidStorage = new MultiFluidStorage(500) {};

    @Override
    public void tick() {
        RedstoneMode mode = getRedstoneMode(getBlockState());
        int redstoneLevel = getRedstoneLevel(getBlockState());

        int energyUsage = coolingSpt * powerFactor;

        actualCooling = switch (mode.matchesRedstoneSignalIgnoringSPT(redstoneLevel) ? RedstoneMode.IGNORED : mode) {
            case IGNORED -> coolingSpt;
            case SPT_WHEN_HIGH -> GeneralUtil.mapValue(redstoneLevel, 15, coolingSpt);
            case SPT_WHEN_LOW -> GeneralUtil.mapValue(15 - redstoneLevel, 15, coolingSpt);
            default -> 0;
        };

        if (hasEnoughEnergy(energyUsage) && actualCooling != 0) {
            if (!useEnergy(energyUsage)) {
                actualCooling = 0;
                active = false;
            }

            if (!active && actualCooling != 0) {
                active = true;
                level.setBlockAndUpdate(worldPosition, setActive(getBlockState(), true));
            }
        } else if (active || actualCooling == 0) {
            active = false;
            level.setBlockAndUpdate(worldPosition, setActive(getBlockState(), false));
        }

        if (active) fluidStorage.fluids.forEach(fluid -> {
            fluid.temperature = Math.max(airTemp, fluid.temperature - actualCooling / 20);
        });

        if (fluidStorage.getStored() == 0) return;
        Direction absoluteOutputDir = getAbsoluteDirFromRelative(getBlockState(), Direction.EAST);
        DisplacementHandler.tryFeed(worldPosition, absoluteOutputDir, level, fluidStorage.fluids, 0.1);
    }

    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, LinkedList<Fluid> fluids) {
        if (active) fluids.forEach(fluid -> {
            fluid.temperature = Math.max(airTemp, fluid.temperature - actualCooling);
        });
        ChemicalReactionHandler.tryReact(fluids);

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
        nbt.putInt("chemcraft.setpoint", coolingSpt);
        super.saveToNBT(nbt);
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        fluidStorage.loadFromNBT(nbt);
        coolingSpt = nbt.getInt("chemcraft.setpoint"); //todo please dont forget to test this
        super.loadFromNBT(nbt);
    }
}
