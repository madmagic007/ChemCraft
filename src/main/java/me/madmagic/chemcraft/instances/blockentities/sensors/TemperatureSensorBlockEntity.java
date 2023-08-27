package me.madmagic.chemcraft.instances.blockentities.sensors;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.menus.SensorMenu;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TemperatureSensorBlockEntity extends BaseSensorBlockEntity {

    public TemperatureSensorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.temperatureSensor.get(), pPos, pBlockState);
    }

    @Override
    public String getLabelToolTip() {
        return "temp";
    }

    @Override
    public String getEditToolTip() {
        return "Temperature";
    }

    @Override
    protected double getValueForSignalCalculation(IFluidContainer container) {
        return container.getFluidStorage().temperature;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Temperature sensor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new SensorMenu(pContainerId, this, containerData);
    }
}
