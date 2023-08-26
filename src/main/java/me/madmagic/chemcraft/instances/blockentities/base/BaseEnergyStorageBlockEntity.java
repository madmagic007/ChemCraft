package me.madmagic.chemcraft.instances.blockentities.base;

import me.madmagic.chemcraft.util.energy.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public abstract class BaseEnergyStorageBlockEntity extends BaseBlockEntity {

    protected LazyOptional<IEnergyStorage> lazyEnergyHandler;
    protected final CustomEnergyStorage energyStorage;

    public BaseEnergyStorageBlockEntity(BlockEntityType<?> pType, BlockPos pos, BlockState state, int energyCapacity) {
        this(pType, pos, state, energyCapacity, 100);
    }

    public BaseEnergyStorageBlockEntity(BlockEntityType<?> pType, BlockPos pos, BlockState state, int energyCapacity, int energyTransferRate) {
        super(pType, pos, state);

        energyStorage = new CustomEnergyStorage(energyCapacity, energyTransferRate) {
            @Override
            public void energyChanged(int energy) {
                BaseEnergyStorageBlockEntity.this.energyChanged(energy);
            }
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected int getDataValue(int index) {
        if (index == 0) return energyStorage.getEnergyStored();
        return 0;
    }

    @Override
    protected void setDataValue(int index, int value) {
        if (index == 1) energyStorage.setEnergyStored(value);
    }

    @Override
    protected int getDataCount() {
        return 1;
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        energyStorage.setEnergyStored(nbt.getInt("chemcraft.energy"));
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        nbt.putInt("chemcraft.energy", energyStorage.getEnergyStored());
    }

    public boolean hasEnoughEnergy(int wanted) {
        return energyStorage.getEnergyStored() > wanted;
    }

    public void useEnergy(int wanted) {
        energyStorage.extractEnergy(wanted, false);
    }

    protected void energyChanged(int energy) {}

    public CustomEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap.equals(ForgeCapabilities.ENERGY)) return lazyEnergyHandler.cast();

        return super.getCapability(cap, side);
    }
}
