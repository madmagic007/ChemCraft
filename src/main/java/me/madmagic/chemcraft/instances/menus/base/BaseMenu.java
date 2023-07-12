package me.madmagic.chemcraft.instances.menus.base;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BaseMenu<T extends BlockEntity> extends AbstractContainerMenu {

    public final T entity;
    private final Level level;

    public BaseMenu(int id, Inventory inv, MenuType menuType, FriendlyByteBuf extraData) {
        this(id, menuType, (T) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public BaseMenu(int id, MenuType menu, T ent) {
        super(menu, id);
        entity = ent;
        level = ent.getLevel();
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), pPlayer, entity.getBlockState().getBlock());
    }
}
