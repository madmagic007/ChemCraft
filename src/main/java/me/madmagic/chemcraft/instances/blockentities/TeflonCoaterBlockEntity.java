package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.blockentities.base.BaseEnergyItemStorageBlockEntity;
import me.madmagic.chemcraft.instances.menus.TeflonCoaterMenu;
import me.madmagic.chemcraft.instances.menus.base.CustomItemSlotTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeflonCoaterBlockEntity extends BaseEnergyItemStorageBlockEntity implements MenuProvider {

    private static final List<CustomItemSlotTemplate> slotTemplates = List.of(
            new CustomItemSlotTemplate(8, 17, CustomItems.fluorite.get()),
            new CustomItemSlotTemplate(42, 17, Items.COAL, Items.COAL_BLOCK),
            new CustomItemSlotTemplate(80, 17, Items.IRON_INGOT, Items.IRON_BLOCK),
            new CustomItemSlotTemplate(80, 53, true),
            new CustomItemSlotTemplate(126, 53, true)
    );

    private int progress = 0;
    private final int defaultMaxProgress = 150;
    private int maxProgress = defaultMaxProgress;
    private final int powerConsumptionPerTick = 15;
    private int fluoriteCount = 0;
    private int coalCount = 0;
    private int boneCount = 0;

    public TeflonCoaterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.fluoriteCoater.get(), pPos, pBlockState, 10000, slotTemplates);

        containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    case 2 -> fluoriteCount;
                    case 3 -> coalCount;
                    case 4 -> boneCount;
                    case 5 -> energyStorage.getEnergyStored();
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> progress = pValue;
                    case 1 -> maxProgress = pValue;
                    case 2 -> fluoriteCount = pValue;
                    case 3 -> coalCount = pValue;
                    case 4 -> boneCount = pValue;
                    case 5 -> energyStorage.setEnergyStored(pValue);
                };
            }

            @Override
            public int getCount() {
                return 6;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Teflon Coater");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new TeflonCoaterMenu(pContainerId, pPlayerInventory, this, containerData);
    }

    @Override
    public void saveToNbt(CompoundTag nbt) {
        nbt.putInt("chemcraft.fluoriteCount", fluoriteCount);
        nbt.putInt("chemcraft.coalCount", coalCount);
        nbt.putInt("chemcraft.boneCount", boneCount);
        super.saveToNbt(nbt);
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        super.loadFromNBT(nbt);
        fluoriteCount = nbt.getInt("chemcraft.fluoriteCount");
        coalCount = nbt.getInt("chemcraft.coalCount");
        boneCount = nbt.getInt("chemcraft.boneCount");
    }

    private boolean lastWasValid = false;
    @Override
    public void tick() {
        checkItemStorageSlots();
        if (stateValid()) {
            lastWasValid = true;
            progress ++;
            useEnergy(powerConsumptionPerTick);
            setChanged();

            if (progress >= maxProgress) coat();
        } else if (lastWasValid) {
            lastWasValid = false;
            progress = 0;
            setChanged();
        }
    }

    /*
    0: fluorite
    1: coal
    2: toCoat (ingot/iron block)
    3: result (coated ingot/block)
    4: bone
     */

    private final Item coatedBlockItem = CustomItems.blockItems.get("teflon_coated_iron_block").get();
    private boolean stateValid() {
        boolean inputIsIngot = isItemInSlot(2, Items.IRON_INGOT);
        boolean inputIsBlock = isItemInSlot(2, Items.IRON_BLOCK);

        boolean outputIsIngot = isItemInSlot(3, CustomItems.teflonCoatedIronIngot.get());
        boolean outputIsBlock = isItemInSlot(3, coatedBlockItem);
        boolean outputEmpty = !(outputIsIngot || outputIsBlock);

        boolean canProcessIngot = inputIsIngot && (outputIsIngot || outputEmpty);
        boolean canProcessBlock = inputIsBlock && (outputIsBlock || outputEmpty);

        boolean outputSpaceLeft = hasSpaceInSlot(3);
        boolean hasEnergy = hasEnoughEnergy(powerConsumptionPerTick);

        boolean boneSpaceLeft = hasBoneProductSpaceLeft(inputIsIngot);

        boolean hasFluorite = fluoriteCount > (inputIsBlock ? 9 : 0);
        boolean hasCoal = coalCount > (inputIsBlock ? 9 : 0);

        return boneSpaceLeft && hasFluorite && hasCoal && outputSpaceLeft && hasEnergy && (canProcessIngot || canProcessBlock);
    }

    private void coat() {
        boolean isIngot = itemHandler.extractItem(2, 1, false).getItem().equals(Items.IRON_INGOT);

        Item toSet = isIngot ? CustomItems.teflonCoatedIronIngot.get() : coatedBlockItem;
        itemHandler.insertItem(3, new ItemStack(toSet), false);

        boneCount += isIngot ? 1 : 9;
        if (boneCount >= TeflonCoaterMenu.maxBoneCount && hasSpaceInSlot(4)) {
            boneCount -= TeflonCoaterMenu.maxBoneCount;
            itemHandler.insertItem(4, new ItemStack(Items.BONE), false);
        }

        coalCount -= isIngot ? 1 : 9;
        fluoriteCount -= isIngot ? 1 : 9;

        progress = 0;
        setChanged();
    }

    private void checkItemStorageSlots() {
        boolean change = false;

        int factor = (isItemInSlot(2, Items.IRON_BLOCK) ? 5 : 1);
        maxProgress = defaultMaxProgress * factor;

        if (isItemInSlot(0, CustomItems.fluorite.get()) && hasItemProductSpaceLeft(fluoriteCount, true)) {
            fluoriteCount += TeflonCoaterMenu.countPerItem;
            itemHandler.extractItem(0, 1, false);
            change = true;
        }

        if (isItemInSlot(1, Items.COAL) && hasItemProductSpaceLeft(coalCount, true)) {
            coalCount += TeflonCoaterMenu.countPerItem;
            itemHandler.extractItem(1, 1, false);
            change = true;
        }

        if (isItemInSlot(1, Items.COAL_BLOCK) && hasItemProductSpaceLeft(coalCount, false)) {
            coalCount += 9 * TeflonCoaterMenu.countPerItem;
            itemHandler.extractItem(1, 1, false);
            change = true;
        }

        if (change) setChanged();
    }

    private boolean hasItemProductSpaceLeft(int count, boolean isIngot) {
        return count + (isIngot ? 1 : 9) * TeflonCoaterMenu.countPerItem <= TeflonCoaterMenu.maxItemCount;
    }

    int maxTotalBoneCount = 65 * TeflonCoaterMenu.maxBoneCount;
    private boolean hasBoneProductSpaceLeft(boolean isIngot) {
        int curTotalBoneCount = itemHandler.getStackInSlot(4).getCount() * TeflonCoaterMenu.maxBoneCount + boneCount;
        return curTotalBoneCount + (isIngot ? 1 : 9) <= maxTotalBoneCount;
    }
}
