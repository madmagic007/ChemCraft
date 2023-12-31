package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.AirCoolerBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseMenuScreen;
import me.madmagic.chemcraft.instances.menus.widgets.ButtonedEditBox;
import me.madmagic.chemcraft.instances.menus.widgets.CustomLabel;
import me.madmagic.chemcraft.instances.menus.widgets.RedstoneModeWidget;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.networking.NetworkSender;
import me.madmagic.chemcraft.util.networking.UpdateEntMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AirCoolerMenu extends BaseMenu<AirCoolerBlockEntity> {

    public AirCoolerMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, getEnt(inv, extraData), new SimpleContainerData(2));
    }

    public AirCoolerMenu(int id, BlockEntity ent, ContainerData data) {
        super(id, CustomMenus.airCooler.get(), ent, data);
    }

    public static class Screen extends BaseMenuScreen<AirCoolerMenu> {

        public Screen(AirCoolerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, ScreenHelper.noInvPower, 176, 86);
        }

        @Override
        protected void init() {
            super.init();

            screenHelper.addContainerDataVerticalWidget(159, 17, 8, 52, "Energy", "FE", menu.data, 0, menu.entity.getEnergyStorage().getMaxEnergyStored(), 0xb51500, 0x600b00);

            ButtonedEditBox editBox = new ButtonedEditBox(0, 0, AirCoolerBlockEntity.maxCoolingSpt, 1, screenHelper,
                    () -> menu.data.get(1),
                    newVal -> NetworkSender.sendToServer(new UpdateEntMessage(menu.entity.getBlockPos(), newVal))
            ).addTo(screenHelper).center(screenHelper);

            addRenderableWidget(
                    editBox.getEditBox()
                            .setTooltip("Cooling (°C)")
                            .setMaxCharLength(2)
            );

            new CustomLabel(0, editBox.getY() - screenHelper.y - 2, "Cooling:")
                    .addTo(screenHelper)
                    .centerHorizontally(screenHelper);

            new CustomLabel(0, imageHeight - 14, "")
                    .lateCenter()
                    .setValue(() -> "Power Consumption: " + menu.data.get(1) * AirCoolerBlockEntity.powerFactor + " FE/t")
                    .addTo(screenHelper)
                    .centerHorizontally(screenHelper);

            new RedstoneModeWidget(4, imageHeight - 35, 16, menu.entity)
                    .addTo(screenHelper);
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            renderOnlyTitle(pGuiGraphics);
        }
    }
}
