package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.menus.widgets.base.VerticalGradientWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerData;

import java.util.List;

public class ContainerDataVerticalGradientWidget extends VerticalGradientWidget {

    private final ContainerData itemStorage;
    private final int valuePos;
    private final String itemName, suffix;

    public ContainerDataVerticalGradientWidget(int x, int y, int width, int height, String itemName, String suffix, ContainerData itemStorage, int valueIndex, int maxValue, int colorFrom, int colorTo) {
        super(x, y, width, height, maxValue, colorFrom, colorTo);
        this.itemStorage = itemStorage;
        this.valuePos = valueIndex;
        this.itemName = itemName;
        this.suffix = suffix;
    }

    @Override
    protected int getValue() {
        return itemStorage.get(valuePos);
    }

    @Override
    public List<Component> getTooltips() {
        return List.of(
                Component.literal(itemName + ":").withStyle(ChatFormatting.GOLD),
                Component.literal(String.format("%s/%s %s", getValue(), max, suffix)).withStyle(ChatFormatting.YELLOW)
        );
    }
}
