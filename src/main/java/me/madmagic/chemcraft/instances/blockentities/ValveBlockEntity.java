package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IHasRedstonePowerLevel;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRedstoneMode;
import me.madmagic.chemcraft.instances.menus.ValveMenu;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.fluids.DisplacementHandler;
import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.fluids.MultiFluidStorage;
import me.madmagic.chemcraft.util.networking.INetworkUpdateAble;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedList;

public class ValveBlockEntity extends BaseBlockEntity implements IFluidContainer, INetworkUpdateAble, MenuProvider, IRedstoneMode, IHasRedstonePowerLevel {

    public static final int maxSetting = 50000;
    public int flowSetting = maxSetting;

    private final ContainerData data;

    public ValveBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.valve.get(), pPos, pBlockState);

        data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return flowSetting;
            }

            @Override
            public void set(int pIndex, int pValue) {
                flowSetting = pValue;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Valve");
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ValveMenu(pContainerId, this, data);
    }


    @Override
    public void loadFromNBT(CompoundTag nbt) {
        flowSetting = nbt.getInt("chemcraft.flowSetting");
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        nbt.putInt("chemcraft.flowSetting", flowSetting);
    }

    @Override
    public void updateFromNetworking(int... values) {
        flowSetting = values[0];
    }

    private double getConvertedFactor() {
        RedstoneMode mode = getRedstoneMode(getBlockState());
        int redstoneLevel = getRedstoneLevel(getBlockState());

        double actualFlowSetting  = switch (mode.matchesRedstoneSignalIgnoringSPT(redstoneLevel) ? RedstoneMode.IGNORED : mode) {
            case IGNORED -> flowSetting;
            case SPT_WHEN_HIGH -> GeneralUtil.mapValue(redstoneLevel, 15, flowSetting);
            case SPT_WHEN_LOW -> GeneralUtil.mapValue(15 - redstoneLevel, 15, flowSetting);
            default -> 0;
        };

        return actualFlowSetting * DisplacementHandler.tickFactor;
    }

    private Direction toFeedToDir;

    @Override
    public void tick() {
        double factor = getConvertedFactor();
        storage.capacity = factor;

        if (toFeedToDir == null) return;
        DisplacementHandler.tryFeed(worldPosition, toFeedToDir, level, storage.fluids, factor);
        if (storage.getStored() == 0) toFeedToDir = null;
    }

    private final MultiFluidStorage storage = new MultiFluidStorage(1);

    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, LinkedList<Fluid> fluids) {
        storage.add(fluids);
        toFeedToDir = pipeDir.getOpposite();
    }

    @Override
    public double extract(BlockPos pipePos, Direction pipeDir, double amount, LinkedList<Fluid> extractTo) {
       return storage.extract(amount, extractTo);
    }

    @Override
    public MultiFluidStorage getFluidStorage(BlockPos pipePos, Direction pipeDir) {
        return storage;
    }
}
