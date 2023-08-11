package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.TeflonCoaterBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseMenuScreen;
import me.madmagic.chemcraft.instances.menus.widgets.ToolTippedItem;
import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TeflonCoaterMenu extends BaseMenu<TeflonCoaterBlockEntity> {

    public static final int maxItemCount = 100;
    public static final int countPerItem = 2;
    public static final int usagePerIngot = 3;
    public static final int maxBoneCount = 16;

    public TeflonCoaterMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getEnt(inv, extraData), new SimpleContainerData(6));
    }

    public TeflonCoaterMenu(int id, Inventory inv, BlockEntity ent, ContainerData data) {
        super(id, inv, CustomMenus.teflonCoaterMenu.get(), ent, 5, data);
    }

    public static class Screen extends BaseMenuScreen<TeflonCoaterMenu> {

        private static final ResourceLocation texture = ScreenHelper.getTexture("teflon_coater");
        private ToolTippedItem coalDisplay;

        public Screen(TeflonCoaterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, texture, 176, 166);
        }

        @Override
        protected void init() {
            super.init();

            screenHelper.addContainerDataVerticalWidget(29, 17, 8, 52, "Fluorite", "", menu.data, 2, maxItemCount, 0x3a685a, 0x4fba98);
            screenHelper.addContainerDataVerticalWidget(64, 17, 8, 52, "Coal", "", menu.data, 3, maxItemCount, 0x111111, 0x1c1c1c);
            screenHelper.addContainerDataVerticalWidget(114, 17, 8, 52, "Calcium waste", "", menu.data, 4, maxBoneCount, 0xfcfbed, 0x7b7e6b);

            screenHelper.addContainerDataVerticalWidget(159, 17, 8, 52, "Energy", "FE", menu.data, 5, menu.entity.getEnergyStorage().getMaxEnergyStored(), 0xb51500, 0x600b00);

            screenHelper.addItem(9, 35, CustomItems.fluorite.get(), "Accepts: Fluorite");
            coalDisplay = screenHelper.addItem(44, 35, Items.COAL, "Accepts: Coal / Block of Coal");
        }

        @Override
        protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
            super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);

            if (menu.isCrafting()) {
                int progress = menu.getScaledProgress(14);
                pGuiGraphics.blit(texture, topPos + 84, leftPos + 36, 176, 0, 8, progress);
            }
        }

        int elapsedTicks = 0;

        @Override
        protected void containerTick() {
            if (elapsedTicks > 40) {
                if (coalDisplay.isItem(Items.COAL)) coalDisplay.setItem(Items.COAL_BLOCK);
                else coalDisplay.setItem(Items.COAL);

                elapsedTicks = 0;
            }
            elapsedTicks ++;
        }
    }
}
