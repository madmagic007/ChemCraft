package me.madmagic.chemcraft.instances.items;

import me.madmagic.chemcraft.instances.items.base.BaseItem;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.fluids.MultiFluidStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class ProbeItem extends BaseItem {

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockEntity entity = pContext.getLevel().getBlockEntity(pContext.getClickedPos());
        if (entity == null) return InteractionResult.FAIL;
        if (pContext.getLevel().isClientSide) return InteractionResult.SUCCESS;
        MutableComponent msg = Component.empty()
                .append(entity.getBlockState().getBlock().getName().withStyle(ChatFormatting.GOLD));


        if (entity instanceof IFluidContainer fluidContainer) {
            List<MultiFluidStorage> storages = fluidContainer.getFluidStorages();

            if (storages.isEmpty()) msg.append(" contains no fluids.");
            else {
                msg.append(" contains the following:\n");

                GeneralUtil.forEachIndexed(storages, (storage, i) -> {
                    msg.append(Component.literal(String.valueOf(i)).withStyle(ChatFormatting.AQUA));
                    msg.append(": ");

                    storage.fluids.forEach(fluid -> {
                        msg.append(fluid.toString() + ", ");
                    });

                    msg.append("\n");
                });
            }
        } else msg.append(" is not a fluid container.");

        pContext.getPlayer().displayClientMessage(msg, false);
        return InteractionResult.PASS;
    }
}
