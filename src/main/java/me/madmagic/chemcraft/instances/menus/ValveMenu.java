package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.ValveBlockEntity;
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

public class ValveMenu extends BaseMenu<ValveBlockEntity> {

    public ValveMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, getEnt(inv, extraData), new SimpleContainerData(1));
    }

    public ValveMenu(int id, BlockEntity ent, ContainerData data) {
        super(id, CustomMenus.valve.get(), ent, data);
    }

    public static class Screen extends BaseMenuScreen<ValveMenu> {

        public Screen(ValveMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, ScreenHelper.noInv, 176, 86);
        }

        @Override
        protected void init() {
            super.init();

            ButtonedEditBox editBox = new ButtonedEditBox(0, 0, ValveBlockEntity.maxSetting, 1000, screenHelper,
                    () -> menu.data.get(0),
                    newVal -> NetworkSender.sendToServer(new UpdateEntMessage(menu.entity.getBlockPos(), newVal))
            ).addTo(screenHelper).center(screenHelper);

            addRenderableWidget(
                    editBox.getEditBox()
                            .setTooltip("Max flow (l/hr)")
                            .setMaxCharLength(5)
            );

            new CustomLabel(0, editBox.getY() - screenHelper.y - 2, "Max flow:")
                    .addTo(screenHelper)
                    .centerHorizontally(screenHelper);

            new RedstoneModeWidget(4, imageHeight - 20,16, menu.entity)
                    .addTo(screenHelper);
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            renderOnlyTitle(pGuiGraphics);
        }
    }
}
