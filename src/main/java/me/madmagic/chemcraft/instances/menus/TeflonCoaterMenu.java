package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.TeflonCoaterBlockEntity;
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

public class TeflonCoaterMenu extends BaseMenu<TeflonCoaterBlockEntity> {

    public static final int maxItemCount = 640;
    public static final int countPerItem = 10;
    public static final int maxBoneCount = 16;

    public TeflonCoaterMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getEnt(inv, extraData), new SimpleContainerData(6));
    }

    public TeflonCoaterMenu(int id, Inventory inv, BlockEntity ent, ContainerData data) {
        super(id, inv, CustomMenus.teflonCoaterMenu.get(), ent, 5, data);
    }

    public static class Screen extends BaseScreen<TeflonCoaterMenu> {

        private static final ResourceLocation texture = defineTexture("teflon_coater");

        public Screen(TeflonCoaterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, texture, 175, 165);
        }

        @Override
        protected void init() {
            super.init();

            addContainerDataVerticalWidget(28, 17, 8, 52, "Fluorite", "", menu.data, 2, maxItemCount, 0x3a685a, 0x4fba98);
            addContainerDataVerticalWidget(62, 17, 8, 52, "Coal", "", menu.data, 3, maxItemCount, 0x111111, 0x1c1c1c);
            addContainerDataVerticalWidget(114, 17, 8, 52, "Calcium waste", "", menu.data, 4, maxBoneCount, 0xfcfbed, 0x7b7e6b);

            addContainerDataVerticalWidget(160, 17, 8, 52, "Energy", "FE", menu.data, 5, menu.entity.getEnergyStorage().getMaxEnergyStored(), 0xb51500, 0x600b00);
        }

        @Override
        protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
            super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);

            if (menu.isCrafting()) {
                int progress = menu.getScaledProgress(14);
                pGuiGraphics.blit(texture, x + 84, y + 36, 176, 0, 8, progress);
            }
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
            //todo add labels for fluorite/coal/bone?
        }
    }
}
