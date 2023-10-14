package me.madmagic.chemcraft.instances.blockentities.machines;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.blockentities.base.BaseEnergyItemStorageBlockEntity;
import me.madmagic.chemcraft.instances.menus.CatalystRefinerMenu;
import me.madmagic.chemcraft.instances.menus.base.CustomItemSlotTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalystRefinerBlockEntity extends BaseEnergyItemStorageBlockEntity implements MenuProvider {

    public static final Map<Item, Item> validCatalysts = new HashMap<>() {{
        put(Items.COAL_BLOCK, CustomItems.blockItems.get("activated_carbon_catalyst").get());
        put(Items.IRON_BLOCK, CustomItems.blockItems.get("iron_catalyst").get());
    }};

    private static final List<CustomItemSlotTemplate> slotTemplates = List.of(
            new CustomItemSlotTemplate(56, 35, validCatalysts.keySet().toArray(new Item[] {})),
            new CustomItemSlotTemplate(116, 35, true)
    );

    private int progress = 0;
    private int maxProgress = 150;
    private final int powerConsumptionPerTick = 15;

    public CatalystRefinerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.catalystRefiner.get(), pPos, pBlockState, 10000, slotTemplates);
    }

    @Override
    protected int getDataValue(int index) {
        return switch (index) {
            case 1 -> progress;
            case 2 -> maxProgress;
            default -> super.getDataValue(index);
        };
    }

    @Override
    protected void setDataValue(int index, int value) {
        switch (index) {
            case 1 -> progress = value;
            case 2 -> maxProgress = value;
            default -> super.setDataValue(index, value);
        }
    }

    @Override
    protected int getDataCount() {
        return 3;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Catalyst Refiner");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CatalystRefinerMenu(pContainerId, pPlayerInventory, this, containerData);
    }

    private boolean lastWasValid = false;
    @Override
    public void tick() {
        if (stateValid()) {
            lastWasValid = true;
            progress ++;
            useEnergy(powerConsumptionPerTick);

            if (progress >= maxProgress) coat();

            setChanged();
        } else if (lastWasValid) {
            lastWasValid = false;
            progress = 0;
            setChanged();
        }
    }

    private boolean stateValid() {
        boolean hasInput = false;

        for (Item item : validCatalysts.keySet()) {
            if (isItemInSlot(0, item)) hasInput = true;
        }

        boolean outputSpaceLeft = hasSpaceInSlot(1);
        boolean hasEnergy = hasEnoughEnergy(powerConsumptionPerTick);

        return hasInput && hasEnergy && outputSpaceLeft;
    }

    private void coat() {
        Item input = itemHandler.extractItem(0, 1, false).getItem();
        Item output = validCatalysts.get(input);
        itemHandler.insertItem(1, new ItemStack(output), false);
    }
}
