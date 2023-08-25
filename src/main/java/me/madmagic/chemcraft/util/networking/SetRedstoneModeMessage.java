package me.madmagic.chemcraft.util.networking;

import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRedstoneMode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class SetRedstoneModeMessage {

    private final BlockPos pos;
    private final IRedstoneMode.RedstoneMode mode;

    public SetRedstoneModeMessage(BlockPos pos, IRedstoneMode.RedstoneMode mode) {
        this.pos = pos;
        this.mode = mode;
    }

    public SetRedstoneModeMessage(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        mode = buf.readEnum(IRedstoneMode.RedstoneMode.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeEnum(mode);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Level level = context.getSender().level();
            BlockEntity entity = level.getBlockEntity(pos);
            if (!(entity instanceof IRedstoneMode ent)) return;

            BlockState state = ent.setMode(level.getBlockState(pos), mode);
            level.setBlockAndUpdate(pos, state);
        });
        return true;
    }

    public static void define(SimpleChannel channel, int id) {
        channel.messageBuilder(SetRedstoneModeMessage.class, id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetRedstoneModeMessage::new)
                .encoder(SetRedstoneModeMessage::encode)
                .consumerMainThread(SetRedstoneModeMessage::handle)
                .add();
    }
}
