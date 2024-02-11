package net.iung.simplySnow.mixin;

import net.iung.simplySnow.Rules;
import net.iung.simplySnow.SnowOperator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;


//在ServerLevel类中的mixin，目的是在下雨时额外增加下雪过程

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements WorldGenLevel {


    protected ServerLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, registryAccess, holder, supplier, bl, bl2, l, i);
    }

    @Shadow
    public abstract ServerLevel getLevel();

    @Shadow
    public abstract void levelEvent(@Nullable Player player, int i, BlockPos blockPos, int j);

    private static final boolean SNOW_IN_EVERY_BIOME = true;
    private static final int SNOW_CHANCE_PERCENT = 100;
    private static final int MAX_SNOW_LIGHTLEVEL = 3;


    @Inject(
            method = "Lnet/minecraft/server/level/ServerLevel;tickChunk(Lnet/minecraft/world/level/chunk/LevelChunk;I)V",
            at = @At(value = "TAIL"),
            remap = true
    )
    public void levelChuckMixin(LevelChunk levelChunk, int iWillNotUseThis, CallbackInfo ci) {
        if (levelChunk.getLevel().dimensionType().ultraWarm()) {
            return;
        }
        if (!isRaining()) {//没下雨就什么都不做，之后都是下雨的情况
            return;
        }
        if (this.random.nextDouble() < Rules.SNOW_CHANCE_PERCENT) {
            //随机数满足下雪的机率
        } else {
            return;
        }
        ;
        //获取一个随机位置
        ChunkPos chunkPos = levelChunk.getPos();
        int j = chunkPos.getMinBlockX();
        int k = chunkPos.getMinBlockZ();
        BlockPos blockPos = this.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, this.getBlockRandomPos(j, 0, k, 15));
        BlockPos blockPosBelow = blockPos.below();


        Biome biome = (Biome) this.getBiome(blockPos).value();
        if (Rules.SNOW_IN_EVERY_BIOME || !biome.warmEnoughToRain(blockPos)) {
            //继续之后的操作
        } else {
            return;//群系不满足下雪的条件
        }

        if (this.getBrightness(LightLayer.BLOCK, blockPos) > Rules.MAX_SNOW_LIGHTLEVEL) {//太亮了
            return;
        }


        SnowOperator.increaseSnowLayer(getLevel(), blockPos);

    }


    @Inject(
            method = "Lnet/minecraft/server/level/ServerLevel;tick(Ljava/util/function/BooleanSupplier;)V",
            at = @At(value = "HEAD")
    )
    public void tickMixin(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        //SnowLayerBlockMixin
        Rules.SNOW_ON_ICE = this.getGameRules().getRule(Rules.SIMPLYSNOW_SNOW_ON_ICE).get();
        Rules.SNOW_ON_DIRT_PATH = this.getGameRules().getRule(Rules.SIMPLYSNOW_SNOW_ON_DIRT_PATH).get();
        Rules.SNOW_MELT_CHANCE_PERCENT = this.getGameRules().getRule(Rules.SIMPLYSNOW_SNOW_MELT_CHANCE_PERCENT).get();
        Rules.MIN_MELT_LIGHTLEVEL = this.getGameRules().getRule(Rules.SIMPLYSNOW_MIN_MELT_LIGHTLEVEL).get();
        Rules.SNOW_MELT_CHANCE_WARM_BIOME_PERCENT = this.getGameRules().getRule(Rules.SIMPLYSNOW_SNOW_MELT_CHANCE_WARM_BIOME_PERCENT).get();
        Rules.SNOW_MELT_CHANCE_INDOOR_PERCENT = this.getGameRules().getRule(Rules.SIMPLYSNOW_SNOW_MELT_CHANCE_INDOOR_PERCENT).get();
        Rules.MIN_MELT_LIGHTLEVEL_INDOOR = this.getGameRules().getRule(Rules.SIMPLYSNOW_MIN_MELT_LIGHTLEVEL_INDOOR).get();
        Rules.SPREAD_DOWN = this.getGameRules().getRule(Rules.SIMPLYSNOW_SPREAD_DOWN).get();
        //ServerLevelMixin
        Rules.SNOW_IN_EVERY_BIOME = this.getGameRules().getRule(Rules.SIMPLYSNOW_SNOW_IN_EVERY_BIOME).get();
        Rules.SNOW_CHANCE_PERCENT = this.getGameRules().getRule(Rules.SIMPLYSNOW_SNOW_CHANCE_PERCENT).get();
        Rules.MAX_SNOW_LIGHTLEVEL = this.getGameRules().getRule(Rules.SIMPLYSNOW_MAX_SNOW_LIGHTLEVEL).get();
    }
}
