package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseItemStorageBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IActivateAble;
import me.madmagic.chemcraft.instances.menus.FurnaceHeaterMenu;
import me.madmagic.chemcraft.instances.menus.base.CustomItemSlotTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FurnaceHeaterBlockEntity extends BaseItemStorageBlockEntity implements MenuProvider, IActivateAble {

    public int currentTick = 0;
    public int burnTimeForItem = 0;

    private static final List<CustomItemSlotTemplate> slotTemplates = List.of(
            new CustomItemSlotTemplate(80, 38, stack -> getBurnTime(stack) > 0)
    );

    public FurnaceHeaterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.furnaceHeater.get(), pPos, pBlockState, slotTemplates);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new FurnaceHeaterMenu(pContainerId, pPlayerInventory, this, containerData);
    }

    public static int getBurnTime(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
    }

    @Override
    public void tick() {
        if (burnTimeForItem > 0) {
            if (currentTick <= burnTimeForItem) currentTick++;
            else {
                burnTimeForItem = 0;
                currentTick = 0;

                level.setBlockAndUpdate(worldPosition, setActive(getBlockState(), false));
            }
            setChanged();
        } else {
            ItemStack stack = itemHandler.getStackInSlot(0);
            if (stack.isEmpty()) return;

            int burnTime = getBurnTime(stack);
            if (burnTime < 0) return;

            burnTimeForItem = burnTime;
            itemHandler.extractItem(0, 1, false);

            ;
            level.setBlockAndUpdate(worldPosition, setActive(getBlockState(), true));
            setChanged();
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Furnace Heater");
    }

    @Override
    protected int getDataCount() {
        return 3;
    }

    @Override
    protected void setDataValue(int index, int value) {
        if (index == 1) currentTick = value;
        else burnTimeForItem = value;
    }

    @Override
    protected int getDataValue(int index) {
        return index == 1 ? currentTick : burnTimeForItem;
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        super.saveToNBT(nbt);

        nbt.putInt("chemcraft.current_tick", currentTick);
        nbt.putInt("chemcraft.time_for_item", burnTimeForItem);
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        super.loadFromNBT(nbt);

        currentTick = nbt.getInt("chemcraft.current_tick");
        burnTimeForItem = nbt.getInt("chemcraft.time_for_item");
    }
}
