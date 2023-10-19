package me.madmagic.chemcraft.util.reloaders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.madmagic.chemcraft.util.fluids.FluidHandler;
import me.madmagic.chemcraft.util.fluids.FluidType;
import net.minecraft.util.StringRepresentable;

import java.util.Map;

public class FluidRegisterer extends BasePreparableReloadListener<Map<String, FluidType>> {

    private static final StringRepresentable.EnumCodec<FluidType.SolubilityType> solventTypeCodec = StringRepresentable.fromEnum(FluidType.SolubilityType::values);
    public static final Codec<FluidType> fluidTypeCodec = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("boilingPoint", Integer.MAX_VALUE).forGetter(FluidType::boilingPoint),
            solventTypeCodec.optionalFieldOf("solubilityType", FluidType.SolubilityType.WATER).forGetter(FluidType::solubilityType)
    ).apply(instance, FluidType::new));

    private static final Codec<Map<String, FluidType>> codec = Codec.unboundedMap(Codec.STRING, fluidTypeCodec);

    public FluidRegisterer() {
        super("chemistry/products", codec);
    }

    @Override
    protected void removeAllElements() {
        FluidHandler.fluidTypes.clear();
    }

    @Override
    protected void registerElement(Map<String, FluidType> element) {
        FluidHandler.fluidTypes.putAll(element);
    }
}
