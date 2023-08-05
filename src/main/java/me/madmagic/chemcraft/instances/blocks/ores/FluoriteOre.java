package me.madmagic.chemcraft.instances.blocks.ores;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FluoriteOre extends DropExperienceBlock {

    public static final String blockName = "fluorite_ore";

    public FluoriteOre() {
        super(
                BlockBehaviour.Properties.copy(Blocks.COPPER_ORE),
                UniformInt.of(4, 7)
                );
    }
}
