package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.menus.widgets.base.VerticalGradientWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerData;

import java.util.List;

public class ContainerDataVerticalGradientWidget extends VerticalGradientWidget<ContainerDataVerticalGradientWidget> {

    private final ContainerData data;
    private final String suffix, itemName;
    private final int valuePos;

    public ContainerDataVerticalGradientWidget(int x, int y, int width, int height, String itemName, String suffix, ContainerData data, int valueIndex, int maxValue, int colorFrom, int colorTo) {
        super(x, y, width, height, maxValue, colorFrom, colorTo);
        this.data = data;
        this.suffix = suffix;
        this.itemName = itemName;
        this.valuePos = valueIndex;
    }

    @Override
    public List<Component> getToolTips() {
        return List.of(
                Component.literal(itemName + ":").withStyle(ChatFormatting.GOLD),
                Component.literal(String.format("%s/%s %s", getValue(), max, suffix)).withStyle(ChatFormatting.YELLOW)
        );
    }

    @Override
    protected int getValue() {
        return data.get(valuePos);
    }
}
