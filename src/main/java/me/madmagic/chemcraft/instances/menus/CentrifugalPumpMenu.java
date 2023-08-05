package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.CentrifugalPumpBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseScreen;
import me.madmagic.chemcraft.instances.menus.widgets.ToolTippedItem;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.networking.NetworkSender;
import me.madmagic.chemcraft.util.networking.UpdateCentrifugalPumpMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SimpleContainerData;

public class CentrifugalPumpMenu extends BaseMenu<CentrifugalPumpBlockEntity> {

    public CentrifugalPumpMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(id, inv, CustomMenus.centrifugalPumpMenu.get(), extraData, 0);
    }

    public CentrifugalPumpMenu(int id, CentrifugalPumpBlockEntity ent) {
        super(id, CustomMenus.centrifugalPumpMenu.get(), ent, 0, new SimpleContainerData(0));
    }

    public static class Screen extends BaseScreen<CentrifugalPumpMenu> {

        private static final ResourceLocation texture = defineTexture("no_inv");

        private EditBox flowBox;
        private ToolTippedItem motorDisplay;

        public Screen(CentrifugalPumpMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, texture, 175, 85);
        }

        private int flowBoxY;

        @Override
        protected void init() {
            super.init();

            int flowBoxWidth = 41;
            int flowBoxHeight = 12;

            int flowBoxX = x + imageWidth / 2 - flowBoxWidth / 2;
            flowBoxY = y + imageHeight / 2 - flowBoxHeight / 2;

            int bY = flowBoxY + flowBoxHeight + 3;

            addImageButton(flowBoxX + 2, bY, 16, 16, buttonUp, p -> {
                NetworkSender.sendToServer(new UpdateCentrifugalPumpMessage(menu.entity.getBlockPos(), getNewFlowVal(true)));
            }, "Add 10");

            addImageButton(flowBoxX + 23, bY, 16, 16, buttonDown, p -> {
                NetworkSender.sendToServer(new UpdateCentrifugalPumpMessage(menu.entity.getBlockPos(), getNewFlowVal(false)));
            }, "Subtract 10");

            flowBox = addEditorBox(flowBoxX, flowBoxY, flowBoxWidth, flowBoxHeight, "Flowrate (mb/tick)");
            flowBox.setValue(String.valueOf(menu.entity.flowRate));
            flowBox.setMaxLength(4);

            boolean motorDetected = menu.entity.hasMotor();
            motorDisplay = addItem(x + imageWidth - 20, y + titleLabelY - 2, CustomItems.blockItems.get("motor").get(), motorDetected ? "Motor Detected" : "Motor Not Detected");
            if (!motorDetected) motorDisplay.setOverLay(cross);
        }

        private static final int buttonVal = 10;
        private int getNewFlowVal(boolean increase) {
            int val = menu.entity.flowRate;

            if (increase) val += buttonVal;
            else val -= buttonVal;

            val = clamp(val);

            menu.entity.flowRate = val;
            return val;
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            renderOnlyTitle(pGuiGraphics);

            String flowStr = "Flowrate:";
            int flowStrWidth = font.width(flowStr);
            int flowTextX = imageWidth / 2 - flowStrWidth / 2;

            pGuiGraphics.drawString(font, flowStr, flowTextX, flowBoxY - y - 12, 0x545454, false);

            String powerUsageStr = "Motor Power Consumption: " + menu.entity.flowRate + " FE";
            int powerUsageWidth = font.width(powerUsageStr);
            int powerTextX = imageWidth / 2 - powerUsageWidth / 2;

            pGuiGraphics.drawString(font, powerUsageStr, powerTextX, imageHeight - 12, 0x545454, false);
        }

        @Override
        protected void containerTick() {
            super.containerTick();

            int flowVal = menu.entity.flowRate;
            if (!flowBox.isFocused()) flowBox.setValue(String.valueOf(flowVal));

            if (menu.entity.hasMotor()) {
                motorDisplay.setToolTip("Motor Detected");
                motorDisplay.setOverLay(null);
            } else {
                motorDisplay.setToolTip("Motor Not Detected");
                motorDisplay.setOverLay(cross);
            }
        }

        @Override
        public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
            if (flowBox.isFocused()) {
                ChemCraft.info(pKeyCode);
                if (GeneralUtil.isAny(pKeyCode, 257, 335, 256)) {
                    flowBox.setFocused(false);

                    int val;

                    try {
                        val = clamp(Integer.parseInt(flowBox.getValue()));
                    } catch (Exception ignored) {
                        val = menu.entity.flowRate;
                    }

                    menu.entity.flowRate = val;
                    NetworkSender.sendToServer(new UpdateCentrifugalPumpMessage(menu.entity.getBlockPos(), val));
                    return false;
                }

                return flowBox.keyPressed(pKeyCode, pScanCode, pModifiers);
            }
            return super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }

        @Override
        public boolean charTyped(char pCodePoint, int pModifiers) {
            if (flowBox.isFocused() && !Character.isDigit(pCodePoint)) return true;
            return super.charTyped(pCodePoint, pModifiers);
        }

        private static int clamp(int desiredVal) {
            return Math.max(0, Math.min(CentrifugalPumpBlockEntity.maxFlowRate, desiredVal));
        }
    }
}
