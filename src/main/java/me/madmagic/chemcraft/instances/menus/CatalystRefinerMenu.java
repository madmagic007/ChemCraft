package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.machines.CatalystRefinerBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseMenuScreen;
import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CatalystRefinerMenu extends BaseMenu<CatalystRefinerBlockEntity> {

    public CatalystRefinerMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getEnt(inv, extraData), new SimpleContainerData(3));
    }

    public CatalystRefinerMenu(int id, Inventory inv, BlockEntity ent, ContainerData data) {
        super(id, inv, CustomMenus.catalystRefiner.get(), ent, 2, data);
    }

    public static class Screen extends BaseMenuScreen<CatalystRefinerMenu> {

        private static final ResourceLocation texture = ScreenHelper.getTexture("catalyst_refiner");

        public Screen(CatalystRefinerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, texture, 176, 166);
        }

        @Override
        protected void init() {
            super.init();

            screenHelper.addContainerDataVerticalWidget(159, 17, 8, 52, "Energy", "FE", menu.data, 0, menu.entity.getEnergyStorage().getMaxEnergyStored(), 0xb51500, 0x600b00);
        }

        @Override
        protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
            super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);

            if (menu.isCrafting()) {
                int progress = menu.getScaledProgress(22);
                pGuiGraphics.blit(texture, leftPos + 80, topPos + 35, 176, 0, progress, 16);
            }
        }
    }
}
