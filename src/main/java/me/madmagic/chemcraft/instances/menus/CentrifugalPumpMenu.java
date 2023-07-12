package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.blocks.entity.CentrifugalPumpBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.MenuHandler;
import me.madmagic.chemcraft.instances.menus.base.BaseScreen;
import me.madmagic.chemcraft.util.networking.NetworkSender;
import me.madmagic.chemcraft.util.networking.UpdateCentrifugalPumpMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CentrifugalPumpMenu extends BaseMenu<CentrifugalPumpBlockEntity> {

    public static final String name = CentrifugalPumpMenu.class.getSimpleName().toLowerCase();

    public CentrifugalPumpMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(id, inv, MenuHandler.centrifugalPumpMenu.get(), extraData);
    }

    public CentrifugalPumpMenu(int id, CentrifugalPumpBlockEntity ent) {
        super(id, MenuHandler.centrifugalPumpMenu.get(), ent);
    }

    public static class Screen extends BaseScreen<CentrifugalPumpMenu> {

        private static final ResourceLocation texture = defineTexture("base_small");
        private static final ResourceLocation buttonUp = defineTexture("button_up");
        private static final ResourceLocation buttonDown = defineTexture("button_down");

        public Screen(CentrifugalPumpMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, texture, 124, 73);
        }

        @Override
        protected void init() {
            super.init();

            int bX = x + (imageWidth / 2 -8);
            int bY1 = y + (imageHeight / 2 -18);
            int bY2 = y + imageHeight / 2 + 12;

            //todo send packages to chaneg pressure value
            addImageButton(bX, bY1, 16, 16, buttonUp, p -> {
                NetworkSender.sendToServer(new UpdateCentrifugalPumpMessage(menu.entity.getBlockPos(), getNewVal(true)));
            });

            addImageButton(bX, bY2, 16, 16, buttonDown, p -> {
                NetworkSender.sendToServer(new UpdateCentrifugalPumpMessage(menu.entity.getBlockPos(), getNewVal(false)));
            });
        }

        private int getNewVal(boolean increase) {
            int curVal = menu.entity.pressureSetting;
            if (increase && curVal < 16) curVal++;
            else if (!increase && curVal > 1) curVal--;

            menu.entity.pressureSetting = curVal;
            return curVal;
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
            int val = menu.entity.pressureSetting;
            pGuiGraphics.drawCenteredString(this.font,  val + " bar", imageWidth /2, imageHeight /2, 4210752);
        }
    }
}
