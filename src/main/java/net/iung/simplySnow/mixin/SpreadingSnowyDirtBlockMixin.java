package net.iung.simplySnow.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(SpreadingSnowyDirtBlock.class)
public class SpreadingSnowyDirtBlockMixin extends SnowyDirtBlock {
    private static final boolean GRASS_UNDER_SNOW=true;
    public SpreadingSnowyDirtBlockMixin(Properties properties) {
        super(properties);
    }



    @Inject(at = @At("HEAD"), method = "canBeGrass", cancellable = true)
    private static void canBeGrassMixin(BlockState state, LevelReader level, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if(level.getBlockState(blockPos.above()).is(Blocks.SNOW)){
            cir.setReturnValue(GRASS_UNDER_SNOW);
        }
    }

    @Inject(at = @At("HEAD"), method = "canPropagate", cancellable = true)
    private static void canPropagateMixin(BlockState state, LevelReader level, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if(level.getBlockState(blockPos.above()).is(Blocks.SNOW)){
            cir.setReturnValue(GRASS_UNDER_SNOW);
        }
    }


}
