package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.MotorBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseMenuScreen;
import me.madmagic.chemcraft.instances.menus.widgets.CustomLabel;
import me.madmagic.chemcraft.instances.menus.widgets.RedstoneModeWidget;
import me.madmagic.chemcraft.instances.menus.widgets.ToolTippedItem;
import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

import static me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRedstoneMode.RedstoneMode.*;

public class MotorMenu extends BaseMenu<MotorBlockEntity> {

    public MotorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, getEnt(inv, extraData), new SimpleContainerData(1));
    }

    public MotorMenu(int id, BlockEntity ent, ContainerData data) {
        super(id, CustomMenus.motor.get(), ent, data);
    }

    public static class Screen extends BaseMenuScreen<MotorMenu> {

        private ToolTippedItem pumpDisplay;

        public Screen(MotorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, ScreenHelper.noInvPower, 176, 86);
        }

        @Override
        protected void init() {
            super.init();
            screenHelper.addContainerDataVerticalWidget(159, 17, 8, 52, "Energy", "FE", menu.data, 0, menu.entity.getEnergyStorage().getMaxEnergyStored(), 0xb51500, 0x600b00);

            boolean pumpDetected = menu.entity.hasPump();
            pumpDisplay = screenHelper.addItem(4, imageHeight - 20, CustomItems.blockItems.get("centrifugal_pump").get(), pumpDetected ? "Pump Detected" : "Pump Not Detected");
            if (!pumpDetected) pumpDisplay.setOverLay(ScreenHelper.cross);

            new CustomLabel(titleLabelX, titleLabelY + 33, "Uses 100 FE/").setScale(.8f).addTo(screenHelper);

            new RedstoneModeWidget(pumpDisplay.getX() - screenHelper.x + 20, pumpDisplay.getY() - screenHelper.y,
                    16, menu.entity, List.of(IGNORED, WHEN_HIGH, WHEN_LOW))
                    .addTo(screenHelper);
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            renderOnlyTitle(pGuiGraphics);
        }

        @Override
        protected void containerTick() {
            if (menu.entity.hasPump()) {
                pumpDisplay.setToolTips("Pump Detected");
                pumpDisplay.setOverLay(null);
            } else {
                pumpDisplay.setToolTips("Pump Not Detected");
                pumpDisplay.setOverLay(ScreenHelper.cross);
            }
        }
    }
}
