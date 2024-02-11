package net.iung.simplySnow;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.SnowLayerBlock.LAYERS;

public class SnowOperator {
    private static final boolean REPLACE_REPLACEABLE_PLANTS = true;

    //可以传入已经获取的BlockState以不必重复获取
    public static boolean increaseSnowLayer(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState) {
        Main.LOGGER.info("increaseSnowLayer");
        if (blockPos.getY() >= serverLevel.getMinBuildHeight() && blockPos.getY() < serverLevel.getMaxBuildHeight()) {
            //在有效的建筑范围内
        } else {
            Main.LOGGER.info("out of range");
            return false;
        }

        if (!Blocks.SNOW.defaultBlockState().canSurvive(serverLevel, blockPos)) {//这一格根本不该有雪，所以什么都不干
            Main.LOGGER.info("no survive at {}",blockPos);
            return false;
        }
        Main.LOGGER.info("survive at {}",blockPos);

        if (blockState.isAir() || (blockState.getTags().toList().contains(BlockTags.REPLACEABLE) && !blockState.is(Blocks.SNOW))) {//可以直接放置顶层雪
            if (blockState.getTags().toList().contains(BlockTags.REPLACEABLE) && !blockState.is(Blocks.SNOW) && (!REPLACE_REPLACEABLE_PLANTS)) {//不能覆盖植物
                Main.LOGGER.info("replace failed");
                return false;
            }
            Main.LOGGER.info("new snow block");
            serverLevel.setBlockAndUpdate(blockPos, Blocks.SNOW.defaultBlockState());

            return true;
        }
        if (!blockState.is(Blocks.SNOW)) {//为其它不能替换的方块
            return false;
        }
        //之后肯定是雪
        int layers = blockState.getValue(LAYERS);
        if (layers <= 7) {//可以直接原地+1
            serverLevel.setBlockAndUpdate(blockPos, Blocks.SNOW.defaultBlockState().setValue(LAYERS, layers + 1));
            return true;
        }
        //之后肯定不能原地+1,在上一格继续
        return increaseSnowLayer(serverLevel, blockPos.above());
    }

    public static boolean increaseSnowLayer(ServerLevel serverLevel, BlockPos blockPos) {
        return increaseSnowLayer(serverLevel, blockPos, serverLevel.getBlockState(blockPos));
    }


    public static boolean decreaseSnowLayer(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState) {
        if (!blockState.is(Blocks.SNOW)) {//不是雪的话减少个鬼啊
            return false;
        }
        int layers = blockState.getValue(LAYERS);
        if (layers == 1) {//只有一层的话就放空气得了
            serverLevel.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
            return true;
        }
        //剩下的是多层的雪
        if (layers == 8) {//八层的要分两种情况
            BlockPos blockPosAbobe = blockPos.above();
            BlockState blockStateAbove = serverLevel.getBlockState(blockPosAbobe);
            if (blockStateAbove.is(Blocks.SNOW)) {//如果上层也是雪就不减少这一层的
                return decreaseSnowLayer(serverLevel, blockPosAbobe, blockStateAbove);
            } else {
                //直接进行之后的操作
            }
        }
        serverLevel.setBlockAndUpdate(blockPos, Blocks.SNOW.defaultBlockState().setValue(LAYERS, layers - 1));
        return true;
    }

    public static boolean decreaseSnowLayer(ServerLevel serverLevel, BlockPos blockPos) {
        return decreaseSnowLayer(serverLevel, blockPos, serverLevel.getBlockState(blockPos));
    }


}
