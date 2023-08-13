package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.AirCoolerBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseMenuScreen;
import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AirCoolerMenu extends BaseMenu<AirCoolerBlockEntity> {

    public AirCoolerMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, getEnt(inv, extraData), new SimpleContainerData(1));
    }

    public AirCoolerMenu(int id, BlockEntity ent, ContainerData data) {
        super(id, CustomMenus.airCoolerMenu.get(), ent);
        this.data = data;
        addDataSlots(data);
    }

    public static class Screen extends BaseMenuScreen<AirCoolerMenu> {

        public Screen(AirCoolerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, ScreenHelper.noInvPower, 176, 86);
        }

        @Override
        protected void init() { super.init();
            screenHelper.addContainerDataVerticalWidget(159, 17, 8, 52, "Energy", "FE", menu.data, 0, menu.entity.getEnergyStorage().getMaxEnergyStored(), 0xb51500, 0x600b00);
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            renderOnlyTitle(pGuiGraphics);
        }
    }
}
