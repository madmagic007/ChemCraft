package me.madmagic.chemcraft.instances.menus.widgets;

import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRedstoneMode;
import me.madmagic.chemcraft.instances.menus.widgets.base.CustomWidget;
import me.madmagic.chemcraft.util.ScreenHelper;
import me.madmagic.chemcraft.util.networking.NetworkSender;
import me.madmagic.chemcraft.util.networking.SetRedstoneModeMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class RedstoneModeWidget extends CustomWidget<RedstoneModeWidget> {


    private IRedstoneMode.RedstoneMode mode;
    private List<IRedstoneMode.RedstoneMode> allowedModes = List.of(IRedstoneMode.RedstoneMode.values());
    private final BlockEntity entity;

    public RedstoneModeWidget(int x, int y, int width, IRedstoneMode entity, IRedstoneMode.RedstoneMode defaultMode) {
        super(x, y, width, 16, ScreenHelper.buttonBlank);
        this.entity = (BlockEntity) entity;
        mode = defaultMode;
        setToolTips(mode.toolTip);
    }

    public RedstoneModeWidget(int x, int y, int width, IRedstoneMode entity, IRedstoneMode.RedstoneMode defaultMode, List<IRedstoneMode.RedstoneMode> allowedModes) {
        this(x, y, width, entity, defaultMode);
        this.allowedModes = allowedModes;
    }

    @Override
    public boolean onClicked() {
        if (!isHovered) return false;

        int newIndex = allowedModes.indexOf(mode) + 1;
        if (newIndex == allowedModes.size()) newIndex = 0;

        mode = allowedModes.get(newIndex);
        NetworkSender.sendToServer(new SetRedstoneModeMessage(entity.getBlockPos(), mode));

        toolTips.clear();
        setToolTips(mode.toolTip);

        return true;
    }

    private static final ResourceLocation barrier = ScreenHelper.getMinecraftItemTexture("barrier");
    private static final ResourceLocation whenHigh = ScreenHelper.getMinecraftBlockTexture("redstone_torch");
    private static final ResourceLocation whenLow = ScreenHelper.getMinecraftBlockTexture("redstone_torch_off");
    private static final ResourceLocation sptWhenHigh = ScreenHelper.getTexture("spt_when_high");
    private static final ResourceLocation sptWhenLow = ScreenHelper.getTexture("spt_when_low");

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.blit(
                switch (mode) {
                    case WHEN_HIGH -> whenHigh;
                    case WHEN_LOW -> whenLow;
                    case SPT_WHEN_HIGH -> sptWhenHigh;
                    case SPT_WHEN_LOW -> sptWhenLow;
                    default -> barrier;
                }, x + 3, y + 3, 0, 0, 0, width - 6, height - 6, width - 6, height - 6);
    }
}