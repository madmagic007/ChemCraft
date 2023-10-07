package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.FurnaceHeaterBlockEntity;
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

public class FurnaceHeaterMenu extends BaseMenu<FurnaceHeaterBlockEntity> {

    public FurnaceHeaterMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getEnt(inv, extraData), new SimpleContainerData(3));
    }

    public FurnaceHeaterMenu(int id, Inventory inv, BlockEntity ent, ContainerData data) {
        super(id, inv, CustomMenus.furnaceHeater.get(), ent, 1, data);
    }

    public static class Screen extends BaseMenuScreen<FurnaceHeaterMenu> {

        private static final ResourceLocation texture = ScreenHelper.getTexture("furnace_heater");

        public Screen(FurnaceHeaterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, texture, 176, 166);
        }

        @Override
        protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
            super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);

            if (menu.isCrafting()) {
                int progress = menu.getScaledProgress(14);
                pGuiGraphics.blit(texture, leftPos + 80, topPos + 21 + progress, 176, progress, 14, 14);
            }
        }
    }
}
