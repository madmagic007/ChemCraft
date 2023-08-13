package me.madmagic.chemcraft.instances.menus.base;

import me.madmagic.chemcraft.instances.blockentities.base.BaseEnergyItemStorageBlockEntity;
import me.madmagic.chemcraft.util.GeneralUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public abstract class BaseMenu<T extends BlockEntity> extends AbstractContainerMenu {

    protected final T entity;
    private final Level level;
    private int slotCount;
    protected ContainerData data;

    public BaseMenu(int id, Inventory inventory, MenuType menu, BlockEntity ent, int slotCount, ContainerData data) {
        this(id, menu, ent);
        this.slotCount = slotCount;
        this.data = data;

        addPlayerInventory(inventory);
        addDataSlots(data);

        entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            GeneralUtil.forEachIndexed(((BaseEnergyItemStorageBlockEntity) entity).slotTemplates, (template, index) ->
                    addSlot(new CustomItemSlot(handler, template, index))
            );
        });
    }

    public BaseMenu(int id, MenuType menu, BlockEntity ent) {
        super(menu, id);
        entity = (T) ent;
        level = ent.getLevel();
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + slotCount, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + slotCount) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    //assuming progress = 1, maxProgress = 2
    protected boolean isCrafting() {
        return data != null && data.getCount() > 0 && data.get(1) > 0;
    }

    protected int getScaledProgress(int maxScale) {
        if (data == null) return 0;

        int progress = data.get(1);
        int maxProgress = data.get(2);

        return progress * maxScale / maxProgress + 1;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), pPlayer, entity.getBlockState().getBlock());
    }

    private void addPlayerInventory(Inventory playerInv) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        addPlayerHotBar(playerInv);
    }

    private void addPlayerHotBar(Inventory playerInv) {
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }

    protected static BlockEntity getEnt(Inventory inv, FriendlyByteBuf extraData) {
        return inv.player.level().getBlockEntity(extraData.readBlockPos());
    }
}
