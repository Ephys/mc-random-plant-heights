package be.ephys.random_plant_heights.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static be.ephys.random_plant_heights.helpers.Utils.randomIntInclusive;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {

  @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
  private void randomTick$randomiseHeight(BlockState state, ServerWorld worldIn, BlockPos pos, Random random, CallbackInfo callbackInfo) {
    callbackInfo.cancel();

    BlockPos up = pos.up();
    if (!worldIn.isAirBlock(up)) {
      return;
    }

    SugarCaneBlock self = (SugarCaneBlock) (Object) this;

    int i;
    for(i = 1; worldIn.getBlockState(pos.down(i)).isIn(self); ++i) {
    }

    Random random2 = new Random(pos.toLong());
    int maxHeight = randomIntInclusive(random2, 2, 4);

    if (i < maxHeight) {
      int j = state.get(SugarCaneBlock.AGE);
      if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, true)) {
        if (j == 15) {
          worldIn.setBlockState(up, self.getDefaultState());
          worldIn.setBlockState(pos, state.with(SugarCaneBlock.AGE, Integer.valueOf(0)), 4);
        } else {
          worldIn.setBlockState(pos, state.with(SugarCaneBlock.AGE, Integer.valueOf(j + 1)), 4);
        }
      }
    }
  }
}
