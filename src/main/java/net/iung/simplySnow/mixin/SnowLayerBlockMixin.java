package net.iung.simplySnow.mixin;


import net.iung.simplySnow.Main;
import net.iung.simplySnow.Rules;
import net.iung.simplySnow.SnowOperator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(SnowLayerBlock.class)
public class SnowLayerBlockMixin {
    @Shadow
    @Final
    public static IntegerProperty LAYERS;
    private static final boolean SNOW_ON_ICE = true;
    private static final boolean SNOW_ON_DIRT_PATH = true;
    private static final int SNOW_MELT_CHANCE_PERCENT = 0;
    private static final int MIN_MELT_LIGHTLEVEL = 3;
    private static final int SNOW_MELT_CHANCE_INDOOR_PERCENT = 70;
    private static final int SNOW_MELT_CHANCE_WARM_BIOME_PERCENT = 0;
    private static final int MIN_MELT_LIGHTLEVEL_INDOOR = 1;

    private static final int SPREAD_DOWN = 4;

    private static final Random random = new Random();

    @ModifyConstant(
            method = "Lnet/minecraft/world/level/block/SnowLayerBlock;randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
            constant = @Constant(intValue = 11),
            require = 0
    )
    private int snowMeltMinLightLevel(int lightLevel) {
        return 30;
    }


    @Inject(method = "Lnet/minecraft/world/level/block/SnowLayerBlock;canSurvive(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void canSurviveMixin(BlockState blockState, LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState2 = levelReader.getBlockState(blockPos.below());
        if (blockState2.getTags().toList().contains(BlockTags.ICE)) {
            cir.setReturnValue(Rules.SNOW_ON_ICE);
            return;
        }
        if (blockState2.is(Blocks.DIRT_PATH)) {
            cir.setReturnValue(Rules.SNOW_ON_DIRT_PATH);
            return;
        }
        //cir.setReturnValue(true);
    }


    @Inject(method = "Lnet/minecraft/world/level/block/SnowLayerBlock;randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
            at = @At("TAIL")
    )
    public void randomTickMixin(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        int layers = blockState.getValue(LAYERS);
        if (!serverLevel.isRaining()) {//如果不下雨的话就尝试融化一下
            if (random.nextDouble(100) < Rules.SNOW_MELT_CHANCE_PERCENT && (
                    ((serverLevel.getBrightness(LightLayer.SKY, blockPos) >= Rules.MIN_MELT_LIGHTLEVEL ||
                            layers == 8) &&
                            serverLevel.getDayTime() % 24000 > 0 &&
                            serverLevel.getDayTime() % 24000 < 12000) ||
                            serverLevel.getBrightness(LightLayer.BLOCK, blockPos) >= Rules.MIN_MELT_LIGHTLEVEL)) {//融化概率
                SnowOperator.decreaseSnowLayer(serverLevel, blockPos, blockState);
                return;
            }
            if (random.nextDouble(100) < Rules.SNOW_MELT_CHANCE_WARM_BIOME_PERCENT) {//在温暖群系融化
                Biome biome = (Biome) serverLevel.getBiome(blockPos).value();
                if (biome.warmEnoughToRain(blockPos)) {
                    SnowOperator.decreaseSnowLayer(serverLevel, blockPos, blockState);
                    return;
                }
            }
        }
        if (random.nextDouble(100) < Rules.SNOW_MELT_CHANCE_INDOOR_PERCENT) {//室内融化
            if (serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos).getY() > blockPos.getY() + 2) {//室内
                if (serverLevel.getBrightness(LightLayer.BLOCK, blockPos) >= Rules.MIN_MELT_LIGHTLEVEL_INDOOR) {//光照足够
                    SnowOperator.decreaseSnowLayer(serverLevel, blockPos, blockState);
                    return;
                }
            }
        }


        if (layers == 1 && (!serverLevel.getBlockState(blockPos.below()).is(Blocks.SNOW))) {//如果只有一层的话就不扩散
            return;
        }
        int t = random.nextInt(4);
        BlockPos blockPos1;
        switch (t) {//决定扩散方向
            case 0:
                blockPos1 = blockPos.north();
                break;
            case 1:
                blockPos1 = blockPos.south();
                break;
            case 2:
                blockPos1 = blockPos.east();
                break;
            case 3:
            default:
                blockPos1 = blockPos.west();
        }
        BlockState blockState1 = serverLevel.getBlockState(blockPos1);
        if (blockState1.is(Blocks.SNOW)) {
            int layers1 = blockState1.getValue(LAYERS);
            if (layers1 <= layers - 2) {
                SnowOperator.increaseSnowLayer(serverLevel, blockPos1, blockState1);
                SnowOperator.decreaseSnowLayer(serverLevel, blockPos, blockState);
                return;
            }
            return;
        }
        if (blockState1.getTags().toList().contains(BlockTags.REPLACEABLE) && !blockState1.isAir() && !blockState1.is(Blocks.SNOW) && !blockState1.is(Blocks.SNOW_BLOCK)) {//可以覆盖的植物，直接放置
            if (layers >= 2) {
                Main.LOGGER.info("REPLACE");
                SnowOperator.increaseSnowLayer(serverLevel, blockPos1, blockState1);
                SnowOperator.decreaseSnowLayer(serverLevel, blockPos, blockState);
                return;
            }
            return;
        }
        if (!blockState1.isAir()) {//此时如果不是空气的话那肯定不能扩散了
            return;
        }
        if (Blocks.SNOW.defaultBlockState().canSurvive(serverLevel, blockPos1)) {//可以直接扩散
            if (layers >= 2) {
                SnowOperator.increaseSnowLayer(serverLevel, blockPos1, blockState1);
                SnowOperator.decreaseSnowLayer(serverLevel, blockPos, blockState);
                return;
            }
            return;
        }

        //向下扩散
        SnowOperator.decreaseSnowLayer(serverLevel, blockPos, blockState);
        Main.LOGGER.info("down");
        for (int i = 1; i <= Rules.SPREAD_DOWN; i++) {
            BlockPos blockPos2 = blockPos1.below(i);
            BlockState blockState2 = serverLevel.getBlockState(blockPos2);
            if (SnowOperator.increaseSnowLayer(serverLevel, blockPos2, blockState2)
                    ||
                    !(
                            blockState2.isAir() ||
                                    (
                                            blockState2.getTags().toList().contains(BlockTags.REPLACEABLE)
                                                    && !blockState2.is(Blocks.SNOW)
                                                    && !blockState2.is(Blocks.SNOW_BLOCK)
                                    )
                    )) {
                Main.LOGGER.info("i: {}", i);
                return;
            }
        }


    }
}
