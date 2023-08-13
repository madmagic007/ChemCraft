package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseEnergyStorageBlockEntity;
import me.madmagic.chemcraft.instances.menus.AirCoolerMenu;
import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.fluids.MultiFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AirCoolerBlockEntity extends BaseEnergyStorageBlockEntity implements MenuProvider, IFluidContainer {

    public AirCoolerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.airCooler.get(), pPos, pBlockState, 10000);
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

    private final MultiFluidStorage fluidStorage = new MultiFluidStorage(1000) {};

    @Override
    public void tick() {

    }

    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, List<Fluid> fluids, double amount) {
        fluidStorage.add(fluids, amount);
    }

    @Override
    public double extract(BlockPos pipePos, Direction pipeDir, double amount, List<Fluid> extractTo) {
        return fluidStorage.extract(amount, extractTo);
    }

    @Override
    public MultiFluidStorage getFluidStorage() {
        return fluidStorage;
    }
}
