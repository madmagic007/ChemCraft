package me.madmagic.chemcraft.instances.menus;

import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.blockentities.sensors.BaseSensorBlockEntity;
import me.madmagic.chemcraft.instances.blockentities.sensors.SensorReceiverBlockEntity;
import me.madmagic.chemcraft.instances.menus.base.BaseMenu;
import me.madmagic.chemcraft.instances.menus.base.BaseMenuScreen;
import me.madmagic.chemcraft.instances.menus.widgets.ToolTippedItem;
import me.madmagic.chemcraft.util.ScreenHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.stream.Collectors;

public class SensorReceiverMenu extends BaseMenu<SensorReceiverBlockEntity> {

    public SensorReceiverMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, getEnt(inv, extraData));
    }

    public SensorReceiverMenu(int id, BlockEntity ent) {
        super(id, CustomMenus.sensorReceiver.get(), ent);
    }

    public static class Screen extends BaseMenuScreen<SensorReceiverMenu> {

        public Screen(SensorReceiverMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle, ScreenHelper.noInv, 176, 86);
        }

        private ToolTippedItem sourceSensorWidget;
        private List<String> sensorItemNames;
        private String curItemName = "";

        @Override
        protected void init() {
            super.init();

            sensorItemNames = CustomItems.blockItems.keySet().stream().filter(str -> str.contains("sensor")).collect(Collectors.toList());
            curItemName = sensorItemNames.get(0);

            sourceSensorWidget = new ToolTippedItem(0, 0, Items.AIR, "")
                    .addTo(screenHelper)
                    .setScale(2)
                    .center(screenHelper);

            containerTick();
        }

        @Override
        protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            renderOnlyTitle(pGuiGraphics);
        }

        int delayTicks = 40;
        int curTick = 0;

        @Override
        protected void containerTick() {
            if (getSensorEnt() instanceof BaseSensorBlockEntity sensor) {
                sourceSensorWidget.setItem(sensor.getBlockState().getBlock().asItem());
                sourceSensorWidget.setOverLay(null);

                BlockPos pos = sensor.getBlockPos();
                sourceSensorWidget.setToolTips("Source: " + sensor.getDisplayName().getString(), String.format("(%s, %s, %s)", pos.getX(), pos.getY(), pos.getZ()));
                return;
            }

            sourceSensorWidget.setOverLay(ScreenHelper.cross);
            sourceSensorWidget.setToolTips("No source sensor set");

            curTick++;
            if (curTick < delayTicks && !sourceSensorWidget.isItem(Items.AIR)) return;
            curTick = 0;

            int newIndex = sensorItemNames.indexOf(curItemName) + 1;
            if (newIndex == sensorItemNames.size()) newIndex = 0;
            curItemName = sensorItemNames.get(newIndex);

            Item item = CustomItems.blockItems.get(curItemName).get();
            sourceSensorWidget.setItem(item);
        }

        private BlockEntity getSensorEnt() {
            if (menu.entity.sourcePos == null) return null;
            return menu.entity.getLevel().getBlockEntity(menu.entity.sourcePos);
        }
    }
}
