package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.CentrifugalPumpBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseMenuScreen;
import me.madmagic.chemcraft.instances.menus.widgets.CustomLabel;
import me.madmagic.chemcraft.instances.menus.widgets.ToolTippedEditBox;
import me.madmagic.chemcraft.instances.menus.widgets.ToolTippedItem;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.networking.NetworkSender;
import me.madmagic.chemcraft.util.networking.UpdateCentrifugalPumpMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CentrifugalPumpMenu extends BaseMenu<CentrifugalPumpBlockEntity> {

    public CentrifugalPumpMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, getEnt(inv, extraData), new SimpleContainerData(1));
    }

    public CentrifugalPumpMenu(int id, BlockEntity ent, ContainerData data) {
        super(id, CustomMenus.centrifugalPumpMenu.get(), ent, data);
    }

    public static class Screen extends BaseMenuScreen<CentrifugalPumpMenu> {

        private static final ResourceLocation texture = ScreenHelper.getTexture("no_inv");

        private EditBox flowBox;
        private ToolTippedItem motorDisplay;
        private static final int buttonVal = 1000;

        public Screen(CentrifugalPumpMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, texture, 176, 86);
        }

        @Override
        protected void init() {
            super.init();

            int flowBoxWidth = 41;
            int flowBoxHeight = 12;
            int halfWidth = imageWidth / 2;
            int halfHeight = imageHeight / 2;

            int flowBoxX = halfWidth - flowBoxWidth / 2;
            int flowBoxY = halfHeight - flowBoxHeight / 2;

            int bY = flowBoxY + flowBoxHeight + 3;

            screenHelper.addImageButton(flowBoxX + 2, bY, 16, 16, ScreenHelper.buttonUp, () -> {
                NetworkSender.sendToServer(new UpdateCentrifugalPumpMessage(menu.entity.getBlockPos(), getNewFlowVal(true)));
            }, "Add 10");

            screenHelper.addImageButton(flowBoxX + 23, bY, 16, 16, ScreenHelper.buttonDown, () -> {
                NetworkSender.sendToServer(new UpdateCentrifugalPumpMessage(menu.entity.getBlockPos(), getNewFlowVal(false)));
            }, "Subtract 10");

            flowBox = screenHelper.addEditorBox(flowBoxX, flowBoxY, flowBoxWidth, flowBoxHeight, "Flowrate (l/hr)")
                            .setValue(() -> menu.data.get(0));
            flowBox.setMaxLength(5);
            addRenderableWidget(flowBox); //ugh

            boolean motorDetected = menu.entity.hasMotor();
            motorDisplay = screenHelper.addItem(imageWidth - 20, titleLabelY - 2, CustomItems.blockItems.get("motor").get(), motorDetected ? "Motor Detected" : "Motor Not Detected");
            if (!motorDetected) motorDisplay.setOverLay(ScreenHelper.cross);

            new CustomLabel(halfWidth, flowBoxY - 12, "Flowrate:").center().addTo(screenHelper);
            new CustomLabel(halfWidth, imageHeight - 12, "")
                    .setScale(.9f)
                    .lateCenter()
                    .setValue(() -> "Motor Power Consumption: " + menu.data.get(0) / CentrifugalPumpBlockEntity.powerUsageFactor + " FE/t")
                    .addTo(screenHelper);
        }

        private int getNewFlowVal(boolean increase) {
            int val = menu.data.get(0);

            if (increase) val += buttonVal;
            else val -= buttonVal;

            return clamp(val);
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

        @Override
        public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
            if (flowBox.isFocused()) {
                boolean toReturn = flowBox.keyPressed(pKeyCode, pScanCode, pModifiers);
                if (GeneralUtil.isAny(pKeyCode, 257, 335, 256)) {
                    flowBox.setFocused(false);

                    int val = menu.data.get(0);

                    try {
                        val = clamp(Integer.parseInt(flowBox.getValue()));
                    } catch (Exception ignored) {}

                    NetworkSender.sendToServer(new UpdateCentrifugalPumpMessage(menu.entity.getBlockPos(), val));
                    return false;
                }

                return toReturn;
            }
            return super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }

        @Override
        public boolean charTyped(char pCodePoint, int pModifiers) {
            ChemCraft.info(getFocused() instanceof ToolTippedEditBox);
            if (flowBox.isFocused() && !Character.isDigit(pCodePoint)) return true;
            return super.charTyped(pCodePoint, pModifiers);
        }

        private static int clamp(int desiredVal) {
            return Math.max(0, Math.min(CentrifugalPumpBlockEntity.maxFlowRate, desiredVal));
        }
    }
}
