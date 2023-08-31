package me.madmagic.chemcraft.util.multiblock;

import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IMultiBlockComponent;
import me.madmagic.chemcraft.util.ConnectionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public abstract class MultiBlockStructure {

    public final BlockPos masterPos;
    public final Level level;
    public Set<BlockPos> allBlocks = new HashSet<>();
    public TreeMap<Integer, Set<BlockPos>> blocks = new TreeMap<>();

    public MultiBlockStructure(BlockPos masterPos, Level level) {
        this.level = level;
        this.masterPos = masterPos;
    }

    public void add(BlockPos pos) {
        allBlocks.add(pos);
        blocks.computeIfAbsent(pos.getY(), key -> new HashSet<>()).add(pos);
    }

    @SafeVarargs
    public final void addAllConnectedOfType(Class<? extends Block>... targetBlocks) {
        ConnectionHandler.getConnectedWhereType(masterPos, level, targetBlocks).forEach(this::add);
    }

    public boolean contains(BlockPos pos, BlockState state) {
        return allBlocks.contains(pos) && level.getBlockState(pos).equals(state);
    }

    public boolean contains(BlockPos pos, Level level) {
        return allBlocks.contains(pos) && this.level.equals(level);
    }

    public void created(boolean isExisting) {
        allBlocks.forEach(pos -> {
            BlockState state = level.getBlockState(pos);

            if (!(state.getBlock() instanceof IMultiBlockComponent component)) return;
            state = component.setIsMultiBlock(state, true);

            level.setBlockAndUpdate(pos, state);
        });
    }

    public void destroyed(boolean unload) {
        if (!unload) level.removeBlockEntity(masterPos);

        allBlocks.forEach(pos -> {
            BlockState state = level.getBlockState(pos);

            if (!(state.getBlock() instanceof IMultiBlockComponent component)) return;
            state = component.setIsMultiBlock(state, false);

            level.setBlockAndUpdate(pos, state);
        });
    }

    public BlockEntity getBlockEntity() {
        BlockState state = level.getBlockState(masterPos);
        if (!state.hasProperty(IMultiBlockComponent.property)) return null;
        return level.getBlockEntity(masterPos);
    }

    protected AABB dimAtLevel(int level) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (BlockPos pos : blocks.get(level)) {
            int x = pos.getX();
            int z = pos.getZ();

            if (x < minX) {
                minX = x;
            }
            if (x > maxX) {
                maxX = x;
            }
            if (z < minZ) {
                minZ = z;
            }
            if (z > maxZ) {
                maxZ = z;
            }
        }

        return new AABB(minX, level, minZ, maxX, level, maxZ);
    }

    protected static boolean isSquare(AABB dim, boolean checkHeight) {
        double width = dim.getXsize();
        double length = dim.getZsize();
        double height = dim.getYsize();

        if (checkHeight) return width == length && width == height;
        return width == length;
    }

    protected static boolean isSame(AABB base, AABB level) {
        AABB editedLevel = new AABB(level.minX, base.minY, level.minZ, level.maxX, base.maxY, level.maxZ);
        return base.equals(editedLevel);
    }

    @SafeVarargs
    protected final boolean isFilled(AABB dim, Class<? extends Block>... validBlocks) {
        List<BlockState> originalDim = level.getBlockStates(dim).toList();

        List<BlockState> collected = originalDim.stream().filter(state -> {
            for (Class<? extends Block> validBlock : validBlocks) {
                if (ConnectionHandler.isStateOfType(state, validBlock)) return true;
            }
            return false;
        }).toList();
        return collected.size() == originalDim.size();
    }

    protected boolean isFilled(AABB dim) {
        List<BlockState> originalDim = level.getBlockStates(dim).toList();

        List<BlockState> collected = originalDim.stream().filter(state -> !ConnectionHandler.isStateOfType(state, AirBlock.class)).toList();
        return collected.size() == originalDim.size();
    }

    @SafeVarargs
    protected final boolean isCenterFilled(AABB dim, Class<? extends Block>... validBlocks) {
        return isFilled(dim.deflate(1, 0, 1), validBlocks);
    }

    public abstract boolean validate();

    public boolean check(boolean isExisting) {
        if (validate()) {
            MultiBlockHandler.add(this, isExisting);
            return true;
        }

        return false;
    }
}
