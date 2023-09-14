package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.CentrifugalPumpBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseMenuScreen;
import me.madmagic.chemcraft.instances.menus.widgets.ButtonedEditBox;
import me.madmagic.chemcraft.instances.menus.widgets.CustomLabel;
import me.madmagic.chemcraft.instances.menus.widgets.ToolTippedItem;
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

public class CentrifugalPumpMenu extends BaseMenu<CentrifugalPumpBlockEntity> {

    public CentrifugalPumpMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, getEnt(inv, extraData), new SimpleContainerData(1));
    }

    public CentrifugalPumpMenu(int id, BlockEntity ent, ContainerData data) {
        super(id, CustomMenus.centrifugalPump.get(), ent, data);
    }

    public static class Screen extends BaseMenuScreen<CentrifugalPumpMenu> {

        private ToolTippedItem motorDisplay;

        public Screen(CentrifugalPumpMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, ScreenHelper.noInv, 176, 86);
        }

        @Override
        protected void init() {
            super.init();

            ButtonedEditBox editBox = new ButtonedEditBox(0, 0, CentrifugalPumpBlockEntity.maxFlowRate, 1000, screenHelper,
                    () -> menu.data.get(0),
                    newVal -> NetworkSender.sendToServer(new UpdateEntMessage(menu.entity.getBlockPos(), newVal))
            ).addTo(screenHelper).center(screenHelper);

            addRenderableWidget(
                    editBox.getEditBox()
                            .setTooltip("Flowrate (l/hr)")
                            .setMaxCharLength(5)
            );

            boolean motorDetected = menu.entity.hasMotor();
            motorDisplay = screenHelper.addItem(imageWidth - 20, titleLabelY - 2, CustomItems.blockItems.get("motor").get(), motorDetected ? "Motor Detected" : "Motor Not Detected");
            if (!motorDetected) motorDisplay.setOverLay(ScreenHelper.cross);


            new CustomLabel(0, editBox.getY() - screenHelper.y - 2, "Flowrate:")
                    .addTo(screenHelper)
                    .centerHorizontally(screenHelper);

            new CustomLabel(0, imageHeight - 13, "")
                    .lateCenter()
                    .setScale(.9f)
                    .setValue(() -> "Motor Power Consumption: " + menu.data.get(0) / CentrifugalPumpBlockEntity.powerUsageFactor + " FE/t")
                    .addTo(screenHelper)
                    .centerHorizontally(screenHelper);
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            renderOnlyTitle(pGuiGraphics);
        }

        @Override
        protected void containerTick() {
            if (menu.entity.hasMotor()) {
                motorDisplay.setToolTips("Motor Detected");
                motorDisplay.setOverLay(null);
            } else {
                motorDisplay.setToolTips("Motor Not Detected");
                motorDisplay.setOverLay(ScreenHelper.cross);
            }
        }
    }
}
