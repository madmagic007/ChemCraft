package me.madmagic.chemcraft.instances.blocks.entity;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.blocks.entity.base.BaseBlockEntity;
import me.madmagic.chemcraft.instances.blocks.entity.base.BlockEntityHandler;
import me.madmagic.chemcraft.util.energy.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MotorBlockEntity extends BaseBlockEntity implements MenuProvider {

    public static final String name = "motorblockentity";

    private LazyOptional<IEnergyStorage> lazyEnergyHandler;

    public MotorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityHandler.motorBlockEntity.get(), pos, state);
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
    public void loadFromNBT(CompoundTag nbt) {
        energyStorage.setEnergyStored(nbt.getInt("motor.energy"));
    }

    @Override
    public void saveToNbt(CompoundTag nbt) {
        nbt.putInt("motor.energy", energyStorage.getEnergyStored());
    }

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(100000, 1000) {
        @Override
        public void energyChanged(int energy) {
        }
    };

    public boolean hasEnoughPower(int pressureSetting) {
        return pressureSetting * 1000 > energyStorage.getEnergyStored();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap.equals(ForgeCapabilities.ENERGY)) return lazyEnergyHandler.cast();

        ChemCraft.info("Cap called");

        return super.getCapability(cap, side);
    }

    public static <E extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, E e) {
        if (level.isClientSide) return;
    }
}
