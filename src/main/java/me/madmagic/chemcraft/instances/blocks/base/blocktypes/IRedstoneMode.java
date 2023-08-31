package me.madmagic.chemcraft.instances.blocks.base.blocktypes;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public interface IRedstoneMode {

    EnumProperty<RedstoneMode> mode = EnumProperty.create("redstone_mode", RedstoneMode.class);

    default void addModeState(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(mode);
    }

    default RedstoneMode getRedstoneMode(BlockState state) {
        return state.getValue(mode);
    }

    default BlockState setMode(BlockState state, RedstoneMode mode) {
        return state.setValue(this.mode, mode);
    }

    enum RedstoneMode implements StringRepresentable {
        IGNORED("ignored", "Redstone Ignored"),
        WHEN_HIGH("when_high", "Active when redstone high"),
        WHEN_LOW("when_low", "Active when redstone low"),
        SPT_WHEN_HIGH("spt_when_high", "Setpoint when redstone level 15"),
        SPT_WHEN_LOW("spt_when_low", "Setpoint when redstone level 0");

        private final String name;
        public final String toolTip;

        RedstoneMode(String name, String toolTip) {
            this.name = name;
            this.toolTip = toolTip;
        }

        public String toString() {
            return this.getSerializedName();
        }

        public String getSerializedName() {
            return this.name;
        }

        public boolean isIgnored() {
            return this.equals(IGNORED);
        }

        public boolean isWhenHigh() {
            return this.equals(WHEN_HIGH);
        }

        public boolean isWhenLow() {
            return this.equals(WHEN_LOW);
        }

        public boolean isSptWhenLow() {
            return this.equals(SPT_WHEN_LOW);
        }

        public boolean isSptWhenHigh() {
            return this.equals(SPT_WHEN_HIGH);
        }

        public boolean matchesRedstoneSignalIgnoringSPT(int signal) {
            return isWhenLow() && signal == 0 ||
                    isWhenHigh() && signal == 15
                    || isIgnored();
        }

        public boolean matchesRedstoneSignal(int signal) {
             return matchesRedstoneSignalIgnoringSPT(signal) ||
                isSptWhenHigh() && signal != 0 ||
                    isSptWhenLow() && signal != 15;
        }
    }
}
