package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.CentrifugalPumpBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseScreen;
import me.madmagic.chemcraft.util.networking.NetworkSender;
import me.madmagic.chemcraft.util.networking.UpdateCentrifugalPumpMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CentrifugalPumpMenu extends BaseMenu<CentrifugalPumpBlockEntity> {

    public CentrifugalPumpMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(id, inv, CustomMenus.centrifugalPumpMenu.get(), extraData, 0);
    }

    public CentrifugalPumpMenu(int id, CentrifugalPumpBlockEntity ent) {
        super(id, CustomMenus.centrifugalPumpMenu.get(), ent, 0);
    }

    public static class Screen extends BaseScreen<CentrifugalPumpMenu> {

        private static final ResourceLocation texture = defineTexture("no_inv");

        private EditBox flowBox;

        public Screen(CentrifugalPumpMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, texture, 175, 85);
        }

        @Override
        protected void init() {
            super.init();

            int startX = x + titleLabelX;

            int flowBoxHeight = 12;
            int flowBoxY = y + imageHeight / 2 - flowBoxHeight / 2;

            int bY = flowBoxY + flowBoxHeight + 3;

            addImageButton(startX + 2, bY, 16, 16, buttonUp, p -> {
                NetworkSender.sendToServer(new UpdateCentrifugalPumpMessage(menu.entity.getBlockPos(), getNewFlowVal(true)));
            }, "Add 50");

            addImageButton(startX + 23, bY, 16, 16, buttonDown, p -> {
                NetworkSender.sendToServer(new UpdateCentrifugalPumpMessage(menu.entity.getBlockPos(), getNewFlowVal(false)));
            }, "Subtract 50");

            flowBox = addEditorBox(startX, flowBoxY, 41, flowBoxHeight, "Flowrate (mb/tick)");
            flowBox.setMaxLength(4);
        }

        private int getNewFlowVal(boolean increase) {
            int curVal = menu.entity.flowRate;
            if (increase && curVal < 1000) curVal += 50;
            else if (!increase && curVal > 0) curVal -= 50;

            if (curVal > 1000) curVal = 1000;
            if (curVal < 0) curVal = 0;

            menu.entity.flowRate = curVal;
            return curVal;
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            renderOnlyTitle(pGuiGraphics);
        }

        @Override
        protected void containerTick() {
            super.containerTick();

            int flowVal = menu.entity.flowRate;
            if (!flowBox.isFocused()) flowBox.setValue((flowVal + "").trim());
        }

        @Override
        public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
            if (flowBox.isFocused()) {
                if (pKeyCode == 257) {
                    flowBox.setFocused(false);

                    int val = menu.entity.flowRate;

                    try {
                        val = Integer.parseInt(flowBox.getValue());
                    } catch (Exception ignored) {}

                    if (val > 1000) val = 1000;
                    if (val < 0) val = 0;

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
    }
}
