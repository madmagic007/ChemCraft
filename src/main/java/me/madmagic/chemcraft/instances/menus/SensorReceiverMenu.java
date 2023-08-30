package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.sensors.SensorReceiverBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseMenuScreen;
import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SensorReceiverMenu extends BaseMenu<SensorReceiverBlockEntity> {

    public SensorReceiverMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, getEnt(inv, extraData));
    }

    public SensorReceiverMenu(int id, BlockEntity ent) {
        super(id, CustomMenus.sensorReceiverMenu.get(), ent);
    }

    public static class Screen extends BaseMenuScreen<SensorReceiverMenu> {

        public Screen(SensorReceiverMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, ScreenHelper.noInv, 176, 86);
        }

        @Override
        protected void init() {
            super.init();

            ChemCraft.info("test");
            ChemCraft.info(menu.entity.sourcePos);
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            renderOnlyTitle(pGuiGraphics);
        }
    }
}
