package me.madmagic.chemcraft.instances.blockentities.base;

import me.madmagic.chemcraft.instances.menus.base.CustomItemSlotTemplate;
import me.madmagic.chemcraft.util.itemstorage.WrappedItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseEnergyItemStorageBlockEntity extends BaseEnergyStorageBlockEntity {

    private LazyOptional<IItemHandler> lazyItemHandler;
    protected final ItemStackHandler itemHandler;

    private LazyOptional<IItemHandler> lazyWrappedHandler;
    private final WrappedItemStackHandler wrappedHandler;

    public final List<CustomItemSlotTemplate> slotTemplates;

    public BaseEnergyItemStorageBlockEntity(BlockEntityType<?> pType, BlockPos pos, BlockState state, int energyCapacity, List<CustomItemSlotTemplate> slotTemplates) {
        super(pType, pos, state, energyCapacity);
        this.slotTemplates = slotTemplates;

        itemHandler = new ItemStackHandler(slotTemplates.size()) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                onItemContentsChanged(slot);
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return true;
                //return isItemValidForSlot(slot, stack);
            }
        };

        wrappedHandler = new WrappedItemStackHandler(itemHandler, slotTemplates);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyWrappedHandler = LazyOptional.of(() -> wrappedHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyWrappedHandler.invalidate();
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        super.loadFromNBT(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("chemcraft.inventory"));
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        nbt.put("chemcraft.inventory", itemHandler.serializeNBT());
        super.saveToNBT(nbt);
    }

    protected void onItemContentsChanged(int slot) {}

    protected boolean isItemValidForSlot(int slot, ItemStack stack) {
        return slotTemplates.size() >= slot && slotTemplates.get(slot).mayPlace(stack, true);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) return lazyItemHandler.cast();
            return lazyWrappedHandler.cast();
        }

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
