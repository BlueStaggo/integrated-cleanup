package io.bluestaggo.integratedcleanup.mixin.block;

import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.minecraft.block.Block;
import net.minecraft.block.LiquidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiquidBlock.class)
public abstract class LiquidBlockMixin extends Block {
	protected LiquidBlockMixin(Material material) {
		super(material);
	}

	@Inject(
		method = "playFizzSound",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/World;playSound(DDDLjava/lang/String;FF)V",
			shift = At.Shift.AFTER
		),
		cancellable = true
	)
	public void playFixedFizzSound(World world, int x, int y, int z, CallbackInfo ci) {
		world.doEvent(IntegratedCleanup.WORLD_EVENT_LIQUID_FIZZ, x, y, z, 0);
		ci.cancel();
	}
}
