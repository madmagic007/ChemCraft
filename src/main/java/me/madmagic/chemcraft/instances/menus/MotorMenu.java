package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.MotorBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MotorMenu extends BaseMenu<MotorBlockEntity> {

    public MotorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, getEnt(inv, extraData), new SimpleContainerData(1));
    }

    public MotorMenu(int id, BlockEntity ent, ContainerData data) {
        super(id, CustomMenus.motorMenu.get(), ent);
        this.data = data;
        addDataSlots(data);
    }

    public static class Screen extends BaseScreen<MotorMenu> {

        private static final ResourceLocation texture = defineTexture("no_inv_power");

        public Screen(MotorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, texture, 175, 85);
        }

        @Override
        protected void init() {
            super.init();
            addContainerDataVerticalWidget(160, 17, 8, 52, "Energy", "FE", menu.data, 0, menu.entity.getEnergyStorage().getMaxEnergyStored(), 0xb51500, 0x600b00);
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            renderOnlyTitle(pGuiGraphics);
        }
    }
}
