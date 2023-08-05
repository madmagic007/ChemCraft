package me.madmagic.chemcraft.util.networking;

import me.madmagic.chemcraft.instances.blockentities.CentrifugalPumpBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class UpdateCentrifugalPumpMessage {

    private final BlockPos pos;
    private final int value;

    public UpdateCentrifugalPumpMessage(BlockPos pos, int value) {
        this.pos = pos;
        this.value = value;
    }

    public UpdateCentrifugalPumpMessage(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        value = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(value);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            BlockEntity entity = context.getSender().level().getBlockEntity(pos);
            if (!(entity instanceof CentrifugalPumpBlockEntity ent)) return;

            ent.flowRate = value;
            ent.setChanged();
        });
        return true;
    }

    public static void define(SimpleChannel channel, int id) {
        channel.messageBuilder(UpdateCentrifugalPumpMessage.class, id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateCentrifugalPumpMessage::new)
                .encoder(UpdateCentrifugalPumpMessage::encode)
                .consumerMainThread(UpdateCentrifugalPumpMessage::handle)
                .add();
    }
}
