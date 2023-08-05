package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.blockentities.base.BaseEnergyStorageBlockEntity;
import me.madmagic.chemcraft.instances.CustomBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MotorBlockEntity extends BaseEnergyStorageBlockEntity implements MenuProvider {

    public MotorBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntities.motor.get(), pos, state, 10000, 1000);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Motor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }

    public boolean hasEnoughPower(int flowSetting) {
        return flowSetting / 10 > energyStorage.getEnergyStored();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (side == Direction.UP) return lazyEnergyHandler.cast();
        return LazyOptional.empty();
    }

    @Override
    public void tick() {}
}