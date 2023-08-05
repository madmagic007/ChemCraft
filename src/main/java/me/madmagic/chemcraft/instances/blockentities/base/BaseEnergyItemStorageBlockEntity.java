package me.madmagic.chemcraft.instances.blockentities.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public abstract class BaseEnergyItemStorageBlockEntity extends BaseEnergyStorageBlockEntity {

    protected LazyOptional<IItemHandler> lazyItemHandler;
    protected final ItemStackHandler itemHandler;

    public BaseEnergyItemStorageBlockEntity(BlockEntityType<?> pType, BlockPos pos, BlockState state, int energyCapacity, int slotCount) {
        super(pType, pos, state, energyCapacity);

        itemHandler = new ItemStackHandler(slotCount) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                BaseEnergyItemStorageBlockEntity.this.onContentsChanged(slot);
            }
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        super.loadFromNBT(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("chemcraft.inventory"));
    }

    @Override
    public void saveToNbt(CompoundTag nbt) {
        nbt.put("chemcrft.inventory", itemHandler.serializeNBT());
        super.saveToNbt(nbt);
    }

    protected void onContentsChanged(int slot) {}

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();

        return super.getCapability(cap, side);
    }

    public void dropContents() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(level, worldPosition, inv);
    }

    protected boolean isItemInSlot(int slot, Item item) {
        return item.equals(itemHandler.getStackInSlot(slot).getItem());
    }

    protected boolean hasSpaceInSlot(int slot) {
        return itemHandler.getStackInSlot(slot).getCount() < 64;
    }
}
