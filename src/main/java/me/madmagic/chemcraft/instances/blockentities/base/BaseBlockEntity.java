package me.madmagic.chemcraft.instances.blockentities.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseBlockEntity extends BlockEntity {

    protected ContainerData containerData;

    public BaseBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);

        containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return getDataValue(pIndex);
            }

            @Override
            public void set(int pIndex, int pValue) {
                setDataValue(pIndex, pValue);
            }

            @Override
            public int getCount() {
                return getDataCount();
            }
        };
    }

    protected int getDataValue(int index) {
        return 0;
    }

    protected void setDataValue(int index, int value) {}

    protected int getDataCount() {
        return 0;
    }


    public abstract void loadFromNBT(CompoundTag nbt);
    public abstract void saveToNBT(CompoundTag nbt);

    @Override
    public void load(CompoundTag nbt) {
        loadFromNBT(nbt);
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        saveToNBT(nbt);
        super.saveAdditional(nbt);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveToNBT(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        loadFromNBT(nbt);
        super.handleUpdateTag(nbt);
    }

    public abstract void tick();
}
