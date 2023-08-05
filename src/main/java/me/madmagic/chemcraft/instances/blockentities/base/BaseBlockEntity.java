package me.madmagic.chemcraft.instances.blockentities.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseBlockEntity extends BlockEntity {

    public BaseBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public abstract void loadFromNBT(CompoundTag nbt);
    public abstract void saveToNbt(CompoundTag nbt);

    @Override
    public void load(CompoundTag nbt) {
        loadFromNBT(nbt);
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        saveToNbt(nbt);
        super.saveAdditional(nbt);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveToNbt(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        loadFromNBT(nbt);
        super.handleUpdateTag(nbt);
    }

    public abstract void tick();
}
