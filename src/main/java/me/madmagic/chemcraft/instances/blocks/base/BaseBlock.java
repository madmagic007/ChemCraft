package me.madmagic.chemcraft.instances.blocks.base;

import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.instances.blockentities.base.BaseEnergyItemStorageBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class BaseBlock extends Block {

    private Map<Direction, VoxelShape> shapes = new HashMap<>();
    private VoxelShape shape = Shapes.block();

    public BaseBlock(Properties pProperties) {
        super(pProperties);

        ifActivateAble(a -> registerDefaultState(a.setActive(defaultBlockState(),false)));
        ifRedstonePowerAble(p -> registerDefaultState(p.setPowered(defaultBlockState(), false)));
        ifHasRedstonePowerLevel(r -> registerDefaultState(r.setPowerLevel(defaultBlockState(), 0)));
        ifMultiBlockComponent(m -> registerDefaultState(m.setIsMultiBlock(defaultBlockState(), false)));
    }

    public BaseBlock(Properties pProperties, VoxelShape shape) {
        this(pProperties);
        this.shape = shape;
    }

    public BaseBlock(Properties pProperties, Map<Direction, VoxelShape> shapes) {
        this(pProperties);
        this.shapes = shapes;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity entity = pLevel.getBlockEntity(pPos);
        if (!(entity instanceof MenuProvider ent))
            return InteractionResult.FAIL;
        if (pLevel.isClientSide)
            return InteractionResult.SUCCESS;

        NetworkHooks.openScreen((ServerPlayer) pPlayer, ent, pPos);

        return InteractionResult.CONSUME;
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        try { //first forced upon, useless try catch I wrote, achievement unlocked
            BlockEntity ent = pParams.getParameter(LootContextParams.BLOCK_ENTITY);
            if (!(ent instanceof BaseBlockEntity blockEnt)) return super.getDrops(pState, pParams);

            ItemStack stack = new ItemStack(this);

            CompoundTag nbt = new CompoundTag();
            blockEnt.saveToNBT(nbt);
            nbt.remove("chemcraft.inventory");
            stack.addTagElement("BlockEntityTag", nbt);

            if (blockEnt instanceof BaseEnergyItemStorageBlockEntity itemEnt) itemEnt.dropContents();

            return List.of(stack);
        } catch (Exception ignored) {
            return super.getDrops(pState, pParams);
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        AtomicReference<VoxelShape> toReturn = new AtomicReference<>(shape);

        ifRotateAble(r ->
                toReturn.set(shapes.getOrDefault(r.getFacing(pState), shape))
        );

        return toReturn.get();
    }

    //region shared
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        ifRotateAble(r -> r.addRotateableState(pBuilder));
        ifRedstonePowerAble(p -> p.addPowerAbleState(pBuilder));
        ifHasRedstonePowerLevel(r -> r.addRedstonePowerAbleState(pBuilder));
        ifActivateAble(a -> a.addActivateAbleState(pBuilder));
        ifRedstoneMode(r -> r.addModeState(pBuilder));
        ifMultiBlockComponent(m -> m.addMultiblockState(pBuilder));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        AtomicReference<BlockState> state = new AtomicReference<>(this.defaultBlockState());

        ifRotateAble(r ->
                state.set(state.get().setValue(IRotateAble.facing, pContext.getHorizontalDirection()))
        );

        return state.get();
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        ifRedstonePowerAble(r -> {
            boolean hasNeighborSignal = pLevel.hasNeighborSignal(pPos);
            if (r.isPowered(pState) != hasNeighborSignal)
                pLevel.setBlockAndUpdate(pPos, r.setPowered(pState, hasNeighborSignal));
        });

        ifHasRedstonePowerLevel(r -> {
            int neighborLevel = pLevel.getBestNeighborSignal(pPos);
            if (r.getRedstoneLevel(pState) != neighborLevel)
                pLevel.setBlockAndUpdate(pPos, r.setPowerLevel(pState, neighborLevel));
        });

        
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
    }

    //endregion

    //region rotateable
    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        AtomicReference<BlockState> toReturn = new AtomicReference<>(super.rotate(state, level, pos, direction));

        ifRotateAble(r ->
            toReturn.set(state.setValue(IRotateAble.facing, direction.rotate(r.getFacing(state))))
        );

        return toReturn.get();
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        AtomicReference<BlockState> toReturn = new AtomicReference<>(super.mirror(state, mirror));

        ifRotateAble(r ->
                toReturn.set(state.rotate(mirror.getRotation(r.getFacing(state))))
        );

        return toReturn.get();
    }

    private void ifRotateAble(Consumer<IRotateAble> consumer) {
        if (this instanceof IRotateAble rotateAble) consumer.accept(rotateAble);
    }
    //endregion

    private void ifActivateAble(Consumer<IActivateAble> consumer) {
        if (this instanceof IActivateAble a) consumer.accept(a);
    }

    private void ifRedstonePowerAble(Consumer<IRedstonePowerAble> consumer) {
        if (this instanceof IRedstonePowerAble powerAble) consumer.accept(powerAble);
    }

    private void ifHasRedstonePowerLevel(Consumer<IHasRedstonePowerLevel> consumer) {
        if (this instanceof IHasRedstonePowerLevel powerAble) consumer.accept(powerAble);
    }

    private void ifRedstoneMode(Consumer<IRedstoneMode> consumer) {
        if (this instanceof IRedstoneMode r) consumer.accept(r);
    }

    private void ifMultiBlockComponent(Consumer<IMultiBlockComponent> consumer) {
        if (this instanceof IMultiBlockComponent m) consumer.accept(m);
    }
}
