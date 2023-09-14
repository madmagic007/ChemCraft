package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.sensors.BaseSensorBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseMenuScreen;
import me.madmagic.chemcraft.instances.menus.widgets.CustomLabel;
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

public class SensorMenu extends BaseMenu<BaseSensorBlockEntity> {

    public SensorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, getEnt(inv, extraData), new SimpleContainerData(2));
    }

    public SensorMenu(int id, BlockEntity ent, ContainerData data) {
        super(id, CustomMenus.sensor.get(), ent, data);
    }

    public static class Screen extends BaseMenuScreen<SensorMenu> {

        public Screen(SensorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, ScreenHelper.noInv, 176, 86);
        }

        @Override
        protected void init() {
            super.init();

            CustomLabel label1 = screenHelper.addString(titleLabelX, screenHelper.halfImageHeight - 20,
                    String.format("Emit redstone signal 0 at %s.: ", menu.entity.getLabelToolTip()), 1f);

            CustomLabel label2 = screenHelper.addString(titleLabelX, screenHelper.halfImageHeight + 12,
                    String.format("Emit redstone signal 15 at %s.: ", menu.entity.getLabelToolTip()), 1f);

            addRenderableWidget(
                    screenHelper.createEditorBox(screenHelper.imageWidth - 30,
                                    label1.getY() + label1.height - screenHelper.y + 12,
                                    40, 12)
                            .setMaxCharLength(3)
                            .setOnValueChanged(val -> NetworkSender.sendToServer(new UpdateEntMessage(menu.entity.getBlockPos(), val, menu.data.get(1))))
                            .setValue(() -> menu.data.get(0))
                            .setTooltip(String.format("%s for redstone signal 0", menu.entity.getEditToolTip()))
                            .centerHorizontally(screenHelper)
            );

            addRenderableWidget(
                    screenHelper.createEditorBox(screenHelper.imageWidth - 30,
                                    label2.getY() + label2.height - screenHelper.y + 12,
                                    40, 12)
                            .setMaxCharLength(3)
                            .setOnValueChanged(val -> NetworkSender.sendToServer(new UpdateEntMessage(menu.entity.getBlockPos(), menu.data.get(0), val)))
                            .setValue(() -> menu.data.get(1))
                            .setTooltip(String.format("%s for redstone signal 15", menu.entity.getEditToolTip()))
                            .centerHorizontally(screenHelper)
            );
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            renderOnlyTitle(pGuiGraphics);
        }
    }
}
