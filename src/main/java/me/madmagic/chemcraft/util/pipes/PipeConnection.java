package me.madmagic.chemcraft.util.pipes;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Blocks;

public enum PipeConnection implements StringRepresentable {

    NONE("none"),
    DISCONNECTED("disconnected"),
    CONNECTED("connected");

    private final String name;

    PipeConnection(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        return this.name;
    }

    public boolean isConnected() {
        return this.equals(CONNECTED);
    }

    public boolean isDisconnected() {
        return this.equals(DISCONNECTED);
    }
}
